package de.fetzerms.irccatx.server;

import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.util.ColorMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
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

                String target = inputArray[0];
                String message = inputArray[1];


                if (target.startsWith("#")) {
                    // target is a channel
                    IrcClient.getInstance().getBot().getUserChannelDao().getChannel(target).send().message(message);

                } else if (target.startsWith("@")) {
                    // target is a user
                    target = target.substring(1);
                    IrcClient.getInstance().getBot().getUserChannelDao().getUser(target).send().message(message);

                } else if (target.startsWith("%TOPIC")) {
                    // set the topic
                    String channel = message.split(" ", 2)[0];
                    String topic = message.split(" ", 2)[1];
                    IrcClient.getInstance().getBot().getUserChannelDao().getChannel(channel).send().setTopic(topic);
                } else {
                    // no target specified, do nothing
                }
            }

        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }

    }
}
