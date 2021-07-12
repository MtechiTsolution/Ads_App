package mtech.pak.tajrabaadds;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Intent.ACTION_SEND;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    FirebaseAuth mAuth;
    FirebaseUser user;

   ImageView circleImageView;
    AdView mAdView,mAdView2;
    TextView textView,textView2;
    FirebaseDatabase fb;
    DatabaseReference dr;
    long balance=0;
    Button b5,chk1,abt1;
     LinearLayout b1,b3,b2,b4;
    BroadcastReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        abt1=findViewById( R.id.abt);

        circleImageView=findViewById( R.id.imageView );
        textView2=findViewById( R.id.textViewkl );
        textView=findViewById( R.id.textvp );
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();

        networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                if (network()) {

                    dr.child( "Loading" ).addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
//                                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getApplicationContext());
//                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                View customView  = inflater.inflate(R.layout.sarvererror, null);
//
//                                dialogBuilder.setView(customView);
//                                final android.app.AlertDialog dialog= dialogBuilder.show();
//                                dialog.show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );



                } else {



                }

            }
        };



        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        try {
            registerReceiver(networkStateReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }




        final DrawerLayout drawer = findViewById( R.id.drawer_layout );
        NavigationView navigationView = findViewById( R.id.nav_view );
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow ,R.id.file,R.id.share,R.id.About,R.id.Disclaimer,R.id.Exit,R.id.Rate)
                .setDrawerLayout( drawer )
                .build();
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        NavigationUI.setupActionBarWithNavController( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, navController );

        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    drawer.close();
                    return true;
                } else if (item.getItemId() == R.id.share) {

                    Intent shareintent = new Intent();
                    shareintent.setAction( ACTION_SEND );
                    shareintent.putExtra( Intent.EXTRA_TEXT,
                            "https://play.google.com/store/apps/details?id=" + this.getPackageName() );
                    shareintent.setType( "text/plain" );
                    startActivity( Intent.createChooser( shareintent, "share via" ) );

                      return true;
                }
                else if (item.getItemId()==R.id.About){

                    final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView  = inflater.inflate(R.layout.about, null);
                    Button button=customView.findViewById( R.id.abt );
                    dialogBuilder.setCancelable( true );

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
                    return  true;
                }

                else if (item.getItemId()==R.id.Rate) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + this.getPackageName())));
                    } catch (android.content.ActivityNotFoundException e)
                    {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
                    }
                    return  true;
                }
                else  if(item.getItemId()==R.id.nav_slideshow)
                {
                    Intent intent=new Intent(MainActivity.this, MainActivity3.class);
                    intent.putExtra("ali","11abbas");
                    startActivity(intent);
                    drawer.close();
                    return true;
                }
                else  if(item.getItemId()==R.id.file)
                {
                    Intent intent=new Intent(MainActivity.this, Main10Activity.class);
                    startActivity(intent);
                    drawer.close();
                    return true;
                }
                else  if (item.getItemId()==R.id.Disclaimer){

                    String theurl = "https://mtechorg747.blogspot.com/2020/08/privacypolicy-mtech-built-payearn-app.html";
                    Uri urlstr = Uri.parse(theurl);
                    Intent urlintent = new Intent();
                    urlintent.setData(urlstr);
                    urlintent.setAction(Intent.ACTION_VIEW);
                    startActivity(urlintent);
                    return  true;
                }
                else  if(item.getItemId()==R.id.Exit)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle( "LogOut" );
                    builder1.setMessage("Are you sure want to logout");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            Intent intent=new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                            drawer.close();
                        }
                    } );
                    builder1.setNegativeButton( "no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drawer.close();
                            dialog.dismiss();

                        }
                    } );
                    AlertDialog dialog=builder1.create();
                    dialog.show();

                    return true;
                }
                else
                {
                    return false;
                }

            }

            private String getPackageName() {
                return MainActivity.this.getPackageName();
            }
        } );



    }






    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp();
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
                final android.app.AlertDialog.Builder dialogBuilder = new
                        android.app.AlertDialog.Builder(MainActivity.this);
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
                                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
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
                                    finish();
                                }
                            } );

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