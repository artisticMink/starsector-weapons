package maver.weapons.extensions

import com.fs.starfarer.api.util.Misc
import java.util.Random

fun ClosedFloatingPointRange<Float>.random(r: Random = Misc.random): Float =
    start + r.nextFloat() * (endInclusive - start)

fun Float.remap(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float =
    (this - inMin) / (inMax - inMin) * (outMax - outMin) + outMin
