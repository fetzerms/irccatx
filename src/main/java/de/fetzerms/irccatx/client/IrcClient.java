package de.fetzerms.irccatx.client;

import de.fetzerms.irccatx.listeners.GenericListener;
import de.fetzerms.irccatx.listeners.ScriptListener;
import de.fetzerms.irccatx.util.Config;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class IrcClient {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    private static IrcClient INSTANCE;

    private PircBotX bot;

    private IrcClient() {

    }

    public static IrcClient getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new IrcClient();
        }
        return INSTANCE;
    }

    public void init() {

        LOG.info("Initializing pircbotx.");

        LOG.debug("Retrieving configuration options.");
        // Read values from configuration
        String host = Config.getClientHost();
        String password = Config.getClientPassword();
        String nick = Config.getClientNick();
        int port = Config.getClientPort();
        boolean ssl = Config.getClientSSL();
        long messageDelay = Config.getClientMessageDelay();


        LOG.debug("Creating configuration");
        // Create configuration
        @SuppressWarnings("unchecked") Configuration.Builder configBuilder = new Configuration.Builder()
                .setName(nick)
                .setServerHostname(host)
                .setServerPort(port)
                .setLogin("irccatx")
                .setRealName("IRCCatX")
                .addListener(new GenericListener()) // Generic Listener
                .addListener(new ScriptListener())  // Script Listener
                .setServerPassword(password).setMessageDelay(messageDelay);

        LOG.info("Adding channels.");
        // Add all channels
        for (Map.Entry<String, String> channelEntry : Config.getClientChannels().entrySet()) {

            String channelName = channelEntry.getKey();
            String channelPassword = channelEntry.getValue();

            LOG.debug("Adding channel {} with password: {}", channelName, channelPassword);

            configBuilder.addAutoJoinChannel(channelName, channelPassword);
        }

        // Set SSLSocket, if ssl is enabled.
        if (ssl) {
            LOG.info("Using ssl connection.");
            configBuilder.setSocketFactory(new UtilSSLSocketFactory());
        }

        this.bot = new PircBotX(configBuilder.buildConfiguration());
    }

    public void start() throws IOException, IrcException {
        LOG.info("Starting the IRC bot.");
        this.bot.startBot();
    }

    public PircBotX getBot() {
        return this.bot;
    }
}
