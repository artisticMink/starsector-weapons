package maver.weapons.combat.weapons.portalpod

/**
 * To be passed to [PortalMissileAiConfig]
 *
 * Noteworthy toggles are:
 * - withCollisionCheck
 *   When true, projectiles have to actually be aimed at the portal and hit it in order to teleport.
 * - spawnVisualOnFire
 *   Whether the missile shoud spawn its own portal
 */
data class PortalMissileAiConfig(
    val maxRange: Float = 3500f,
    val launchDuration: Float = 0.8f,
    val enterFadeDuration: Float = 0.8f,
    val warpDuration: Float = 2f,
    val exitFadeDuration: Float = 0.4f,
    val exitOffset: Float = 275f,
    val portalDistance: Float = 200f,
    val catchRadius: Float = 22f,
    val withCollisionCheck: Boolean = false,
    val spawnVisualOnFire: Boolean = true,
    val luckyClose: Float = -100f,
    val luckyFar: Float = 50f,
    val attackAngle:Float = 260f,
)
