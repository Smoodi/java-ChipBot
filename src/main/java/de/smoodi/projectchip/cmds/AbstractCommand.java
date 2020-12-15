package de.smoodi.projectchip.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractCommand {

    /**
     * Abstract helper class - All Commands require to be executable.
     * @param ev
     * @param args
     */
    public abstract void execute(MessageReceivedEvent ev, String[] args);

    /**
     * Returns a string array with all executable aliases for the command to run with.
     * @return
     */
    public abstract String[] getAliases();

    /**
     * Used for the help command list.
     * @return
     */
    public abstract String getShortDescription();

    /**
     * Returns a short usage description.
     */
    public abstract String getUsage();

    /**
     * Returns a usage regex pattern - TODO
     */
    //public abstract String getPattern();

    /**
     * Returns a description for the help command for this command.
     */
    public abstract String getDescription();

    /**
     * Returns if the  command can be executed in it's current channel.
     * @param ev The event that fired this command.
     * @return
     */
    public boolean canBeExecuted(MessageReceivedEvent ev) {
        /**
         * Default implementation based on a global whitelist - can be overriden by individual commands.
         */

        return (CommandHandler.getDefaultWhitelist().isEnabled()) ? CommandHandler.getDefaultWhitelist().isWhitelisted(ev.getChannel()) : true;
    }

    /**
     * To be implemented.
     * @return
     */
    public int getCooldown() {

        return 0;
        //throw new NotImplementedException();
    }

}
