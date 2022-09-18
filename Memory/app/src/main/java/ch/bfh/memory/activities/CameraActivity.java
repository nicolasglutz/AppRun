package ch.bfh.memory.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import ch.bfh.memory.R;

public class CameraActivity extends AppCompatActivity {

    private BarcodeScannerOptions bcScannerOptions;

    private Button btn_takePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        initialize();

        createLister();

        checkForPermissions();
    }

    private void checkForPermissions() {
        //Check for permission of camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    private void initialize(){
        btn_takePicture = findViewById(R.id.btn_takePicture);
    }

    private void createLister(){
        btn_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void InitBarcodeScanner(){
        bcScannerOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE)
                        .build();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Pictures");
            imagesFolder.mkdirs();

            //File image = new File(imagesFolder, new Date().getTime()+ "image.jpg");
            File image = new File(imagesFolder,  "image.jpg");

            Uri uriSavedImage = Uri.fromFile(image);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivity(takePictureIntent);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

}