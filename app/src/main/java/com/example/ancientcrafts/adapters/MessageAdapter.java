package com.example.ancientcrafts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.ChatMessage;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<ChatMessage> messageList;
    private final String currentUserId;

    public MessageAdapter(List<ChatMessage> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.messageText.setText(message.getMessage());

        // Simple styling: align message left or right based on sender
        if (message.getSenderId().equals(currentUserId)) {
            // Message from current user — align right
            holder.messageText.setBackgroundResource(R.drawable.message_background_sent);
            holder.messageText.setTextColor(0xFFFFFFFF);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        } else {
            // Message from other user — align left
            holder.messageText.setBackgroundResource(R.drawable.message_background_received);
            holder.messageText.setTextColor(0xFF000000);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }

    }
}
