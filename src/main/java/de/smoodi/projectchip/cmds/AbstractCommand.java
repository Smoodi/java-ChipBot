package de.smoodi.projectchip.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractCommand {

    /**
     * Abstract helper class - All Commands require to be executable.
     * @param ev
     * @param args
     */
    public abstract void execute(MessageReceivedEvent ev, String[] args);

}
