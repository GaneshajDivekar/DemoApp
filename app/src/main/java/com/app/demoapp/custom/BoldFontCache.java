package com.app.demoapp.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * Created by Ashutosh Verma on 20-Apr-17.
 */

public class BoldFontCache {

    public static final int BOLD=2;

    private static SparseArray<Typeface> fontCache = new SparseArray<>();


    public static Typeface getTypeface(Context context){
        return createTypeface(context, BOLD);
    }

    public static Typeface getTypefacee(Context context, int type){
        return createTypeface(context, type);
    }


    public static Typeface getTypeface(Context context, String fontType){
        if(fontType!=null){
            if(fontType.equalsIgnoreCase("bold")){ return createTypeface(context, BOLD); }
            else{ return createTypeface(context, BOLD); }
        }
        return createTypeface(context, BOLD);
    }


    private static Typeface createTypeface(Context context, int type){
        Typeface typeface=fontCache.get(type);
        if(typeface==null){
            try{
                typeface= Typeface.createFromAsset(context.getAssets(), "fonts/"+getFontName(type));
            }catch (Exception e){ return null; }

            fontCache.put(type, typeface);
        }
        return typeface;
    }

    private static String getFontName(int type){
        switch (type){
            case BOLD: return "DMSans-Bold.ttf";
            default: return "DMSans-Medium.ttf";
        }
    }

}
