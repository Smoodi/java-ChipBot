package de.smoodi.projectchip.sql;

import java.sql.Timestamp;
import java.util.UUID;

public class MemberProfile {
    /**
     * Helper class filled with information received by a database command.
     * This represents all the data stored about a discord user.
     */
    private boolean isBanned;
    private boolean isCertified;
    private long discordId;
    private String firstJoinName;
    private Timestamp firstJoinTime;

    public MemberProfile(long discordId, boolean isCertified, String firstJoinName, Timestamp firstJoinTime, boolean isBanned) {
        this.isBanned = isBanned;
        this.isCertified = isCertified;
        this.firstJoinName = firstJoinName;
        this.firstJoinTime = firstJoinTime;
        this.discordId = discordId;
    }

    public boolean isBanned() { return isBanned; }
    public boolean isCertified() { return isCertified; }
    public long getDiscordId() { return discordId; }
    public String getFirstJoinName() { return firstJoinName; }
    public Timestamp getFirstJoinTime() { return firstJoinTime; }

}
