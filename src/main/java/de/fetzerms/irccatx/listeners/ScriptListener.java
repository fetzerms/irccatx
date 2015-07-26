package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.script.Script;
import de.fetzerms.irccatx.util.Config;
import de.fetzerms.irccatx.script.ScriptRunner;
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

        List<Script> scripts = getScripts(event);
        for(Script script : scripts){

            String target = event.getChannel().getName();
            String command = event.getMessage().replace(script.getTrigger(), ""); // command without trigger.
            String hostmask = event.getUser().getLogin() + "@" + event.getUser().getHostmask();

            if(script.isChannelAllowed() && script.isAuthorizedChannel(target) && script.isAuthorizedHostmask(hostmask)){
                String args = null;
                if (event.getMessage().split(" ", 2).length > 1) {
                    args = event.getMessage().split(" ", 2)[1];
                }

                LOG.info("Executing script {}", script.getCommand());
                new Thread(new ScriptRunner(script.getCommand(), target, hostmask, command, args)).run();
            } else {
                LOG.info("Not authorized to execute script: {}", script.getCommand());
            }

        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {

        List<Script> scripts = getScripts(event);
        for(Script script : scripts){
            String target = event.getUser().getNick();
            String command = event.getMessage().replace(script.getTrigger(), ""); // command without trigger.
            String hostmask = event.getUser().getLogin() + "@" + event.getUser().getHostmask();

            if(script.isQueryAllowed() && script.isAuthorizedHostmask(hostmask)) {
                String args = null;
                if (event.getMessage().split(" ", 2).length > 1) {
                    args = event.getMessage().split(" ", 2)[1];
                }

                LOG.info("Executing script {}", script.getCommand());
                new Thread(new ScriptRunner(script.getCommand(), target, hostmask, command, args)).run();
            } else {
                LOG.info("Not authorized to execute script: {}", script.getCommand());
            }
        }

    }

    public List<Script> getScripts(GenericMessageEvent event) {

        String message = event.getMessage();
        ArrayList<String> scripts = new ArrayList<>();

        List<Script> availableScripts = Config.getScripts();
        List<Script> matchingScripts = new ArrayList<>();

        for(Script script : availableScripts){
            if(message.startsWith(script.getTrigger())){
                String scriptCommand = script.getCommand();
                LOG.info("Found script for message with trigger {}: {}", script.getTrigger(), script.getCommand());

                matchingScripts.add(script);
            }
        }

        return matchingScripts;
    }


}
