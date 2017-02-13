package com.example.arpithbackup.todoapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.arpithbackup.todoapp.models.ItemModel;
import com.example.arpithbackup.todoapp.R;

public class EditItemActivity extends AppCompatActivity {
    private int editPos;
    private ItemModel item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item = getIntent().getParcelableExtra("item");
        editPos = getIntent().getIntExtra("pos", 0);

        EditText etNewItem = (EditText) findViewById(R.id.editText);
        etNewItem.setText(item.getName());
        etNewItem.setSelection(etNewItem.getText().length());
    }

    public void onDeleteItem(View v) {
        Intent data = new Intent();
        data.putExtra("delPos", editPos);
        data.putExtra("code", 1);
        setResult(RESULT_OK, data);
        this.finish();
    }

    public void onSaveItem(View v) {
        Intent data = new Intent();
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        String editedItem = etNewItem.getText().toString();
        item.setName(editedItem);
        data.putExtra("editItem", item);
        data.putExtra("editPos", editPos);
        data.putExtra("code", 2);
        setResult(RESULT_OK, data);
        this.finish();
    }

}
