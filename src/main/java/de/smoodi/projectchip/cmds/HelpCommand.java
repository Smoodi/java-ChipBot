package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends AbstractCommand {


    /**
     * Basic help command.
     * @param ev
     * @param args
     */
    @Override
    public void execute(MessageReceivedEvent ev, String[] args) {

        StringBuilder str = new StringBuilder();
        str.append(Util.randomTitleHelp());
        str.append("\n\nHere's a little help for you.\n" +
                "\n" +
                "**Prefix:** \n" + Main.PREFIX +
                "\n" +
                "__**Commands usable:**__\n" +
                "\n" +
                " - ``mc`` This command will allow you to add your minecraft account or change it's current setting.\n" +
                "\n" +
                "__**Conceptual commands in work:**__\n" +
                "\n" +
                " - ``gif`` This command will allow you to use a range of carefully moderated gifs out of a library in chats.\n" +
                " - ``gifsuggest`` This command will allow you to generate gifs based on YouTube videos and their timestamps. They will be suggested and stored.\n" +
                " - ``gifinfo`` This command will give you information about a gif out of the moderated gif-library, such as it's source, timestamp and more.\n");

        ev.getChannel().sendMessage(str.toString()).queue();

    }
}
