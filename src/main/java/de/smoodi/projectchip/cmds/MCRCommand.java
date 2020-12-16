package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class MCRCommand extends AbstractCommand{

    @Override
    public String[] getAliases() {
        return new String[] {
                "mcr",
                "minecraftremove"
        };
    }

    @Override
    public String getShortDescription() {
        return "Used for removing the linked minecraft account.";
    }

    @Override
    public String getUsage() {
        return "\nUse `" + Main.mainConfig.getBotPrefix() + "mcr` to remove your linked minecraft account";
    }

    @Override
    public String getDescription() {
        return "This command allows you to remove your linked minecraft account, thus removing it from the whitelist.";
    }


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
