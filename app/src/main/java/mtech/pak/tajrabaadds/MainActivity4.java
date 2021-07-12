package mtech.pak.tajrabaadds;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Random;


public class MainActivity4 extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd m1RewardedVideoAd;
    private InterstitialAd mInterstitialAd;
TextView textView,textView2;
EditText editText;
Button button;
     int random;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    int i=1,counter,ads_size;
    float balance=0;
    AdView mAdView1,mAdView2,mAdView3,mAdView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        textView=findViewById(R.id.txt);
        textView2=findViewById(R.id.txt2);
        editText=findViewById(R.id.edttxt);
        button=findViewById(R.id.okg);
        mAdView1 = findViewById(R.id.adView1);
        mAdView2 = findViewById(R.id.adView2);
        mAdView3 = findViewById(R.id.adView3);

        mAdView10 = findViewById(R.id.pkg);


        m1RewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        m1RewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        m1RewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        m1RewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();


        reperts();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);
        AdRequest adRequest10 = new AdRequest.Builder().build();
        mAdView10.loadAd(adRequest10);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(String.valueOf(random)))
                {


                        progressDialog=new ProgressDialog(MainActivity4.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setTitle( "Loading Rewards...." );
                        progressDialog.show();


                    reperts();
                    editText.setText("");
                   // balance= (float) (balance+0.01);
                  //  dr.child("user profile").child(user.getUid()).child("balance").setValue(String.valueOf(balance));
                   // onStart();
                }
            }
        });


    }

    public String reperts()
    {
         random = new Random().nextInt((99999-10000)+1)+10000 ;
        textView.setText(String.valueOf(random));
        return String.valueOf(random);
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
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity4.this);
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
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity4.this);
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
                                    Intent intent=new Intent(MainActivity4.this,MainActivity.class);
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


                                    if (dataSnapshot.hasChild("balance"))
                                    {
                                        balance=Float.parseFloat(dataSnapshot.child("balance").getValue().toString());
                                       // textView2.setText(dataSnapshot.child("balance").getValue().toString()+" $");
                                        Float f=Float.parseFloat( dataSnapshot.child("balance").getValue().toString() );
                                        textView2.setText( new DecimalFormat("##.##").format( f )+" $" );

                                    }
                                    else
                                    {
                                        balance=0;
                                        textView2.setText(" 0.$");
                                    }

                                    if(dataSnapshot.hasChild( "Ads size" ))
                                    {
                                        ads_size=Integer.parseInt( dataSnapshot.child( "Ads size" ).getValue().toString() );

                                    }
                                    else{
                                        ads_size=2;
                                        dr.child("user profile").child(user.getUid()).child("Ads size").setValue("2");
                                    }
                                    if(dataSnapshot.hasChild( "counter" ))
                                    {
                                        counter=Integer.parseInt( dataSnapshot.child( "counter" ).getValue().toString() );
                                        if(counter>=ads_size)
                                        {
                                            Intent intent=new Intent(MainActivity4.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                        //dr.child("user profile").child(user.getUid()).child("counter").setValue("0");
                                    }
                                    else
                                    {
                                        counter=0;
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

    @Override
    public void onRewardedVideoAdLoaded() {

        if (m1RewardedVideoAd.isLoaded()) {
            m1RewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        Toast.makeText(this, "onRewardedVideoAdFailedToLoad"+i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }
//    @Override
//    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
//        if (mRewardedVideoAd.isLoaded()) {
//
//            progressDialog.dismiss();
//            mRewardedVideoAd.show();
//        }
//    }
//
//    @Override
//    public void onRewardedVideoAdOpened() {
//
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoStarted() {
//
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdClosed() {
//
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onRewarded(RewardItem rewardItem) {
//        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
//                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
//        dr.child("user profile").child(user.getUid()).child("counter").setValue(counter+1);
//        balance= (float) (balance+0.1);
//        dr.child("user profile").child(user.getUid()).child("balance").setValue(String.valueOf(balance));
//
//
//    }
//
//    @Override
//    public void onRewardedVideoAdLeftApplication() {
//
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdFailedToLoad(int i) {
//
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onRewardedVideoCompleted() {
//
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
//        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-1882443780838389/5473280466");
//        AdRequest adRequestInter = new AdRequest.Builder().build();
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                mInterstitialAd.show();
//            }
//        });
//        mInterstitialAd.loadAd(adRequestInter);
//
//    }
//    public void loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());
//
//    }

    private void loadRewardedVideoAd() {
        m1RewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }


}