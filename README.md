# FateUBW 
[![](http://cf.way2muchnoise.eu/full_271719_Forge_%20.svg)![](http://cf.way2muchnoise.eu/versions/271719.svg)](https://www.curseforge.com/minecraft/mc-mods/fate-ubw)   
[![](http://cf.way2muchnoise.eu/full_605485_Fabric_%20.svg)![](http://cf.way2muchnoise.eu/versions/605485.svg)](https://www.curseforge.com/minecraft/mc-mods/fate-ubw-fabric)  
[![](https://img.shields.io/modrinth/dt/q021y7rs?logo=modrinth&label=Modrinth)![](https://img.shields.io/modrinth/game-versions/q021y7rs?logo=modrinth&label=Latest%20for)](https://modrinth.com/mod/fate-ubw)  
[![Discord](https://img.shields.io/discord/790631506313478155?color=0a48c4&label=discord)](https://discord.gg/8Cx26tfWNs)

A minecraft mod bringing the fate universe into mc.

To use this mod as a dependency add the following snippet to your build.gradle:  
```groovy
repositories {
    maven {
        name = "Flemmli97"
        url "https://maven.blazing-coop.net/releases"
    }
}

dependencies {    
    //Fabric/Loom==========    
    modImplementation("io.github.flemmli97:fateubw:${minecraft_version}-${mod_version}-${mod_loader}")
    
    //Forge==========    
    compile fg.deobf("io.github.flemmli97:fateubw:${minecraft_version}-${mod_version}-${mod_loader}")
}
```