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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    EditText e1,e2;
    Button b1,b2;

    String mVerificationId,match;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fb;
    DatabaseReference dr;
    PhoneAuthProvider.ForceResendingToken tok;
    int kfc;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        e1=findViewById(R.id.phn);
        e2=findViewById(R.id.phnvari);
        b1=findViewById(R.id.btsvar);
        b2=findViewById(R.id.bvar);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        adView=findViewById( R.id.adView32 );
        AdRequest adRequest9 = new AdRequest.Builder().build();
        adView.loadAd(adRequest9);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                String phoneNumber=e1.getText().toString();
                if (phoneNumber.length()==0){

                    e1.setError("must required");
                }
                else  if(kfc==1)
                {
                    if(phoneNumber.equals( match ))
                    {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                MainActivity2.this,               // Activity (for callback binding)
                                mCallbacks);        // OnVerificationStateChangedCallbacks
                    }
                    else
                    {
                        Toast.makeText( MainActivity2.this,"given number are not match",Toast.LENGTH_SHORT ).show();
                    }
                }
                else{
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            MainActivity2.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(MainActivity2.this,"Success",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity2.this,"Error "+e.getMessage(),Toast.LENGTH_SHORT).show();

                e1.setVisibility(View.VISIBLE);
                b1.setVisibility(View.VISIBLE);
                e2.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,@NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                tok = token;
                b1.setVisibility(View.INVISIBLE);
                e1.setVisibility(View.INVISIBLE);

                b2.setVisibility(View.VISIBLE);
                e2.setVisibility(View.VISIBLE);

            }
        };


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setVisibility(View.VISIBLE);
                e2.setVisibility(View.VISIBLE);
                b1.setVisibility(View.INVISIBLE);
                e1.setVisibility(View.INVISIBLE);
                onStart();

                String varificatiocode=e2.getText().toString();
                if (e2.getText().toString().length()==0){
                    e2.setError("must required");
                }    else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varificatiocode);
                    signInWithPhoneAuthCredential(credential);

                }

            }

        });



    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential( credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //dr.child("user profile").child(user.getUid()).child("number").setValue(e1.getText().toString());

                            if(kfc==1)
                            {
                                Intent intent=new Intent(MainActivity2.this,MainActivity5.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent=new Intent(MainActivity2.this,MainActivity3.class);
                                intent.putExtra("ali",e1.getText().toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }



                        } else {

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
                final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity2.this);
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
                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity2.this);
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
                                    Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );

                        }
                        else
                        {
                            Intent intent=getIntent();
                            kfc=intent.getIntExtra( "abc",0 );
                            if(kfc==1)
                            {
                                dr.child("user profile").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if((dataSnapshot.child("number").exists()))
                                        {
                                            match=dataSnapshot.child("number").getValue().toString();

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        Toast.makeText( MainActivity2.this,"error  "+databaseError.getDetails(),Toast.LENGTH_SHORT ).show();
                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        }
        else
        {
           if(!network())
           {
               final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity2.this);
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
        }




    }
}