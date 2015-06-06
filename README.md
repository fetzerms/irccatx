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

Sending to IRC
===============

By default, just like the original IRCCat, IRCCatX listens on 127.0.0.1:12345

Sending messages to IRC works as follows:

$ echo "<message>" | netcat -q0 127.0.0.1 12345

The message can be prefixed to send to specific (or a set of) channels or users.

Sending to all default channels
-------------------------------

To send text to all default channels, simply do not prefix the message:

$ echo "this will be sent to all default channels" | netcat -q0 127.0.0.1 12345

Sending text to a specific channel
----------------------------------

To send text to a specific channel, prefix the message with #channelname:

$ echo "#channelName this will be sent to #channelName" | netcat -q0 127.0.0.1 12345

Sending text to a all channels
------------------------------

To send text to a specific channel, prefix the message with #*:

$ echo "#* this will be sent to all channels" | netcat -q0 127.0.0.1 12345


Sending text to a specific user
--------------------------------
To send text to a specific user (query), prefix the message with @user:

$ echo "@userName this will be sent to the user userName" | netcat -q0 127.0.0.1 12345

Sending text to a all users
---------------------------

To send text to a specific channel, prefix the message with @*:

$ echo "@* this will be sent to all users." | netcat -q0 127.0.0.1 12345

Use this with caution. You can easily get kicked/banned on major IRC Networks for spamming all users. 
Only use this, if you are sure that all members in the channel want to receive your messages.

Combination of the above
------------------------

All of the above prefixes can be combined, using a comma seperated list as prefix e.g.:

$ echo "#*,@operator sent to all channels and to operator." | netcat -q0 127.0.0.1 12345

$ echo "#channel1,#channel2 send to #channel1 and #channel2" | netcat -q0 127.0.0.1 12345

$ echo "#channel1,@user send to #channel1 and user" | netcat -q0 127.0.0.1 12345


Changing to topic of a channel
------------------------------

To change the topic of a channel, prefix the message with %TOPIC #channel

$ echo "%TOPIC #channel This is the new topic on #channel" | netcat -q0 127.0.0.1 12345

Changing to topic of several channels
--------------------------------------

To change the topic of several channels at once, prefix the message with %TOPIC #channel1,#channel2

$ echo "%TOPIC #channel1,#channel2 This is the new topic on #channel1 and #channel2" | netcat -q0 127.0.0.1 12345

Colorizing output
-----------------

The output to channels and users can be colorized using the following variables:

NORMAL, BOLD, UDNERLINE, REVERSE, WHITE, BLACK, DBLUE, DGREEN, RED, BROWN, PURPLE, ORANGE, YELLOW, GREEN, TEAL, CYAN,
BLUE, PINK, DGRAY, GRAY. The variables can be prefixed with either "#" or "%". (Adapted from original IrcCat).

echo "#channel1 this will be sent %REDin red%NORMAL to #channel1" | netcat -q0 127.0.0.1 12345


Extending IRCCatX
=================

IRCCatX can easily be extended to use own Listeners. To achieve this, you have to create a new listener in 
de.fetzerms.irccatx.listeners and make sure to include it in de.fetzerms.irccatx.client.IrcClient. For the actual
implementation of the Listener, please refer to the GenericLIstener or the ScriptListener, or check the pircbotx
documentation.

Using scripts with IRCCatX
==========================

To have IRCCatX execute scripts on certain triggers, you need to add those to the irccatx configuration file. 
See the supplied examples. The arguments passed to the script are as follows: 

- $1: Triggered command, without the trigger itself
- $2: Hostmask of the sender
- $3: Any further arguments

Contributing
============

Contributions to IRCCatX are welcome!

If you have any additions, examples or bugfixes ready, feel free to create a pull request on GitHub. The pull requests will be reviewed
and will be merged as soon as possible.To ease the process of merging the pull requests, please create one pull request
 per feature/fix, so those can be selectively included in IRCCatX.