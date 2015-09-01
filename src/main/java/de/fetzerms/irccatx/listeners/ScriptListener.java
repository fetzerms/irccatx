package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.blowfish.Blowfish;
import de.fetzerms.irccatx.script.Script;
import de.fetzerms.irccatx.script.ScriptRunner;
import de.fetzerms.irccatx.util.Config;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * <p/>
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

        String message = event.getMessage();
        String target = event.getChannel().getName();
        String hostmask = event.getUser().getLogin() + "@" + event.getUser().getHostmask();


        boolean isEncrypted = Blowfish.isEncrypted(message);

        if (isEncrypted && Blowfish.hasKey(target)) {
            message = Blowfish.decryptMessage(target, message);
        }

        List<Script> scripts = getScripts(message);
        for (Script script : scripts) {

            // Do not answer / process script if message was not encrypted
            // while it requires encryption.
            if (!isEncrypted && script.isFishOnly()) {
                LOG.info("Not executing script {} because it requires an encrypted message.", script.getCommand());
                continue;
            }

            String command = message.replace(script.getTrigger(), "").split("\\s+")[0]; // command without trigger.

            if (script.isChannelAllowed() && script.isAuthorizedChannel(target) && script.isAuthorizedHostmask(hostmask)) {
                String args = null;
                if (message.split(" ", 2).length > 1) {
                    args = message.split(" ", 2)[1];
                }

                LOG.info("Executing script {}", script.getCommand());
                new Thread(new ScriptRunner(script.getCommand(), target, hostmask, command, args, isEncrypted)).run();
            } else {
                LOG.info("Not authorized to execute script: {}", script.getCommand());
            }

        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {

        String message = event.getMessage();
        String target = event.getUser().getNick();
        boolean isEncrypted = Blowfish.isEncrypted(message);

        if (isEncrypted && Blowfish.hasKey(target)) {
            message = Blowfish.decryptMessage(event.getUser().getNick(), message);
        }

        List<Script> scripts = getScripts(message);
        for (Script script : scripts) {

            // Do not answer / process script if message was not encrypted
            // while it requires encryption.
            if (!isEncrypted && script.isFishOnly()) {
                LOG.info("Not executing script {} because it requires an encrypted message.", script.getCommand());
                continue;
            }

            // If the script requires to user to be in a channel,
            // but the user is not -> ignore.
            if(script.needsChannel() &&  ! script.isInChannel(target)){
                LOG.info("Not executing script {} because it requires the user to be on one of the channels.", script.getCommand());
                continue;
            }

            String command = message.replace(script.getTrigger(), "").split("\\s+")[0]; // command without trigger.
            String hostmask = event.getUser().getLogin() + "@" + event.getUser().getHostmask();

            if (script.isQueryAllowed() && script.isAuthorizedHostmask(hostmask)) {
                String args = null;
                if (event.getMessage().split(" ", 2).length > 1) {
                    args = event.getMessage().split(" ", 2)[1];
                }

                LOG.info("Executing script {}", script.getCommand());
                new Thread(new ScriptRunner(script.getCommand(), target, hostmask, command, args, false)).run();
            } else {
                LOG.info("Not authorized to execute script: {}", script.getCommand());
            }
        }

    }

    public List<Script> getScripts(String message) {

        // String message = event.getMessage();
        ArrayList<String> scripts = new ArrayList<>();

        List<Script> availableScripts = Config.getScripts();
        List<Script> matchingScripts = new ArrayList<>();

        for (Script script : availableScripts) {
            if (message.startsWith(script.getTrigger())) {
                String scriptCommand = script.getCommand();
                LOG.info("Found script for message with trigger {}: {}", script.getTrigger(), script.getCommand());

                matchingScripts.add(script);
            }
        }

        return matchingScripts;
    }


}
