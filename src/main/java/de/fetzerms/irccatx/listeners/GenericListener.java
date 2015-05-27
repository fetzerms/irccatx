package de.fetzerms.irccatx.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class GenericListener extends ListenerAdapter {
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?version")) {
            event.respond("Test something bla!");
        }
    }


}
