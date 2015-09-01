package de.fetzerms.irccatx.script;

import de.fetzerms.irccatx.blowfish.Blowfish;
import de.fetzerms.irccatx.client.IrcClient;
import de.fetzerms.irccatx.util.ColorMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 *
 * This class runs a script and prints all output to the desired target.
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class ScriptRunner implements Runnable {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);


    private final String script;
    private final String command;
    private final String args;
    private final String target;
    private final String hostmask;
    private final boolean crypt;


    /**
     * Constructor for the ScriptRunner.
     *
     * @param script    Script to run
     * @param target    Target to print the output to
     * @param hostmask  The hostmask of the initiator
     * @param command   The triggered command, without the trigger itself
     * @param args      Args for the Script
     */
    public ScriptRunner(String script, String target, String hostmask, String command, String args, boolean crypt) {
        this.script = script;
        this.target = target;
        this.hostmask = hostmask;
        this.command = command;
        this.args = args;
        this.crypt = crypt;
    }

    public void run() {
        try {

            // Use the processbuilder to run the script. Append the arguments, if there are any.
            ProcessBuilder processBuilder;
            if (args != null) {
                processBuilder = new ProcessBuilder(script, command, hostmask, args).redirectErrorStream(true);
            } else {
                processBuilder = new ProcessBuilder(script, command, hostmask).redirectErrorStream(true);
            }

            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = "";
            String line;

            // Print the output to irc
            while ((line = br.readLine()) != null) {
                String message = ColorMap.colorize(line);
                if(Blowfish.hasKey(this.target) && this.crypt){
                    // message = BlowfishManager.getBlow().getBlow(this.target).encrypt(message);
                    message = Blowfish.encryptMessage(this.target, message);
                }

                IrcClient.getInstance().getBot().sendIRC().message(target, message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
