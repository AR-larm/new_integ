package com.unity3d.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getIntent();
        // 알람음 재생
        this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
        this.mediaPlayer.start();

        findViewById(R.id.btnClose).setOnClickListener(mClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer release
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    /* 알람 종료 */
    private void close() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }

        finish();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //원래 눌렀을 때 해야하는데.
            Calendar calendar = Calendar.getInstance();

            try {
                Date d1 = calendar.getTime();
                String formedDate = new SimpleDateFormat("yy/MM/dd/HH/mm").format(d1);
                System.out.println(formedDate);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            if (v.getId() == R.id.btnClose) {// 알람 종료
                close();
                Intent intent2 = new Intent(getApplicationContext(), UnityPlayerActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
            }
        }
    };
}
