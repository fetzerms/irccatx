package de.fetzerms.irccatx.listeners;

import de.fetzerms.irccatx.util.DH1080Impl;
import de.fetzerms.irccatx.blowfish.DH1080;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.NoticeEvent;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @Author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class DH1080Listener extends ListenerAdapter {


    @Override
    public void onNotice(NoticeEvent event){

        if(event.getMessage().startsWith(DH1080Impl.DH1080_FINISH)){
            String key = event.getMessage().trim().substring(DH1080Impl.DH1080_FINISH.length() + 1);
            DH1080.completeDH1080(event.getUser().getNick(), key);
        }

        if(event.getMessage().startsWith(DH1080Impl.DH1080_INIT)){
            String key = event.getMessage().trim().substring(DH1080Impl.DH1080_INIT.length() + 1);
            DH1080.finishDH1080(event.getUser().getNick(), key);
        }
    }
}
