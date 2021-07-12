package mtech.pak.tajrabaadds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Main11Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<withdrwadata> models;
    adopter adapters;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
    FirebaseAuth mauth;
 //   Uri filepath;
    FirebaseUser user;
  //  String nmp,nmp2,nmp3,downloodurl;
    withdrwadata messages;
//    TextView timg,ttxt,tlnk,timg2,ttxt2,tlnk2,Wdraw,Invester;
//    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main11 );
        recyclerView=findViewById(R.id.recler);
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        models = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Main11Activity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(Main11Activity.this, DividerItemDecoration.VERTICAL));
        adapters = new adopter(Main11Activity.this , models,1);

        recyclerView.setAdapter(adapters);
    }
    @Override
    protected void onStart() {
        super.onStart();
        models.clear();
        dr.child("Withdraw").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages = dataSnapshot.getValue(withdrwadata.class);
                models.add(messages);
                adapters.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
