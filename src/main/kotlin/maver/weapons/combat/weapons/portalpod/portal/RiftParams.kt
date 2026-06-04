package maver.weapons.combat.weapons.portalpod.portal

import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual.NEParams
import com.fs.starfarer.api.impl.combat.RiftCascadeEffect
import java.awt.Color

/**
 * @see [com.fs.starfarer.api.impl.combat.NegativeExplosionVisual]
 *
 * Property              Description
 *
 * numRiftsToSpawn          Rifts spawned per call, default is 2, presumably for a shift effect.
 * radius                   Radius of the inner circle
 * thickness                Thickness of the outer explosion.
 * fadeIn                   Time for the whole rift to fade in.
 * fadeOut                  Time for the whole rift to fade out.
 * noiseMag                 Intensity of the animated wobble on all outlines (circle, border, glow). // Not sure if it's really all
 * noiseMult                Border wobble intensity // Difference to mag not clear. Factor?
 * noisePeriod              Border refresh rate
 * spawnHitGlowAt           Brightness 0f..1f during fadeIn at which the burst fires (~spawnHitGlowAt*fadeIn delay).
 * hitGlowSizeMult          Size factor for all burst particles (white flash, underglow cloud, negative particles).
 * withHitGlow              Switch for the burst.
 * withNegativeParticles    Adds 7 nebula particles to the burst (color = inverted color).
 * color                    OUTER glow-ring/halo color (additive); also tints the hit-flash. NOT the circle border.
 * blackColor               Fill of the inner circle its border.
 * underglow                Color of the nebula puffs in the burst, dominates a small rift.
 * additiveBlend            Whether to draw hole and border with additive color mixing
 * invertForDarkening       optional negative particle color
 */
object RiftParams {
    private val WARP_PINK = Color(235, 55, 205)
    private val TT_BLUE = Color(70, 140, 255)
    private val PIRATE_RED = Color(180, 10, 10)

    private fun Color.alpha(a: Int) = Color(red, green, blue, a)

    /**
     * @see [com.fs.starfarer.api.impl.combat.RiftCascadeMineExplosion]
     */
    fun getBaseRiftParams(): NEParams =  NEParams().apply {
        hitGlowSizeMult = 0.75f
        spawnHitGlowAt = 0.0f
        noiseMag = 1.0f
        fadeIn = 0.1f
        underglow = RiftCascadeEffect.EXPLOSION_UNDERCOLOR
        withHitGlow = true
        radius = radius
    }

    fun getContinuousRiftParams(newRadius: Float, newThickness: Float): NEParams = NEParams().apply {
        hitGlowSizeMult = 0.1f
        spawnHitGlowAt = 0.1f
        noiseMag = 0.55f
        fadeIn = 0.2f
        fadeOut = 0.2f
        withHitGlow = true
        underglow = TT_BLUE.alpha(140)
        withNegativeParticles = false
        thickness = newThickness
        radius = newRadius
        color = WARP_PINK.alpha(200)
    }

    fun getRiftCollapseParams(): NEParams = NEParams().apply {
        hitGlowSizeMult = 1.1f
        spawnHitGlowAt = 0.4f
        noiseMag = 1f
        fadeIn = 0.1f
        fadeOut = 0.5f
        underglow = TT_BLUE.alpha(200)
        withNegativeParticles = true
        withHitGlow = true
        thickness = 5f
        radius = 5f
        color = WARP_PINK.alpha(215)
    }

    fun getSmallEntryRiftParams(newRadius: Float = 5f, newThickness: Float = 2f): NEParams = NEParams().apply {
        hitGlowSizeMult = 0.3f
        spawnHitGlowAt = 0.2f
        noiseMag = 0.7f
        fadeIn = 0.08f
        fadeOut = 0.16f
        withHitGlow = true
        withNegativeParticles = false
        thickness = newThickness
        radius = newRadius
        color = WARP_PINK.alpha(200)
    }

    fun getSmallEscapeRiftParams(newRadius: Float = 4f, newThickness: Float = 1.5f): NEParams = NEParams().apply {
        hitGlowSizeMult = 0.9f
        spawnHitGlowAt = 0.25f
        noiseMag = 0.9f
        fadeIn = 0.15f
        fadeOut = 0.55f
        underglow = TT_BLUE.alpha(160)
        withNegativeParticles = true
        withHitGlow = true
        thickness = newThickness
        radius = newRadius
        color = WARP_PINK.alpha(210)
    }

    fun getSmallPirateEntryRiftParams(newRadius: Float = 5f, newThickness: Float = 2f): NEParams = NEParams().apply {
        hitGlowSizeMult = 0.3f
        spawnHitGlowAt = 0.2f
        noiseMag = 0.7f
        fadeIn = 0.08f
        fadeOut = 0.16f
        withHitGlow = true
        withNegativeParticles = false
        thickness = newThickness
        radius = newRadius
        color = PIRATE_RED.alpha(200)
    }

    fun getSmallPirateEscapeRiftParams(newRadius: Float = 4f, newThickness: Float = 1.5f): NEParams = NEParams().apply {
        hitGlowSizeMult = 0.9f
        spawnHitGlowAt = 0.25f
        noiseMag = 0.9f
        fadeIn = 0.15f
        fadeOut = 0.55f
        underglow = PIRATE_RED.alpha(160)
        withNegativeParticles = true
        withHitGlow = true
        thickness = newThickness
        radius = newRadius
        color = TT_BLUE.alpha(210)
    }
}