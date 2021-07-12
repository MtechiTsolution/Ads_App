package mtech.pak.tajrabaadds;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity7 extends AppCompatActivity {

    Button b;
    EditText e1,e2,e3;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fb;
    DatabaseReference dr,RootRef;
    int i=0;
   float balance=0;
    String pins="";
    String messagePushID;
    AdView mAdView1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        RootRef = FirebaseDatabase.getInstance().getReference();
        b=findViewById(R.id.okdrw);
        e1=findViewById(R.id.acnt);
        e2=findViewById(R.id.phnmber);
        e3=findViewById(R.id.epin);
        textView=findViewById( R.id.fgpin );
        mAdView1 = findViewById(R.id.adView1p);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

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
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (e1.getText().toString().length()!=0&&e2.getText().toString().length()!=0&&e3.getText().toString().length()!=0)
               {
                 if(pins.equals(e3.getText().toString() ))
                 {
                     if(i>=0)
                     {
                         if(balance>=0)
                         {

                             withdrwadata withdrwadatap=new withdrwadata(e1.getText().toString(),e2.getText().toString(),user.getUid());

                             dr.child("Withdraw").child(user.getUid()).setValue(withdrwadatap);
                             dr.child("user profile").child(user.getUid()).child("drawmoney").setValue("1");
                             pbg();


                         }
                         else {

                             dialoguesd("your belence is low....\nit must greater than or equal to 200 $");
                             return;
                         }
                     }
                     else
                     {
                         dialoguesd("minmum five shere are must and install this app");
                         return;
                     }
                 }
                 else
                 {
                     dialoguesd("wrong pin code...");
                     return;
                 }
               }
               else
               {
                  dialoguesd("fill up all fields");
                   return;
               }

            }
        });

        textView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity7.this,MainActivity2.class);
                intent.putExtra( "abc",1 );
                startActivity(intent);
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user!=null)
        {
            if(!network())
            {
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity7.this);
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
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity7.this);
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
                                    Intent intent=new Intent(MainActivity7.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );

                        }
                        else
                        {
                            if(i==0)
                            {
                                dr.child("user profile").child(user.getUid()).child("shere").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        if(dataSnapshot.exists()&&dataSnapshot.getValue().toString().equals("set"))
                                        {
                                            i++;
                                        }
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
                            dr.child("user profile").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("balance"))
                                    {
                                       // balance=Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                                        balance=Float.parseFloat( dataSnapshot.child( "balance" ).getValue().toString() );

                                    }
                                    if (dataSnapshot.hasChild("pin"))
                                    {
                                        pins=dataSnapshot.child("pin").getValue().toString();
                                    }
                                    if (dataSnapshot.hasChild("drawmoney"))
                                    {
                                        if( dataSnapshot.child("drawmoney").getValue().toString().equals("1"))
                                        {
                                            pbg();

                                        }
                                        else  if( dataSnapshot.child("drawmoney").getValue().toString().equals("0"))
                                        {
                                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity7.this);
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View customView  = inflater.inflate(R.layout.congratulation, null);
                                            Button button=customView.findViewById( R.id.congpl );
                                            dialogBuilder.setView(customView);
                                            final android.app.AlertDialog dialog= dialogBuilder.show();
                                            dialog.show();
                                            button.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                                Uri.parse("market://details?id=" + this.getPackageName())));
                                                    } catch (android.content.ActivityNotFoundException e)
                                                    {
                                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                                Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
                                                    }
                                                }

                                                private String getPackageName() {
                                                    return MainActivity7.this.getPackageName();
                                                }
                                            } );
                                            dr.child("user profile").child(user.getUid()).child("drawmoney").setValue("2");
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
    void dialoguesd(String s)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity7.this);
        builder1.setMessage(s);
        builder1.setCancelable(true);
        AlertDialog dialog=builder1.create();
        dialog.show();
    }
    void pbg()
    {

        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity7.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView  = inflater.inflate(R.layout.processing, null);
        Button button=customView.findViewById( R.id.asdf );
        dialogBuilder.setCancelable( false );
        dialogBuilder.setView(customView);
        final android.app.AlertDialog dialog= dialogBuilder.show();
        dialog.show();
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        } );
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