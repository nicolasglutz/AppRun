package ch.bfh.bootcamp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import ch.bfh.bootcamp.R;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    private TextView textView_qr_content;
    private Button button_submit;


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
        button_submit = findViewById(R.id.button_submit);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
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
        //Set button to submit to logbook
        button_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("Logging works!");
            }
        });

        mCodeScanner.startPreview();

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


    private void log(String solution) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
// format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        try {
            log.put("task","Metaldetector");
            log.put("solution",solution);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // log.put(…);
        intent.putExtra("ch.apprun.logmessage", log.toString());
        startActivity(intent);
    }

}