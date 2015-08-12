package vaibhav.com.cricketquiz;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFont {

	
	
public static final String APP_FONT = "gameofthrone.ttf";
public static final String RALE = "raleway.ttf";

	public static Typeface getTypeface(Context context, String fontPath) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(context.getAssets(), fontPath);
		} catch (Exception e) {
		}
		return tf;
	}

}
