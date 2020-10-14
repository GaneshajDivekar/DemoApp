package com.app.demoapp.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.app.demoapp.R;


public class CBoldButton extends AppCompatButton {

    public CBoldButton(Context context) {
        super(context);
        init(null);
    }

    public CBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CBoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs){
        if(attrs!=null){
            try{
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextFont);
                String font=typedArray.getString(R.styleable.CustomTextFont_fontName);
                setTypeface(BoldFontCache.getTypeface(getContext(), font));

                typedArray.recycle();
            }catch (Exception e){ e.printStackTrace(); }
        }else{ setTypeface(BoldFontCache.getTypeface(getContext())); }

    }

}
