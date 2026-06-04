package maver.weapons.combat.weapons.portalpod

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.util.Misc

/**
 * Prevents weapon from being fired until a target is locked and within range
 */
open class AllowedOnlyWithTargetWeaponEffect : EveryFrameWeaponEffectPlugin {
    val maxRange = 2500f

    override fun advance(amount: Float, engine: CombatEngineAPI, weapon: WeaponAPI) {
        val ship = weapon.ship ?: return
        val target = weapon.ship.shipTarget

        if (target == null || Misc.getDistanceSq(ship.location, target.location) > maxRange * maxRange) {
            weapon.isForceNoFireOneFrame = true
        }
    }
}
