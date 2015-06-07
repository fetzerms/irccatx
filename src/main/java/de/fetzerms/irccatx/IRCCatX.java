package de.fetzerms.irccatx;

import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.server.CatServer;
import de.fetzerms.irccatx.util.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    public static void main(String[] args) throws IOException, IrcException, ConfigurationException {

        LOG.info("IRCCatX started.");

        /**
         * Check if a parameter is given, and use it as config file.
         * If no parameter is given, try to use irccat.xml as the config.
         * If the config does not exist, the program aborts.
         */
        if (args.length != 1 && !(new File(config).exists())) {
            LOG.error("No configuration file parameter set and no irccatx.xml found. Shutting down.");
            System.exit(1);
        } else if (args.length == 1) {

            config = args[0];
        } else {
            LOG.info("Defaulting to configuration file: {}", config);
        }

        LOG.info("Using configuration file: {}", config);

        // Initialize the config.
        Config.init(config);

        // Initialize the bot
        IrcClient ircClient = IrcClient.getInstance();
        ircClient.init();

        // Start the CatServer
        new Thread(new CatServer()).start();

        // Start the bot
        ircClient.start();

    }

}
