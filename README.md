IRCCatX
=======

This software is inspired by the original IRCCat by Richard Jones (https://github.com/RJ/irccat), which is not actively
maintained anymore. It is meant to be a replacement for the original IRCCat.

Why IRCCatX?
------------
The original IRCCat lacked the support for SSL encrypted connections, as the underlying version of pircbot does not
support SSL connections aswell. There is a fork of the original pircbot, called PircBotX which provides SSL support.
Hence IRCCatX was born.

Installation
============
Copy and edit the configuration file inside the examples folder.

Build the IRCCatX with: 

$ mvn package

The resulting .jar file will be placed into the target/ directory. It can be started with:

$ java -jar irccatx.jar [config-file]

If the config file parameter is omitted, it searches for irccat.xml in the working directory.

