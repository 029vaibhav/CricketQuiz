package vaibhav.com.cricketquiz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class serv extends Service{

	MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    public void onCreate()
    {   
//    	float log1=(float)(Math.log(50)/Math.log(30));
        mp = MediaPlayer.create(this, R.raw.got);
//        mp.setVolume(1-log1, 1-log1);
        mp.setLooping(true);
    }
    public void onDestroy()
    {       
        mp.stop();
    }
    public void onStart(Intent intent,int startid){

       
        mp.start();
    }

}
