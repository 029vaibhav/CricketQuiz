package vaibhav.com.cricketquiz;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);


        Typeface rail = CustomFont.getTypeface(AboutUs.this, CustomFont.RALE);
        TextView ans1 = (TextView) findViewById(R.id.abt);

        ans1.setTypeface(rail);
        ans1.setText("criCket quiZ is for cricket lovers"
                        + " \n\nIts the game of spelling out the correct answers so mind your spellings"
                        + "\n\nIn a way it's an attempt to refresh a memory lane of Internation cricket and cricketers!"
                        + "\n\nPut your fingers on and SPELL out the answers"
        );

        ans1 = (TextView) findViewById(R.id.abt2);

        ans1.setText("Enjoy & Cheers,"
                        + "\nTeam Mobiweb"
        );

    }

}
