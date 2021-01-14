    package de.smoodi.projectchip;

import de.smoodi.projectchip.cmds.CommandHandler;
import de.smoodi.projectchip.cmds.HelpCommand;
import de.smoodi.projectchip.cmds.MCCommand;
import de.smoodi.projectchip.cmds.MCRCommand;
import de.smoodi.projectchip.listeners.CertificationListener;
import de.smoodi.projectchip.listeners.JoinLeaveListener;
import de.smoodi.projectchip.listeners.MessageListener;
import de.smoodi.projectchip.sql.SQLBridge;
import de.smoodi.projectchip.mc.sql.SQLMCBridge;
import de.smoodi.projectchip.util.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Main class for the bot to boot in.
 */

public class Main {

    /**
     * Boots the bot arguments expected are the bot token.
     **/

    public static SQLMCBridge sqlmc;
    public static SQLBridge sqlpr;
    public static Config mainConfig;

    public static void main(String[] args) {

        //We build our config
        mainConfig = new Config();
        try {
            if(!mainConfig.load("config.yml")) {
                System.err.println("Error loading config.");
                return;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        sqlmc = new SQLMCBridge(mainConfig.getMcdbhost(), String.valueOf(mainConfig.getMcdbport()), mainConfig.getMcdbname(),
                mainConfig.getMcdbuser(), mainConfig.getMcdbpw());

        sqlpr = new SQLBridge(mainConfig.getCoredbhost(), String.valueOf(mainConfig.getCoredbport()), mainConfig.getCoredbname(),
                mainConfig.getCoredbuser(), mainConfig.getCoredbpw());


        //We would our bot.
        JDABuilder builder = JDABuilder.createDefault(mainConfig.getBotToken());

        //We set basic settings.
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setAutoReconnect(true);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        //We choose a random but cool text.
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                builder.setActivity(Activity.watching("Chipflake videos."));
                break;
            case 1:
                builder.setActivity(Activity.watching("over the server..."));
                break;
            case 2:
                builder.setActivity(Activity.listening("Chipflake intro music on repeat."));
            case 3:
                builder.setActivity(Activity.playing("Hide and seek but is very bad at it."));
            case 4:
                builder.setActivity(Activity.watching("nothing"));
        }

        //Building JDA.
        JDA jda;
        try {
            jda = builder.build();
            jda.awaitReady();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Registering commands.
        //For the init.
        new CommandHandler();
        CommandHandler.registerTextCommand(new HelpCommand());
        CommandHandler.registerTextCommand(new MCCommand());
        CommandHandler.registerTextCommand(new MCRCommand());

        CommandHandler.getDefaultWhitelist().whitelist(539057336904450058L); // ?
        CommandHandler.getDefaultWhitelist().whitelist(542064174826520582L); // Botto
        CommandHandler.getDefaultWhitelist().whitelist(550080151040294922L); // Botto testo

        //Dumping listeners.
        jda.addEventListener(new MessageListener());
        jda.addEventListener(new JoinLeaveListener());
        jda.addEventListener(new CertificationListener());

    }

}
