package com.ca2.todolistapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ca2.todolistapp.Databases.Database;
import com.ca2.todolistapp.Interfaces.ItemId;
import com.ca2.todolistapp.Interfaces.Listner;
import com.ca2.todolistapp.Pojo.Data;
import com.ca2.todolistapp.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
    Context context;
    ArrayList<Data> arrayList;
    Database database;
    Listner listner;
    ItemId itemIdListner;
    public ItemAdapter(Context context, ArrayList<Data> arrayList,Listner listner,ItemId itemIdListner)
    {
        this.itemIdListner=itemIdListner;
        this.listner=listner;
        this.context=context;
        this.arrayList=arrayList;
        database=new Database(context);
    }
    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_list_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, final int position)
    {
        final Data data=arrayList.get(position);
        holder.topic.setText(data.getTopic());
        holder.description.setText(data.getDescription());
        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               conformDialog(data.getId());
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                itemIdListner.currentItem(data.getId());
            }
        });
    }
    public void conformDialog(final int id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Are you sure you want delete?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which)
            {
                int r=database.deleteItem(id);
                listner.response(r);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView topic,description;
        ImageView delete,update;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            topic=itemView.findViewById(R.id.topic);
            description=itemView.findViewById(R.id.description);
            delete=itemView.findViewById(R.id.deleteItem);
            update=itemView.findViewById(R.id.edit);

        }
    }
}
