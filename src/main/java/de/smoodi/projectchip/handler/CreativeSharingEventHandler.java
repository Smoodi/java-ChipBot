package de.smoodi.projectchip.handler;

import de.smoodi.projectchip.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CreativeSharingEventHandler {

    private static HashMap<Long, UserPostToken> permittedDescriptions = new HashMap<Long, UserPostToken>();

    private final static Pattern pattern = Pattern.compile("(.)*((http|https):\\/\\/(www.|)[a-zA-Z0-9]+(.[a-zA-Z0-9]+)+\\/([a-zA-Z0-9]+(\\/|))*)+(.)*", Pattern.MULTILINE | Pattern.DOTALL);
    //private final static Pattern pattern = Pattern.compile("(.*)?(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)(.*)?", Pattern.MULTILINE | Pattern.DOTALL);

    private static boolean isPermitted(Long userId) {
        if(permittedDescriptions.containsKey(userId)) {
            if(Duration.between(permittedDescriptions.get(userId).getPostTime(), OffsetDateTime.now()).toMinutes() < 5) {
                System.out.println("The time past between the media post and the message sent now was < 5 minutes. We let it pass.");
                return true;
            }
            else {
                //Automatic cleanup.
                System.out.println("The time past between the media post and the message sent now was more than 5 minutes.. (User: " + userId+ ")");
                revokePermission(userId);
                return false;
            }
        }
        else return false;
    }

    public static void revokePermission(Long userId) {
        System.out.println("We revoked user id " + userId + " 's creative channel permissions.");
        permittedDescriptions.remove(userId);
    }

    public static void handle(MessageReceivedEvent e) {

        //Is MediaPost?

        POST_ERROR post_error = (isMediaPost(e.getMessage()));
        boolean isMedia = ( post_error == POST_ERROR.SUCCESS_ONLINE || post_error == POST_ERROR.SUCCESS_ATTACHEMENT );

        System.out.println("Latest message: \"" + e.getMessage().getContentDisplay().substring(0,Math.min(e.getMessage().getContentDisplay().length(),15))+ " is MEDIA POST: " + String.valueOf(isMedia));

        if (isMedia){
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
                            "Please make sure the messages you send fit the following criteria: \n"+
                            " - They contain 1 attachment / image max. per message sent\n"+
                            " - You sent 3 messages max. in a row (only 3 images total in a row)\n"+
                            " - Livestream links should be posted to a relevant community channel to reduce spam.\n" +
                            " - Your description message (must still not be more than 3 messages total) must be sent within 5 minutes after sending your image / creative content.\n\n" +
                            "For further information, please contact a moderator or member of the staff team or consult the rules.\n" +
                            "This action has been logged. Usually there is no further action taken.\n" +
                            "If you think the automatic deletion of your post was wrong or un-rightfully so, please contact a member of the staff team " +
                            "or Smoodi for bot purposes directly.\n").queue();
                });
                e.getGuild().getTextChannelById(Main.mainConfig.getModLogChannel()).sendMessage("**User ID:** " + e.getAuthor().getIdLong() +" ("+ e.getAuthor().getName() +")\n" +
                        "**Action taken:** automatic verbal warning\n" +
                        "**Reason:** chatting in <#"+ Main.mainConfig.getCreativeSharingChannel()+">\n" +
                        "**Automatic bot flag triggered:** "+post_error.name()+"\n" +
                        "**Attached files count:** "+e.getMessage().getAttachments().size()+"\n" +
                        "**Original message:** ``" + trimMessage(e.getMessage().getContentDisplay(), 255) + "``").queue();
                e.getMessage().delete().queue();
            }
            else {
                revokePermission(e.getAuthor().getIdLong());
            }
        }
    }

    private static void grantCommentPermission(User author, Message message) {
        System.out.println("The user " +author.getName() + " ( " + author.getIdLong() + " ) is now authorised for 5 minutes to send a message.");
        permittedDescriptions.put(author.getIdLong(), new UserPostToken(message.getTimeCreated(), message.getIdLong()));
    }

    private static POST_ERROR isMediaPost(Message e) {
        //We make sure it was not too many messages in a row
        if (isLastMessagesAuthor(3, e.getChannel(), e.getAuthor())) return POST_ERROR.TOO_MANY_MESSAGES;
        //Make sure it is no livestream.
        if (e.getContentDisplay().toLowerCase().contains("twitch.tv")) return POST_ERROR.LIVESTREAM;
        if (e.getContentDisplay().toLowerCase().contains("picarto.tv")) return POST_ERROR.LIVESTREAM;
        if (e.getContentDisplay().toLowerCase().contains("piczel.tv")) return POST_ERROR.LIVESTREAM;

        if(e.getAttachments().size() > 1) return POST_ERROR.TOO_MANY_ATTACHEMENTS;
        else if (pattern.matcher(e.getContentDisplay()).matches()) {
            //It at least contains a web post.

            //Make sure it does not contain an attachements as well
            return (e.getAttachments().size() != 0) ? POST_ERROR.ATTACHEMENT_AND_LINK : POST_ERROR.SUCCESS_ONLINE;
        } else {
            return (e.getAttachments().size() == 1) ? POST_ERROR.SUCCESS_ATTACHEMENT : POST_ERROR.NO_MEDIA;
        }
        /*
        return (e.getAttachments().size() == 1 ||
                pattern.matcher(e.getContentDisplay()).matches())
                && !isLastMessagesAuthor(3, e.getChannel(), e.getAuthor())
                && !e.getContentDisplay().toLowerCase().contains("twitch.tv");
                */
    }

    private static boolean isLastMessagesAuthor(int count, MessageChannel channel, User author) {
        MessageHistory _m = channel.getHistory();
        _m.retrievePast(count).complete();
        List<Message> history = _m.getRetrievedHistory();
        User u = history.get(0).getAuthor();
        System.out.println("Last message (" + "0" + ") is by: " + u.getName());
            for(int i = 1; i < history.size(); i++ ) {
                User _u = history.get(i).getAuthor();
                System.out.println("Last message (" + i + ") is by: " + u.getName());
                if (u.getIdLong() != _u.getIdLong()) return false;
                else u = _u;
            };
        return true;
    }

    public enum POST_ERROR {
        NO_MEDIA,
        SUCCESS_ONLINE,
        SUCCESS_ATTACHEMENT,
        TOO_MANY_MESSAGES,
        ATTACHEMENT_AND_LINK,
        TOO_MANY_ATTACHEMENTS,
        LIVESTREAM
    }

    private static String trimMessage(String msg, int maxLength) {
        if(msg == "") return "EMPTY_MSG";
        return (msg.length() > maxLength) ? msg.substring(0,maxLength-1) + "..." : msg;
    }

}
