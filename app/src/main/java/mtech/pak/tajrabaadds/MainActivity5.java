package mtech.pak.tajrabaadds;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity5 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    String downloodurl;
    FirebaseDatabase fb;
    DatabaseReference dr;
    EditText pin;
    Button ok;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        pin=findViewById(R.id.pins);
        ok=findViewById(R.id.pinok);

        adView=findViewById( R.id.adView3o );
        AdRequest adRequest9 = new AdRequest.Builder().build();
        adView.loadAd(adRequest9);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                if(pin.getText().toString().length()==4)
                {
                    dr.child("user profile").child(user.getUid()).child("pin").setValue(pin.getText().toString());
                    Intent intent=new Intent(MainActivity5.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity5.this,"Pin code must contain 4 digit",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity5.this);
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
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity5.this);
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
                                    Intent intent=new Intent(MainActivity5.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );

                        }
                        else
                        {
                            dr.child("user profile").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("pin"))
                                    {
                                        pin.setText(dataSnapshot.child("pin").getValue().toString());
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        }


    }
}