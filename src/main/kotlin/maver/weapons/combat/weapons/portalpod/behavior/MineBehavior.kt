package maver.weapons.combat.weapons.portalpod.behavior

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.CollisionClass
import com.fs.starfarer.api.combat.DamageType
import com.fs.starfarer.api.combat.MissileAPI
import com.fs.starfarer.api.combat.MissileAIPlugin
import maver.weapons.combat.weapons.portalpod.portal.RiftParams
import maver.weapons.extensions.random
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

class MineBehavior : BehaviorInterface {
    override val collisionClass = CollisionClass.MISSILE_FF

    private var blinksRemaining = 3
    private var timeUntilNextBlink = (3f..5f).random()

    override fun onExit(missile: MissileAPI, facingDeg: Float) {
        if ((1..7).random() == 7) {
            missile.damage.type = DamageType.HIGH_EXPLOSIVE
            missile.damage.damage = 1000f
        }

        // Make enemy PD skip it. Own PD will target it.
        missile.owner = missile.weapon.ship.shipTarget.owner
    }

    override fun advanceTerminal(amount: Float, missile: MissileAPI, wrappedAI: MissileAIPlugin?) {
        if (blinksRemaining <= 0) return

        timeUntilNextBlink -= amount
        if (timeUntilNextBlink <= 0f) {
            blink(missile)
            blinksRemaining--
            timeUntilNextBlink = (3f..5f).random()
        }
    }

    private fun blink(missile: MissileAPI) {
        Global.getCombatEngine().addSmoothParticle(
            missile.location, Vector2f(), 50f, 1f, 0.4f, Color.RED,
        )
    }

    override fun getEntryPortalParams() = RiftParams.getSmallPirateEntryRiftParams()

    override fun getExitPortalParams() = RiftParams.getSmallPirateEscapeRiftParams()
}