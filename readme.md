Galacticraft
============

An advanced space exploration mod for Minecraft.

<<<<<<< HEAD

Reporting issues
=======

Before reporting an issue, please read https://github.com/micdoodle8/Galacticraft/blob/master/CONTRIBUTING.md
and follow the six guidelines given.


Developers
=======

Detailed information on how to set up a development environment for the source code - if you want to use either the Galacticraft API or the full sources - is here: https://wiki.micdoodle8.com/wiki/GC3_API

To build, run the command `gradlew build packCoreJar packPlanetsJar PackMicCoreJar`
 
=======
Building
=======

To setup a development environment and build the mod, you must first download and extract the appropriate Minecraft Forge source archive.

Next, download or clone this repo and the MicdoodleCore repo, and place them in the same folder as Forge, merging folders if asked. Make sure your copies of Forge, Galacticraft, and MicdoodleCore are all for the same Minecraft version.

Run the command `gradlew setupDecompWorkspace`

Run the appropriate command to create the project for the Java IDE you want to use. This would either be `gradlew eclipse` or `gradlew idea`

Make your changes to the code, if any.

Run the command `gradlew build packCoreJar packPlanetsJar PackMicCoreJar` and fetch your built jar files from build/libs/

Repeat the previous two steps as needed.
>>>>>>> origin/MC1.7
 
License
=======

License can be found here: https://github.com/micdoodle8/Galacticraft/blob/master/LICENSE.txt
