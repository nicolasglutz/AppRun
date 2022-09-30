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

import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;
import ch.bfh.memory.utils.DataUtil;
import ch.bfh.memory.utils.LogAppUtil;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static List<MemoryCard> memoryCards;
    private static List<MemoryPair> pairs;
    public static View.OnClickListener memoryListener;
    public static RecyclerView.Adapter memoryAdaptor;
    private Button logBookButton;
    private Button clearConstraintsButton;
    private Button clearAllButton;
    private Button btn_addCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

       addListeners();
    }

    private void init() {
        memoryListener = new MemoryOnClickListener();
        pairs = new ArrayList<>();
        memoryCards = new ArrayList<>(List.of(new MemoryCard("test", "miow", null), new MemoryCard("test2", "miow", null), new MemoryCard("test3", "miow", null), new MemoryCard("test4", "miow", null)));
        recyclerView = findViewById(R.id.recyclerViewMomory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new MemoryTypeAdaptor(memoryCards);
        recyclerView.setAdapter(memoryAdaptor);
        logBookButton = findViewById(R.id.logbook_btn);
        clearAllButton = findViewById(R.id.clearall_btn);
        clearConstraintsButton = findViewById(R.id.clearconstraints_btn);

        btn_addCard = findViewById(R.id.btn_addCard);

    }

    private void addListeners() {
        logBookButton.setOnClickListener((View view) -> {
            createConfirmationDialog(R.string.confirmation_title, R.string.confirmation_save, R.string.cancel, (dialog, id) -> {
                startActivity(LogAppUtil.createIntent(pairs));
            }, (dialog, id) -> {
            });
        });
        clearAllButton.setOnClickListener((View view) -> {
            createConfirmationDialog(R.string.clearall_title, R.string.clearall_save, R.string.cancel, (dialog, id) -> {
                pairs.clear();
                memoryCards.clear();
                memoryAdaptor.notifyDataSetChanged();
            }, (dialog, id) -> {
            });
        });
        clearConstraintsButton.setOnClickListener((View view) -> createConfirmationDialog(R.string.clearconstraint_title,
                R.string.clearconstraints_save, R.string.cancel,(dialog, id) -> {
                    pairs.clear();
                    memoryCards.forEach(card -> card.setId(null));
                    memoryAdaptor.notifyDataSetChanged();
                }, (dialog, id) -> {} ));

        btn_addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchQr();
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


    private static class MemoryOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int postion = recyclerView.getChildAdapterPosition(view);
            addCard(memoryCards.get(postion));
            memoryAdaptor.notifyItemChanged(postion);
        }

        private void addCard(MemoryCard card) {
            if (!pairs.isEmpty()) {
                MemoryPair memoryPair = pairs.get(pairs.size() - 1);
                if (!memoryPair.isComplete()) {
                    card.setId(String.valueOf(pairs.size() - 1));
                    memoryPair.cardTwo = (card);

                    return;
                }
            }
            MemoryPair memoryPairNew = new MemoryPair(card);
            pairs.add(memoryPairNew);
            card.setId(String.valueOf(pairs.size() - 1));
        }
    }





    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {


                    String qrContent = result.getContents();
                    String imagePath = result.getBarcodeImagePath();

                    MemoryCard mCard = new MemoryCard(qrContent,imagePath);

                    DataUtil.MemoryCards.add(mCard);

                    //Wie kann ich content von dem setzen??
                    memoryAdaptor = new MemoryTypeAdaptor(DataUtil.MemoryCards);

                    Toast.makeText(MainActivity.this, "Scanned: " + mCard.path, Toast.LENGTH_LONG).show();
                }
            });

    // Launch
    public void launchQr() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a barcode");
        options.setCaptureActivity(MyCaptureActivity.class);
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        options.setOrientationLocked(false);


        barcodeLauncher.launch(options);
    }



//    /**
//     * Scan the QR-Code
//     */
//    private final ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(
//            new ScanContract(),
//            result -> {
//                if (result.getContents() == null) {
//                    Intent originalIntent = result.getOriginalIntent();
//                    if (originalIntent == null) {
//                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
//                        // alert user of missing Camera permission
//                    }
//                } else {
//                    String code = result.getContents();
//                    String path = result.getBarcodeImagePath();
//                    if(createNewPair)
//                        dataBaseHelper.CreatePair(new Card(path,code));
//                    else if(!createNewPair && selectedPair != null){
//                        dataBaseHelper.AddToPair(selectedPair,new Card(path, code));
//                    }
//                    selectedPair = null;
//                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                    loadRecyclerView();
//                }
//            });
//    public void launchQr() {
//        ScanOptions scanOptions = new ScanOptions();
//        scanOptions.setCaptureActivity(MyCaptureActivity.class);
//        scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
//        scanOptions.setOrientationLocked(false);
//        scanOptions.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
//        scanOptions.setBeepEnabled(false);
//        scanOptions.setPrompt("Scan a QR code");
//        qrLauncher.launch(scanOptions);
//    }




}