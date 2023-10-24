package com.example.tapme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatWindow extends AppCompatActivity {

    String recevierimg, recevieruid,receviername, senderUid;
    CircleImageView profile;
    TextView nameReceiver;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverImg;

    String senderRoom;
    String reciverRoom;
    RecyclerView recyclerView;
    ArrayList<MessageModelClass> messagesArrayList;

    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        messagesArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.chatrecycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatWindow.this,messagesArrayList);
        recyclerView.setAdapter(messageAdapter);



        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        receviername = getIntent().getStringExtra("nameee");
        recevierimg = getIntent().getStringExtra("receiverImage");
        recevieruid = getIntent().getStringExtra("uid");
        senderUid = firebaseAuth.getUid();
        senderRoom = senderUid + recevieruid;
        reciverRoom = recevieruid+ senderUid;




        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.writemsg);
        profile = findViewById(R.id.chatpic);
        nameReceiver = findViewById(R.id.receviername);

        Picasso.get().load(recevierimg).into(profile);
        nameReceiver.setText(""+receviername);

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModelClass messageModelClass = dataSnapshot.getValue(MessageModelClass.class);
                    messagesArrayList.add(messageModelClass);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                reciverImg = recevierimg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = textmsg.getText().toString();
                if(msg.isEmpty()){
                    Toast.makeText(ChatWindow.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date date = new Date();
                MessageModelClass messageModelClass = new MessageModelClass(msg,senderUid,date.getTime());

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push().
                        setValue(messageModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(reciverRoom).child("messages").push().
                                        setValue(messageModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });



    }


}