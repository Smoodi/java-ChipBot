package de.smoodi.projectchip.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Config {

    private String botToken;
    private String botVersion;
    private String botPrefix;

    private String mcdbhost;
    private String mcdbname;
    private String mcdbuser;
    private String mcdbpw;
    private int mcdbport;

    private String coredbhost;
    private String coredbname;
    private String coredbuser;
    private String coredbpw;
    private int coredbport;

    private long rulesChannelId;
    private long certifiedRoleId;
    private long excludedRoleReactionMsgId0;
    private long excludedRoleReactionMsgId1;
    private long creativeSharingChannel;
    private long moddoChatto;
    private long modLogChannel;

    public static final String serverTimezone = "Europe/Berlin";

    public Config() {
    }

    public boolean load(String filename) throws FileNotFoundException {

        File cfgFile = new File(filename);
        if(!cfgFile.exists()) return false;
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(cfgFile);

        Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);

        botToken = (String) obj.get("botToken");
        botVersion = (String)  obj.get("botVersion");
        botPrefix = (String) obj.get("botPrefix");

        mcdbhost = (String) obj.get("whitelistDBHost");
        mcdbname = (String) obj.get("whitelistDBName");
        mcdbport = (int) obj.get("whitelistDBPort");
        mcdbuser = (String) obj.get("whitelistDBUser");
        mcdbpw = (String) obj.get("whitelistDBPassword");

        coredbhost = (String) obj.get("primaryDBHost");
        coredbname = (String) obj.get("primaryDBName");
        coredbport = (int) obj.get("primaryDBPort");
        coredbuser = (String) obj.get("primaryDBUser");
        coredbpw = (String) obj.get("primaryDBPassword");

        certifiedRoleId = (long) obj.get("certifiedRoleId");
        rulesChannelId = (long) obj.get("rulesChannelId");
        excludedRoleReactionMsgId0 = (long) obj.get("excludedRoleReactionMsgId0");
        excludedRoleReactionMsgId1 = (long) obj.get("excludedRoleReactionMsgId1");
        moddoChatto = (long) obj.get("modChannelId");
        creativeSharingChannel = (long) obj.get("creativeSharingChannelId");
        modLogChannel = (long)obj.get("modLogChannelId");

        return true;

    }

    public String getBotToken() { return botToken; }

    public String getBotVersion() { return botVersion; }

    public String getBotPrefix() { return botPrefix; }


    public String getMcdbhost() {
        return mcdbhost;
    }

    public String getMcdbname() {
        return mcdbname;
    }

    public String getMcdbuser() {
        return mcdbuser;
    }

    public String getMcdbpw() {
        return mcdbpw;
    }

    public int getMcdbport() {
        return mcdbport;
    }


    public String getCoredbhost() {
        return coredbhost;
    }

    public String getCoredbname() {
        return coredbname;
    }

    public String getCoredbuser() {
        return coredbuser;
    }

    public String getCoredbpw() {
        return coredbpw;
    }

    public int getCoredbport() {
        return coredbport;
    }

    public long getCertifiedRoleId() {
        return certifiedRoleId;
    }

    public long getRulesChannelId() { return rulesChannelId; }

    public long getExcludedRoleReactionMsgId() { return excludedRoleReactionMsgId0; }
    public long getExcludedRoleReactionMsgId2() { return excludedRoleReactionMsgId1; }

    public long getModdoChatto() {
        return moddoChatto;
    }

    public void setModdoChatto(long moddoChatto) {
        this.moddoChatto = moddoChatto;
    }

    public long getCreativeSharingChannel() {
        return creativeSharingChannel;
    }

    public void setCreativeSharingChannel(long creativeSharingChannel) {
        this.creativeSharingChannel = creativeSharingChannel;
    }

    public long getModLogChannel() {
        return modLogChannel;
    }

    public void setModLogChannel(long modLogChannel) {
        this.modLogChannel = modLogChannel;
    }
}
