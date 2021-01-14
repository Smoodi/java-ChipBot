package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.sql.MemberProfile;
import de.smoodi.projectchip.sql.SQLBridge;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class JoinLeaveListener extends ListenerAdapter {

    /**
     * Fired on a member joining.
     * @param event
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println("A member joined the discord.");

        try {
            boolean known = Main.sqlpr.existsUserEntry(event.getUser().getIdLong());

            if(known) {
                System.out.println("We know this user. Let's check it's certification status.");
                MemberProfile mp = Main.sqlpr.getMemberInformation(event.getUser().getIdLong());
                System.out.println("Looks like " + mp.getFirstJoinName() + " rejoined. He was " + (!mp.isCertified() ? "not " : "") + "certified.");

                //TOOD: ADD TEMPORARY ROC FUNCTIONALITY
                if(!mp.isBanned()) {
                    if(mp.isCertified()) {
                        event.getGuild().addRoleToMember(event.getUser().getIdLong(), event.getJDA().getRoleById(Main.mainConfig.getCertifiedRoleId())).queue();
                        System.out.println("We gave this user back his certification role.");
                    }

                    //Minecraft reauthentification.
                    if (Main.sqlmc.isDiscordLinked(event.getUser().getIdLong())) {
                        System.out.println("This member has linked their Minecraft before - We'll reactivate his account.");


                        Main.sqlmc.updateWhitelistedStatus(event.getUser().getIdLong(), true);

                    } else {
                        System.out.println("This member has no discord / minecraft account linked.");
                    }
                } else {
                    if(mp.isCertified()) {
                        System.out.println("This user has been banned. It will not be certified again.");
                    }
                }
            }
            else {
                Main.sqlpr.addUserEntry(event.getUser().getIdLong(), event.getUser().getName());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fired on a member leaving.
     * @param ev
     */
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent ev) {
        System.out.println("User " + ev.getUser().getIdLong() + " left..");

        try {
            if( Main.sqlmc.isDiscordLinked(ev.getUser().getIdLong())) {
                System.out.println("This member was having a linked account.. We'll unwhitelist it.");

                Main.sqlmc.updateWhitelistedStatus(ev.getUser().getIdLong(), false);
            }
            else {
                System.out.println("This member had no linked discord / minecraft.");
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
