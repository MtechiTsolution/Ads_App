package mtech.pak.tajrabaadds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity9 extends AppCompatActivity {

    RecyclerView recyclerView;
    List<withdrwadata> models;
    adopter adapters;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
    FirebaseAuth mauth;
    Uri filepath;
    FirebaseUser user;
    String nmp,nmp2,nmp3,downloodurl;
   withdrwadata messages;
   TextView timg,ttxt,tlnk,timg2,ttxt2,tlnk2,Wdraw,Invester;
   EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);
//        recyclerView=findViewById(R.id.recler);
        timg=findViewById(R.id.upnotiimg);
        ttxt=findViewById(R.id.upnotitxt);
        tlnk=findViewById(R.id.stopapp);
        timg2=findViewById(R.id.upnotiimg2);
        ttxt2=findViewById(R.id.upnotitxt2);
        tlnk2=findViewById(R.id.stopapp2);
        Wdraw=findViewById(R.id.withdrawhost);
        Invester=findViewById(R.id.investerhost);
        editText=findViewById(R.id.multitxt);

        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        models = new ArrayList<>();

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1882443780838389/5473280466");
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(adRequestInter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity9.this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity9.this, DividerItemDecoration.VERTICAL));
//        adapters = new adopter(MainActivity9.this , models,1);
//
//        recyclerView.setAdapter(adapters);

        timg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),1);
            }
        } );
        ttxt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length()!=0)
                {
                    dr.child("Notifications").child("text").setValue(editText.getText().toString());
                    editText.setText( "" );
                }
            }
        } );
        tlnk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("Loading").setValue("1");
            }
        } );

        timg2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                database.getReference("Notifications").child("images")
                        .removeValue();
            }
        } );
        ttxt2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                database.getReference("Notifications").child("text")
                        .removeValue();
            }
        } );
        tlnk2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                database.getReference("Loading")
                        .removeValue();
            }
        } );

        Wdraw.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity9.this,Main11Activity.class);

                startActivity(intent);
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {

            filepath=data.getData();

            if (filepath!=null)
            {

                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setTitle("uploading....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final StorageReference stdf=sr.child("notifications/"+user.getUid() );
                UploadTask uploadTask = stdf.putFile(filepath);

                Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot,
                        Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return stdf.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            downloodurl=downloadUri.toString();


                            dr.child("Notifications").child("images").setValue(downloodurl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {

                                            if (task2.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity9.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(MainActivity9.this,"error "+task2.getException(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

                    }

                });

            }
        }
    }


}