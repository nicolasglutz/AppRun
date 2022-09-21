package ch.bfh.memory.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {
        memoryListener = new MemoryOnClickListener();
        pairs = new ArrayList<>();
        memoryCards = List.of(new MemoryCard("test", "miow", null), new MemoryCard("test2", "miow", null), new MemoryCard("test3", "miow", "3"), new MemoryCard("test4", "miow", "3") );
        recyclerView = findViewById(R.id.recyclerViewMomory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter( new MemoryTypeAdaptor(memoryCards));

    }

    private static class MemoryOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int postion = recyclerView.getChildAdapterPosition(view);
            addCard(memoryCards.get(postion));
        }

        private void addCard(MemoryCard card) {
            if (!pairs.isEmpty())
            {
                MemoryPair memoryPair = pairs.get(pairs.size() - 1);
                if (!memoryPair.isComplete()) {
                    card.setId(String.valueOf(pairs.size()-1));
                    memoryPair.setCardTwo(card);
                    return;
                }
            }
            MemoryPair memoryPairNew = new MemoryPair(card);
            pairs.add(memoryPairNew);
            card.setId(String.valueOf(pairs.size()-1));
        }
    }
}