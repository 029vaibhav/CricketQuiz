package vaibhav.com.cricketquiz;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.doorbell.android.Doorbell;
import io.fabric.sdk.android.Fabric;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onResume() {
		super.onResume();
		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);

		if (GlobalThings.music) {
			Intent intentx = new Intent(MainActivity.this, serv.class);
			MainActivity.this.startService(intentx);
		} else {
			Intent intentx = new Intent(MainActivity.this, serv.class);
			MainActivity.this.stopService(intentx);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
		Intent intentx = new Intent(MainActivity.this, serv.class);
		MainActivity.this.stopService(intentx);
	}

	private boolean canPresentShareDialogWithPhotos;
	private static final String PERMISSION = "publish_actions";
	String MyStr = "_";
	Doorbell doorbellDialog;
	TranslateAnimation translation;
	float FromTextX, FromTextY, ToTextX, ToTextY;
	String currentquestion;
	Intent intent;
	char[] c;
	LinearLayout ans1, ans2, que1, que2;
	TextView text;
	Typeface got, rail;
	List<String> list;
	List<String> list2;
	int count = 0;
	int spacecount = 0;
	ImageView volume;
	int[] mydraw = { R.drawable.black_gradiant_bg };
	String titles[] = { "About Us", "Feedback", "share" };
	String share[] = { "Twitter", "Whatsapp", "Facebook" };
	int icon[] = { R.drawable.about, R.drawable.feedback, R.drawable.share };
	int shareicon[] = { R.drawable.twitter, R.drawable.whatsapp, R.drawable.fb };
	RelativeLayout mLinearLayout;
	List<EasyBean> mMasterArray;
	List<EasyBean> mQuestioneasy;
	SharedPreferences shared;
	Editor editor;
	ResideMenu resideMenu;
	Button mHint, mAskFriend;
	ImageView image, menu;
	Boolean sharedprefbuilt;
	int level = -1;
	public TextView question;
	TextView mTextLevel;
	boolean isInternetPresent = false;
	ConnectionDetector cd;
	AlertDialog alertDialog;
	ConnectivityManager connMgr;
	private ShareDialog shareDialog;
	private CallbackManager callbackManager;
	LinearLayout.LayoutParams params;
	int smallTextSize, quoteTextSize;
	MediaPlayer mp;
	ShimmerFrameLayout mShimmerViewContainer;

	 Typeface bottomLetter;

	private final String PENDING_ACTION_BUNDLE_KEY = "com.example.mobilefirst.arraygame:PendingAction";
	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onCancel() {
			Log.d("HelloFacebook", "Canceled");
		}

		@Override
		public void onError(FacebookException error) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
			String title = getString(R.string.error);
			String alertMessage = error.getMessage();
			showResult(title, alertMessage);
		}

		@Override
		public void onSuccess(Sharer.Result result) {
			Log.d("HelloFacebook", "Success!");
			if (result.getPostId() != null) {
				String title = getString(R.string.success);
				String id = result.getPostId();
				String alertMessage = getString(
						R.string.successfully_posted_post, id);
				showResult(title, alertMessage);
			}
		}

		private void showResult(String title, String alertMessage) {
			new AlertDialog.Builder(MainActivity.this).setTitle(title)
					.setMessage(alertMessage)
					.setPositiveButton(R.string.ok, null).show();
		}
	};
	private PendingAction pendingAction = PendingAction.NONE;
	private ImageView loading;

	private enum PendingAction {
		NONE, POST_PHOTO
	}

	static int posted = 0;

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		FacebookSdk.sdkInitialize(this.getApplicationContext());
		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						handlePendingAction();
					}

					@Override
					public void onCancel() {
						if (pendingAction != PendingAction.NONE) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
					}

					@Override
					public void onError(FacebookException exception) {
						if (pendingAction != PendingAction.NONE
								&& exception instanceof FacebookAuthorizationException) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
					}

					private void showAlert() {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle(R.string.cancelled)
								.setMessage(R.string.permission_not_granted)
								.setPositiveButton(R.string.ok, null).show();
					}
				});

		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.activity_main);
		
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		
		

		canPresentShareDialogWithPhotos = ShareDialog
				.canShow(SharePhotoContent.class);
		mp = MediaPlayer.create(MainActivity.this, R.raw.explosoin);
		mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
		mShimmerViewContainer.startShimmerAnimation();
		sharedprefbuilt = true;
		rail = CustomFont.getTypeface(MainActivity.this, CustomFont.RALE);
		got = CustomFont.getTypeface(MainActivity.this, CustomFont.APP_FONT);
		bottomLetter = Typeface.createFromAsset(getAssets(), "font/cut.ttf");

		ans1 = (LinearLayout) findViewById(R.id.ansrow1);
		ans2 = (LinearLayout) findViewById(R.id.ansrow2);
		que1 = (LinearLayout) findViewById(R.id.quesrow1);
		que2 = (LinearLayout) findViewById(R.id.quesrow2);
		volume = (ImageView) findViewById(R.id.volume);
		shared = MainActivity.this.getSharedPreferences("game",
				Context.MODE_PRIVATE);
		editor = shared.edit();

		if (shared.getBoolean("volume", true)) {
			GlobalThings.music = true;
			volume.setImageResource(R.drawable.volumeon);
		} else {
			GlobalThings.music = false;

			volume.setImageResource(R.drawable.volumeoff);
		}

		smallTextSize = (int) getResources().getDimension(
				R.dimen.quote_small_size);
		quoteTextSize = (int) getResources().getDimension(
				R.dimen.quote_text_size);
		mLinearLayout = (RelativeLayout) findViewById(R.id.background);
		mMasterArray = new ArrayList<EasyBean>();

		menu = (ImageView) findViewById(R.id.expanded_menu);
		image = (ImageView) findViewById(R.id.image);
		loading = (ImageView) findViewById(R.id.loading);
		params = new LinearLayout.LayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		params.setMargins(2, 2, 2, 2);

		mAskFriend = (Button) findViewById(R.id.askafriend);
		mAskFriend.setTypeface(got);
		mHint = (Button) findViewById(R.id.hint);
		mHint.setTypeface(got);
		question = (TextView) findViewById(R.id.question);
		question.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.quote_text_size));
		question.setTypeface(rail);
		mTextLevel = (TextView) findViewById(R.id.level);
		mTextLevel.setTypeface(got);
		resideMenu = new ResideMenu(this);
		resideMenu.attachToActivity(this);
		feedback();
		for (int i = 0; i < titles.length; i++) {
			ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
			item.setOnClickListener(this);
			item.setTag(300 + i);
			resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or
																		// ResideMenu.DIRECTION_RIGHT
		}

		for (int i = 0; i < share.length; i++) {
			ResideMenuItem itemx = new ResideMenuItem(this, shareicon[i],
					share[i]);
			itemx.setOnClickListener(this);
			itemx.setTag(i + 303);
			resideMenu.addMenuItem(itemx, ResideMenu.DIRECTION_RIGHT); // or
																		// ResideMenu.DIRECTION_RIGHT
		}

		resideMenu.setBackground(R.drawable.lors);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		mAskFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});

		levelchanger();

		volume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalThings.music) {
					volume.setImageResource(R.drawable.volumeoff);
					GlobalThings.music = false;
					editor.putBoolean("volume", GlobalThings.music);
					editor.commit();
					Intent intentx = new Intent(MainActivity.this, serv.class);
					MainActivity.this.stopService(intentx);
				} else {
					volume.setImageResource(R.drawable.volumeon);
					Intent intentx = new Intent(MainActivity.this, serv.class);
					MainActivity.this.startService(intentx);
					GlobalThings.music = true;
					editor.putBoolean("volume", GlobalThings.music);
					editor.commit();
				}

			}
		});

		mHint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));

				dialog.setContentView(R.layout.dailog);

				Button btn = (Button) dialog.findViewById(R.id.button);
				btn.setTypeface(got);
				Button btn2 = (Button) dialog.findViewById(R.id.button2);
				btn2.setTypeface(got);

				overridePendingTransition(R.anim.left_to_right,
						R.anim.right_to_left);

				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int mNoOfHints = randomcount(1, 4);
						Log.e("mNoOfHints", "" + mNoOfHints);

						String letter1, letter2;

						if (currentquestion.contains(" ")) {
							count = -1;
						} else {
							count = 0;
						}

						switch (mNoOfHints) {
						case 1:

							text = (TextView) findViewById(201);
							text.setText("" + c[0]);
							text.setTextColor(Color.RED);
							count++;

							letter1 = text.getText().toString();

							text = (TextView) findViewById(202);
							text.setText("" + c[1]);
							text.setTextColor(Color.RED);
							count++;

							letter2 = text.getText().toString();

							for (int i = 0; i < 20; i++) {
								text = (TextView) findViewById(101 + i);
								text.setText(""
										+ list2.get(i).toString().toLowerCase());
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter1.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter2.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}

							for (int i = 0; i < c.length; i++) {
								text = (TextView) findViewById(201 + i);
								if (i == 0 || i == 1) {

								} else {
									String s = "" + c[i];
									if (!s.equalsIgnoreCase(" ")) {
										text.setTextColor(Color.WHITE);
										text.setText(MyStr);
									} else {
										spacecount++;
									}
								}
							}

							mHint.setEnabled(false);
							dialog.dismiss();
							break;

						case 2:

							text = (TextView) findViewById(201 + c.length - 1);
							text.setText("" + c[c.length - 1]);
							text.setTextColor(Color.RED);
							count++;

							letter1 = text.getText().toString();

							text = (TextView) findViewById(201 + c.length - 2);
							text.setText("" + c[c.length - 2]);
							text.setTextColor(Color.RED);
							count++;

							letter2 = text.getText().toString();

							for (int i = 0; i < 20; i++) {
								text = (TextView) findViewById(101 + i);
								text.setText(""
										+ list2.get(i).toString().toLowerCase());
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter1.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter2.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}
							for (int i = 0; i < c.length; i++) {
								text = (TextView) findViewById(201 + i);
								if (i == c.length - 1 || i == c.length - 2) {

								} else {
									String s = "" + c[i];
									if (!s.equalsIgnoreCase(" ")) {
										text.setTextColor(Color.WHITE);
										text.setText(MyStr);
									} else {
										spacecount++;
									}
								}
							}

							mHint.setEnabled(false);
							dialog.dismiss();

							break;

						default:
							text = (TextView) findViewById(201);
							text.setText("" + c[0]);
							text.setTextColor(Color.RED);
							count++;

							letter1 = text.getText().toString();

							text = (TextView) findViewById(201 + c.length - 1);
							text.setText("" + c[c.length - 1]);
							text.setTextColor(Color.RED);
							count++;

							letter2 = text.getText().toString();

							for (int i = 0; i < 20; i++) {
								text = (TextView) findViewById(101 + i);
								text.setText(""
										+ list2.get(i).toString().toLowerCase());
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter1.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}

							for (int i1 = 0; i1 < 20; i1++) {
								text = (TextView) findViewById(101 + i1);
								if (letter2.equalsIgnoreCase(text.getText()
										.toString())) {
									text.setText("");
									break;
								}
							}

							for (int i = 0; i < c.length; i++) {
								text = (TextView) findViewById(201 + i);
								if (i == 0 || i == c.length - 1) {
								} else {
									String s = "" + c[i];
									if (!s.equalsIgnoreCase(" ")) {
										text.setTextColor(Color.WHITE);
										text.setText(MyStr);
									} else {
										spacecount++;
									}
								}
							}

							mHint.setEnabled(false);
							dialog.dismiss();

						}

						if (count + spacecount >= c.length) {
							String s = "";
							for (int i1 = 0; i1 < c.length; i1++) {

								text = (TextView) findViewById(201 + i1);
								s = s + "" + text.getText().toString();

							}
							checkans(s);
						}
					}

					private int randomcount(int Low, int high) {
						Random r = new Random();

						int R = r.nextInt(high - Low) + Low;
						return R;
					}
				});

				btn2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						list2.clear();
						if (currentquestion.contains(" ")) {
							count = -1;
						} else {
							count = 0;
						}
						for (int i = 0; i < c.length; i++) {
							text = (TextView) findViewById(201 + i);

							String s = "" + c[i];
							if (!s.equalsIgnoreCase(" ")) {
								text.setTextColor(Color.WHITE);
								text.setText(MyStr);
							} else {
								spacecount++;
							}
						}

						for (int i = 0; i < c.length; i++) {
							String s = "" + c[i];
							if (!s.equalsIgnoreCase(" ")) {
								list2.add("" + c[i]);
							}
						}

						int size = c.length - 1;
						for (int i = 0; i < 20 - size; i++) {
							list2.add("");
						}

						Collections.shuffle(list2);

						for (int i = 0; i < 20; i++) {
							text = (TextView) findViewById(101 + i);
							text.setText(""
									+ list2.get(i).toString().toLowerCase());
						}
						mHint.setEnabled(false);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case NONE:
			break;
		case POST_PHOTO:
			postPhoto();
			break;

		default:
			break;
		}
	}

	private void postPhoto() {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/gamers/cache/share.png", options);

		SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap)
				.build();
		ArrayList<SharePhoto> photos = new ArrayList<SharePhoto>();
		photos.add(sharePhoto);

		SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
				.setPhotos(photos).build();
		if (canPresentShareDialogWithPhotos) {
			shareDialog.show(sharePhotoContent);
		} else if (hasPublishPermission()) {
			ShareApi.share(sharePhotoContent, shareCallback);
		} else {
			pendingAction = PendingAction.POST_PHOTO;
		}
	}

	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null
				&& accessToken.getPermissions().contains("publish_actions");
	}

	private void feedback() {



		int appId = 1951; // Replace with your application's ID
		String apiKey = "BfPBhCgT9xApES6AxJnhLbEZeqXk2AzLpVKFRlHZMYH8dIGLnTooJcJ0Gsi6C4C2";
		doorbellDialog = new Doorbell(this, appId, apiKey);

		// doorbellDialog.setEmail("mobilefirstapps@gmail.com"); // Prepopulate
		// the
		// email address
		// field
		// Set the name of the user (if known)
														// properties
		doorbellDialog.setEmailFieldVisibility(View.GONE); // Hide the email
		doorbellDialog.setPoweredByVisibility(View.GONE);

	}

	private void levelchanger() {

		isOnline();

		if (isInternetPresent) {

			if (shared.getBoolean("volume", true)) {
				Intent intent = new Intent(MainActivity.this, serv.class);
				MainActivity.this.startService(intent);
			} else {
				Intent intent = new Intent(MainActivity.this, serv.class);
				MainActivity.this.stopService(intent);
			}

			int mSpacecounter = 0;
			mHint.setEnabled(true);
			Boolean mSpaceEncountered = false;
			Random r = new Random();
			int Low = 0;
			int High = mydraw.length;
			int R = r.nextInt(High - Low) + Low;

			mLinearLayout.setBackgroundResource(mydraw[R]);
			level = shared.getInt("level", 0);
			list = null;
			list2 = null;
			list = new ArrayList<String>();
			list2 = new ArrayList<String>();
			que1.removeAllViews();
			ans1.removeAllViews();
			que2.removeAllViews();
			ans2.removeAllViews();
			count = 0;
			spacecount = 0;
			c = null;

			if (level == 0) {
				createquestion("easy");
				Collections.shuffle(mQuestioneasy);
				List<EasyBean> easy = new ArrayList<EasyBean>(mQuestioneasy);
//				createquestion("med");
//				Collections.shuffle(mQuestioneasy);
//				List<EasyBean> medium = new ArrayList<EasyBean>(mQuestioneasy);
//				createquestion("hard");
//				Collections.shuffle(mQuestioneasy);
//				List<EasyBean> hard = new ArrayList<EasyBean>(mQuestioneasy);
//				createquestion("vhard");
//				Collections.shuffle(mQuestioneasy);
//				List<EasyBean> veryhard = new ArrayList<EasyBean>(mQuestioneasy);
				mMasterArray.addAll(easy);
//				mMasterArray.addAll(medium);
//				mMasterArray.addAll(hard);
//				mMasterArray.addAll(veryhard);

				Gson gson2 = new Gson();
				String jsonr = gson2.toJson(mMasterArray);
				editor.putString("MyObject", jsonr);
				editor.commit();
			} else {
				if (sharedprefbuilt) {
					Gson gson = new Gson();
					String json = shared.getString("MyObject", "");
					Type type = new TypeToken<List<EasyBean>>() {
					}.getType();
					mMasterArray = gson.fromJson(json, type);
					sharedprefbuilt = false;
				}
			}
			if (level == 51) {
				Intent intentx = new Intent(MainActivity.this, LastScreen.class);
				startActivity(intentx);
				finish();
			}

			if (mMasterArray.get(level).getType().equalsIgnoreCase("i")) {
				image.setVisibility(View.VISIBLE);
				loading.setVisibility(View.VISIBLE);
				mShimmerViewContainer.setVisibility(View.VISIBLE);
				question.setVisibility(View.GONE);
				String imageUrl = mMasterArray.get(level).getImage();
				Picasso.with(MainActivity.this).load(imageUrl).into(image);
				question.setVisibility(View.INVISIBLE);
			} else {
				question.setVisibility(View.VISIBLE);
				image.setImageBitmap(null);
				image.setVisibility(View.INVISIBLE);
				loading.setVisibility(View.INVISIBLE);
				mShimmerViewContainer.setVisibility(View.INVISIBLE);
				question.setText("" + mMasterArray.get(level).getText());

				if (String.valueOf(mMasterArray.get(level).getText()).length() > 50) {
					question.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							smallTextSize);
				} else {
					question.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							quoteTextSize);
				}
			}

			currentquestion = null;
			currentquestion = mMasterArray.get(level).getAnswer();
			mTextLevel.setText("Level " + level);

			char[] abc = "abcdefghijklmnopqrstuvwxyz".toCharArray();

			for (int i = 0; i < 26; i++) {
				list.add("" + abc[i]);
			}

			Collections.shuffle(list);

			c = currentquestion.toCharArray();

			for (int i = 0; i < c.length; i++) {
				String s = "" + c[i];
				if (!s.equalsIgnoreCase(" ")) {
					list2.add("" + c[i]);
				}
			}

			int size = c.length - 1;
			for (int i = 0; i < 20 - size; i++) {
				list2.add(list.get(i).toString());
			}

			Collections.shuffle(list2);

			for (int i = 0; i < list2.size(); i++) {

				text = new TextView(MainActivity.this);
				text.setTypeface(bottomLetter);
				text.setBackgroundColor(Color.WHITE);
				text.setGravity(Gravity.CENTER);
				text.setText("" + list2.get(i).toString().toLowerCase());
				text.setPadding(10, 10, 10, 10);
				text.setTextSize(35f);
				text.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						5f));

				text.setId(101 + i);
				if (i < 10) {
					que1.addView(text);
				} else {
					que2.addView(text);
				}
				text.setOnClickListener(this);
			}

			for (int i = 0; i < c.length; i++) {
				text = new TextView(MainActivity.this);
				text.setTypeface(bottomLetter);

				String s = "" + c[i];
				if (!s.equalsIgnoreCase(" ")) {
					text.setTextColor(Color.WHITE);
					text.setText(MyStr);
				} else {
					mSpaceEncountered = true;
					spacecount++;
					mSpacecounter++;
				}

				text.setId(201 + i);
				if (mSpaceEncountered == false) {
					text.setLayoutParams(params);
					text.setPadding(10, 10, 10, 10);
					text.setTextSize(20f);
					text.setTypeface(rail);
					ans1.addView(text);
				} else {
					if (mSpacecounter == 1) {
						mSpacecounter = 0;
						text.setVisibility(View.GONE);
						ans2.addView(text);
					} else {
						text.setLayoutParams(params);
						text.setPadding(10, 10, 10, 10);
						text.setTextSize(20f);
						text.setTypeface(rail);
						ans2.addView(text);
					}
				}
				text.setOnClickListener(this);
			}
		} else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);
			alertDialogBuilder.setTitle("Connectivity Error");
			alertDialogBuilder
					.setMessage("Are You Connected to Internet ??")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, close
									// current activity
									isOnline();
									if (isInternetPresent) {
										dialog.dismiss();
									} else {
										dialog.dismiss();
										levelchanger();
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									MainActivity.this.finish();
								}
							});
			alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	private void createquestion(String string) {

		if (string.equals("easy")) {
			//String[] type = GlobalThings.TypeEasy;
			String[] text = GlobalThings.TextEasy;
			String[] ans = GlobalThings.AnswerEasy;

			Log.e("easy", "" + text.length);
			mQuestioneasy = new ArrayList<EasyBean>();

			for (int i = 0; i < text.length; i++) {
				EasyBean eb = new EasyBean();
				eb.setType("t");
				eb.setText(text[i]);
				eb.setAnswer(ans[i]);


				mQuestioneasy.add(eb);
			}
		}

//		else if (string.equals("med")) {
//			String[] type = GlobalThings.typeMed;
//			String[] text = GlobalThings.textmed;
//			String[] ans = GlobalThings.ansmed;
//
//			Log.e("med", "" + type.length);
//			mQuestioneasy.clear();
//			mQuestioneasy = new ArrayList<EasyBean>();
//			for (int i = 0; i < type.length; i++) {
//				EasyBean eb = new EasyBean();
//
//				eb.setType(type[i]);
//				eb.setText(text[i]);
//				eb.setAnswer(ans[i]);
//				eb.setSolved(0);
//
//				mQuestioneasy.add(eb);
//			}
//		}

//		else if (string.equals("hard")) {
//			String[] type = GlobalThings.typehard;
//			String[] image = GlobalThings.imagehard;
//			String[] text = GlobalThings.texthard;
//			String[] ans = GlobalThings.answerhard;
//			Log.e("hard", "" + type.length);
//			mQuestioneasy.clear();
//			mQuestioneasy = new ArrayList<EasyBean>();
//			for (int i = 0; i < type.length; i++) {
//				EasyBean eb = new EasyBean();
//
//				eb.setType(type[i]);
//				eb.setImage(image[i]);
//				eb.setText(text[i]);
//				eb.setAnswer(ans[i]);
//				eb.setSolved(0);
//
//				mQuestioneasy.add(eb);
//			}
//		}

//		else if (string.equals("vhard")) {
//			String[] type = GlobalThings.typevhard;
//			String[] image = GlobalThings.imagevhard;
//			String[] text = GlobalThings.textvhard;
//			String[] ans = GlobalThings.answervhard;
//			Log.e("vhard", "" + type.length);
//			mQuestioneasy.clear();
//			mQuestioneasy = new ArrayList<EasyBean>();
//
//			for (int i = 0; i < type.length; i++) {
//				EasyBean eb = new EasyBean();
//
//				eb.setType(type[i]);
//				eb.setImage(image[i]);
//				eb.setText(text[i]);
//				eb.setAnswer(ans[i]);
//				eb.setSolved(0);
//
//				mQuestioneasy.add(eb);
//			}
//		}
	}

	@Override
	public void onClick(View v) {
		int i = 0;
		if (v.getTag() != null) {
			i = Integer.parseInt(v.getTag().toString());
		}
		if (i != 0) {
			switch (i) {
			case 300:
				Intent intent = new Intent(MainActivity.this, AboutUs.class);
				startActivity(intent);
				break;
			case 301:
				doorbellDialog
						.setOnShowCallback(new io.doorbell.android.callbacks.OnShowCallback() {
							@Override
							public void handle() {
							}
						});

				doorbellDialog.show();
				break;
			case 302:
				String txtLuv = "Cricket fan ? 'criCket quiZ ' is a quiz challenge to prove your criCket Quotient !! \nDownload http://cricketquiz.mobiweb.co.in";
				Intent intentx = new Intent(Intent.ACTION_SEND);
				intentx.setType("text/plain");
				intentx.putExtra(Intent.EXTRA_TEXT, txtLuv);
				startActivity(Intent.createChooser(intentx, "Share via"));
				break;

			case 303:
				twitter();

				break;

			case 304:
				whatsapp();
				break;

			case 305:
				save("share");
				posted = 1;
				LoginManager.getInstance().logInWithReadPermissions(this,
						Arrays.asList("public_profile"));
				onClickPostPhoto();
				break;
			}
		} else {
			int id = v.getId();
			TextView text = (TextView) findViewById(id);
			mp.start();
			String currentletter = "";

			if (id > 100 && id < 200) {
				currentletter = text.getText().toString();
				if (!currentletter.equalsIgnoreCase("")) {
					for (int i1 = 0; i1 < c.length; i1++) {
						text = (TextView) findViewById(201 + i1);
						if (text.getText().toString().equalsIgnoreCase(MyStr)) {
							boolean tempboolean = false;
							int tempid = i1;

							if (i1 != 0) {
								text = (TextView) findViewById(201 + tempid - 1);
								if (text.getText() == "") {
									tempboolean = true;
								}
							}
							if (i1 == 0 || tempboolean) {
								currentletter = currentletter.toUpperCase();
								tempboolean = false;
							}
							text = (TextView) findViewById(201 + i1);
							text.setText(currentletter);
							text = (TextView) findViewById(id);
							text.setText("");
							count++;
							break;
						}
					}
				}
			}

			if (id > 200) {
				currentletter = text.getText().toString();

				if (!currentletter.equalsIgnoreCase(MyStr)) {
					for (int i1 = 0; i1 < list2.size(); i1++) {
						text = (TextView) findViewById(101 + i1);
						if (text.getText().toString() == "") {
							text.setText(currentletter.toLowerCase());
							text = (TextView) findViewById(id);
							text.setText(MyStr);
							count--;
							break;
						}
					}
				}
			}
			if (count + spacecount >= c.length) {
				String s = "";
				for (int i1 = 0; i1 < c.length; i1++) {

					text = (TextView) findViewById(201 + i1);
					s = s + "" + text.getText().toString();
				}
				checkans(s);
			}
		}
	}

	private void onClickPostPhoto() {
		performPublish(PendingAction.POST_PHOTO,
				canPresentShareDialogWithPhotos);
	}

	private void performPublish(PendingAction action, boolean allowNoToken) {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
				return;
			} else {
				// We need to get new permissions, then complete the action when
				// we get called back.
				LoginManager.getInstance().logInWithPublishPermissions(this,
						Arrays.asList(PERMISSION));
				return;
			}
		}
		if (allowNoToken) {
			pendingAction = action;
			handlePendingAction();
		}
	}

	private void whatsapp() {
		try {
			save("share");

			String filename = "share.png";
			File imageFile = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gamers/cache", filename);

			Uri fileuri = Uri.fromFile(imageFile);

			Intent whatsapp = new Intent(Intent.ACTION_SEND);
			whatsapp.setPackage("com.whatsapp");
			whatsapp.setType("text/plain");
			whatsapp.putExtra(Intent.EXTRA_TEXT,
					"Can You Help Me With This\n-via cricketquiz.mobiweb.co.in ");
			whatsapp.setType("image/*");
			whatsapp.putExtra(Intent.EXTRA_STREAM, fileuri);

			PackageManager packManager = getPackageManager();
			List<ResolveInfo> resolvedInfoList = packManager
					.queryIntentActivities(whatsapp,
							PackageManager.MATCH_DEFAULT_ONLY);

			boolean resolved = false;
			for (ResolveInfo resolveInfo : resolvedInfoList) {
				if (resolveInfo.activityInfo.packageName
						.startsWith("com.whatsapp")) {
					whatsapp.setClassName(resolveInfo.activityInfo.packageName,
							resolveInfo.activityInfo.name);
					resolved = true;
					break;
				}
			}
			if (resolved) {
				startActivity(whatsapp);
			} else {
				Intent shareIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id=com.whatsapp"));
				startActivity(shareIntent);
			}
		} catch (Exception e) {
			Intent shareIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=com.whatsapp"));
			startActivity(shareIntent);
		}
	}

	private void twitter() {
		try {
			save("share");

			String filename = "share.png";
			File imageFile = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gamers/cache", filename);

			Intent tweetIntent = new Intent(Intent.ACTION_SEND);
			tweetIntent.setType("image/png");
			tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));

			PackageManager packManager = getPackageManager();
			List<ResolveInfo> resolvedInfoList = packManager
					.queryIntentActivities(tweetIntent,
							PackageManager.MATCH_DEFAULT_ONLY);

			boolean resolved = false;
			for (ResolveInfo resolveInfo : resolvedInfoList) {
				if (resolveInfo.activityInfo.packageName
						.startsWith("com.twitter.android")) {
					tweetIntent.setClassName(
							resolveInfo.activityInfo.packageName,
							resolveInfo.activityInfo.name);
					resolved = true;
					break;
				}
			}
			if (resolved) {
				startActivity(tweetIntent);
			} else {
				Intent shareIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id=com.twitter.android"));
				startActivity(shareIntent);
			}
		} catch (Exception e) {
			Intent shareIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=com.twitter.android"));
			startActivity(shareIntent);
		}
	}

	protected void save(String source) {
		OutputStream output;
		Bitmap temp;
		image.invalidate();
		// mainimage.requestLayout();

		View v = findViewById(R.id.rl);
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

	protected void checkans(String s) {
		String currentquestionwithoutspace = currentquestion.replace(" ", "");
		if (s.equalsIgnoreCase(currentquestionwithoutspace)) {
			level = level + 1;
			editor.putInt("level", level);
			editor.commit();
			final MediaPlayer mp2 = MediaPlayer.create(MainActivity.this,
					R.raw.swordcut);
			mp2.start();
			Toast.makeText(MainActivity.this, "Congo !! Correct Answer",
					Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				public void run() {
					if (level % 3 == 0) {
						intent = new Intent(MainActivity.this,
								AdsActivity.class);
						intent.putExtra("level", level);
						startActivityForResult(intent, 101);
					} else {
						mp2.release();
						levelchanger();
					}
				}
			}, 1000);
		} else {
			Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.shake);
			image.startAnimation(shake);
			question.startAnimation(shake);
			final MediaPlayer mp3 = MediaPlayer.create(MainActivity.this,
					R.raw.bell);
			mp3.start();
		}
	}

	public void isOnline() {
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 101) {
			if (resultCode == RESULT_OK) {
				levelchanger();
			}
		}
	}
}