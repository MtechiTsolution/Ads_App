package mtech.pak.tajrabaadds;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;



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

public class Main12Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Showuserdata> models;
    adopter2 adapters;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
    FirebaseAuth mauth;

    FirebaseUser user;

    Showuserdata messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main12);
        recyclerView=findViewById(R.id.showuserrecler);
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        models = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Main12Activity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(Main12Activity.this, DividerItemDecoration.VERTICAL));
        adapters = new adopter2(Main12Activity.this , models,1);

        recyclerView.setAdapter(adapters);
    }

    @Override
    protected void onStart() {
        super.onStart();


        models.clear();



        dr.child("user profile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages = dataSnapshot.getValue(Showuserdata.class);
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
