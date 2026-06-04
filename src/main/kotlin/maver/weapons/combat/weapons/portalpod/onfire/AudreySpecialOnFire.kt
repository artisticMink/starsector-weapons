package maver.weapons.combat.weapons.portalpod.onfire

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.combat.MissileAPI
import com.fs.starfarer.api.combat.OnFireEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import maver.weapons.combat.weapons.portalpod.PortalMissileAi
import maver.weapons.combat.weapons.portalpod.PortalMissileAiConfig
import maver.weapons.combat.weapons.portalpod.behavior.MineBehavior

class AudreySpecialOnFire : OnFireEffectPlugin {
    override fun onFire(projectile: DamagingProjectileAPI, weapon: WeaponAPI, engine: CombatEngineAPI) {
        if (projectile !is MissileAPI) return
        val firer = weapon.ship ?: return

        val vanillaAI = projectile.unwrappedMissileAI
        projectile.missileAI = PortalMissileAi(
            PortalMissileAiConfig(
                luckyClose = -225f,
                luckyFar = 175f,
                attackAngle = 360f,
                portalDistance = 50f
            ),
            MineBehavior(),
            projectile,
            firer,
            weapon,
            vanillaAI,
        )
    }
}