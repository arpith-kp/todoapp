package com.example.arpithbackup.todoapp.adapters;

/**
 * Created by arpithbackup on 2/12/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arpithbackup.todoapp.models.ItemModel;
import com.example.arpithbackup.todoapp.R;
import com.example.arpithbackup.todoapp.activities.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<ItemModel> todos;
    private WeakReference<Context> mContextWeakReference;

    //public RecyclerViewAdapter(List<String> items, Context context) {
    public RecyclerViewAdapter(ArrayList<ItemModel> items, Context context) {
        this.todos = items;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_list, parent, false);
        Context context = mContextWeakReference.get();

        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.todoText.setText(todos.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void setItems(List<ItemModel> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todoText;
        public LinearLayout ll;

        public ViewHolder(View v, final Context context) {
            super(v);
            todoText = (TextView) v.findViewById(R.id.lvTodoItem);
            todoText.setTextColor(ContextCompat.getColor(context, R.color.colorBlueWhale));
            ll = (LinearLayout) itemView.findViewById(R.id.rr_layout);
            ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ((MainActivity) context).itemClick(getAdapterPosition());
                }
            });
        }


    }
}

