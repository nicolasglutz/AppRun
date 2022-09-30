package ch.bfh.memory.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import ch.bfh.memory.R;

import ch.bfh.memory.interfaces.ClickListener;
import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;
import ch.bfh.memory.utils.DataUtil;
import ch.bfh.memory.utils.LogAppUtil;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    public static RecyclerView.Adapter memoryAdaptor;
    private Button logBookButton;
    private Button clearConstraintsButton;
    private Button clearAllButton;
    private Button btn_addCard;

    public View.OnClickListener addSecondCardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

       addListeners();

        memoryAdaptor = new MemoryTypeAdaptor(DataUtil.MemoryPairs, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                currentPosition = position;

                launchQr(true);
                Toast.makeText(MainActivity.this, "HOIIIII " + position, Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(memoryAdaptor);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerViewMomory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        logBookButton = findViewById(R.id.logbook_btn);
        clearAllButton = findViewById(R.id.clearall_btn);
        clearConstraintsButton = findViewById(R.id.clearconstraints_btn);

        btn_addCard = findViewById(R.id.btn_addCard);

    }

    private void addListeners() {
        logBookButton.setOnClickListener((View view) -> {
            createConfirmationDialog(R.string.confirmation_title, R.string.confirmation_save, R.string.cancel, (dialog, id) -> {
                startActivity(LogAppUtil.createIntent(DataUtil.MemoryPairs));
            }, (dialog, id) -> {
            });
        });
        clearAllButton.setOnClickListener((View view) -> {
            createConfirmationDialog(R.string.clearall_title, R.string.clearall_save, R.string.cancel, (dialog, id) -> {

                DataUtil.MemoryPairs.clear();

                memoryAdaptor.notifyDataSetChanged();
            }, (dialog, id) -> {
            });
        });
        clearConstraintsButton.setOnClickListener((View view) -> createConfirmationDialog(R.string.clearconstraint_title,
                R.string.clearconstraints_save, R.string.cancel,(dialog, id) -> {
//                    pairs.clear();
//                    memoryCards.forEach(card -> card.setId(null));
                    memoryAdaptor.notifyDataSetChanged();
                }, (dialog, id) -> {} ));

        btn_addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchQr(false);
            }
        });

    }

    private void createConfirmationDialog(int text, int textPositive, int textNegative,
                                          DialogInterface.OnClickListener listenerPositive,
                                          DialogInterface.OnClickListener listenerNegative) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(text)
                .setPositiveButton(textPositive, listenerPositive)
                .setNegativeButton(textNegative, listenerNegative)
                .create()
                .show();
    }


    boolean isSecond = false;
    public static int currentPosition;

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {

                    String qrContent = result.getContents();
                    String imagePath = result.getBarcodeImagePath();


                    MemoryCard mCard = new MemoryCard(qrContent,imagePath);
                    if(isSecond){
                        DataUtil.MemoryPairs.get(currentPosition).cardTwo = mCard;
//                        Toast.makeText(MainActivity.this,"Pos: " +postion, Toast.LENGTH_LONG).show();
                    }else{
                        DataUtil.MemoryPairs.add(new MemoryPair(mCard));
                    }

                    memoryAdaptor.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Scanned: " + mCard.path, Toast.LENGTH_LONG).show();
                }
            });

    // Launch
    public void launchQr(boolean isSecond) {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a barcode");
        options.setCaptureActivity(MyCaptureActivity.class);
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        options.setOrientationLocked(false);

        this.isSecond = isSecond;

        barcodeLauncher.launch(options);
    }



//    private static class MemoryOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            currentView = view;
//
//            int postion = recyclerView.getChildAdapterPosition(view);
//            addCard(memoryCards.get(postion));
//            memoryAdaptor.notifyItemChanged(postion);
//        }
//
//        private void addCard(MemoryCard card) {
//            if (!pairs.isEmpty()) {
//                MemoryPair memoryPair = pairs.get(pairs.size() - 1);
//                if (!memoryPair.isComplete()) {
//                    card.setId(String.valueOf(pairs.size() - 1));
//                    memoryPair.cardTwo = (card);
//
//                    return;
//                }
//            }
//            MemoryPair memoryPairNew = new MemoryPair(card);
//            pairs.add(memoryPairNew);
//            card.setId(String.valueOf(pairs.size() - 1));
//        }
//    }
//


    }