package com.lrot.pingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Typeface;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Bingo extends  AppCompatActivity {
    Button[][] boardButtons;
    boolean topicMentioned[][];

    void gotBingo() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View bingoPopupView = inflater.inflate(R.layout.succesful_bingo_popup_window, null);

        ImageView imgView = bingoPopupView.findViewById(R.id.pospecImageView);
        imgView.setImageResource(0);
        Drawable drawable = getResources().getDrawable(R.drawable.pospec_big_rounded_bordered_corners);
        imgView.setImageDrawable(drawable);

        final PopupWindow bingoPopupWindow = new PopupWindow(bingoPopupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        bingoPopupWindow.setAnimationStyle(R.style.pospec_pop_in_animation_style);
        bingoPopupWindow.showAtLocation(boardButtons[0][0], Gravity.CENTER, 0, 100);

        KonfettiView viewKonfetti = bingoPopupView.findViewById(R.id.konfetti_view);
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(false)
                .setTimeToLive(3000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                //     .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .setPosition(0, 0)
                .streamFor(300, 3000L);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        viewKonfetti.build()
                .addColors(Color.BLUE, Color.WHITE, Color.CYAN)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(false)
                .setTimeToLive(3000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(displayMetrics.widthPixels, 0)
                .streamFor(300, 3000L);

        bingoPopupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bingoPopupWindow.dismiss();
                return true;
            }
        });
    }

    protected void createBoard(final int boardSize) {
        TableLayout board = findViewById(R.id.boardGrid);

        board.setWeightSum(boardSize);

        Typeface trebuchetFont = Typeface.createFromAsset(getAssets(), "trebucbd.ttf");

        topicMentioned = new boolean[boardSize][boardSize];

        for (int rows = 0; rows < boardSize; rows++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(boardSize);

            for (int cols = 0; cols < boardSize; cols++) {
                boardButtons[rows][cols] = new Button(this);
                topicMentioned[rows][cols] = false;

                if (boardSize == 3) boardButtons[rows][cols].setTextSize(11);
                else boardButtons[rows][cols].setTextSize(8);
                boardButtons[rows][cols].setLines(3);
                boardButtons[rows][cols].setTypeface(trebuchetFont);
                boardButtons[rows][cols].setText("");

                // make boardButtons do things when tapped
                final int tempRows, tempCols;
                tempCols = cols;
                tempRows = rows;

                boardButtons[rows][cols].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Drawable buttonDrawable = boardButtons[tempRows][tempCols].getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        if (topicMentioned[tempRows][tempCols]) {
                            DrawableCompat.setTint(buttonDrawable, 0xffd6d7d7); // the colour is consistent with light themes - in value files called button_material_light
                            topicMentioned[tempRows][tempCols] = false;
                        } else {
                            DrawableCompat.setTint(buttonDrawable, Color.GREEN);
                            topicMentioned[tempRows][tempCols] = true;
                            // check for bingo
                            int i;
                            for (i = 0; i < boardSize; i++) {
                                if (!topicMentioned[tempRows][i]) break;
                            }
                            if (i == boardSize) {
                                gotBingo();
                                return;
                            } else {
                                for (i = 0; i < boardSize; i++) {
                                    if (!topicMentioned[i][tempCols]) break;
                                }
                            }
                            if (i == boardSize) {
                                gotBingo();
                                return;
                            }
                            if (tempCols == tempRows) {
                                for (i = 0; i < boardSize; i++) {
                                    if (!topicMentioned[i][i]) break;
                                }
                                if (i == boardSize) {
                                    gotBingo();
                                    return;
                                }
                            }
                            if (tempCols + tempRows == boardSize - 1) {
                                for (i = 0; i < boardSize; i++) {
                                    if (!topicMentioned[i][boardSize - 1 - i]) break;
                                }
                                if (i == boardSize) gotBingo();
                            }
                            //end of checking for bingo

                        }
                        boardButtons[tempRows][tempCols].setBackground(buttonDrawable);

                    }
                });
                // \

                tableRow.addView(boardButtons[rows][cols]);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 0, 0, 10);
                boardButtons[rows][cols].setLayoutParams(params);
            }
            board.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
        }
    }

    protected void generateTopics(int boardSize) {
        ArrayList<String> topicsArrayList = new ArrayList<>();

        SharedPreferences getTopics = getSharedPreferences("PREF_TOPICS", MODE_PRIVATE);
        String buffer;
        int index = getTopics.getInt("indexOfLastTopic", 0);
        for ( int i = 0; i <= index; i++ )
        {
            buffer = getTopics.getString("topic"+i, "");
            if( !buffer.equals("") ) topicsArrayList.add( buffer );
        }

        Log.d("Bingo:", "generateTopics: "+topicsArrayList.size());

        String buttonText;
        Random random = new Random();
        int randomIndex;

        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                randomIndex = random.nextInt(topicsArrayList.size());
                buttonText = topicsArrayList.get(randomIndex);
                topicsArrayList.remove(randomIndex);
                boardButtons[rows][cols].setText(buttonText);
            }
        }
    }

    protected void resetPressedButtonsOnTopicRegeneration(int boardSize) {
        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                Drawable buttonDrawable = boardButtons[rows][cols].getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, 0xffd6d7d7);
                boardButtons[rows][cols].setBackground(buttonDrawable);

                topicMentioned[rows][cols] = false;
            }
        }
    }

    protected void saveButtonTextWithSharedPreferences() {
        Intent activityThatCalled = getIntent();
        int boardSize = activityThatCalled.getExtras().getInt("boardSize");

        SharedPreferences backButtonPressedActivityStateSave = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor backButtonPressedActivityStateSaveEditor = backButtonPressedActivityStateSave.edit();

        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                backButtonPressedActivityStateSaveEditor.putString("button" + rows + cols + boardSize, (boardButtons[rows][cols].getText()).toString());

                if (topicMentioned[rows][cols])
                    backButtonPressedActivityStateSaveEditor.putInt("button_tint" + rows + cols + boardSize, Color.GREEN);
                else
                    backButtonPressedActivityStateSaveEditor.putInt("button_tint" + rows + cols + boardSize, 0xffd6d7d7);

                backButtonPressedActivityStateSaveEditor.putBoolean("button_pressed" + rows + cols + boardSize, topicMentioned[rows][cols]);

            }
        }
        backButtonPressedActivityStateSaveEditor.apply();
    }

    protected void loadButtonTextWithSharedPreferences() {
        Intent activityThatCalled = getIntent();
        int boardSize = activityThatCalled.getExtras().getInt("boardSize");

        SharedPreferences backButtonPressedActivityStateSave = getPreferences(MODE_PRIVATE);
        //   SharedPreferences.Editor backButtonPressedActivityStateSaveEditor = backButtonPressedActivityStateSave.edit();

        if (backButtonPressedActivityStateSave.getString("button00" + boardSize, "").equals(""))
            return;

        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                boardButtons[rows][cols].setText(backButtonPressedActivityStateSave.getString("button" + rows + cols + boardSize, ""));

                Drawable buttonDrawable = boardButtons[rows][cols].getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, backButtonPressedActivityStateSave.getInt("button_tint" + rows + cols + boardSize, 0xffd6d7d7));
                boardButtons[rows][cols].setBackground(buttonDrawable);

                topicMentioned[rows][cols] = backButtonPressedActivityStateSave.getBoolean("button_pressed" + rows + cols + boardSize, topicMentioned[rows][cols]);
            }
        }
        // backButtonPressedActivityStateSaveEditor.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bingo_layout);

        firstTimeWriteTopicsIntoSharedPreferences(); //is done only once on the first board generation

        Intent activityThatCalled = getIntent();
        final int boardSize = activityThatCalled.getExtras().getInt("boardSize");

        boardButtons = new Button[boardSize][boardSize];

        createBoard(boardSize);
        generateTopics(boardSize);
        loadButtonTextWithSharedPreferences();
        if (savedInstanceState != null) {
            for (int rows = 0; rows < boardSize; rows++) {
                for (int cols = 0; cols < boardSize; cols++) {
                    boardButtons[rows][cols].setText(savedInstanceState.getString("button" + rows + cols));
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bingo_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.topic_regeneration) {
            Intent activityThatCalled = getIntent();
            final int boardSize = activityThatCalled.getExtras().getInt("boardSize");

            generateTopics(boardSize);
            resetPressedButtonsOnTopicRegeneration(boardSize);
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // savedInstanceState.put

        Intent activityThatCalled = getIntent();
        int boardSize = activityThatCalled.getExtras().getInt("boardSize");

        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                savedInstanceState.putString("button" + rows + cols, (boardButtons[rows][cols].getText()).toString());
            }
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        saveButtonTextWithSharedPreferences();
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        saveButtonTextWithSharedPreferences();
        super.onStop();
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