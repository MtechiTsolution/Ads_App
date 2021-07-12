package mtech.pak.tajrabaadds.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import mtech.pak.tajrabaadds.Main12Activity;
import mtech.pak.tajrabaadds.Main13Activity;
import mtech.pak.tajrabaadds.MainActivity2;
import mtech.pak.tajrabaadds.MainActivity3;
import mtech.pak.tajrabaadds.MainActivity4;
import mtech.pak.tajrabaadds.MainActivity5;
import mtech.pak.tajrabaadds.MainActivity6;
import mtech.pak.tajrabaadds.MainActivity7;
import mtech.pak.tajrabaadds.MainActivity8;
import mtech.pak.tajrabaadds.MainActivity9;
import mtech.pak.tajrabaadds.R;

public class HomeFragment extends Fragment {
    private RewardedVideoAd mRewardedVideoAd;
    private HomeViewModel homeViewModel;
    FirebaseAuth mAuth;
    FirebaseUser user;

    CircleImageView circleImageView;
    AdView mAdView,mAdView2;
    TextView textView,textView2;
    FirebaseDatabase fb;
    DatabaseReference dr;
    float balance=0;
    int counter,ads_size;
    Button b5,pti6;
    LinearLayout b1,b3,b2,b4,b52,b6;
    String klpk,sdate,DATEFORMAT="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of( this ).get( HomeViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );

        mAdView = root.findViewById(R.id.adViewpk);
        mAdView2 = root.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



       mAuth=FirebaseAuth.getInstance();
       user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        textView=root.findViewById(R.id.nmes);
        textView2=root.findViewById(R.id.blnc);
        b1=root.findViewById(R.id.wtcad);
        b2=root.findViewById(R.id.notif);
        b3=root.findViewById(R.id.withdr);
        b4=root.findViewById(R.id.shre);
        b5=root.findViewById(R.id.mng);
        b52=root.findViewById(R.id.notif2);
        b6=root.findViewById(R.id.shre2);

        circleImageView=root.findViewById(R.id.profileids);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter<ads_size)
                {
                    Intent intent=new Intent(getActivity(), MainActivity4.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText( getActivity(),"ads complete",Toast.LENGTH_SHORT ).show();
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), MainActivity7.class);
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), MainActivity6.class);
                startActivity(intent);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity8.class);
                startActivity(intent);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity9.class);
                startActivity(intent);
            }
        });
        b52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Main13Activity.class);
                startActivity(intent);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Main12Activity.class);
                startActivity(intent);
            }
        });

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

       // mAuth.signOut();
        if(user==null)
        {

            Intent intent=new Intent( getActivity(), MainActivity2.class );
            startActivity( intent );


        }
        else
        {
            AdRequest adRequests = new AdRequest.Builder().build();
            mAdView2.loadAd(adRequests);
            dr.child("user profile").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if((dataSnapshot.child("name").exists()))
                    {
                        textView.setText(dataSnapshot.child("name").getValue().toString());
                        if(!(dataSnapshot.child("pin").exists()))
                        {
                            Intent intent=new Intent(getActivity(), MainActivity5.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent=new Intent(getActivity(), MainActivity3.class);
                        intent.putExtra("ali","11abbas");
                        startActivity(intent);
                    }

                    if(dataSnapshot.hasChild("images"))
                    {
                        Picasso.get().load(dataSnapshot.child("images").getValue().toString()).placeholder(R.drawable.man).into(circleImageView);
                    }
                    if (dataSnapshot.hasChild("balance"))
                    {
                        balance=Float.parseFloat(dataSnapshot.child("balance").getValue().toString());
                        Float f=Float.parseFloat( dataSnapshot.child("balance").getValue().toString() );
                        textView2.setText( new DecimalFormat("##.##").format( f )+"$" );

                    }
                    else
                    {
                        balance=0;
                        textView2.setText(" 0.$");
                    }
                    if (dataSnapshot.hasChild("number"))
                    {
                        klpk=dataSnapshot.child("number").getValue().toString();
                       if(klpk.equals( "+923098103410" )||klpk.equals( "+923036710864" ))
                       {
                           b5.setVisibility( View.VISIBLE );
                       }
                    }
                    if(dataSnapshot.hasChild( "date" ))
                    {
                        sdate= dataSnapshot.child( "date" ).getValue().toString() ;
                       // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
                        if(!sdate.equals( DATEFORMAT ))
                        {
                            dr.child("user profile").child(user.getUid()).child("date").setValue(DATEFORMAT);
                            dr.child("user profile").child(user.getUid()).child("counter").setValue("0");
                        }

                    }
                    else
                    {

                    }
                    if(dataSnapshot.hasChild( "counter" ))
                    {
                        counter=Integer.parseInt( dataSnapshot.child( "counter" ).getValue().toString() );
                        //dr.child("user profile").child(user.getUid()).child("counter").setValue("0");
                    }
                    else
                    {
                        counter=0;
                    }

                    if(dataSnapshot.hasChild( "Ads size" ))
                    {
                        ads_size=Integer.parseInt( dataSnapshot.child( "Ads size" ).getValue().toString() );

                    }
                    else{
                        ads_size=2;
                        dr.child("user profile").child(user.getUid()).child("Ads size").setValue("2");;
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {



                }
            });


        }

    }
}
