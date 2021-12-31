package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.cmds.CommandHandler;
import de.smoodi.projectchip.handler.CreativeChannelEventHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private CreativeChannelEventHandler _creativeSharing = new CreativeChannelEventHandler();
    private CreativeChannelEventHandler _chipSharing = new CreativeChannelEventHandler();

    /**
     * Fired when a message is received.
     * @param e
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        /**
         * Message handling
         */
        if (e.getChannel().getIdLong() == Main.mainConfig.getCreativeSharingChannel())
            _creativeSharing.handle(e);
        else if (e.getChannel().getIdLong() == Main.mainConfig.getChipSharingChannel())
            _chipSharing.handle(e);
        else {
            String msg = e.getMessage().getContentRaw();

            //Parsing the command.
            if (msg.startsWith(Main.mainConfig.getBotPrefix())) {
                int i = msg.indexOf(" ");
                if (i == -1) i = msg.length();

                String cmd = msg.substring(Main.mainConfig.getBotPrefix().length(), Math.max(Main.mainConfig.getBotPrefix().length(), i));

                final int min = Math.min(i + 1, msg.length());
                int argc = 0;
                String[] args = new String[0];
                if (msg.substring(i).indexOf(" ") != -1) {
                    args = msg.substring(min).split(" ");
                    argc = args.length;
                }

                System.out.println("We received (COMMAND): " + cmd + " and " + argc + " arguments: ");
                for (int ii = 0; ii < argc; ii++) {
                    System.out.println(" - " + args[ii]);
                }

                CommandHandler.executeCommand(cmd, e, args);
            }
        }
    }


}
