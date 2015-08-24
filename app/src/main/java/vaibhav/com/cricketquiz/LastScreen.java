package vaibhav.com.cricketquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class LastScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_last_screen);

		Intent intentx = new Intent(LastScreen.this, serv.class);
		LastScreen.this.stopService(intentx);

		Typeface got = CustomFont.getTypeface(LastScreen.this,
				CustomFont.APP_FONT);
		TextView ans1 = (TextView) findViewById(R.id.textView1);

		ans1.setTypeface(got);
		ans1.setText("Its all yours");

		ImageView share = (ImageView) findViewById(R.id.share);

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				save("share");

				String filename = "share.png";
				File imageFile = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/gamers/cache", filename);

				Uri fileuri = Uri.fromFile(imageFile);

				String txtLuv = "-via cricketquiz.mobiweb.co.in";
//				Intent intentx = new Intent(Intent.ACTION_SEND);
//				intentx.setType("text/plain");
//				intentx.putExtra(Intent.EXTRA_TEXT, txtLuv);
//				startActivity(Intent.createChooser(intentx, "Share via"));
				
				
				File filePath = getFileStreamPath("shareimage.jpg");  //optional //internal storage
			     Intent shareIntent = new Intent();
			     shareIntent.setAction(Intent.ACTION_SEND);
			     shareIntent.putExtra(Intent.EXTRA_TEXT, txtLuv);
			     shareIntent.putExtra(Intent.EXTRA_STREAM,fileuri);  //optional//use this when you want to send an image
			     shareIntent.setType("image/jpeg");
			     shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			     startActivity(Intent.createChooser(shareIntent, "Share via"));
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
		});

	}
	
	protected void save(String source) {
		OutputStream output;
		Bitmap temp;
		
		// mainimage.requestLayout();

		View v = findViewById(R.id.screenshot);
		v.setDrawingCacheEnabled(true);
		// Bitmap bitmapx
		v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		temp = v.getDrawingCache();

		// new codes
		Config config = temp.getConfig();
		config = Config.ARGB_8888;

		Bitmap newBitmap = Bitmap.createBitmap(temp.getWidth(),
				temp.getHeight(), config);
		Canvas newCanvas = new Canvas(newBitmap);
		Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		newCanvas.drawBitmap(temp, 0, 0, paint);

		// Find the SD Card path
		File filepath = Environment.getExternalStorageDirectory();
		File file;
		File dir = new File(filepath.getAbsolutePath() + "/gamers/cache");
		dir.mkdirs();

		// Create a name for the saved image
		file = new File(dir, "share.png");
		if (file.exists()) {
			file.delete();
		} else {
			file = new File(dir, "share.png");
		
		}
		try {
			output = new FileOutputStream(file);
			// Compress into png format image from 0% - 100%
			newBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
