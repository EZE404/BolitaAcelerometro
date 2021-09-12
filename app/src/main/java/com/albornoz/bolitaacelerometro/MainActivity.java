package com.albornoz.bolitaacelerometro;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


public class MainActivity extends Activity implements SensorEventListener {

    SensorManager sensorManager=null;
    private FrameLayout ball;
    private float dx, dy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ball = findViewById(R.id.ball);
        dx = ball.getX();
        dy = ball.getY();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor Accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // register this class as a listener for the orientation and accelerometer sensors
        sensorManager.registerListener(this, Accel, SensorManager.SENSOR_DELAY_FASTEST);
    }


    public void onSensorChanged(SensorEvent event){
        Sensor sensor = event.sensor;
        float [] values = event.values;
        synchronized (this) {
            Log.d("ball", "onSensorChanged: " + sensor + ", x: " +
                    values[0] + ", y: " + values[1] + ", z: " + values[2]);
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {

                ball.setX((ball.getX() + (values[0] - dx))*-1);
                ball.setY(ball.getY() + (values[1] - dy));

                dx = ball.getX();
                dy = ball.getY();
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ball","onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
    }


}