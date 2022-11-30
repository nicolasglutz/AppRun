package ch.morseencoder.morse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.List;

import ch.morseencoder.R;

public class FlashActivity extends AppCompatActivity {

    final static int DIT_IN_MS = 500;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);


    }

    @Override
    protected void onStart() {
        super.onStart();
        view = findViewById( R.id.simpleview );
        String input = getIntent().getStringExtra("message");
        lightOff();
        try {
            sendMessage(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws Exception {
        MorseEncoder morseEncoder = new MorseEncoder();
        List<Primitive> code = morseEncoder.textToCode(message.toUpperCase());

        if (code.isEmpty()) {
            return;
        }
        Handler handler = new Handler();
        int totalTimeUntilCurrentIndexInMS = 0;
        for (Primitive letter : code) {
            handler.postDelayed(
                    (Runnable) () -> {
                        if (letter.isLightOn()) {
                            lightOn();
                        } else {
                            lightOff();
                        }
                    }
            , totalTimeUntilCurrentIndexInMS );
            totalTimeUntilCurrentIndexInMS+=letter.getSignalLengthInDits() * DIT_IN_MS;
        }
        handler.postDelayed( () -> {
            lightOff();
        }, totalTimeUntilCurrentIndexInMS );
        handler.postDelayed( () -> {
            startActivity(new Intent(this, MainActivity.class));
        }, totalTimeUntilCurrentIndexInMS+5000);
    }


    private void lightOn ()
    {
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white ));
    }

    private  void lightOff ()
    {
        view.setBackgroundColor(ContextCompat.getColor( this, R.color.black ));
    }
}
