package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends AbstractCommand {


    @Override
    public String[] getAliases() {
        return new String[] {
                "help",
            "?"
        };
    }

    @Override
    public String getShortDescription() {
        return "Used to receive help about the bot or its commands.";
    }

    @Override
    public String getUsage() {
        return "\nUse this command to get help about which command are available. Get further help about specific commands by entering them as an argument.\nFor example "
                + Main.mainConfig.getBotPrefix() + "help mc";
    }

    @Override
    public String getDescription() {
        return "This command will allow you to get further information about any command usable.";
    }

    /**
     * Basic help command.
     * @param ev
     * @param args
     */
    @Override
    public void execute(MessageReceivedEvent ev, String[] args) {

        if(args.length < 1) {
        StringBuilder str = new StringBuilder();
        str.append(Util.randomTitleHelp());
        str.append("\n\nHere's a little help for you.\n" +
                "\n" +
                "**Prefix:** \n" + Main.mainConfig.getBotPrefix() +
                "\n" +
                "__**Commands usable:**__\n" +
                "\n");

        for (AbstractCommand cmd : CommandHandler.getCommands()) {
            str.append(" `" + cmd.getAliases()[0] + "`: " + cmd.getShortDescription() + "\n");
        }

        str.append(
                "\n" +
                "__**Conceptual commands in work:**__\n" +
                "\n" +
                " - `gif` This command will allow you to use a range of carefully moderated gifs out of a library in chats.\n" +
                " - `gifsuggest` This command will allow you to generate gifs based on YouTube videos and their timestamps. They will be suggested and stored.\n" +
                " - `gifinfo` This command will give you information about a gif out of the moderated gif-library, such as it's source, timestamp and more.\n");

        ev.getChannel().sendMessage(str.toString()).queue();

        }
        else {
            AbstractCommand cmd = CommandHandler.getCommand(args[0]);
            if(cmd != null) {
                StringBuilder str = new StringBuilder();
                str.append(Util.randomTitleHelp());
                str.append("\n\nHere's a little help for you.\n" +
                        "\n" +
                        "__**Information about the " + cmd.getAliases()[0] + " command:**__ "+
                        "\n" +
                        "**Usage:** " + Main.mainConfig.getBotPrefix() + cmd.getAliases()[0] + " " + cmd.getUsage() +
                        "\n");
                str.append("**Cooldown:** " + getCooldown() + " seconds.\n");
                str.append("**Aliases:** ");
                String[] al = cmd.getAliases();
                for(int i = 1; i < al.length; i++) {
                    str.append(al[i]);
                    if(i != al.length - 1) str.append(", ");
                }
                str.append("\n\n**Description:**\n" + cmd.getDescription());

                ev.getChannel().sendMessage(str.toString()).queue();
            }
            else {
                ev.getChannel().sendMessage(":x: Command not found. Please enter " + Main.mainConfig.getBotPrefix() + "help for all commands.").queue();
            }
        }
    }

}
