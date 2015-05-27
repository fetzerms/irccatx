package de.fetzerms.irccatx;

import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.server.CatServer;
import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.exception.IrcException;

import java.io.File;
import java.io.IOException;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * <p/>
 * Main class for IRCCatX
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class IRCCatX {

    private static String config = "irccatx.xml";

    public static void main(String[] args) throws IOException, IrcException, ConfigurationException {

        if (args.length != 1 && !(new File(config).exists())) {
            System.out.println("Error.. no config");
            System.exit(1);
        } else if (args.length != 1) {
            config = args[0];
        }


        IrcClient ircClient = IrcClient.getInstance();
        ircClient.init();

        new Thread(new CatServer()).start();

        ircClient.start();

    }

}
