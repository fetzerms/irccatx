package de.fetzerms.irccatx.util;


import de.fetzerms.irccatx.script.Script;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class Config {

    public static String VERSION = "0.1b";
    private static XMLConfiguration config = null;

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    /**
     * Initializes the Config with the values from a given XML file
     *
     * @param configFile The config file to read the values from
     *
     * @throws ConfigurationException if malformed XML file
     */
    public static void init(String configFile) throws ConfigurationException {
        LOG.info("Initializing configuration file.");
        config = new XMLConfiguration(configFile);
    }

    public static String getClientHost() {
        return config.getString("ircclient.server.host");
    }

    public static int getClientPort() {
        return config.getInt("ircclient.server.port", 6667);
    }

    public static boolean getClientSSL() {
        return config.getBoolean("ircclient.server.ssl", false);
    }

    public static String getClientNick() {
        return config.getString("ircclient.client.nick");
    }

    public static String getClientPassword() {
        return config.getString("ircclient.server.password", "");
    }

    /**
     * Retrieves a map of all channels and their password in the configuration.
     * If no password is set, the password is an empty string.
     *
     * @return Map, mapping channels to passwords
     */
    public static Map<String, String> getClientChannels() {
        HashMap<String, String> channelPasswordMap = new HashMap<>();
        List<HierarchicalConfiguration> channelList = config.configurationsAt("channels.channel");
        for (HierarchicalConfiguration channel : channelList) {
            String channelName = channel.getString("name");
            if (!channelName.startsWith("#")) {
                channelName = "#" + channelName;
            }
            channelPasswordMap.put(channelName, channel.getString("password", ""));
        }

        return channelPasswordMap;
    }

    /**
     * Retrieves a list of all default channels.
     *
     * @return List containing all default channels
     */
    public static List<String> getDefaultChannels() {
        ArrayList<String> defaultChannelList = new ArrayList<>();
        List<HierarchicalConfiguration> channelList = config.configurationsAt("channels.channel");
        for (HierarchicalConfiguration channel : channelList) {
            if (channel.getBoolean("default", false)) {
                String channelName = channel.getString("name");
                if (!channelName.startsWith("#")) {
                    channelName = "#" + channelName;
                }
                defaultChannelList.add(channelName);
            }
        }
        return defaultChannelList;
    }

    /**
     * Retrieves all triggers and the related scripts.
     *
     * @return Map <trigger, script>
     */
    public static List<Script> getScripts() {

        ArrayList<Script> scriptList = new ArrayList<>();

        List<HierarchicalConfiguration> scriptConfig = config.configurationsAt("scripts.script");
        for (HierarchicalConfiguration script : scriptConfig) {


            List<String> hostmasks = new ArrayList<>();
            List<HierarchicalConfiguration> hostmaskConfig = script.configurationsAt("authorization.hostmasks.hostmask");
            for(HierarchicalConfiguration hostmask : hostmaskConfig){
                hostmasks.add(hostmask.getString("."));
            }

            List<String> channels = new ArrayList<>();
            List<HierarchicalConfiguration> channelsConfig = script.configurationsAt("authorization.channels.channel");
            for(HierarchicalConfiguration channel : channelsConfig){
                channels.add(channel.getString("."));
            }

            Boolean queryAllowed = script.getBoolean("authorization.queryAllowed", true);
            Boolean channelAllowed = script.getBoolean("authorization.channelAllowed", true);

            scriptList.add(new Script(script.getString("trigger"), script.getString("handler"), hostmasks, channels, queryAllowed, channelAllowed));

        }

        return scriptList;
    }

    public static long getClientMessageDelay() {
        return config.getLong("ircclient.server.messagedelay", 250L);
    }

    public static String getCatHost() {
        return config.getString("catserver.server.host", "127.0.0.1");
    }

    public static int getCatPort() {
        return config.getInt("catserver.server.port", 12345);
    }
}
