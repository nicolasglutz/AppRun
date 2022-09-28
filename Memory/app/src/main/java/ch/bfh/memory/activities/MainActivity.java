package ch.bfh.memory.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

//        writeObjectFile();

//        loadObjectFile();
    }

    private void loadObjectFile(){

        try{
            FileInputStream fis = getApplicationContext().openFileInput("cards.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            MemoryCard mCard = (MemoryCard) is.readObject();

            Log.d("SERIALIZECARDS", "loadObjectFile: " + mCard.path);

            is.close();
            fis.close();

        }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void writeObjectFile(){


        try {

            FileOutputStream fos = null;
            fos = getApplicationContext().openFileOutput("cards.txt", Context.MODE_PRIVATE);

            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(new MemoryCard("dasdis","dsfsdfds"));
            os.close();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                    memoryPair.setCardTwo(card);

                    return;
                }
            }
            MemoryPair memoryPairNew = new MemoryPair(card);
            pairs.add(memoryPairNew);
            card.setId(String.valueOf(pairs.size() - 1));
        }
    }
}