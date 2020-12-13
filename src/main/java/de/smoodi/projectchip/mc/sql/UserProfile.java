package de.smoodi.projectchip.mc.sql;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Helper class filled with information received by a database command.
 */
public class UserProfile {
    private boolean isBanned;
    private UUID uuid;
    private boolean isWhitelisted;
    private long discordId;
    private String nick;
    private Timestamp time_added;

    public UserProfile(UUID uuid, long discordId, boolean isWhitelisted, String nick, Timestamp time_added, boolean isBanned) {
        this.isBanned = isBanned;
        this.isWhitelisted = isWhitelisted;
        this.uuid = uuid;
        this.nick = nick;
        this.time_added = time_added;
        this.discordId = discordId;
    }

    public boolean isBanned() { return isBanned; }
    public UUID getUuid() { return uuid; }
    public boolean isWhitelisted() { return isWhitelisted; }
    public long getDiscordId() { return discordId; }
    public String getNick() { return nick; }
    public Timestamp getTime_added() { return time_added; }
}