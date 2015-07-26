package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.util.Config;
import de.fetzerms.irccatx.util.ScriptRunner;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 *
 * Script listener. This class will listen on Messages and PrivateMessages and
 * start the execution of scripts that are associated with triggers.
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class ScriptListener extends ListenerAdapter {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    @Override
    public void onMessage(MessageEvent event) {

        List<String> scripts = getScripts(event);
        for(String script : scripts){
            String target = event.getChannel().getName();
            String command = event.getMessage().split(" ", 2)[0].substring(1); // command without prefix
            String hostmask = event.getUser().getHostmask();

            String args = null;
            if (event.getMessage().split(" ", 2).length > 1) {
                args = event.getMessage().split(" ", 2)[1];
            }

            LOG.info("Executing script {}", script);
            new Thread(new ScriptRunner(script, target, hostmask, command, args)).run();
        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {

        List<String> scripts = getScripts(event);
        for(String script : scripts){
            String target = event.getUser().getNick();
            String command = event.getMessage().split(" ", 2)[0].substring(1); // command without prefix
            String hostmask = event.getUser().getHostmask();

            String args = null;
            if (event.getMessage().split(" ", 2).length > 1) {
                args = event.getMessage().split(" ", 2)[1];
            }
            new Thread(new ScriptRunner(script, target, hostmask, command, args)).run();
        }

    }

    public boolean hasScript(char key) {

        HashMap<String, String> scriptMap = Config.getScripts();
        return scriptMap.containsKey(Character.toString(key));

    }

    public List<String> getScripts(GenericMessageEvent event) {

        String message = event.getMessage();
        ArrayList<String> scripts = new ArrayList<>();

        for(String trigger : Config.getScripts().keySet()){
            if(message.startsWith(trigger)){
                String script = Config.getScripts().get(trigger);
                LOG.info("Found script for message with trigger {}: {}", trigger, script);
                scripts.add(script);
            }
        }

        return scripts;
    }


}
