package ch.bfh.memory.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.memory.R;
import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;
import ch.bfh.memory.utils.LogAppUtil;

public class MainActivity extends AppCompatActivity{

    private static RecyclerView recyclerView;
    private static List<MemoryCard> memoryCards;
    private static List<MemoryPair> pairs;
    public static View.OnClickListener memoryListener;
    public static RecyclerView.Adapter memoryAdaptor;
    public Button logBookButton;

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
        memoryCards = List.of(new MemoryCard("test", "miow", null), new MemoryCard("test2", "miow", null), new MemoryCard("test3", "miow", null), new MemoryCard("test4", "miow", null) );
        recyclerView = findViewById(R.id.recyclerViewMomory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new MemoryTypeAdaptor(memoryCards);
        recyclerView.setAdapter( memoryAdaptor );
        logBookButton = findViewById(R.id.logbook_btn);
        logBookButton.setOnClickListener((View view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.confirmation_title)
                    .setPositiveButton(R.string.confirmation_save, (dialog, id) -> {
                        // Start logging activity
                        startActivity(LogAppUtil.createIntent(pairs));
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                        // User cancelled the dialog
                    });
            builder.create().show();
        });
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