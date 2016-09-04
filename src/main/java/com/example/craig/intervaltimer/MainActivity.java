package com.example.craig.intervaltimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    //TODO:: saves and presets
    //TODO:: Edit lengths while in use
    //TODO:: Float or minimize working?

    Handler handler = new Handler();
    int startTime;
    boolean timerGo;
    Context thingy = this;
    int lastTime = 0;
    Integer int1Time;
    Integer int2Time;
    int round;
    int currentTime;
    boolean isPaused;
    boolean reset = true;
    boolean pauseOnBeep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(timerTick);

    }

    public void startTimer(View view){

        if (!reset){
            return;
        }
        reset = false;
        timerGo = true;
        lastTime = 0;

        if (isPaused){
            startTime = (int) System.currentTimeMillis() /1000 - currentTime;
            isPaused = false;
            handler.post(timerTick);
        }
        else{
            startTime = (int) System.currentTimeMillis() / 1000;
            round = 1;
            EditText editText = (EditText) findViewById(R.id.int1Time);

            //TODO:: Parse colons, nulls
            int1Time = Integer.parseInt(editText.getText().toString());

            EditText editText2 = (EditText) findViewById(R.id.int2Time);

            //TODO:: Parse colons, nulls
            int2Time = Integer.parseInt(editText2.getText().toString());
            isPaused = false;
            handler.post(timerTick);

        }


    }

    public void endTimer(View view){
        handler.removeCallbacks(timerTick);
        timerGo = false;
        isPaused = false;
        reset = true;
    }

    public void pauseTimer(View view){
        handler.removeCallbacks(timerTick);
        timerGo = false;
        reset = true;
        isPaused = true;
    }

    public void pauseOpt(View view){
         pauseOnBeep = ((ToggleButton) view).isChecked();
    }

    public void onSave(View view){
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                "test_save", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("saved_int1", int1Time);
        editor.commit();

    }

    public void onLoad(View view){
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                "test_save", Context.MODE_PRIVATE);

        int1Time = sharedPref.getInt("saved_int1", -1);
        System.out.println(int1Time);
        EditText e = (EditText) findViewById(R.id.int1Time);
        e.setText(int1Time.toString());


    }

  public  void playSound(final Context context){
      Thread t = new Thread() {
          public void run() {
              MediaPlayer player = null;
              player = MediaPlayer.create(context, R.raw.beep);
              player.start();
              try {
                  Thread.sleep(player.getDuration());
                  player.release();
              } catch (InterruptedException e2) {
                  // TODO Auto-generated catch block
                  e2.printStackTrace();
              }
          }


      };
         t.start();


  }


    private Runnable timerTick = new Runnable() {
        public void run() {


            TextView e = (TextView) findViewById(R.id.timer);
            currentTime = (int) System.currentTimeMillis() / 1000 - startTime;
            String seconds = Integer.toString(currentTime % 60);
            if (seconds.length() == 1) {
                seconds = "0" + seconds;
            }
            String testString = Integer.toString(currentTime / 60) + ":" + seconds;
            e.setText(testString);

            if (lastTime != currentTime) {
                if (round == 1){
                    int tmpTime = currentTime - int1Time;
                    if (tmpTime%(int1Time + int2Time) == 0){
                        round = 2;
                        lastTime = currentTime;
                        playSound(thingy);
                        if (pauseOnBeep){
                            pauseTimer(null);
                        }
                    }
                }
                else if (round == 2 && currentTime%(int1Time+int2Time) == 0){
                    round = 1;
                    lastTime = currentTime;
                    playSound(thingy);
                    if (pauseOnBeep){
                        pauseTimer(null);
                    }
                }
            }
            if (timerGo) {
                handler.postDelayed(this, 50);  //Much higher and stopping is sketchy.
            }
        }


    };

}