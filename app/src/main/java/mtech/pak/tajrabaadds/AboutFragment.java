package mtech.pak.tajrabaadds;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.zip.Inflater;

public class AboutFragment extends AppCompatActivity {

    private Inflater inflater;
    private int container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.about_activity );
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace( R.id.container, AboutFragment2.newInstance() )
                    .commitNow();

        }
    }
}
