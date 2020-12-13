package de.smoodi.projectchip.listeners;

import de.smoodi.projectchip.Main;
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
        System.out.println("New / Old member (re-)joined...");

        try {
            if( Main.sqlmc.isDiscordLinked(event.getUser().getIdLong())) {
                System.out.println("This member was active in here before - We'll reactivate his account.");


                Main.sqlmc.updateWhitelistedStatus(event.getUser().getIdLong(), true);

            }
            else {
                System.out.println("This member has no discord / minecraft account linked.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
