package com.example.tapme;

import static com.example.tapme.ChatWindow.reciverImg;
import static com.example.tapme.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModelClass> messageModelClassArrayListAdapeter;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE=2;

    public MessageAdapter(Context context,ArrayList<MessageModelClass> messageModelClassArrayListAdapeter) {
        this.messageModelClassArrayListAdapeter = messageModelClassArrayListAdapeter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModelClass messageModelClass = messageModelClassArrayListAdapeter.get(position);
        if(holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtext.setText(messageModelClass.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else{
            receiverViewHolder viewHolder= (receiverViewHolder) holder;
            viewHolder.msgtext.setText(messageModelClass.getMessage());
            Picasso.get().load(reciverImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelClassArrayListAdapeter.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModelClass messageModelClass = messageModelClassArrayListAdapeter.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModelClass.getSenderId())){
            return ITEM_SEND;
        }
        return ITEM_RECEIVE;
    }

    class senderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtext;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.msgsenderphoto);
            msgtext = itemView.findViewById(R.id.sendertext);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtext;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.msgreceiverphoto);
            msgtext = itemView.findViewById(R.id.msgreceivertext);

        }
    }
}
