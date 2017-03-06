package com.jeeps.charades.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import static com.jeeps.charades.SetupGame.*;

/**
 * Created by jeeps on 2/28/2017.
 */
public class CustomTextView extends TextView {

    public static final String ARCHITECTS_DAUGHTER = "fonts/ArchitectsDaughter.ttf";
    public static final String GLORIA_HALLELUJAH = "fonts/GloriaHallelujah.ttf";
    public static final String INDIE_FLOWER = "fonts/IndieFlower.ttf";

    private static String currentFont;


    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            String font = GLORIA_HALLELUJAH;
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
            currentFont = font;
            setTypeface(tf);;
        }
    }

    public static String getFont() {
        return currentFont;
    }

}
