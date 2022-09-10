package ch.bfh.bootcamp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import ch.bfh.bootcamp.R;
import ch.bfh.bootcamp.utils.LogAppUtil;
import ch.bfh.bootcamp.utils.MagnetUtil;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    private TextView textView_qr_content;
    private Button button_submit;
    private ProgressBar progressBar;

    //Sensors
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener magnetListener;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();


        mCodeScanner = new CodeScanner(this, findViewById(R.id.scanner_view));
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    private void initialize() {
        progressBar = findViewById(R.id.progressBar);
        textView_qr_content = findViewById(R.id.qr_content);
        button_submit = findViewById(R.id.button_submit);

        createListener();
        checkForPermissions();
    }

    private void checkForPermissions() {
        //Check for permission of camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
        mSensorManager.registerListener(magnetListener, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);

    }

    public void createListener() {
        //Set button to submit to logbook
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.confirmation_title)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Start logging activity
                                startActivity(LogAppUtil.createIntent(textView_qr_content.getText().toString()));
                            }
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            // User cancelled the dialog
                        });
                builder.create().show();
            }
        });

        //Set callback if qrcode is detected
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_qr_content.setText(result.getText());
                    }
                });
            }
        });

        //create magnet lister
        magnetListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] mag = sensorEvent.values;
                progressBar.setProgress(MagnetUtil.calculateMagnetPercentage(mag, mSensor.getMaximumRange()));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

    }

    @Override
    public void onPause() {
        super.onPause();
        //release resources of qr code scanner
        mCodeScanner.releaseResources();
        //release magnet scanner
        mSensorManager.unregisterListener(magnetListener);
    }




}