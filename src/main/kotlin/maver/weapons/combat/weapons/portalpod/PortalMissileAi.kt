package maver.weapons.combat.weapons.portalpod

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual
import com.fs.starfarer.api.util.Misc
import maver.weapons.combat.weapons.portalpod.behavior.MissileBehavior
import maver.weapons.combat.weapons.portalpod.behavior.BehaviorInterface
import maver.weapons.combat.weapons.portalpod.portal.RiftParams
import maver.weapons.extensions.random
import org.lazywizard.lazylib.CollisionUtils
import org.lwjgl.util.vector.Vector2f


/**
 * A missile that will enter subspace and then emerge at a random point
 * somewhere around its target, the proceeds to accelerate into its direction.
 *
 * @link https://www.youtube.com/watch?v=0fBy_DTGNW8
 *
 * For custom behavior implement [BehaviorInterface]
 *
 * Missiles with this AI will go through 5 phases:
 * LAUNCH - Accelerate towards the portal
 * ENTER - Vanish into the portal
 * WARP - zero speed, invisible, waiting
 * EXIT - Teleport to the exit and accelerate again
 * TERMINAL - Vanilla missile behavior
 *
 * To be used with [PortalMissileAiConfig]
 *
 */
open class PortalMissileAi(
    private val config: PortalMissileAiConfig,
    private val behavior: BehaviorInterface = MissileBehavior(),
    private val missile: MissileAPI,
    private val ship: ShipAPI,
    private val weapon: WeaponAPI,
    private val wrappedAI: MissileAIPlugin?,
) : MissileAIPlugin, GuidedMissileAI {
    enum class Phase { LAUNCH, ENTER, WARP, EXIT, TERMINAL }

    private val portalLocation: Vector2f = Vector2f()
    private val engine: CombatEngineAPI = Global.getCombatEngine()

    private var lastMissileLocation: Vector2f = Vector2f(missile.location)
    private var phase = Phase.LAUNCH
    private var phaseTime = 0f
    private var phaseEntered = false
    private var target: CombatEntityAPI? = null
    private val lastKnownTargetLoc = Vector2f()
    private var haveLastKnown = false

    init {
        (wrappedAI as? GuidedMissileAI)?.target?.let { target = it }
        missile.maxFlightTime += config.launchDuration + config.enterFadeDuration + config.warpDuration + config.exitFadeDuration
    }

    override fun advance(amount: Float) {
        if (missile.isExpired) return

        phaseTime += amount

        // Take last known location on target loss/switch
        target?.let { t ->
            if (Global.getCombatEngine().isEntityInPlay(t)) {
                lastKnownTargetLoc.set(t.location)
                haveLastKnown = true

                // When the target is out of range the missile fails
                if (Misc.getDistanceSq(ship.location, t.location) > config.maxRange * config.maxRange) {
                    missile.flameOut()
                    return
                }
            }
        }

        when (phase) {
            Phase.LAUNCH -> {
                wrappedAI?.advance(amount)

                // Check whether the missile actually hits the portal.
                if (config.withCollisionCheck) {
                    updatePortalLocation()
                    if (CollisionUtils.getCollides(
                            lastMissileLocation,
                            missile.location,
                            portalLocation,
                            config.catchRadius
                        )
                    ) transition(Phase.ENTER)
                } else if (phaseTime >= config.launchDuration) {
                    transition(Phase.ENTER)
                    missile.fadeOutThenIn(config.enterFadeDuration)
                }
            }

            Phase.ENTER -> {
                if (!phaseEntered) {
                    phaseEntered = true

                    if (config.spawnVisualOnFire) spawnPortalVisual(behavior.getEntryPortalParams())

                    // Missile moves onto the portal and hides
                    missile.spriteAlphaOverride = 0f
                    missile.collisionClass = CollisionClass.NONE
                    missile.velocity.set(0f, 0f)

                    // It's surprisingly difficult to hide all parts of the
                    // engine trail so just move it
                    teleportToSafeSpace()
                }

                if (phaseTime >= config.enterFadeDuration) transition(Phase.WARP)
            }

            Phase.WARP -> {
                missile.velocity.set(0f, 0f)
                if (phaseTime >= config.warpDuration) transition(Phase.EXIT)
            }

            Phase.EXIT -> {
                if (!phaseEntered) {
                    phaseEntered = true

                    missile.spriteAlphaOverride = 1f
                    missile.collisionClass = behavior.collisionClass
                    missile.armingTime = 0.5f

                    behavior.onExit(missile, teleportAroundTarget())
                    spawnPortalVisual(behavior.getExitPortalParams())
                }
                if (phaseTime >= config.exitFadeDuration) transition(Phase.TERMINAL)
            }

            Phase.TERMINAL -> {
                behavior.advanceTerminal(amount, missile, wrappedAI)
            }
        }

        lastMissileLocation = Vector2f(missile.location)
    }

    private fun transition(next: Phase) {
        phase = next
        phaseTime = 0f
        phaseEntered = false
    }

    private fun teleportToSafeSpace() {
        missile.location.set(Vector2f( -10000f,-10000f))
    }

    /**
     * Moves the missile to somewhere behind / around the target.
     * Missiles might be closer or slightly further away from the target depending on their roll.
     * Giving PD a window to pick them off.
     */
    private fun teleportAroundTarget(): Float {
        // Shouldn't be a race condition. Still, lock it.
        val target = this@PortalMissileAi.target

        val anchor: Vector2f = when {
            target != null && Global.getCombatEngine().isEntityInPlay(target) -> Vector2f(target.location)
            haveLastKnown -> Vector2f(lastKnownTargetLoc)
            else -> Vector2f(missile.location)
        }

        val firerToTarget = Misc.getAngleInDegrees(ship.location, anchor)
        val angleDeg = firerToTarget + (Misc.random.nextFloat() - 0.5f) * config.attackAngle
        val outward = Misc.getUnitVectorAtDegreeAngle(angleDeg)

        // The lucky roll
        val nearOffset = (config.luckyClose..config.luckyFar).random()

        // Take target ship size into account
        val targetRadius = 0.6f * ((target?.takeIf { Global.getCombatEngine().isEntityInPlay(it) }?.collisionRadius) ?: 0f)
        val exitDistance = targetRadius.plus(config.exitOffset)

        val exitLoc = Vector2f(
            (anchor.x + outward.x * exitDistance) + nearOffset,
            (anchor.y + outward.y * exitDistance) + nearOffset,
        )
        val facingDeg = Misc.getAngleInDegrees(exitLoc, anchor)

        missile.location.set(exitLoc)
        missile.facing = facingDeg

        return facingDeg
    }

    private fun spawnPortalVisual(params: NegativeExplosionVisual.NEParams) {
        val rift = engine.addLayeredRenderingPlugin(NegativeExplosionVisual(params))
        rift.location.set(missile.location)
    }

    override fun getTarget(): CombatEntityAPI? = target

    override fun setTarget(newTarget: CombatEntityAPI?) {
        target = newTarget
        (wrappedAI as? GuidedMissileAI)?.target = newTarget
    }

    private fun updatePortalLocation() {
        val outward = Misc.getUnitVectorAtDegreeAngle(weapon.currAngle)
        portalLocation.set(
            weapon.location.x + outward.x * config.portalDistance,
            weapon.location.y + outward.y * config.portalDistance,
        )
    }

}
