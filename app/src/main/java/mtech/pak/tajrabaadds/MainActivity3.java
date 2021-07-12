package mtech.pak.tajrabaadds;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity3 extends AppCompatActivity {
    FloatingActionButton Profile_image;
    CircleImageView imageView;
    EditText name;
    Button next;
    Uri filepath;
    FirebaseAuth mauth;
    FirebaseUser user;
    String downloodurl;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
     String kp;
     String DATEFORMAT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Profile_image=findViewById(R.id.pfpic);
        imageView=findViewById(R.id.profileid);
        name=findViewById(R.id.nme);
        next=findViewById(R.id.sve);
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();


        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                DATEFORMAT= response.getFirstHeader("Date").getValue();
                //Here I do something with the Date String
                //Toast.makeText( MainActivity3.this,""+dateStr ,Toast.LENGTH_SHORT).show();

            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
        }catch (IOException e) {
            Log.d("Response", e.getMessage());
        }


        Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),1);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                if(name.getText().toString().length()!=0)
                {
                   // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    dr.child("user profile").child(user.getUid()).child("name").setValue(name.getText().toString());
                    dr.child("user profile").child(user.getUid()).child("date").setValue(DATEFORMAT);
                    dr.child("user profile").child(user.getUid()).child("Ads size").setValue("2");
                    if(kp.equals( "11abbas" ))
                    {
                        Intent intent=new Intent(MainActivity3.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        dr.child("Number").child(kp).setValue("1");
                        Intent intent=new Intent(MainActivity3.this,MainActivity5.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }

            }
        });
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

                final StorageReference stdf=sr.child("images/"+user.getUid() );
                UploadTask uploadTask = stdf.putFile(filepath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
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


                            dr.child("user profile").child(user.getUid()).child("images").setValue(downloodurl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {

                                            if (task2.isSuccessful())
                                            {

                                                Picasso.get().load(downloodurl).placeholder(R.drawable.man).into(imageView);
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity3.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(MainActivity3.this,"Error "+task2.getException(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

                    }

                });




            }
        }
    }

    public boolean network()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return  connected;
    }

    @Override
    protected void onStart() {
        super.onStart();


        if(user!=null)
        {
            if(!network())
            {
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity3.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView  = inflater.inflate(R.layout.networkingerror, null);
                Button button=customView.findViewById( R.id.neteror );
                dialogBuilder.setCancelable( false );

                dialogBuilder.setView(customView);
                final android.app.AlertDialog dialog= dialogBuilder.show();
                dialog.show();
                button.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStart();
                        dialog.dismiss();
                    }
                } );
            }
            else
            {
                dr.child( "Loading" ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity3.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView  = inflater.inflate(R.layout.sarvererror, null);
                            Button button=customView.findViewById( R.id.servreror );
                            dialogBuilder.setCancelable( false );
                            dialogBuilder.setView(customView);
                            final android.app.AlertDialog dialog= dialogBuilder.show();
                            dialog.show();
                            button.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(MainActivity3.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );

                        }
                        else
                        {
                            Intent intent=getIntent();
                            kp=intent.getStringExtra("ali");
                            if(!kp.equals( "11abbas" ))
                            {
                                dr.child("user profile").child(user.getUid()).child("number").setValue(kp);
                            }



                            dr.child("user profile").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull final DataSnapshot dataSnapshotp, @Nullable String s) {

                                    dr.child("Number").child(kp).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {

                                               // Toast.makeText(MainActivity3.this,"ohoo",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                if(dataSnapshotp.child("shere").child(kp).exists())
                                                {
                                                    dr.child("user profile").child(dataSnapshotp.getKey()).child("shere").child(kp).setValue("set");

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


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
                            retrive();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        }




    }

    void retrive()
    {
        dr.child("user profile").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name"))
                {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                }
                if(dataSnapshot.exists()&& dataSnapshot.hasChild("images"))
                {
                    downloodurl=dataSnapshot.child("images").getValue().toString();
                    Picasso.get().load(downloodurl).placeholder(R.drawable.man).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }





}