package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.sql.MemberProfile;
import de.smoodi.projectchip.util.Config;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class CertificationListener extends ListenerAdapter {

    /**
     * Fired when a new message reaction is added.
     * @param event
     */
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if(event.getChannel().getIdLong() != Main.mainConfig.getRulesChannelId()) return;
        if(event.getMessageIdLong() == Main.mainConfig.getExcludedRoleReactionMsgId0()) return;
        if(event.getMessageIdLong() == Main.mainConfig.getExcludedRoleReactionMsgId1()) return;
        if(!event.getReactionEmote().equals(MessageReaction.ReactionEmote.fromUnicode("\uD83D\uDC7D", event.getJDA()))) { System.out.println("Wrong emote."); return; }
        try {
            if (!Main.sqlpr.existsUserEntry(event.getUser().getIdLong())) {
                Main.sqlpr.addUserEntry(event.getUser().getIdLong(), event.getUser().getName());
            }
            MemberProfile mb = Main.sqlpr.getMemberInformation(event.getUserIdLong());
            if (mb.isBanned()) {
                System.out.println("User " + event.getUser() + " [" + event.getUserId() + "] tried to certify but is banned.");
            } else {
                System.out.println("User " + event.getUser() + " [" + event.getUserId() + "] certified themselves.");
                Main.sqlpr.setCertification(event.getUserIdLong(), true);
                event.getGuild().addRoleToMember(event.getUser().getIdLong(), event.getJDA().getRoleById(Main.mainConfig.getCertifiedRoleId())).queue();
                event.getTextChannel().retrieveMessageById(event.getMessageId()).queue((message) -> {
                    message.clearReactions("\uD83D\uDC7D").queue();
                }, (failure) -> {
                    //if the message somehow does not exist?!
                    System.err.println("I can smell it.. The code smell is real!");
                });
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
            System.err.println("Retrying...");
            Main.sqlpr.establishConnection();
            event.getGuild().getTextChannelById(Main.mainConfig.getModdoChatto()).sendMessage("**Informations for the moderators:** \nSomeone attempted to certify themselves but the connection to the database failed.\n" +
                    "Hence it was not possible for me to figure out whether or not they were banned. Please manually certify them.").queue();
            System.err.println("We were not able to certify a user - logged.");
        }
    }

}
