package com.example.myNotes.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myNotes.Entity.Notes;
import com.example.myNotes.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ListAdapter<Notes,MyAdapter.MyHolder> {

    private Context context;
    public static onItemTouchListener ItemTouchListener;
    private List<Notes> MainList = new ArrayList<>();
    private List<Notes> selectedList = new ArrayList<>();
    private int MainColor;
    public MyAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Notes> DIFF_CALLBACK = new DiffUtil.ItemCallback<Notes>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return  oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
            return (oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.getTimestamp().equals(newItem.getTimestamp()) &&
                    oldItem.getCharachters().equals(newItem.getCharachters())
                    );
        }
    };

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.single_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Notes note = getItem(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.timestamp.setText(note.getTimestamp());
        if (selectedList.contains(note)){
            holder.itemView.setBackgroundColor(Color.RED);
        }
        else holder.itemView.setBackgroundColor(MainColor);
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView title,content,timestamp;
        public MyHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            timestamp = itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ItemTouchListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                        ItemTouchListener.onItemClick(getItem(getAdapterPosition()));

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (ItemTouchListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION)
                        ItemTouchListener.onItemLongClick(getItem(getAdapterPosition()));
                    return true;

                }
            });
        }
    }

    public Notes getNoteAt(int position){
        return getItem(position);
    }

    public interface onItemTouchListener{
        void onItemClick(Notes notes);
        void onItemLongClick(Notes notes);
    }


    public void setonItemTouchListener(onItemTouchListener ItemTouchListener){
        this.ItemTouchListener=ItemTouchListener;
    }
    public void removeOnItemClickListener(){
        ItemTouchListener = null;
    }


    public void setSelectedList(List<Notes> selectedList) {
        this.selectedList = selectedList;
        MainList = getCurrentList();
        notifyDataSetChanged();

    }


    public void setMainColor(int mainColor) {
        MainColor = mainColor;
    }

}
