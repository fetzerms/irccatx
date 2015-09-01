package de.fetzerms.irccatx.script;

import de.fetzerms.irccatx.client.IrcClient;
import org.pircbotx.Channel;

import java.util.List;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Script object.
 *
 * @Author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class Script {

    private Boolean needsChannel;
    private String trigger;
    private String command;
    private List<String> authorizedHostmasks;
    private List<String> authorizedChannels;
    private Boolean queryAllowed;
    private Boolean channelAllowed;
    private Boolean fishOnly;


    public Script(String trigger, String command, List<String> authorizedHostmasks, List<String> authorizedChannels, Boolean queryAllowed, Boolean needsChannel, Boolean channelAllowed, Boolean fishOnly) {
        this.trigger = trigger;
        this.command = command;
        this.authorizedHostmasks = authorizedHostmasks;
        this.authorizedChannels = authorizedChannels;
        this.queryAllowed = queryAllowed;
        this.channelAllowed = channelAllowed;
        this.fishOnly = fishOnly;
        this.needsChannel = needsChannel;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getAuthorizedHostmasks() {
        return authorizedHostmasks;
    }

    public void setAuthorizedHostmasks(List<String> authorizedHostmasks) {
        this.authorizedHostmasks = authorizedHostmasks;
    }

    public List<String> getAuthorizedChannels() {
        return authorizedChannels;
    }

    public void setAuthorizedChannels(List<String> authorizedChannels) {
        this.authorizedChannels = authorizedChannels;
    }


    public Boolean isQueryAllowed() {
        return queryAllowed;
    }

    public void setQueryAllowed(Boolean queryAllowed) {
        this.queryAllowed = queryAllowed;
    }

    public Boolean isChannelAllowed() {
        return channelAllowed;
    }

    public void setChannelAllowed(Boolean channelAllowed) {
        this.channelAllowed = channelAllowed;
    }

    /**
     * This method checks if a given hostmask is allowed to execute the script.
     * If no hostmasks are defined, execution is permitted.
     *
     * @param hostmask to check against.
     *
     * @return true if permitted.
     */
    public boolean isAuthorizedHostmask(String hostmask) {

        if (authorizedHostmasks.isEmpty()) {
            return true;
        }

        for (String authorizedHosmask : authorizedHostmasks) {
            if (simpleMatch(hostmask, authorizedHosmask) || hostmask.equals(authorizedHosmask)) {
                return true;
            }
        }
        return true;
    }

    /**
     * This method checks if the script is allowed to be triggered from a given
     * channel. If no channels are defined, execution is permitted.
     *
     * @param channel to check against.
     *
     * @return true if permitted.
     */
    public boolean isAuthorizedChannel(String channel) {

        if (authorizedChannels.isEmpty()) {
            return true;
        }

        for (String authorizedChannel : authorizedChannels) {
            if (simpleMatch(channel, authorizedChannel) || authorizedChannel.equals(channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInChannel(String nick) {
        for (Channel channel : IrcClient.getInstance().getBot().getUserChannelDao().getUser(nick).getChannels()) {
            if (authorizedChannels.contains(channel.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to convert "*" into java notation
     *
     * @param text    to match
     * @param pattern to match against
     *
     * @return true if matching
     */
    private boolean simpleMatch(String text, String pattern) {
        return text.matches(pattern.replace("*", ".*?"));
    }

    public Boolean isFishOnly() {
        return fishOnly;
    }

    public Boolean needsChannel() {
        return needsChannel;
    }
}
