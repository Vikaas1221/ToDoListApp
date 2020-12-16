package com.ca2.todolistapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ca2.todolistapp.Adapters.ItemAdapter;
import com.ca2.todolistapp.Databases.Database;
import com.ca2.todolistapp.Interfaces.ItemId;
import com.ca2.todolistapp.Interfaces.Listner;
import com.ca2.todolistapp.Pojo.Data;
import com.ca2.todolistapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Listner, ItemId
{
    FloatingActionButton floatButton;
    RecyclerView recyclerView;
    Database database;
    AlertDialog dialog;
    RecyclerView.Adapter adapter;
    int currentItemId;
    ArrayList<Data> dataArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=new Database(this);
        floatButton=findViewById(R.id.fab);
        recyclerView=findViewById(R.id.todolistRecycler);
        dataArrayList=displayAllItems();
        displayItemsInRecylerView(dataArrayList);

        floatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAlertDialogToAddItem(view);
            }
        });
    }
    // Method for recyclerview
    public void displayItemsInRecylerView(ArrayList<Data> arrayList)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new ItemAdapter(this,arrayList,this,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    // This alert dialog is to add new item to database
    public void showAlertDialogToAddItem(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View view1=getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);
        builder.setView(view1);
        Button addButton=view1.findViewById(R.id.addButton);
        final EditText topic=view1.findViewById(R.id.itemName);
        final EditText description=view1.findViewById(R.id.description);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String ItemName=topic.getText().toString();
                String Description=description.getText().toString();
                if(ItemName.isEmpty())
                {
                    topic.setError("Required");
                    topic.requestFocus();
                    return;
                }
                if (Description.isEmpty())
                {
                    description.setError("Required");
                    description.requestFocus();
                    return;
                }
                AddItemToDatabase(ItemName,Description);
            }
        });
        dialog=builder.create();
        dialog.show();
    }
    // This method is to add item to database
    public void AddItemToDatabase(String topic,String description)
    {
        boolean res=database.addItemToList(topic,description);
        if (res)
        {
            Toast.makeText(MainActivity.this,"Item Added sucessfully",Toast.LENGTH_SHORT).show();
            dialog.cancel();
          reloadListItem();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
    // This method is to retrive all data from data base
    public ArrayList<Data> displayAllItems()
    {
        ArrayList<Data> dataArrayList=database.getAllItems();
        return dataArrayList;
    }
    // This method is to reload our all list item in the recycler view
    public void reloadListItem()
    {
        displayItemsInRecylerView(displayAllItems());
    }

    // This is interface method which is responsible to refresh the list whenever any data item is deleted from the list.This method
    // will be trigger whenever we delete any item from recyclerview adapter
    @Override
    public void response(int i)
    {
       reloadListItem();
    }
    // This method is to show the alert dilog to update the item in the list
    public void showAlertDialogToUpdateItem(Data data)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View view1=getLayoutInflater().inflate(R.layout.alert_dialog_update_layout,null);
        builder.setView(view1);
        Button updateButton=view1.findViewById(R.id.updateButton);
        final EditText topic=view1.findViewById(R.id.itemName);
        final EditText description=view1.findViewById(R.id.description);
        topic.setText(data.getTopic());
        description.setText(data.getDescription());
        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String ItemName=topic.getText().toString();
                String Description=description.getText().toString();
                if(ItemName.isEmpty())
                {
                    topic.setError("Required");
                    topic.requestFocus();
                    return;
                }
                if (Description.isEmpty())
                {
                    description.setError("Required");
                    description.requestFocus();
                    return;
                }
                updateCurrentItem(currentItemId,ItemName,Description);
            }
        });
        dialog=builder.create();
        dialog.show();
    }
    // This interface method is trigger whenever any data item wants to update from adapter and it will retrive the current list item
    // id which will be used to retrive the information about the particular item from database
    @Override
    public void currentItem(int id)
    {
        currentItemId=id;
        Data data=database.getListItems(id); // Here we are retriving single item from database
        showAlertDialogToUpdateItem(data);

    }
    // This method is to upate the data
    public void updateCurrentItem(int id,String topic,String description)
    {
        boolean responce=database.updateItem(id, topic, description);
        if (responce)
        {
            Toast.makeText(getApplicationContext(),"Updated sucessfully",Toast.LENGTH_SHORT).show();
            dialog.cancel();
            reloadListItem();
        }
    }
}