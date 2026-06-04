package maver.weapons.combat.weapons.portalpod

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.WeaponAPI
import maver.weapons.combat.weapons.portalpod.portal.Portal
import org.lwjgl.util.vector.Vector2f

/**
 * Deploys a separate portal and keeps it updated
 * as long as the weapon charges and then some.
 */
class PortalDeployWeaponEffect : AllowedOnlyWithTargetWeaponEffect() {
    private val gracePeriod = 1f

    private var portal: Portal? = null
    private var collapseTimer = 0f

    override fun advance(amount: Float, engine: CombatEngineAPI, weapon: WeaponAPI) {
        super.advance(amount, engine, weapon)

        val anchor = Vector2f(weapon.location)
        val baseAngle = weapon.currAngle
        val isUsingWeapons = weapon.chargeLevel > 0.001f || weapon.isFiring || weapon.isInBurst

        if (isUsingWeapons) {
            collapseTimer = 0f
            if (portal == null) portal = Portal(engine)

        } else if (portal != null) {
            collapseTimer += amount
            if (collapseTimer >= gracePeriod) {
                val activePortal = portal ?: return
                val portalPosition = activePortal.getPortalPosition(anchor, baseAngle)
                activePortal.collapse(portalPosition)
                portal = null
            }
        }

        portal?.update(
            anchor = Vector2f(weapon.location),
            baseAngle = weapon.currAngle,
            amount = amount,
        )
    }
}
