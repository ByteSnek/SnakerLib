# SnakerLib

## Implementing

If using `build.gradle` (or similar):

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation fg.deobf("xyz.snaker.snakerlib:SnakerLib:${sl_version}")
}
```

And define the version in a `gradle.properties` (or similar):

```properties
sl_version=1.7.7 # Replace with your desired version (1.7.7+ is considered stabl', see https://maven.snaker.xyz for the repository)
```

If using maven in `pom.xml` (or similar):

```xml

<dependencies>
    ...
    <groupId>xyz.snaker.snakerlib</groupId>
    <artifactId>SnakerLib</artifactId>
    <!--Replace with your desired version (1.7.7+ is considered stable, see https://maven.snaker.xyz for the repository)-->
    <version>1.7.7</version>
    ...
</dependencies>
```

In your main mod class constructor (with the `@Mod` annotation), invoke `SnakerLib.initialize()`:

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