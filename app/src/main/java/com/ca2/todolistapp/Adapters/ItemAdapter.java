package com.ca2.todolistapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                int r=database.deleteItem(data.getId());
                listner.response(r);
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
