package maver.weapons.combat.weapons.portalpod.behavior

import com.fs.starfarer.api.combat.CollisionClass
import com.fs.starfarer.api.combat.MissileAIPlugin
import com.fs.starfarer.api.combat.MissileAPI
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual

/**
 * Helper interface to influence the behavior of [maver.weapons.combat.weapons.portalpod.PortalMissileAi]
 * without lots of redundant code.
 */
interface BehaviorInterface {
    val collisionClass: CollisionClass
    fun getEntryPortalParams(): NegativeExplosionVisual.NEParams
    fun getExitPortalParams(): NegativeExplosionVisual.NEParams
    fun onExit(missile: MissileAPI, facingDeg: Float)
    fun advanceTerminal(amount: Float, missile: MissileAPI, wrappedAI: MissileAIPlugin?)
}