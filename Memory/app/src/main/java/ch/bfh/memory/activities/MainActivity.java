package ch.bfh.memory.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import ch.bfh.memory.R;
import ch.bfh.memory.interfaces.ClickListener;
import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;
import ch.bfh.memory.utils.DataUtil;
import ch.bfh.memory.utils.LogAppUtil;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    public static RecyclerView.Adapter memoryAdaptor;

    /**
     * Buttons
     */
    private Button btn_logBook;
    private Button btn_removeAllConnections;
    private Button btn_removeAll;
    private Button btn_addCard;

    private View.OnClickListener listener_addCard;
    private View.OnClickListener listener_sendToLogBook;
    private View.OnClickListener listener_removeAll;
    private View.OnClickListener listener_removeAllConnections;
    private ClickListener listener_addSecondCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the listeners before adding them to the buttons and co...
        initListeners();

        //couple the components with the view and load data
        init();
    }

    private void init() {
        //load data from file
        loadPairs();

        //init recycleview
        recyclerView = findViewById(R.id.recyclerViewMemory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new MemoryTypeAdaptor(DataUtil.MemoryPairs, listener_addSecondCard);
        recyclerView.setAdapter(memoryAdaptor);

        //init buttons
        btn_logBook = findViewById(R.id.btn_logBook);
        btn_logBook.setOnClickListener(listener_sendToLogBook);

        btn_removeAll = findViewById(R.id.btn_removeAll);
        btn_removeAll.setOnClickListener(listener_removeAll);

        btn_removeAllConnections = findViewById(R.id.btn_clearAllConnections);
        btn_removeAllConnections.setOnClickListener(listener_removeAllConnections);

        btn_addCard = findViewById(R.id.btn_addCard);
        btn_addCard.setOnClickListener(listener_addCard);

    }

    /**
     * Initialize all listeners for clickable things
     */
    private void initListeners() {

        //The listener for the logbook button
        listener_sendToLogBook = ((View view) -> {
            createConfirmationDialog(R.string.confirmation_title, R.string.confirmation_save, R.string.cancel, (dialog, id) -> {
                startActivity(LogAppUtil.createIntent(DataUtil.MemoryPairs));
            }, (dialog, id) -> {
            });
        });

        //The listener for deleting all cards
        listener_removeAll = ((View view) -> {
            createConfirmationDialog(R.string.clearall_title, R.string.clearall_save, R.string.cancel, (dialog, id) -> {

                DataUtil.MemoryPairs.clear();

                loadRecycleView();

            }, (dialog, id) -> {
            });
        });

        //The listener for removing all connections
        listener_removeAllConnections = ((View view) -> createConfirmationDialog(R.string.clearconstraint_title,
                R.string.clearconstraints_save, R.string.cancel, (dialog, id) -> {

                    ArrayList<MemoryPair> currentMemoryPairs = new ArrayList<MemoryPair>(DataUtil.MemoryPairs);

                    DataUtil.MemoryPairs.clear();

                    currentMemoryPairs.forEach(pair -> {
                        if (pair.cardTwo != null) {
                            DataUtil.MemoryPairs.add(new MemoryPair(pair.cardOne));
                            DataUtil.MemoryPairs.add(new MemoryPair(pair.cardTwo));
                        } else {
                            DataUtil.MemoryPairs.add(new MemoryPair(pair.cardOne));
                        }
                    });
                    loadRecycleView();
                }, (dialog, id) -> {
                }));

        //the listener for adding new cards
        listener_addCard = (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchQr(false);
            }
        });


        //the listener for adding a second card
        listener_addSecondCard = new ClickListener() {
            @Override
            public void onAddSecondClicked(int position) {
                currentPosition = position;
                launchQr(true);
            }

            @Override
            public void onDeleteClick(int position) {
                currentPosition = position;
                DataUtil.MemoryPairs.remove(position);

                loadRecycleView();
            }

            @Override
            public void onSplitClick(int position) {

                MemoryPair mPair = DataUtil.MemoryPairs.get(position);

                MemoryCard mCardOne = mPair.cardOne;
                MemoryCard mCardTwo = mPair.cardTwo;

                if (mCardOne != null && mCardTwo != null) {

                    DataUtil.MemoryPairs.remove(position);

                    DataUtil.MemoryPairs.add(new MemoryPair(mCardOne));
                    DataUtil.MemoryPairs.add(new MemoryPair(mCardTwo));

                } else {
                    Toast.makeText(MainActivity.this, "Cant split a single card!", Toast.LENGTH_LONG).show();
                }

                loadRecycleView();
            }
        };

    }

    /**
     * We need this function becuase .notifyDataSetChanged doesnt work properly
     */
    public void loadRecycleView(){
        memoryAdaptor = new MemoryTypeAdaptor(DataUtil.MemoryPairs, listener_addSecondCard);
        recyclerView.setAdapter(memoryAdaptor);
    }

    /**
     * Helper for creating a confirmation dialog
     *
     * @param text
     * @param textPositive
     * @param textNegative
     * @param listenerPositive
     * @param listenerNegative
     */
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

    /**
     * This is the activity for the qr libary
     */
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {

                    String qrContent = result.getContents();
                    String imagePath = result.getBarcodeImagePath();


                    MemoryCard mCard = new MemoryCard(qrContent, imagePath);
                    if (isSecond) {
                        DataUtil.MemoryPairs.get(currentPosition).cardTwo = mCard;
                    } else {
                        DataUtil.MemoryPairs.add(new MemoryPair(mCard));
                    }

                    loadRecycleView();

                    Toast.makeText(MainActivity.this, "Qr Code sucessfully scanned!", Toast.LENGTH_LONG).show();
                }
            });

    /**
     * Launch the qr code activity
     *
     * @param isSecond if the qr scanner should scan the code for the second card
     */
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


    @Override
    protected void onPause() {
        super.onPause();
        savePairs();
    }

    private String DataFilename = "cards.txt";

    /**
     * Method for saving all pairs that exist
     */
    private void savePairs() {
        FileOutputStream fos;

        try {
            fos = getApplicationContext().openFileOutput(DataFilename, Context.MODE_PRIVATE);
            getApplicationContext().fileList();
            DataUtil.writePairsFile(fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for loading all pairs on app startup
     */
    private void loadPairs() {
        FileInputStream fis;

        try {
            fis = getApplicationContext().openFileInput(DataFilename);
            String[] files = getApplicationContext().fileList();
            DataUtil.loadPairsFile(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}