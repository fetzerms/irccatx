package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.util.Config;
import de.fetzerms.irccatx.util.ScriptRunner;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.HashMap;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class ScriptListener extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {

        String script = getScript(event);
        if (script != null) {
            String target = event.getChannel().getName();
            String command = event.getMessage().split(" ", 2)[0].substring(1); // command without prefix

            String args = null;
            if (event.getMessage().split(" ", 2).length > 1) {
                args = event.getMessage().split(" ", 2)[1];
            }
            new Thread(new ScriptRunner(script, target, command, args)).run();
        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {

        String script = getScript(event);
        if (script != null) {
            String target = event.getUser().getNick();
            String command = event.getMessage().split(" ", 2)[0].substring(1); // command without prefix

            String args = null;
            if (event.getMessage().split(" ", 2).length > 1) {
                args = event.getMessage().split(" ", 2)[1];
            }
            new Thread(new ScriptRunner(script, target, command, args)).run();
        }

    }

    public boolean hasScript(char key) {

        HashMap<String, String> scriptMap = Config.getScripts();
        return scriptMap.containsKey(Character.toString(key));

    }

    public String getScript(GenericMessageEvent event) {

        char key = event.getMessage().charAt(0);

        if (hasScript(key)) {
            return Config.getScripts().get(Character.toString(key));
        } else {
            return null;
        }

    }


}
