package mtech.pak.tajrabaadds;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Main15Activity extends AppCompatActivity {

    TextView screenshot,account_name,account_number;
    Uri filepath;
    FirebaseAuth mauth;
    FirebaseUser user;
    String downloodurl;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
    String kp;
    ImageView imageView;
    int invest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main15 );
        screenshot=findViewById( R.id.tkscrsht );
        account_name=findViewById( R.id.acntname );
       account_number=findViewById( R.id.acnnumb );
        imageView=findViewById( R.id.imgscn );
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        Intent intent=getIntent();
        invest=intent.getIntExtra( "invst",0 );
         String name=intent.getStringExtra( "aname" );
         String number=intent.getStringExtra( "anumber" );
         account_name.setText( name );
         account_number.setText( number);

        screenshot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),1);
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
                progressDialog.setTitle("Image is uploading please wait....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final StorageReference stdf=sr.child("Screenshots/"+user.getUid() );
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


                            dr.child("Invester").child(user.getUid()).child("images").setValue(downloodurl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {

                                            if (task2.isSuccessful())
                                            {
                                                dr.child("Invester").child(user.getUid()).child("invest").setValue(String.valueOf( invest ));
                                                dr.child("Invester").child(user.getUid()).child("status").setValue("panding");

                                                Picasso.get().load(downloodurl).placeholder(R.drawable.man).into(imageView);
                                                progressDialog.dismiss();
                                                Toast.makeText(Main15Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(Main15Activity.this,"Error "+task2.getException(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

                    }

                });




            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        dr.child("Invester").child(user.getUid()).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild( "status" ))
                {
                    if(dataSnapshot.child("status").getValue().toString().equals( "panding" ))
                    {
                        screenshot.setVisibility( View.GONE );
                        AlertDialog.Builder alertd=new AlertDialog.Builder( Main15Activity.this );
                        alertd.setTitle( "Investing Verifying" );
                        //alertd.setCancelable( false );
                        alertd.setMessage( "Dont worry...please waite" );
                        AlertDialog alertDialog=alertd.create();
                        alertDialog.show();
                    }
                }
                if(dataSnapshot.hasChild( "images" ))
                {
                    Picasso.get().load(dataSnapshot.child( "images" ).getValue().toString()).placeholder(R.drawable.man).into(imageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}
