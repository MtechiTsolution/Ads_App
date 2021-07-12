package mtech.pak.tajrabaadds;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity6 extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fb;
    DatabaseReference dr;
    ImageView imageView;
    LottieAnimationView animationView,animationView2;
    TextView t1,t2;
    CardView cardView;
    AdView mAdView1,mAdView11,mAdView12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        imageView=findViewById(R.id.notifimage);
       cardView=findViewById(R.id.crdimg);
        t1=findViewById(R.id.dtxt);
        animationView=findViewById(R.id.animation_view);
        animationView2=findViewById(R.id.notifgr);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        mAdView1 = findViewById(R.id.adView1p);
//ad1
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
//Ad2
        mAdView11 = findViewById(R.id.adView11p);

        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView11.loadAd(adRequest);
//ad3
        mAdView12 = findViewById(R.id.adView12p);

        AdRequest adRequest12 = new AdRequest.Builder().build();
        mAdView12.loadAd(adRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user!=null)
        {
            if(!network())
            {
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity6.this);
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
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity6.this);
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
                                    Intent intent=new Intent(MainActivity6.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );

                        }
                        else {
                            cardView.setVisibility( View.GONE );
                            imageView.setVisibility( View.GONE );
                            t1.setVisibility( View.GONE );
                            animationView2.setVisibility( View.GONE );
                            dr.child("Notifications").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists())
                                    {
                                        animationView.setVisibility(View.GONE);
                                        animationView2.setVisibility( View.VISIBLE );
                                        if (dataSnapshot.child( "images" ).exists())
                                        {
                                            cardView.setVisibility( View.VISIBLE );
                                            imageView.setVisibility( View.VISIBLE );
                                            Picasso.get().load(dataSnapshot.child( "images" ).getValue().toString()).into(imageView);
                                        }
                                        if(dataSnapshot.child( "text" ).exists())
                                        {
                                            t1.setVisibility( View.VISIBLE );
                                            t1.setText( dataSnapshot.child( "text" ).getValue().toString() );
                                        }

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

}