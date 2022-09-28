package ch.bfh.memory.activities;

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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ch.bfh.memory.R;

import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;

public class MainActivity extends AppCompatActivity{

    private static RecyclerView recyclerView;
    private static List<MemoryCard> memoryCards;
    private static List<MemoryPair> pairs;
    public static View.OnClickListener memoryListener;
    public static RecyclerView.Adapter memoryAdaptor;

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


    }

    private void init()
    {
        memoryListener = new MemoryOnClickListener();
        pairs = new ArrayList<>();
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
        File[] files = directory.listFiles();
        memoryCards = new ArrayList<>();
        for (int i = 0; i < files.length; i++)
        {
            memoryCards.add(new MemoryCard("test", files[i].getPath(), 0));
        }
//        memoryCards = List.of(new MemoryCard("test", "miow", null), new MemoryCard("test2", "miow", null), new MemoryCard("test3", "miow", null), new MemoryCard("test4", "miow", null) );
        recyclerView = findViewById(R.id.recyclerViewMomory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new MemoryTypeAdaptor(memoryCards);
        recyclerView.setAdapter( memoryAdaptor );

    }

    private static class MemoryOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int postion = recyclerView.getChildAdapterPosition(view);
            addCard(memoryCards.get(postion));
            memoryAdaptor.notifyItemChanged(postion);
        }

        private void addCard(MemoryCard card) {
            if (!pairs.isEmpty())
            {
                MemoryPair memoryPair = pairs.get(pairs.size() - 1);
                if (!memoryPair.isComplete()) {
                    card.setId((pairs.size()-1));
                    memoryPair.cardTwo = card;

                    return;
                }
            }
            MemoryPair memoryPairNew = new MemoryPair(card);
            pairs.add(memoryPairNew);
            card.setId((pairs.size()-1));
        }
    }
}