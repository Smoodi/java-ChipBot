package de.smoodi.projectchip.util;

import de.smoodi.projectchip.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BotStatusMessage {

    public static final byte ACTIVITY_WATCHING = 0;
    public static final byte ACTIVITY_PLAYING = 1;
    public static final byte ACTIVITY_LISTENING = 2;
    public static final byte ACTIVITY_STREAMING = 3;

    private String msg;
    private byte type;
    private String suffix;
    private String prefix;
    private String url;

    public BotStatusMessage(byte type, String msg) {
        this.msg = msg;
        this.type = type;
        this.suffix = Main.mainConfig.getBotPrefix() + "help";
    }

    public BotStatusMessage(byte type, String prefix, String msg) {
        this.msg = msg;
        this.type = type;
        this.prefix = prefix;
        this.suffix = Main.mainConfig.getBotPrefix() + "help";
    }

    public BotStatusMessage(byte type, String prefix, String msg, String suffix) {
        this.msg = msg;
        this.type = type;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public BotStatusMessage(byte type, String prefix, String msg, String suffix, String url) {
        this.msg = msg;
        this.type = type;
        this.prefix = prefix;
        this.suffix = suffix;
        this.url = url;
    }

    public Activity compile() {
        switch (type) {
            case ACTIVITY_WATCHING:
                return Activity.watching( "" + ((prefix == null) ? "" : prefix + " | ") + msg + ((suffix == null) ? "" : " | " + suffix));
            case ACTIVITY_PLAYING:
                return Activity.playing( "" + ((prefix == null) ? "" : prefix + " | ") + msg + ((suffix == null) ? "" : " | " + suffix));
            case ACTIVITY_LISTENING:
                return Activity.listening( "" + ((prefix == null) ? "" : prefix + " | ") + msg + ((suffix == null) ? "" : " | " + suffix));
            case ACTIVITY_STREAMING:
                return Activity.streaming( "" + ((prefix == null) ? "" : prefix + " | ") + msg + ((suffix == null) ? "" : " | " + suffix), url);
            default:
                msg = "sad videos because something crashed and needs to be fixed! :(";
                return Activity.watching( "" + ((prefix == null) ? "" : prefix + " | ") + msg + ((suffix == null) ? "" : " | " + suffix));
        }
    }
}