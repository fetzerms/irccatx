package de.fetzerms.irccatx.server;

import com.google.common.collect.ImmutableSortedSet;
import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.util.ColorMap;
import de.fetzerms.irccatx.util.Config;
import org.pircbotx.Channel;
import org.pircbotx.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * <p/>
 * CatHandler to process incoming messages on the IRCCatX port.
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class CatHandler implements Runnable {

    private Socket clientSocket;

    public CatHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            String readLine;
            while ((readLine = in.readLine()) != null) {

                readLine = ColorMap.colorize(readLine);

                String[] inputArray = readLine.split(" ", 2);

                if (inputArray.length != 2) {
                    continue;
                }

                String targetString = inputArray[0];
                String message = inputArray[1];

                if (targetString.equals("%TOPIC")) { // Special treatment for %TOPIC

                    String channels = message.split(" ", 2)[0];
                    String topic = message.split(" ", 2)[1];

                    // Iterate over channel list
                    for (String channel : channels.split(",")) {

                        if (channel.equals("#*")) { // Set topic for all channels
                            ImmutableSortedSet<Channel> channelList = IrcClient.getInstance().getBot().getUserChannelDao().getAllChannels();
                            for (Channel currentChannel : channelList) {
                                currentChannel.send().setTopic(topic);
                            }

                        } else { // Set topic for single channel
                            IrcClient.getInstance().getBot().getUserChannelDao().getChannel(channel).send().setTopic(topic);
                        }

                    }

                } else {

                    // Multiple targets are comma seperated
                    String[] targets = targetString.split(",");

                    // Iterate over targets
                    for (String target : targets) {

                        /**
                         * The following checks for the incoming commands.
                         *
                         * The following methods are available:
                         *
                         *  1) Sending to all channels ("#*")
                         *  2) Sending to a specific channel ("#channel")
                         *  3) Sending to all users ("@*")
                         *  4) Sending to a specific user ("@user")
                         *  5) Setting the topic ("%TOPIC #channel")
                         *  6) Sending to default channels (none of the above)
                         *
                         */

                        if (target.equals("#*")) { // Send to all channels
                            ImmutableSortedSet<Channel> channelList = IrcClient.getInstance().getBot().getUserChannelDao().getAllChannels();
                            for (Channel channel : channelList) {
                                channel.send().message(message);
                            }

                        } else if (target.startsWith("#")) { // Send to single channel
                            IrcClient.getInstance().getBot().getUserChannelDao().getChannel(target).send().message(message);

                        } else if (target.equals("@*")) { // Send to all users
                            ImmutableSortedSet<User> userList = IrcClient.getInstance().getBot().getUserChannelDao().getAllUsers();
                            for (User user : userList) {
                                user.send().message(message);
                            }

                        } else if (target.startsWith("@")) { // Send to user
                            target = target.substring(1);
                            IrcClient.getInstance().getBot().getUserChannelDao().getUser(target).send().message(message);

                        } else { // No operator given. Send to all default channels.
                            List<String> channels = Config.getDefaultChannels();
                            for (String channel : channels) {
                                IrcClient.getInstance().getBot().getUserChannelDao().getChannel(channel).send().message(message);
                            }
                        }
                    }
                }
            }

        } catch (IOException e)

        {
            e.printStackTrace();
        }

    }
}
