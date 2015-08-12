package vaibhav.com.cricketquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends Activity {
	ImageView ykn1, ykn2, talwar;
	Animation falldown1, falldown2;
	//cutpage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(Splash.this, MainActivity.class);
				startActivity(i);
//				overridePendingTransition(android.R.anim.slide_in_left,
//						android.R.anim.slide_out_right);
				overridePendingTransition(0, 0);
				finish();
			}
		}, 3000);

//		cutpage = AnimationUtils.loadAnimation(Splash.this, R.anim.cutpage);
		falldown1 = AnimationUtils.loadAnimation(Splash.this, R.anim.fall1);
		falldown2 = AnimationUtils.loadAnimation(Splash.this, R.anim.fall2);

		ykn1 = (ImageView) findViewById(R.id.ykn1);
		ykn2 = (ImageView) findViewById(R.id.ykn2);
//		talwar = (ImageView) findViewById(R.id.talwar);
//		talwar.startAnimation(cutpage);
		ykn1.startAnimation(falldown1);
		ykn2.startAnimation(falldown2);

//		cutpage.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				
//				new Handler().postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						final MediaPlayer mp2 = MediaPlayer.create(Splash.this,
//								R.raw.swordcut);
//						mp2.start();
//					}
//				}, 1000);
//				
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				talwar.setVisibility(View.GONE);
//
//			}
//		});
//
//		falldown1.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				ykn1.setVisibility(View.GONE);
//
//			}
//		});
//		falldown2.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				ykn2.setVisibility(View.GONE);
//			}
//		});
//	}

}
}
