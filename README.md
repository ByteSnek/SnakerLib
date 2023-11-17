# SnakerLib

## Implementing

If using `build.gradle` (or similar):

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation fg.deobf("xyz.snaker.snakerlib:SnakerLib:${snakerlib_version}")
    minecraftLibrary "com.github.ByteSnek:Hiss:${hiss_version}" 
}
```

And define the version in a `gradle.properties` (or similar):

```properties
snakerlib_version=1.7.8 # Replace with your desired version (1.7.8+ is considered stable, see https://maven.snaker.xyz for the repository)
hiss_version=1.0
```

If using maven in `pom.xml` (or similar):

```xml

<dependencies>
    ...
    <groupId>xyz.snaker.snakerlib</groupId>
    <artifactId>SnakerLib</artifactId>
    <!--Replace with your desired version (1.7.8+ is considered stable, see https://maven.snaker.xyz for the repository)-->
    <version>1.7.8</version>
    ...
    <groupId>com.github.ByteSnek</groupId>
    <artifactId>Hiss</artifactId>
    <version>1.0</version>
    ...
</dependencies>
```

In your main mod class constructor (with the `@Mod` annotation), invoke `SnakerLib.initialize()`:

```java

@Mod(SnadFishMod.MODID) // Required
public class SnadFishMod
{
    public static final String MODID = "snadfish";

    public SnadFishMod()
    {
        ...
        SnakerLib.initialize(); // Add this line
        ...
    }
}
```