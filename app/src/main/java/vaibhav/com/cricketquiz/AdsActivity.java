package vaibhav.com.cricketquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdsActivity extends Activity {

	Intent intent;
	ImageView share, proceed;
	TextView text;
	Typeface typeface;

	String[] Details = {
			"Peter Dinklage who plays Tyrion Lannister won Golden Globe award for best Supporting Role in mini series.",

			"Game of Thrones won Best TV BAFTA awards 2013.",

			"The actresses who play Sansa and Arya are good friends in real life.",

			"Nikolaj Coster-Waldau, Jamie Lannister, got his start in the movie Black Hawk Down.",

			"The actress who plays Catelyn Stark, Michelle Fairley, played Mrs. Granger in Harry Potter and the Deathly Hallows.",

			"Jack Gleeson, aka Joffrey, played a little kid in Batman Begins.",

			"Emilia Clarke, who plays Daenerys on the show, does not dye her hair blonde. She wears a wig instead.",

			"There is a Game of Thrones porn parody called \"Game of Bones.\"",

			"Rolling Stone ranked Daenerys Targaryen at No. 1 on a list of 'Top 40 Game of Thrones Characters'.",

			"The Dothraki language was created just for the show and contains more than 3,000 words.",

			"Sophie Turner, who plays Sansa Stark, adopted the dog that played her dire wolf in the show. The dog is named Zunni",

			"Peter Dinklage is vegetarian. All the \"meat\", he eats on the show isn't real.",

			"During the opening credits, each cast member's name is accompanied by the insignia of his/her character's house.",

			"In each of the first four seasons, one king dies: first season - Robert; second season - Renly; third season - Robb; fourth season - Joffrey.",

			"It's the most pirated show in the world According to the filesharing website TorrentFreak, more people are illegally downloading Game of Thrones than any other show in the world",

			"The first season used Northern Inuit dogs, a type specifically bred for wolf-like appearance, to stand in for the direwolves ",

			"Peter Vaughan, who plays Maester Aemon Targaryen, is partially blind in real life.",

			"Jack Gleeson (Joffrey) received a letter from author George R.R. Martin after the show aired, stating 'Congratulations, everyone hates you!'.",

			"The actor who plays The Mountain recently beat a thousand year old Icelandic weight lifting record dating back to the time of the Vikings.",

			"Gandalf's sword from Lord of the Rings is in the iron throne",

			"In the year 2012, over 160 baby girls in the U.S. were legally named 'Khaleesi', after the character in the show, although it is not the character's name (Daenerys) but a title.",

			"Game of Thrones is one of most expensive shows on TV. Some GoT episodes cost even more than $6 million to make.",

			"Season 2's action-packed 'Blackwater' skated in with a reported price tag ofÂ $8 million.",

			"In 2012, public BitTorrent trackers showed that one episode was illegally downloaded about 4,280,000 times.",

			"According to the file sharing websiteTorrentFreak, more people are illegally downloading Game of Thrones than any other show in the world.",

			"Northern Ireland says 'Game of Thrones' has brought more than $100 million to their economy.",

			"Oona O'Neill Chaplin who played Robb Stark's wife is Charlie Chaplin's granddaughter.",

			"You know? Hodor is DJ too :)",

			"More than two feet of human hair is used to make each of the show's Emmy-nominated wigs.	",

			"Daenerys' dragons are named Drogon, Rhaegal, and Viserion",

			"As of 2014, it is the most watched HBO TV-Series of all time, with an average viewership of 16.1 million (Season 4)"

	};
	int x;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trivia);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		intent = getIntent();
		x = intent.getIntExtra("level", 3);
		proceed = (ImageView) findViewById(R.id.proceed);
		share = (ImageView) findViewById(R.id.share);
		text = (TextView) findViewById(R.id.text);

		proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		x = x / 3;
		typeface = CustomFont.getTypeface(AdsActivity.this, CustomFont.RALE);
		text.setTypeface(typeface);
		text.setText(Details[x]);

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String txtLuv = Details[x].toString()+"\n-via ykn.mobilefirst.in";
				Intent intentx = new Intent(Intent.ACTION_SEND);
				intentx.setType("text/plain");
				intentx.putExtra(Intent.EXTRA_TEXT, txtLuv);
				startActivity(Intent.createChooser(intentx, "Share via"));
			}
		});

	}
}
