package com.lrot.pingo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface trebuchetFont = Typeface.createFromAsset( getAssets(), "trebucbd.ttf" );
        TextView textView = findViewById(R.id.welcome_message);
        textView.setTypeface( trebuchetFont );
        textView = findViewById(R.id.welcome_message_part_2);
        textView.setTypeface(trebuchetFont);
    }

    public void makeBoard3x3(View view)
    {
        Intent goToBoardActivityIntent = new Intent( this, Bingo.class );
        goToBoardActivityIntent.putExtra( "boardSize", 3 );
        startActivity( goToBoardActivityIntent );
    }

    public void makeBoard4x4(View view)
    {
        Intent goToBoardActivityIntent = new Intent( this, Bingo.class );
        goToBoardActivityIntent.putExtra( "boardSize", 4 );
        startActivity( goToBoardActivityIntent );
    }

    public void goToInfoPage(View view)
    {
        Intent goToBoardActivityIntent = new Intent( this, Info.class );
        startActivity( goToBoardActivityIntent );
    }

    public void goToSettingsPage(View view){
        Intent goToBoardActivityIntent = new Intent( this, Settings.class );
        startActivity( goToBoardActivityIntent );
    }
}
