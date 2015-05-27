package de.fetzerms.irccatx.util;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

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

    private static XMLConfiguration config = null;

    /**
     * Initializes the Config with the values from a given XML file
     *
     * @param configFile The config file to read the values from
     *
     * @throws ConfigurationException if malformed XML file
     */
    public static void init(String configFile) throws ConfigurationException {
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

    public static Map<String, String> getClientChannels() {
        HashMap<String, String> channelPasswordMap = new HashMap<>();
        List<HierarchicalConfiguration> channelList = config.configurationsAt("channels.channel");
        for (HierarchicalConfiguration channel : channelList) {
            channelPasswordMap.put(channel.getString("name"), channel.getString("password", ""));
        }

        return channelPasswordMap;
    }

    public static HashMap<String, String> getScripts() {
        HashMap<String, String> scriptMap = new HashMap<>();
        List<HierarchicalConfiguration> scriptList = config.configurationsAt("scripts.script");
        for (HierarchicalConfiguration script : scriptList) {
            scriptMap.put(script.getString("trigger"), script.getString("handler"));
        }

        return scriptMap;
    }

    public static long getClientMessageDelay() {
        return config.getLong("ircclient.server.messagedelay", 250L);
    }
}
