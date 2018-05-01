package com.jacob.firemessaging;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView usernameDisplay;
    private TextView messageDisplay;
    private TextView timestampDisplay;

    public MessageViewHolder(View itemView) {
        super(itemView);

        usernameDisplay = (TextView) itemView.findViewById(R.id.username);
        messageDisplay = (TextView) itemView.findViewById(R.id.message);
        timestampDisplay = (TextView) itemView.findViewById(R.id.timestamp);
    }

    public void bind(Message message) {
        messageDisplay.setText(message.getMessage());
        usernameDisplay.setText(message.getUsername());
        timestampDisplay.setText(message.getFormattedTimestamp());
    }
}