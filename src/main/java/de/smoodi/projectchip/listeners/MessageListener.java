package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.cmds.AbstractCommand;
import de.smoodi.projectchip.cmds.HelpCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageListener extends ListenerAdapter {

    private Map<String, AbstractCommand> commandsExecs = new HashMap<String, AbstractCommand>();
    private final AbstractCommand DEFAULT_CMD = new HelpCommand();

    /**
     * Registers a new text command to the command handling.
     * @param cmd the cmd - case ignored.
     * @param exec the Command executioner.
     */
    public void registerTextCommand(String cmd, AbstractCommand exec) {
        if(!this.commandsExecs.containsKey(cmd.toLowerCase()))
            this.commandsExecs.put(cmd.toLowerCase(), exec);
    }

    /**
     * Fired when a message is received.
     * @param e
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        String msg = e.getMessage().getContentRaw();

        //Parsing the command.
        if(msg.startsWith(Main.PREFIX)) {
            int i = msg.indexOf(" ");
            if (i == -1) i = msg.length();

            String cmd = msg.substring(Main.PREFIX.length(), Math.max(Main.PREFIX.length(), i));

            final int min = Math.min(i + 1, msg.length());
            int argc = 0;
            String[] args = new String[0];
            if(msg.substring(i).indexOf(" ") != -1) {
                args = msg.substring(min).split(" ");
                argc = args.length;
            }

            System.out.println("We received (COMMAND): " + cmd + " and " + argc + " arguments: ");
            for (int ii=0;ii<argc;ii++) {
                System.out.println(" - " + args[ii]);
            }

            selectCommand(cmd, e, args);
        }

    }

    /**
     * Selects and executes the appropiate command. Default command help.
     * @param cmd the cmd string.
     * @param event the event passed along.
     * @param args the arguments provided.
     */
    private void selectCommand(String cmd, MessageReceivedEvent event, String[] args) {

        if(commandsExecs.containsKey(cmd.toLowerCase())) {
            commandsExecs.get(cmd.toLowerCase()).execute(event, args);
        }
        else {
            DEFAULT_CMD.execute(event, args);
        }
    }

}
