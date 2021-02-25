package com.lrot.pingo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    private ArrayList<String> topics = new ArrayList<>();
    private static final String TAG = "Settings";

    RecyclerViewAdapter recyclerViewAdapter;

    void initTopicsList()
    {
        Log.d(TAG, "intTopicsList: called.");

        SharedPreferences getTopics = getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);
        String buffer;
        int index = getTopics.getInt("indexOfLastTopic", 0);
        for ( int i = 0; i <= index; i++ )
        {
            buffer = getTopics.getString("topic"+i, "");
            if( !buffer.equals("") ) topics.add( buffer );
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firstTimeWriteTopicsIntoSharedPreferences();

        initTopicsList();



        RecyclerView recyclerView = findViewById( R.id.topic_list );
        recyclerViewAdapter = new RecyclerViewAdapter( topics, this);


        recyclerView.setAdapter(recyclerViewAdapter);


        recyclerView.setLayoutManager( new LinearLayoutManager(this) );

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);


    }


    public void addNewTopic(View view) {

        SharedPreferences addingTopic = getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);
        SharedPreferences.Editor addingTopicEditor = addingTopic.edit();

        EditText editText = findViewById(R.id.new_topic_text);
        String newTopic = String.valueOf(editText.getText());

        int indexOfLastTopic = addingTopic.getInt("indexOfLastTopic", 29 );
        int i = 0;

        while( !addingTopic.getString("topic"+i, "").equals("") ) i++;

        addingTopicEditor.putString("topic"+i, newTopic);
        if( i > indexOfLastTopic ) addingTopicEditor.putInt("indexOfLastTopic", i);

        addingTopicEditor.apply();

        finish();
        startActivity(getIntent());
    }


    private void firstTimeWriteTopicsIntoSharedPreferences() {
        SharedPreferences topicsInit = getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);
        SharedPreferences.Editor topicsInitEditor = topicsInit.edit();

        if( !topicsInit.getString("topic0", "").equals("") ) return;

        String fileName = "topics";
        String line;
        int index = 0;
        try {
            InputStream inputStream;
            int resourceID = getResources().getIdentifier(fileName, "raw", getPackageName());
            inputStream = getResources().openRawResource(resourceID);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-16"));

            while ((line = bufferedReader.readLine()) != null) {
                // topicsArrayList.add( line );

                topicsInitEditor.putString( "topic"+index , line);

                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + ".txt'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + ".txt'");
        }

        topicsInitEditor.putInt("indexOfLastTopic", index-1);

        topicsInitEditor.apply();
    }
}
