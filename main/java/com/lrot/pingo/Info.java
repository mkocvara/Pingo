package com.lrot.pingo;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Typeface trebuchetFont = Typeface.createFromAsset( getAssets(), "trebucbd.ttf" );
        TextView textView = findViewById(R.id.info_title);
        textView.setTypeface( trebuchetFont );
        trebuchetFont = Typeface.createFromAsset( getAssets(), "trebuc.ttf" );
        textView = findViewById(R.id.info_description);
        textView.setTypeface(trebuchetFont);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

}


