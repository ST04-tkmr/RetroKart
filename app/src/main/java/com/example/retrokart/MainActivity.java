package com.example.retrokart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler(Looper.getMainLooper());
    CustomView cv;
    Game game;
    static int sWidth,sHeight;
    SensorManager sm;
    SEL sel;
    protected static float[] a_vals = new float[3];
    CustomButton buttonLeft,buttonRight,watchBack;
    long startTime,nowTime;
    long elapsedTime = 0;
    Runnable runnable1,runnable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //画面サイズ取得
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        sWidth = point.x;
        sHeight = point.y;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        game = new Game();

        cv = this.findViewById(R.id.custom_view);
        cv.setGame(game);
        buttonLeft = this.findViewById(R.id.left_button);
        buttonLeft.buttonId = 0;
        buttonRight = this.findViewById(R.id.right_button);
        buttonRight.buttonId = 1;
        CustomButton buttonGo = this.findViewById(R.id.go_button);
        buttonGo.buttonId = 2;
        CustomButton buttonBack = this.findViewById(R.id.back_button);
        buttonBack.buttonId = 3;
        Button gyroSwitch = this.findViewById(R.id.gyro_on_off);
        gyroSwitch.setOnClickListener(new GyroSwitchClicked());
        watchBack = this.findViewById(R.id.back_seen);
        watchBack.buttonId = 4;
        Button pause = this.findViewById(R.id.pause);
        pause.setOnClickListener(new PauseClicked());
    }

    protected void onResume() {
        super.onResume();

        cv.gameStart = true;

        runnable1 = new Runnable() {
            @Override
            public void run() {
                cv.invalidate();
                handler.postDelayed(this, 10);
            }
        };
        handler.postDelayed(runnable1, 10);

        startTime = System.currentTimeMillis();
        runnable2 = new Runnable() {
            @Override
            public void run() {
                nowTime = System.currentTimeMillis();
                elapsedTime = nowTime - startTime;
                cv.time += elapsedTime / 1000.0;
                startTime = System.currentTimeMillis();
                handler.postDelayed(this, 10);
            }
        };
        handler.postDelayed(runnable2, 10);
    }

    protected void onPause() {
        super.onPause();

        if (game.gyro) {
            sm.unregisterListener(sel);
        }
    }

    private static class SEL implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                a_vals = event.values;
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    }

    private class GyroSwitchClicked implements View.OnClickListener {
        public void onClick(View v) {
            if (game.gyro) {
                game.gyro = false;
                buttonLeft.setAlpha(1f);
                buttonRight.setAlpha(1f);
                ((Button) v).setText(R.string.gyro_on);

                sm.unregisterListener(sel);
            } else {
                game.gyro = true;
                buttonLeft.setAlpha(0f);
                buttonRight.setAlpha(0f);
                ((Button) v).setText(R.string.gyro_off);

                sm = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sel = new SEL();
                sm.registerListener(sel, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }
    }


    private class PauseClicked implements View.OnClickListener {
        public void onClick(View v) {

        }
    }
}