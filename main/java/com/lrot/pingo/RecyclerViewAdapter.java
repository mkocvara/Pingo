package com.lrot.pingo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    
    private ArrayList<String> topics = new ArrayList<>();
    private Context context;


    public RecyclerViewAdapter(ArrayList<String> topics, Context context) {
        this.topics = topics;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         Log.d(TAG, "onCreateViewHolder: called."); 
        
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_topic_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.topicText.setText(topics.get(i));
        Log.d(TAG, "onBindViewHolder: called.");

    //    viewHolder.topicText.setAllCaps(true);

        final int position = viewHolder.getLayoutPosition();

        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAreYouSureAlertDialog( position );


                // notifyDataSetChanged();
                // Update recyclerView (notifyOfChanges or something was the thing, look it up)
            }
        });

    }

    private void makeAreYouSureAlertDialog(int index ) {
        // Implement a dialog checking if user is sure

        SharedPreferences topic = context.getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);

        int i;
        final int index2 = index;
        for( i = 0; i <= index; i++ )
        {
            if( topic.getString("topic"+i, "").equals("") ) index++;
        }
        final int index3 = index;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Opravdu chceš smazat téma \' "+topic.getString("topic"+index, "")+" \'? Toto nelze vrátit zpět a téma je na vždy ztraceno.")
                            .setCancelable(false)
                            .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    topics.remove(index2);
                                    notifyDataSetChanged();

                                    removeTopic(index3);
                                }
                            })
                            .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //removeTopic( index );
    }

    private void removeTopic( int index ) {
        SharedPreferences topics = context.getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);
        SharedPreferences.Editor topicsEditor = topics.edit();

        topicsEditor.remove("topic"+index);

        topicsEditor.apply();
    }


    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout parentLayout;
        TextView topicText;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            parentLayout  = itemView.findViewById(R.id.parent_layout);
            topicText  = itemView.findViewById(R.id.topic);
            imageButton = itemView.findViewById(R.id.delete_button);
        }
    }

}
