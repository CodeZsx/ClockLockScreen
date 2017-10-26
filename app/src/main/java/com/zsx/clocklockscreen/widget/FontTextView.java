package com.zsx.clocklockscreen.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * com.zsx.smartshopping.activity.customView:
 * Created by 酸奶 on 2016/7/17.
 */
public class FontTextView extends TextView{
    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
    }
}
