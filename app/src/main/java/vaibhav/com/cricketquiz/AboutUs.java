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
        ans1.setText("You Know Nothing, is inspired from Game of Thrones."
        		+ " \n\nIt's a game of identifying popular and unpopular characters of GoT. "
        		+ "\n\nIn a way it's an attempt to refresh a memory lane of GoT old seasons!"
        		+ "\n\nEverything except 'sequence' of questions and level numbers are copyright of HBO & very amazing people behind Game of Thrones."
        		);
        
         ans1 = (TextView) findViewById(R.id.abt2);
        
        ans1.setText("Enjoy & Cheers," 
+"\nTeam MobileFirst"
        		);
        
	}

}
