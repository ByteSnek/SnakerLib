# SnakerLib

## Features in the latest version

### Rendering 

- A basic cube remake with the ability to add your own custom render type to it
- A PoseStackBuilder that has all transformation functions (as well as custom ones) that aren't present in the normal PoseStack class
- Remade miscellaneous models for custom rendering
- Super fast shader program loading
- Fully customizable animated creative mode tab icons
- Fully customizable skybox loading for any dimension
- Render type processors / buffers

### Concurrency

- Super fast event registry management 
- Locks, switches, asynchronous buffers and maps

### Logging

- Fully customizable Log4j loggers
- Fully customizable Log4j logging levels
- Fully customizable Log4j logging colours
- Fully customizable Log4j logging markers

### Brigader

- Various debug commands (Entity killing, discarding, hurting, PlayGroundMode)

### Level

- Various block remakes
- Boss entity implementation
- Various entity goals, memories and brains
- Configured features for rubble and stalagmite formations

### Misc

- Heaps of optimized Math functions
- 'Set and forget' ResourceLocation
- Forge ResourceLocation
- Reflection utilities 
- Hundreds of other utility functions

### Unsafe

- A 'versatile object' (an object that can be assigned to a anything!)
- Various casting functions
- Direct class instantiation
- JVM & Windows debug crashing functions
- Direct acess to 'The Unsafe' (sun.misc.Unsafe)

## Implementing

**In `build.gradle` (or similar):**

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation fg.deobf("xyz.snaker.snakerlib:SnakerLib:${sl_version}")
}
```

**And define the version in a `gradle.properties` (or similar):**

```properties
sl_version=1.6.7.1 # Replace with your desired version (see https://maven.snaker.xyz for the repository)
```

**If using maven in `pom.xml` (or similar):**

```xml
<dependencies>
    <groupId>xyz.snaker.snakerlib</groupId>
    <artifactId>SnakerLib</artifactId>
    <!--Replace with your desired version (see https://maven.snaker.xyz for the repository)-->
    <version>1.6.7.1</version>
</dependencies>
```

**In your main mod class constructor (with the `@Mod` annotation), invoke `SnakerLib.initialize()`:**

```java
@Mod(SnadFishMod.MODID)
public class SnadFishMod
{
    public static final String MODID = "snadfish";
    
    public SnadFishMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        SnakerLib.initialize();
    }
}
```

**If all goes well then you should see this output when starting the game:**

```log
[SnakerLib/INFO]: Successfully initialized mod 'snadfish' to SnakerLib
```
## Notes

### Client stuff

For using clientbound stuff (e.g. rendering a skybox), this should be done on the client distribution thread to avoid a server crash like so:

```java
 if (FMLEnvironment.dist.isClient()) {
    SkyBoxRenderer.createForDimension(Dimensions.EETSWA, SkyBoxTexture::new);
}
```

### ResourcePath (Enchaned Resource Locations)

For using a ResourcePath, you do not need to pass in a namespace as that was already passed in when you called `SnakerLib.initialize()`