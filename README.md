# Weapons and stuff, but mostly weapons

## Weapons

| Weapon         | Mount  | Damage Type    | Role        | Damage/Shot | OPs |
|----------------|--------|----------------|-------------|-------------|-----|
| Kinsei HKPM-20 | Medium | Kinetic        | Anti Shield | 300         | 12  |
| Kinsei HKPM-8  | Small  | Kinetic        | Anti Shield | 300         | 6   |
| Kinsei HTRP-4  | Large  | High Explosive | Anti Armor  | 3800        | 22  |
| Audrey Special | Medium | Fragmentation  | Party Trick | 1000        | 8   |

The Kinseis can be aqquired as high level loot or from the historian. The Audrey special might also pop up on specialized markets. 

## Creating Portal Missiles

If you want to create portal missiles yourself, take a look at [PortalMissileAI].

Either grab the code or use this mod as a dependency, [PortalMissileAiConfig] and [BehaviorInterface] already offer extendability.

## Build the mod

### Dependencies

* JDK17
* Gradle 8+
* Lazy Lib

### HowTo

Execute the packageMod configuration from the intelliJ interface or manually run` gradle wrapper && ./gradlew packageMod`. The following requirements must be met:

A folder named libs/ with these jars:
* starfarer-api.jar
* log4j-1.2.9.jar
* json.jar
* LazyLib-Kotlin.jar
* lwjgl.jar
* lwjgl_util.jar

A file `gradle.properties.` in the root directory with `starsectorModFolder=<absolute-path-to-your-mod-folder>`

[PortalMissileAI]: src/main/kotlin/maver/weapons/combat/weapons/portalpod/PortalMissileAi.kt
[PortalMissileAiConfig]: src/main/kotlin/maver/weapons/combat/weapons/portalpod/PortalMissileAiConfig.kt
[BehaviorInterface]: src/main/kotlin/maver/weapons/combat/weapons/portalpod/behavior/BehaviorInterface.kt