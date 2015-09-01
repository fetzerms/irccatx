package de.fetzerms.irccatx.blowfish;

import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.util.DH1080Impl;

import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @Author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class DH1080 {
    private static Map<String, DH1080Impl> dhMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static void initDH1080(String nick) {
        DH1080Impl exchange = new DH1080Impl();
        IrcClient.getInstance().getBot().sendIRC().notice(nick, DH1080Impl.DH1080_INIT + " " + exchange.getPublicKey());
        dhMap.put(nick, exchange);
    }

    public static void completeDH1080(String nick, String key) {
        if (dhMap.containsKey(nick)) {
            DH1080Impl exchange = dhMap.get(nick);
            String secretKey = exchange.getSharedSecret(key);
            Blowfish.setKey(nick, secretKey);
            dhMap.remove(nick);
        }
    }

    public static void finishDH1080(String nick, String key) {
        DH1080Impl exchange = new DH1080Impl();
        IrcClient.getInstance().getBot().sendIRC().notice(nick, DH1080Impl.DH1080_FINISH + " " + exchange.getPublicKey());
        String secretKey = exchange.getSharedSecret(key);
        Blowfish.setKey(nick, secretKey);
    }
}
