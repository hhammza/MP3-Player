package com.progmeleon.musicplayer2;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String title, path;

    public AudioModel(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
