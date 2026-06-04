package maver.weapons.combat.weapons.portalpod.portal

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual
import com.fs.starfarer.api.util.Misc
import org.lwjgl.util.vector.Vector2f

class Portal(
    private val engine: CombatEngineAPI
) {
    private val deployDistance = 200f

    // Should correspond with weapon charge time
    private val portalFormTime = 5f

    // Initial pulse
    private val pulseIntervalStart = 0.18f

    // Steady-state pulse
    private val pulseIntervalStable = 0.01f

    private var portalAge = 0f
    private var portalTimer = 0f
    private val portalStartRadius = 4f
    private var portalMaxRadius = 14f
    private var portalStartThickness = 1.5f
    private var portalMaxThickness = 3.4f

    fun update(
        anchor: Vector2f,
        baseAngle: Float,
        amount: Float,
    ) {
        portalAge = (portalAge + amount).coerceAtMost(portalFormTime)
        val portalCompleted = portalAge / portalFormTime
        val interval = pulseIntervalStart + (pulseIntervalStable - pulseIntervalStart) * portalCompleted

        portalTimer += amount
        if (portalTimer >= interval) {
            portalTimer = 0f
            val portalPosition = getPortalPosition(anchor, baseAngle)
            spawnPortalRift(
                portalPosition,
                (portalStartRadius + (portalCompleted * portalMaxRadius)).coerceAtMost(portalMaxRadius),
                (portalStartThickness + (portalCompleted * portalMaxThickness)).coerceAtMost(portalMaxThickness),
            )
        }
    }

    fun getPortalPosition(anchor: Vector2f, baseAngle: Float): Vector2f {
        val outward = Misc.getUnitVectorAtDegreeAngle(baseAngle)
        return Vector2f(
            anchor.x + outward.x * deployDistance,
            anchor.y + outward.y * deployDistance,
        )
    }

    fun collapse(at: Vector2f) {
        val params = RiftParams.getRiftCollapseParams()
        val rift = engine.addLayeredRenderingPlugin(NegativeExplosionVisual(params))
        rift.location.set(at)
    }

    private fun spawnPortalRift(at: Vector2f, radius: Float = portalMaxRadius, thickness: Float = portalMaxThickness) {
        val params = RiftParams.getContinuousRiftParams(radius, thickness)
        val rift = engine.addLayeredRenderingPlugin(NegativeExplosionVisual(params))
        rift.location.set(at)
    }
}
