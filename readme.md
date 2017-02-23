Galacticraft
============

An advanced space exploration mod for Minecraft.

Building
=======

To setup a development environment and build the mod, you must first download and extract the appropriate Minecraft Forge source archive.

Next, download or clone this repo and the MicdoodleCore repo, and place them in the same folder as Forge, merging folders if asked. Make sure your copies of Forge, Galacticraft, and MicdoodleCore are all for the same Minecraft version.

Run the command `gradlew setupDecompWorkspace`

Run the appropriate command to create the project for the Java IDE you want to use. This would either be `gradlew eclipse` or `gradlew idea`

Make your changes to the code, if any.

Run the command `gradlew build packCoreJar packPlanetsJar PackMicCoreJar` and fetch your built jar files from build/libs/

Repeat the previous two steps as needed.
 
License
=======

License can be found here: https://github.com/micdoodle8/Galacticraft/blob/master/LICENSE.txt
