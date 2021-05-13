package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.cmds.AbstractCommand;
import de.smoodi.projectchip.cmds.CommandHandler;
import de.smoodi.projectchip.cmds.HelpCommand;
import de.smoodi.projectchip.handler.CreativeSharingEventHandler;
import de.smoodi.projectchip.util.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageListener extends ListenerAdapter {

    /**
     * Fired when a message is received.
     * @param e
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        /**
         * Message handling
         */
        if (e.getChannel().getIdLong() == Main.mainConfig.getCreativeSharingChannel()) {
            //This is our creative sharing chat.
            CreativeSharingEventHandler.handle(e);
        }
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
