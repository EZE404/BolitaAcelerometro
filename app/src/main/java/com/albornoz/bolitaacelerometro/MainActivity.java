package com.albornoz.bolitaacelerometro;

import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener {

    /* TODO: EL ImageView SE POSICIONA SIEMPRE EN LA ESQUINA SUPERIOR IZQUIERDA
        NO IMPORTA EL ViewGroup QUE UTILICE NI QUÉ ETIQUETAS LE PONGA A LAS VIEWS
        ASÍ QUE PIENSO QUE A LA POSICIÓN INICIAL HAY QUE DÁRSELA CALCULANDO ALGO
        CON LA RESOLUCIÓN DEL DISPOSITIVO. */

    private ImageView ball;
    private SensorManager sensorManager;
    private Sensor sensor;

    // Para obtener resolución de pantalla. Todavía no sé si lo necesito
    int anchoPantalla, altoPantalla;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.ball);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Para obtener resolución de pantalla. Todavía no sé si lo necesito
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
        // TODO: LA POSICIÓN DE UN VIEW ES 0,0 EN FLOTANTES Y ARRANCA EN UNA ESQUINA
        //ball.setX(anchoPantalla/2?)
        //ball.setX(anchoPantalla/2?)
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            finish();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor sensor = event.sensor;
        float [] values = event.values;
        synchronized (this) {
            Log.d("ball", "onSensorChanged: " + sensor + ", x: " +
                    values[0] + ", y: " + values[1] + ", z: " + values[2]);
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
                float ox, oy, dx, dy;
                // Posición original
                ox = ball.getX();
                oy = ball.getY();
                // Distancia respecto a posición original
                dx = values[0] - ox;
                dy = values[1] - oy;

                // Set posición nueva
                ball.setX((ox + dx) * -1); // el eje x sale invertido, no sé por qué, por eso * -1
                ball.setY(oy + dy);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ball","onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}