package com.jeeps.charades.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeps.charades.R;
import com.jeeps.charades.model.CardColor;

/**
 * Created by jeeps on 3/11/2017.
 */

public class CustomAdapterSpinner extends BaseAdapter {
    Context context;
    CardColor[] colors;
    LayoutInflater inflater;

    public CustomAdapterSpinner(Context applicationContext, CardColor[] colors) {
        this.context = applicationContext;
        this.colors = colors;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int i) {
        return colors[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_element, null);
        TextView item = (TextView) view.findViewById(R.id.item_text);
        //Get color name and format it
        String colorName = colors[i].name().toLowerCase();
        colorName = colorName.substring(0, 1).toUpperCase() + colorName.substring(1);
        item.setText(colorName);
        //Change text color
        int colorHex = Color.parseColor(colors[i].getHexCode());
        item.setTextColor(colorHex);
        return view;
    }
}
