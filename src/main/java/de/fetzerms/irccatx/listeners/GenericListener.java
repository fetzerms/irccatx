package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.util.Config;
import java.util.Map;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * <p/>
 * Example generic Listener. See pircbotx config for further information.
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class GenericListener extends ListenerAdapter {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    @Override
    public void onGenericMessage(GenericMessageEvent event) {

        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?version")) {
            LOG.info("?version message received.");
            event.respond("## Running IRCCatX v" + Config.VERSION);
        }
    }

    @Override
    public void onConnect(ConnectEvent event) throws Exception {
        for (Map.Entry<String, String> channelEntry : Config.getClientChannels().entrySet()) {
            String channelName = channelEntry.getKey();
            String channelPassword = channelEntry.getValue();

            LOG.debug("Adding channel {} with password: {}", channelName, channelPassword);
            if (!channelPassword.isEmpty()) {
                event.getBot().sendIRC().joinChannel(channelName, channelPassword);
            } else {
                event.getBot().sendIRC().joinChannel(channelName);
            }
        }
    }
}
