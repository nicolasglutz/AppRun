package ch.bfh.bootcamp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import ch.bfh.bootcamp.R;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    private TextView textView_qr_content;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check for permission of camera

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 100);
        }

        textView_qr_content = findViewById(R.id.qr_content);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        //Set callback if qrcode is detected
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_qr_content.setText(result.getText());
                        /*Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();*/
                    }
                });
            }
        });
        mCodeScanner.startPreview();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] mag = sensorEvent.values;
                double betrag = Math.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] *
                        mag[2]);
                betrag = betrag / 100;
                betrag = betrag > 100 ? 100 : betrag;
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setMax(100);
                progressBar.setProgress( (int) Math.round(betrag));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        }, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);


    }
    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}