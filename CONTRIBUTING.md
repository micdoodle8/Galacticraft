The Galacticraft Github Issues List
====================================

We welcome issues posted by any Galacticraft user - if you don't post it, we won't know about it!

Issues means:
* crashes and crash reports
* things not working as intended
* visual bugs and glitches
* compatibility issues with other mods

Posting new issues - guidelines
-------------------------------
1. IS IT GALACTICRAFT?
If you have a crash report with ```micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP``` or ```GCEntityClientPlayerMP``` or ```GCPlayerBaseSP``` somewhere in the _middle_ of it (and that's the _only_ link with Galacticraft) then it's probably __not__ a Galacticraft issue, instead it's being caused by something else nearer to the top of the crash report - see further guidance below - so please don't take up Galacticraft mod developers' time by posting these.
2. SEARCH FIRST!
Do not duplicate known issues: do a search first. __Check the closed issues also: maybe somebody already posted your issue and a fix was found.__
3. WHICH VERSION?
When posting, always tell us your exact Galacticraft version.  If it's a conflict with another mod, also tell us the other mod's version.
4. CHECK THE LATEST!
You should download the __latest__ GC version from http://micdoodle8.com/mods/galacticraft/downloads and test if your issue still occurs in that, before posting.
5. GIVE US THE LOGS!
If reporting a crash, please always post your full crash report or log, use Pastebin or http://paste.ubuntu.com/ or https://gist.github.com/ or similar to post it.
6. IS IT AN ADD-ON?
Issues in add-on mods like [GalaxySpace](https://github.com/BlesseNtumble/GalaxySpace),[MorePlanets](https://github.com/SteveKunG/MorePlanets),[AmunRa](https://github.com/katzenpapst/amunra),[4Space](https://github.com/4Space/4Space-5) should not be reported here.  Issues here are only for standard Galacticraft!  Instead report those issues to the authors of those add-on mods.  They have their own pages on Github for reporting issues.

Issue reports not meeting these guidelines will be flagged as __Invalid__ and closed.


If reporting a mod conflict, we might ask you to do some testing to isolate which exact other mod is conflicting with GC.


How to search before posting your issue
---------------------------------------
You're in the Galacticraft repository here, so it's easy to do a search, just enter the search at the top of this page.  The Github results page shows you both "Code" results and "Issues" results.  You need to click on "Issues" to see what has been found.

If you have a crash, it's best to search for some of the info which shows up at the top of that crash report - this search should lead you to any other issues with a similar crash report.


Are you sure it's a Galacticraft issue?
---------------------------------------
If posting a crash, please first be reasonably sure that it's a Galacticraft issue and not an issue in another mod.  (If it _could_ be Galacticraft or it _could_ be something else we will help you figure out the cause: obviously we want to fix anything which __is__ being caused by Galacticraft.)

One to watch out for is crash reports involving the __player__ which include somewhere in the middle of the report this line:
>         at micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.func_70097_a(GCEntityPlayerMP.java:73)

If you have Galacticraft installed, that line will always show up in the middle of the report whenever another mod crashes in a way which involves the player.  __It does not mean Galacticraft is crashing or causing a problem!__  All that's happening here is that Galacticraft has changed the name of the vanilla player code in Minecraft, in a way which does not break anything else.  So usually you will also see this line in the report just above the Galacticraft player line:
>         at net.minecraft.entity.player.EntityPlayerMP.func_70097_a(EntityPlayerMP.java:491)

That's not a vanilla Minecraft crash: likewise, Galacticraft does not have an issue here.  If you want to be 100% sure, you can install the PlayerAPI mod alongside Galacticraft and see if the same crash still happens.  (If PlayerAPI is installed then Galacticraft does not change the name of the vanilla player code in Minecraft.)

In summary: don't post crashes to this issues list if the only connection with Galacticraft is that ```micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP``` line in the __middle__ of the report.  Or ```GCEntityClientPlayerMP``` or ```GCPlayerBaseSP``` or other similar variants, the same guidance applies.


What else is not OK on the issues list?
----------------------------------
The issues list is __not__ for:
* suggestions and feature requests - go to the [suggestions forum](http://forum.micdoodle8.com/index.php?forums/suggestions.7/)
* questions and general support issues - go to the [support forum](http://forum.micdoodle8.com/index.php?forums/support.5/)
* problems downloading, installing or launching the mod: the mod works if you followed the [installation guide](http://wiki.micdoodle8.com/wiki/Tutorials/Galacticraft_Installation_Guide), if it is not working for you that's a support issue for the forum
* questions about when next version or more planets will come - see [sticky topic on this]( http://forum.micdoodle8.com/index.php?threads/when-will-the-next-update-when-will-new-planets-rockets-mobs-or-whatever-be-added.3753/)
* communicating with micdoodle8 - use other communication channels like Twitter or email
* modpack permission requests - modpack permissions are found here: http://wiki.micdoodle8.com/wiki/Modpack_Permission.

On the suggestions thing, we of course make an exception for developers of other mods making compatibility suggestions - those are always welcomed and the Issues list is a great place for that.  Likewise if you're a coder planning a Pull Request for Galacticraft you are welcome to open an issue to discuss it first.

Translation fixes
-----------------
If you spot an error or something missing in the translation of Galacticraft for your language, the best is to fix it yourself.  That's easy to do in 5 simple steps:

1.  Find the .lang file on Github and take a look at what needs fixing.  The language files are here, you'll need to figure out whether it is a GalacticraftCore or Galacticraft Planets item - basically any feature obtainable only after you reach Mars / Venus / Asteroids is probably in Galacticraft Planets.

    https://github.com/micdoodle8/Galacticraft/tree/master/src/main/resources/assets/galacticraftcore/lang
    https://github.com/micdoodle8/Galacticraft/tree/master/src/main/resources/assets/galacticraftplanets/lang

2.  Click the Fork button (top right of this screen) and now in your new fork, navigate back to the language file: click __src/main__ then __resources__ then __assets__

3.  Click the Edit button (it looks like a pencil!)

4.  Edit the text to fix the translation, and __Save__ your edit

5.  On the home page of your Fork, click "Pull Request"


---------------------------------------


Pull Requests
=============
Galacticraft is an open source project and Pull Requests from any competent coder are extremely welcome - we could use the help!

Pull Requests meeting the following guidelines can be automatically merged:

1.  Please make one sensibly-named commit, which describes the aspect you are fixing.  Remember it will show up in the Galacticraft change log for all time.  So 'Fixed moon lander thrust particles remaining visible' is better than 'Moon fix'.

2.  If you made a mistake and fixed it with a second or third minor commit, please combine them into one good commit (so not like some guys do it, commit 1 'fixed it' then commit 2 'oops, missed something' then commit 3 'derp'.)

3.  One Pull Request per commit please.  It confuses us if you mix three unrelated commits in one Pull Request.  Bear in mind that we might, for example, decide to incorporate two of your commits but not the third.  We are happy to have multiple Pull Requests from you.

4.  If you're fixing something which is not already in the issues list and is not obvious, please make comments explaining what you fixed.  A screenshot of before and after can help.  We are not super-human, please help us to understand what you did.

5.  If proposing a change in currently intended behaviour of Galacticraft via a Pull Request, that could be great if it's an improvement, but you have more chance of it being immediately accepted if you discussed it with us first - you are very welcome to open an issue to do that.

6.  Please respect our code style.  You can see the style by looking at existing sources.  Long lines are OK.  We like opening braces { to be on lines by themselves.

Pull Requests not meeting these guidelines are also welcomed, but we may have to manually merge them, which means that you may not be automatically accredited as the author in the changelog....

All Pull Requests automatically become subject to Galacticraft's licence.  If you're interested in the legal stuff, it's here: https://github.com/micdoodle8/Galacticraft/blob/master/LICENSE.txt
Briefly, it's an open source, free for non-commercial use licence, and we think it's important that it remains open source for all time.  You are not allowed to subvert that by making a Pull Request and later asserting that your code, incorporated in the mod, has some different licensing terms.  Please take special care not to make a Pull Request with code which is subject to a GPL licence: once incorporated in Galacticraft it will become subject to Galacticraft's licence instead (which is not a GPL licence).  If you're not good with that, then don't make a Pull Request.

Intending Add-On Authors
------------------------
Galacticraft has an API, and several people have successfully created add-ons using it, for example extra planet packs.  More information about those can be found [here](http://forum.micdoodle8.com/index.php?forums/addons.11/)

If something in the API is not working, then please let us know - or ideally please find a way to fix it so that things work for your add-on, and make a Pull Request.

The Github issues list is __not__ the best place for 'how to' questions from intending add-on authors - especially noobish type questions.  The best place for those is the add-ons area of the forum.  We understand that for some people writing a Galacticraft Add-On could seem like an easy first project, and obviously we encourage anyone who wants to learn to code well.  But we do not have time to hold your hand: we need to spend our coding time working on Galacticraft itself.

You should be able to figure things out for yourself if you know Java and you understand Minecraft modding.
* Galacticraft is open source and you should be able to figure out most things by looking at the sources.
* Especially, look at Galacticraft Mars.  Mars is coded just like an Add-On planet should be: it uses the API in the same way as an Add-On can.
* Setup instructions are here:  http://wiki.micdoodle8.com/wiki/GC3_API
* Micdoodle8 wrote a nice series of beginner modding tutorials a long time ago (2 years), they are still relevant and well-explained: http://micdoodle8.com/oldsite/become.html
* Coding a (good) planet is not easy.  Algorithmic worldgen is a special skill.  A large part of the creativity in vanilla Minecraft is in its high quality, varied worldgen.  You are recommended to study it carefully before attempting to make your own world.  There are also plenty of books and online tutorials about how to generate terrain in computer games: read them.

