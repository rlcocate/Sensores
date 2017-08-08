package com.example.logonrm.sensores;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Sensores
    private TextView textViewX;
    private TextView textViewY;
    private TextView textViewZ;
    private TextView textViewDetail;

    private SensorManager mSensorManager;
    private Sensor mAccelorometer;

    // Shake
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewX = (TextView) findViewById(R.id.textViewX);
        textViewY = (TextView) findViewById(R.id.textViewZ);
        textViewZ = (TextView) findViewById(R.id.textViewY);
        textViewDetail = (TextView) findViewById(R.id.textViewDetail);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelorometer = (Sensor) mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelorometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Float x = event.values[0];
        Float y = event.values[1];
        Float z = event.values[2];

        textViewX.setText("Posição X: " + x.intValue() + " - Float: " + x);
        textViewY.setText("Posição Y: " + y.intValue() + " - Float: " + y);
        textViewZ.setText("Posição Z: " + z.intValue() + " - Float: " + z);

        if (y < 0) { // O dispositivo está de cabeça pra baixo.
            if (x > 0)
                textViewDetail.setText("Virando para ESQUERDA ficando INVERTIDO");
            if (x < 0)
                textViewDetail.setText("Virando para DIREITA ficando INVERTIDO");
        } else {
            if (x > 0)
                textViewDetail.setText("Virando para ESQUERDA");
            if (x < 0)
                textViewDetail.setText("Virando para DIREITA");
        }

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        // SHAKE
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        if (mAccel > 5) {
            count++;
        // precisa mexer 3x
            if (count >= 3) {
                count = 0;
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(2000);
            }
        }


    }

    public void loadTTS(){
        Intent intent = new Intent(this, TTSActivity.class);
        startActivity(intent);
    }
}
