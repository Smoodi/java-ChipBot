package de.smoodi.projectchip.handler;

import java.time.OffsetDateTime;

public class UserPostToken {

    private OffsetDateTime postTime;
    private long mediaPost;

    public UserPostToken(OffsetDateTime postTime, long mediaPost ){

        this.postTime = postTime;
        this.mediaPost = mediaPost;

    }

    public OffsetDateTime getPostTime() {
        return postTime;
    }

    public long getMediaPost() {
        return mediaPost;
    }


}
