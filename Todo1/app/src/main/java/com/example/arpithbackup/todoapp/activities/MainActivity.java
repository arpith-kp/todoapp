package com.example.arpithbackup.todoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.arpithbackup.todoapp.interfaces.ItemClickInterface;
import com.example.arpithbackup.todoapp.models.ItemModel;
import com.example.arpithbackup.todoapp.R;
import com.example.arpithbackup.todoapp.adapters.RecyclerViewAdapter;
import com.example.arpithbackup.todoapp.utils.StoreItemsInDb;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickInterface {
    private ArrayList<String> items;
    private RecyclerViewAdapter itemsAdapter;
    private ListView lvItems;
    private StoreItemsInDb db;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<ItemModel> itemList;

    private final int DELETE_REQUEST = 1;
    private final int EDIT_REQUEST = 2;

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        db = StoreItemsInDb.getInstance(context);
        recyclerView = (RecyclerView) findViewById(R.id.lvItem);
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


