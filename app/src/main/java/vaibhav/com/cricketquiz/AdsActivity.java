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
            "Shahid Afridi was flown in to play for the Pakistan team from West Indies to Nairobi in 1996 and didn't have a proper bat. That's when Waqar Younis gave 'Young Afridi' Sachin Tendulkar’s bat. Afridi hit 11 sixes and six boundaries with the bat and scored a 37-ball century against Sri Lanka which was the then fastest ODI century. The record, broken later by Corey Anderson (36 balls), now belongs to South Africa's AB de Villiers (31 balls).",

            "In the 137 years of Test cricket no cricketer has ever hit a six off the first ball of a Test match. Audacious Chris Gayle achieved this feat against Bangladesh in 2012 off debutant Sohag Gazi.",

            "When Abbas Ali Baig reached a half-century in the third Test match against Australia at the Brabourne Stadium in 1960, a pretty young girl ran all the way from the North Stand to greet the batsman. She kissed Baig on his cheek..",

            "The only cricketer to play Test cricket for India and England is Saif Ali Khan’s grandfather, Iftikhar Ali Khan Pataudi",

            "Alec Stewart was born on 8-4-63 and he scored 8463 Test runs",

            "India is the only country to win the 60-Over, 50-Over and 20-Over World Cup.",

            "The 2000 Lord's Test between England and West Indies saw all the four innings being played on the same day. This feat was repeated 11 years later in the famous Cape Town Test where South Africa bowled out Australia for 47.",

            "This coincidence occurred during the first Test between South Africa and Australia at Cape Town. At 11:11, on 11/11/11, South Africa were 125 for 1 needing a further 111 runs for a win",

            "The highest number of runs scored in an over is not 36. It’s 77  which was scored in RH Vance’s over in a match between Canterbury and Wellington.  This is how the over went - 0 4 4 4 6 6 4 6 1 4 1 0 6 6 6 6 6 0 0 4 0 1 ",

            "In 1989, along with Sachin Tendulkar, 23 other cricketers made their International debuts. The last one to retire before Sachin, was New Zealand’s Chris Cairns, who retired in 2004",

            "MS Dhoni  have never scored an ODI ton outside of Asia",

            "Chinnaswamy Stadium at Bangalore is World's First Solar powered Cricket Stadium.\n" +
                    "A 400kW Grid Interactive Solar Power Plant has been installed on the roof of the stadium",

            "In the entire history of recorded Cricket, only four players have played on all ten batting positions for a team; names include Lance Klusener, Abdur Razzaq, Shoaib Malik and Hashan Tillakaratne.",

            "Hanif Muhammad by the end of his career had done almost everything imaginable in the game of Cricket. He scored 55 first-class centuries with an impressive career average of 52.32; moreover the ambidextrous Hanif could also bowl with either arms along with keeping wickets on a number of occasions for his country.",

            "In all of 400 years history of Cricket, the fastest (electronically measured) speed for a cricket ball bowled by any bowler, ever, is precisely 161.3 km/h (100.23 mph) by Shoaib Akhtar of Pakistan to Nick Knite of England, on the 22nd February 2003 in the World Cup hosted by South Africa",
            " Shoaib Akhtar handed Sachin Tendulkar a Golden Duck the first time these two came head to head, it was a late reverse swinging delivery that knocked Tendulkar’s middle wicket off the ground. He had also bowled Dravid, with a similar reverse swing, one ball before Sachin. These two deliveries are called, ‘Double jeopardy’ when Shoaib Akhtar made his first International impact ",

            "The England Cricket Team is the only team in ODI history to lose a 60 over ODI Final (1979 World Cup), a 50 over ODI Final (1992 World Cup and 2004 Champions Trophy) and a 20 over ODI Final (2013 Champions Trophy) in ICC tournaments",



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

                String txtLuv = Details[x].toString() + "\n-via cricketquiz.mobiweb.co.in";
                Intent intentx = new Intent(Intent.ACTION_SEND);
                intentx.setType("text/plain");
                intentx.putExtra(Intent.EXTRA_TEXT, txtLuv);
                startActivity(Intent.createChooser(intentx, "Share via"));
            }
        });

    }
}
