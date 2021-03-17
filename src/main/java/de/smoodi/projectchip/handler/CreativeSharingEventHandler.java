package de.smoodi.projectchip.handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;

public class CreativeSharingEventHandler {

    private static HashMap<Long, UserPostToken> permittedDescriptions = new HashMap<Long, UserPostToken>();

    private static boolean isPermitted(Long userId) {
        if(permittedDescriptions.containsKey(userId)) {
            if(Duration.between(permittedDescriptions.get(userId).getPostTime(), OffsetDateTime.now()).toMinutes() < 5)
                return true;
            else {
                //Automatic cleanup.
                permittedDescriptions.remove(userId);
                return false;
            }
        }
        else return false;
    }

    public static void revokePermission(Long userId) {
        permittedDescriptions.remove(userId);
    }

    public static void handle(MessageReceivedEvent e) {

        //Is MediaPost?

        if (isMediaPost(e.getMessage())){
            grantCommentPermission(e.getAuthor(), e.getMessage());
        }
        else {

            //No media post:
            //Check if the user has a permission.
            if(!isPermitted(e.getAuthor().getIdLong()))
            {
                e.getAuthor().openPrivateChannel().queue((channel) ->
                {
                    channel.sendMessage("**Hello " + e.getAuthor().getName()+ ",**\nWe've reached out to inform you that we have deleted your message in #creative-sharing on " +
                            "Chipflake's Catto Club discord. Creative sharing is intended to be used for image and art posts only. You are permitted to add a single description message after a media post if this message is being posted within 5 minutes.\n"+
                            "For further information, please contact a moderator or member of the staff team or consult the rules.\n" +
                            "This action has been logged. Usually there is no further action taken.").queue();
                });
                e.getGuild().getTextChannelById(537704220128837634L).sendMessage("User ID: " + e.getAuthor().getIdLong() +" ("+ e.getAuthor().getName() +")\n" +
                        "Action taken: automatic verbal warning\n" +
                        "Reason: chatting in <#522050963658244096>").queue();
                e.getMessage().delete().queue();
            }
            else {
                revokePermission(e.getAuthor().getIdLong());
            }
        }
    }

    private static void grantCommentPermission(User author, Message message) {
        permittedDescriptions.put(author.getIdLong(), new UserPostToken(message.getTimeCreated(), message.getIdLong()));
    }

    private static boolean isMediaPost(Message e) {
        return e.getAttachments().size() > 0 || e.getContentDisplay().matches(
                ".*?(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?(.*)?"
        );
    }

}
