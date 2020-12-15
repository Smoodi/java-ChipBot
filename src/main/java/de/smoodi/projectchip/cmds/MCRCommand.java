package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class MCRCommand extends AbstractCommand{

    /**
     * This command removes the link between a mc account and a discord one.
     * @param ev
     * @param args
     */
    @Override
    public void execute(MessageReceivedEvent ev, String[] args) {
        try {
            if( Main.sqlmc.isDiscordLinked(ev.getAuthor().getIdLong()))
            {
                Main.sqlmc.deleteEntry(ev.getAuthor().getIdLong());
                ev.getChannel().sendMessage(":white_check_mark: Your account has successfully been unlinked.").queue();
            }
            else {
                ev.getChannel().sendMessage(":x: You have no account linked.").queue();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
