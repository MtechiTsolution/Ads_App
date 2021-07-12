package mtech.pak.tajrabaadds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Main13Activity extends AppCompatActivity {

    Button b25,b50,b100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main13 );

        b25=findViewById( R.id.ok25 );
        b50=findViewById( R.id.ok50 );
        b100=findViewById( R.id.ok100 );

        b25.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( Main13Activity.this,Main14Activity.class );
                intent.putExtra( "min" ,25);
                startActivity( intent );
            }
        } );

        b50.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( Main13Activity.this,Main14Activity.class );
                intent.putExtra( "min" ,50);
                startActivity( intent );
            }
        } );

        b100.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( Main13Activity.this,Main14Activity.class );
                intent.putExtra( "min" ,100);
                startActivity( intent );
            }
        } );

    }
}
