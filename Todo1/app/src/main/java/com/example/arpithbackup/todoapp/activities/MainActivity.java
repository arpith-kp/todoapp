package com.example.arpithbackup.todoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import com.example.arpithbackup.todoapp.interfaces.ItemClickInterface;
import com.example.arpithbackup.todoapp.models.ItemModel;
import com.example.arpithbackup.todoapp.R;
import com.example.arpithbackup.todoapp.adapters.RecyclerViewAdapter;
import com.example.arpithbackup.todoapp.utils.StoreItemsInDb;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickInterface {
    private ArrayList<String> items;
    private RecyclerViewAdapter itemsAdapter;
    private StoreItemsInDb db;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<ItemModel> itemList;
    private final int EDIT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        db = StoreItemsInDb.getInstance(context);
        recyclerView = (RecyclerView) findViewById(R.id.lvItem);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(ContextCompat.getColor(context, R.color.colorLighterCoral));
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        itemList = db.getAllItemFromDb();
        itemsAdapter = new RecyclerViewAdapter(itemList, this);
        recyclerView.setAdapter(itemsAdapter);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        ItemModel item = new ItemModel();
        item.setName(itemText);
        db.addItemToDb(item);
        itemList.clear();
        itemList.addAll(db.getAllItemFromDb());
        itemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
    }

    public void onDeleteAllItem(View v) {
        db.deleteAllItemFromDb();
        itemList.clear();
        itemList.addAll(db.getAllItemFromDb());
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_REQUEST) {
                int pos = data.getExtras().getInt("editPos");
                int delPos = data.getExtras().getInt("delPos", -1);
                ItemModel item = data.getParcelableExtra("editItem");
                if (delPos != -1) {
                    db.deleteItemFromDb(itemList.get(delPos));
                    itemList.remove(delPos);
                    itemList.clear();
                    itemList.addAll(db.getAllItemFromDb());
                    itemsAdapter.notifyDataSetChanged();
                    return;
                }
                itemList.set(pos, item);
                db.updateItemToDb(item);
                itemList.clear();
                itemList.addAll(db.getAllItemFromDb());
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void itemClick(int pos) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        ItemModel item = itemList.get(pos);
        i.putExtra("item", item);
        i.putExtra("pos", pos);
        startActivityForResult(i, EDIT_REQUEST);
    }

}


