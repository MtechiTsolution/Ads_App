package mtech.pak.tajrabaadds;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Main14Activity extends AppCompatActivity {

    CardView jazzcash,easypaisa,gpay,paypal,bank;

    Uri filepath;
    FirebaseAuth mauth;
    FirebaseUser user;
    String downloodurl;
    FirebaseDatabase fb;
    DatabaseReference dr;
    FirebaseStorage fs;
    StorageReference sr;
    int invest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main14 );
        jazzcash=findViewById( R.id.jaz );
        easypaisa=findViewById( R.id.esp );
        gpay=findViewById( R.id.gpayz );
        paypal=findViewById( R.id.paypal );
        bank=findViewById( R.id.bank );
        final Intent intent=getIntent();
        invest=intent.getIntExtra( "min",0 );

        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        fb=FirebaseDatabase.getInstance();
        dr=fb.getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();


        gpay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( Main14Activity.this,Main15Activity.class );
                intent1.putExtra( "invst",invest );
                intent1.putExtra( "aname","Muhammad Abbas" );
                intent1.putExtra( "anumber","03098103410" );
                startActivity( intent1 );
            }
        } );
        jazzcash.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( Main14Activity.this,Main15Activity.class );
                intent1.putExtra( "invst",invest );
                intent1.putExtra( "aname","Muhammad Abbas" );
                intent1.putExtra( "anumber","03098103410" );
                startActivity( intent1 );
            }
        } );
        easypaisa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( Main14Activity.this,Main15Activity.class );
                intent1.putExtra( "invst",invest );
                intent1.putExtra( "aname","Muhammad Abbas" );
                intent1.putExtra( "anumber","03098103410" );
                startActivity( intent1 );
            }
        } );
        paypal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( Main14Activity.this,Main15Activity.class );
                intent1.putExtra( "invst",invest );
                intent1.putExtra( "aname","Muhammad Abbas" );
                intent1.putExtra( "anumber","03098103410" );
                startActivity( intent1 );
            }
        } );
        bank.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent( Main14Activity.this,Main15Activity.class );
                intent1.putExtra( "invst",invest );
                intent1.putExtra( "aname","Muhammad Abbas" );
                intent1.putExtra( "anumber","03098103410" );
                startActivity( intent1 );
            }
        } );



    }


}
