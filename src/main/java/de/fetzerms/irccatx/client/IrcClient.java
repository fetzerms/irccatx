package de.fetzerms.irccatx.client;

import de.fetzerms.irccatx.listeners.GenericListener;
import de.fetzerms.irccatx.listeners.ScriptListener;
import de.fetzerms.irccatx.util.Config;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.Map;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class IrcClient {


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

        // Read values from configuration
        String host = Config.getClientHost();
        String password = Config.getClientPassword();
        String nick = Config.getClientNick();
        int port = Config.getClientPort();
        boolean ssl = Config.getClientSSL();
        long messageDelay = Config.getClientMessageDelay();


        @SuppressWarnings("unchecked") Configuration.Builder configBuilder = new Configuration.Builder()
                .setName(nick)
                .setServerHostname(host)
                .setServerPort(port)
                .addListener(new GenericListener()) // Generic Listener
                .addListener(new ScriptListener())  // Script Listener
                .setServerPassword(password)
                .setMessageDelay(messageDelay);

        for (Map.Entry<String, String> channelEntry : Config.getClientChannels().entrySet()) {

            String channelName = channelEntry.getKey();
            String channelPassword = channelEntry.getValue();

            if (!channelName.startsWith("#")) {
                channelName = "#" + channelName;
            }

            configBuilder.addAutoJoinChannel(channelName, channelPassword);
        }

        if (ssl) {
            configBuilder.setSocketFactory(new UtilSSLSocketFactory());
        }

        this.bot = new PircBotX(configBuilder.buildConfiguration());
    }

    public void start() throws IOException, IrcException {
        this.bot.startBot();
    }

    public PircBotX getBot() {
        return this.bot;
    }
}
