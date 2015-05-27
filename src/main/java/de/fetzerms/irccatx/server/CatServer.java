package de.fetzerms.irccatx.server;

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

    public void run() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345, 0, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                new Thread(new CatHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
