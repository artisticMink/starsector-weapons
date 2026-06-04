package maver.weapons.combat.weapons.portalpod.behavior


import com.fs.starfarer.api.combat.CollisionClass
import com.fs.starfarer.api.combat.MissileAIPlugin
import com.fs.starfarer.api.combat.MissileAPI
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual
import com.fs.starfarer.api.util.Misc
import maver.weapons.combat.weapons.portalpod.portal.RiftParams

class MissileBehavior : BehaviorInterface {
    override val collisionClass = CollisionClass.MISSILE_NO_FF

    override fun onExit(missile: MissileAPI, facingDeg: Float) {
        val kick = Misc.getUnitVectorAtDegreeAngle(facingDeg)
        kick.scale(missile.maxSpeed)
        missile.velocity.set(kick)
    }

    override fun advanceTerminal(amount: Float, missile: MissileAPI, wrappedAI: MissileAIPlugin?) {
        wrappedAI?.advance(amount)
    }

    override fun getEntryPortalParams() = RiftParams.getSmallEntryRiftParams()

    override fun getExitPortalParams() = RiftParams.getSmallEscapeRiftParams()
}
