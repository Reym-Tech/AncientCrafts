package com.example.ancientcrafts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.Notification;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {

    private final List<Notification> list = new ArrayList<>();

    public void addFirst(Notification n) {          // newest on top
        list.add(0, n);
        notifyItemInserted(0);
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View vItem = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_notification, p, false);
        return new VH(vItem);
    }
    @Override public void onBindViewHolder(@NonNull VH h,int i){
        Notification n=list.get(i);
        h.name .setText(n.buyerName + " bought:");
        h.msg  .setText(n.productName);
        h.time .setText(DateFormat.getTimeInstance(DateFormat.SHORT)
                .format(new Date(n.timeMillis)));
    }
    @Override public int getItemCount(){return list.size();}

    static class VH extends RecyclerView.ViewHolder{
        TextView name,msg,time;
        VH(View v){super(v);
            name =v.findViewById(R.id.person_chat_name);
            msg  =v.findViewById(R.id.person_chat_msg);
            time =v.findViewById(R.id.person_chat_time);
        }
    }
}

