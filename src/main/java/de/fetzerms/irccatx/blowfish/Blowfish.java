package de.fetzerms.irccatx.blowfish;

import de.fetzerms.irccatx.util.BlowfishImpl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @Author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class Blowfish {

    private static Blowfish instance;

    private static Map<String, BlowfishImpl> fishMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static boolean hasKey(String channel) {
        return fishMap.containsKey(channel);
    }

    public static void setKey(String target, String key) {
        fishMap.put(target, new BlowfishImpl(key));
    }

    public static void delKey(String target){
        fishMap.remove(target);
    }

    private static BlowfishImpl getBlow(String channel) {
        return fishMap.get(channel);
    }

    public static String decryptMessage(String target, String message) {
        String clearText = "";
        BlowfishImpl fish = Blowfish.getBlow(target);
        try {
            clearText = fish.decrypt(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return clearText;
    }

    public static String encryptMessage(String target, String message) {
        String encryptedText = "";
        BlowfishImpl fish = Blowfish.getBlow(target);

        encryptedText = fish.encrypt(message);

        return encryptedText;
    }

    public static boolean isEncrypted(String message) {
        return message.startsWith("+OK ") || message.startsWith("mcps ");
    }

}
