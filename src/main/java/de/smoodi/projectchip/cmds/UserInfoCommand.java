package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class UserInfoCommand extends AbstractCommand {


    @Override
    public String[] getAliases() {
        return new String[] {
                "userinfo",
                "useri",
                "uinfo",
                "me"
        };
    }

    @Override
    public String getShortDescription() {
        return "Used to see user info of a server member/the command user";
    }

    @Override
    public String getUsage() {
        return "\nUse `" + Main.mainConfig.getBotPrefix() + "userinfo <user-mention/user-id>` to view information about a specific user" +
               "\nUse `" + Main.mainConfig.getBotPrefix() + "userinfo` to view user information about yourself";
    }

    @Override
    public String getDescription() {
        return "This command will allow you to view server information of any user in the server, including yourself.";
    }

    /**
     * Basic help command.
     * @param ev
     * @param args
     */
    @Override
    public void execute(MessageReceivedEvent ev, String[] args) {
        StringBuilder sbConceptual = new StringBuilder();

        String ebIcon = ev.getGuild().getIconUrl();
        Color ebColor = new Color(0,153,255);
        Guild guild = ev.getGuild();
        Member member = null;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(ebColor);
        eb.setThumbnail(ebIcon);
        eb.setTimestamp(Instant.now());
        eb.setFooter(Util.randomTitleHelp(),ebIcon);

        if (args.length < 1) {
            member = ev.getMessage().getMember();
        } else {
            if (ev.getMessage().getMentionedMembers().size() > 0) {
                member = ev.getMessage().getMentionedMembers().get(0);
            } else {
                if(args[0].matches("\\d*")) {
                    long uid = Long.parseLong(args[0]);
                    member = guild.getMemberById(uid);
                }
                else {
                    ev.getChannel().sendMessage(":x: Invalid user id. Try tagging instead.").queue();
                }
            }
        }

        if(member != null) {
            eb.setTitle("User Specifics: "+member.getEffectiveName());

            eb.setThumbnail(member.getUser().getAvatarUrl());
            eb.setFooter(Util.randomTitleHelp(),ebIcon);

            eb.addField("ID", member.getId(), false);
            eb.addField("Username", member.getEffectiveName(), false);
            eb.addField("Join Date", member.getTimeJoined().toString(), false);

        } else {
            eb.addField("User not found", "This user does not exist", false);
        }

        ev.getChannel().sendMessage(eb.build()).queue();

    }

}
