package ch.morseencoder.morse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ch.morseencoder.R;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.send_btn);
        message = findViewById(R.id.message_text);

    }

    @Override
    protected void onStart() {
        super.onStart();
        button.setOnClickListener((l) -> {
            Intent intent = new Intent(this, FlashActivity.class);
            intent.putExtra("message", message.getText().toString());
            startActivity(intent);
        });
    }
}