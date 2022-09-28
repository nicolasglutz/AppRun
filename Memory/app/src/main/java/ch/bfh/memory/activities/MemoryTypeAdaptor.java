package ch.bfh.memory.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ch.bfh.memory.R;
import ch.bfh.memory.models.MemoryCard;

public class MemoryTypeAdaptor extends RecyclerView.Adapter<MemoryTypeAdaptor.MemoryViewHolder> {

    List<MemoryCard> memoryCards;

    public MemoryTypeAdaptor(List<MemoryCard> memoryCards) {
        this.memoryCards = memoryCards;
    }

    @NonNull
    @Override
    public MemoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_laout, parent, false);

        MemoryViewHolder viewHolder = new MemoryViewHolder(view);
        view.setOnClickListener(MainActivity.memoryListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryViewHolder holder, int position) {
        TextView text = holder.getText();
        ImageView img = holder.getImg();
        TextView id = holder.getTextid();

        text.setText(memoryCards.get(position).getWord());

        id.setText("1");
        File imgFile = new File(memoryCards.get(position).getPath());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return memoryCards.size();
    }

    public static class MemoryViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView img;
        private TextView textid;

        public MemoryViewHolder(View viewItem) {
            super(viewItem);
            this.text = (TextView) viewItem.findViewById(R.id.memory_text);
            this.img = viewItem.findViewById(R.id.img_memory);
            this.textid = viewItem.findViewById(R.id.memory_id);
        }

        public TextView getText() {
            return text;
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getTextid() {
            return textid;
        }

    }
}
