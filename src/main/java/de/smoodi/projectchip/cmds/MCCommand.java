package de.smoodi.projectchip.cmds;

import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.util.MinecraftUtil;
import de.smoodi.projectchip.mc.sql.UserProfile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.util.UUID;

public class MCCommand extends AbstractCommand{

    @Override
    public String[] getAliases() {
        return new String[] {
                "mc",
                "minecraft"
        };
    }

    @Override
    public String getShortDescription() {
        return "Used for adding and changing your linked minecraft account as well as viewing the current settings.";
    }

    @Override
    public String getUsage() {
        return "\nUse `" + Main.mainConfig.getBotPrefix() + "mc <minecraft-username>` to add or change your current minecraft account linked." +
               "\nUse `" + Main.mainConfig.getBotPrefix() + "mc` to see info about your currently linked one.";
    }

    @Override
    public String getDescription() {
        return "This command allows you to add and change your linked minecraft account.";
    }


    /**
     * This command deals with all cases of adding and readding the user's mc link.
     * @param ev
     * @param args
     */
    @Override
    public void execute(MessageReceivedEvent ev, String[] args) {

        try {
            if (args.length < 1) {
                if (Main.sqlmc.isDiscordLinked((ev.getAuthor().getIdLong()))) {
                    UserProfile p = Main.sqlmc.getEntryInformation(ev.getAuthor().getIdLong());
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Minecraft ist already linked!");
                    eb.setThumbnail("http://dl.smoodi.de/server-icon.png");
                    eb.setDescription("Your discord account is already linked with a Minecraft account.\nIf you want to alter the linked account please write\n - " + Main.mainConfig.getBotPrefix() + "mc <minecraftusername>");

                    eb.addField("Current Minecraft linked: ", MinecraftUtil.getPlayerName(p.getUuid().toString()), false);
                    eb.setImage("https://crafatar.com/renders/body/" + p.getUuid().toString());
                    eb.setFooter("Copyright (c) 2020 - All rights reserved. Minecraft is owned by Mojang AB - Bot provided by Smoodi");

                    ev.getChannel().sendMessageEmbeds(eb.build()).queue();
                } else {

                    ev.getChannel().sendMessage("There is no minecraft account associated with your discord account yet. Please enter ``" + Main.mainConfig.getBotPrefix() + "mc <username>`` to bind an account.").queue();
                }
            }
            else {

                String uuid = MinecraftUtil.getUUIDOfUsername(args[0]);
                if( uuid == null ) ev.getChannel().sendMessage(":x: **We could not find a minecraft account associated with this username.**").queue();
                else {
                    uuid = uuid.substring(0,8) + "-" + uuid.substring(8,12) + "-" + uuid.substring(12,16) + "-" + uuid.substring(16,20) + "-" + uuid.substring(20);
                    System.out.println("UUID: " + uuid);
                    if (Main.sqlmc.isDiscordLinked(ev.getAuthor().getIdLong())) {
                        UserProfile claim = Main.sqlmc.getEntryInformation(uuid);
                        if (claim != null) {
                            ev.getChannel().sendMessage(":x: **It seems as if this username was already claimed** by <@" + claim.getDiscordId() + ">.\n" +
                                    "If you think this claim was not rightfully so please contact an admin about this issue.\n" +
                                    "Please note that false claiming can lead to your accounts rights being revoked.").queue();
                        }
                        else{
                            Main.sqlmc.updateMinecraftAccount(ev.getAuthor().getIdLong(), UUID.fromString(uuid));
                            ev.getChannel().sendMessage(":white_check_mark: **Successfully linked with " + args[0]+".**\nPlease note that your previous minecraft account" +
                                    " will no longer be able to join the server unless reclaimed.").queue();
                        }
                    } else {
                        UserProfile claim = Main.sqlmc.getEntryInformation(uuid);
                        if (claim != null) {
                            ev.getChannel().sendMessage(":x: **It seems as if this username was already claimed** by <@" + claim.getDiscordId() + ">.\n" +
                                    "If you think this claim was not rightfully so please contact an admin about this issue.\n" +
                                    "Please note that false claiming can lead to your accounts rights being revoked.").queue();
                        }
                        else{
                            Main.sqlmc.addMinecraftAccount(UUID.fromString(uuid), ev.getAuthor().getIdLong());
                            ev.getChannel().sendMessage(":white_check_mark: **Successfully linked with " + args[0]+".**\nPlease note that false claiming can lead to bans or other forms of restrictions\n" +
                                    "To check your currently linked account type in ``" + Main.mainConfig.getBotPrefix() + "mc`` without any further arguments.\n" +
                                    "To change your currently linked account just reuse this command like you did now.\n").queue();
                        }
                    }
                }
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
