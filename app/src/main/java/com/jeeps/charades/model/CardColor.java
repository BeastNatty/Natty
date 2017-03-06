package com.jeeps.charades.model;

/**
 * Created by jeeps on 3/5/2017.
 */

public enum CardColor {
    PINK("#f48fb1"),
    PURPLE("#ce93d8"),
    CYAN("#80deea"),
    GREEN("#a5d6a7"),
    LIME("#e6ee9c"),
    ORANGE("#ffcc80");

    private final String hexCode;

    CardColor(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}
