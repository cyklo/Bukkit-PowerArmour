PowerArmour
===========

PowerArmour is a plugin for the Minecraft Bukkit API that lets you
give superhero abilities to certain armour combinations.

The related discussion thread for this plugin is located at
<http://forums.bukkit.org/threads/6374/>

Downloading
-----------

Please note that OtherBlocks contains submodules, so to checkout:

    git clone git://github.com/cyklo/Bukkit-PowerArmour.git
    cd Bukkit-PowerArmour
    git submodule update --init

Building
--------

An Ant makefile is included. Building this project requires a copy of
`bukkit.jar` in the top level directory.

    cd Bukkit-PowerArmour
    wget -O bukkit.jar http://ci.bukkit.org/job/dev-Bukkit/lastSuccessfulBuild/artifact/target/bukkit-0.0.1-SNAPSHOT.jar
    ant
    ant jar