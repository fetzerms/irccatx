package de.fetzerms.irccatx.server;

import de.fetzerms.irccatx.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class CatServer implements Runnable {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    public void run() {

        LOG.info("Starting the Cat-Server.");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Config.getCatPort(), 0, InetAddress.getByName(Config.getCatHost()));
            LOG.info("Cat server accepting connections on {}:{}", Config.getCatHost(), Config.getCatPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                LOG.debug("Received new connection. Dispatching to a new CatHandler thread.");
                new Thread(new CatHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
