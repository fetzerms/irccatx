package de.fetzerms.irccatx.util;

import de.fetzerms.irccatx.client.IrcClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class ScriptRunner implements Runnable {


    private final String script;
    private final String command;
    private final String args;
    private final String target;


    /**
     * Constructor for the ScriptRunner.
     *
     * @param script
     * @param target
     * @param command
     * @param args
     */
    public ScriptRunner(String script, String target, String command, String args) {
        this.script = script;
        this.target = target;
        this.command = command;
        this.args = args;

    }

    public void run() {
        try {
            ProcessBuilder processBuilder = null;
            if (args != null) {
                processBuilder = new ProcessBuilder(script, command, args).redirectErrorStream(true);
            } else {
                processBuilder = new ProcessBuilder(script, command).redirectErrorStream(true);
            }

            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = "";
            String line;
            while ((line = br.readLine()) != null) {
                IrcClient.getInstance().getBot().sendIRC().message(target, line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
