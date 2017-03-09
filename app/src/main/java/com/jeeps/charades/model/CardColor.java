package com.jeeps.charades.model;

/**
 * Created by jeeps on 3/5/2017.
 */

public enum CardColor {
    PINK("#f06292"),
    PURPLE("#ba68c8"),
    CYAN("#4dd0e1"),
    GREEN("#81c784"),
    TEAL("#1de9b6"),
    ORANGE("#ffb74d");

    private final String hexCode;

    CardColor(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}
