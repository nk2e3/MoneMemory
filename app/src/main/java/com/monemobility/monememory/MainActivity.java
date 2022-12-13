package com.monemobility.monememory;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    int length;
    int width;

    int[] difficultySettings = {4,6,8,10,12,14,16,18,20};
    int difficulty = difficultySettings[0];

    Button startButton;
    Button quitButton;
    Button scoreButton;
    SeekBar difficultyBar;
    GameFragment gameFragment;
    TextView tv;
    public int playerScore = 0;

    Boolean musicState;
    ImageView[][] IVArray= new ImageView[length][width];

    Button muteButton;

    int lastScore, hs1, hs2, hs3;
    String lastName, ns1, ns2, ns3;
    String scoreReadout;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SCORE = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        startButton = (Button) findViewById(R.id.start_button);
        Intent intent = new Intent(getApplicationContext(), BackgroundMusicService.class);


        startButton = this.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        muteButton = this.findViewById(R.id.mute_button);
        if(musicState==null) {
            musicState = true;
            startService(intent);
        }
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicState) {
                    musicState = false;
                    Intent intent = new Intent();
                    intent.setAction("musicPause");
                    sendBroadcast(intent);
                    muteButton.setText("PLAY");
                } else {
                    musicState = true;
                    Intent intent = new Intent();
                    intent.setAction("musicPlay");
                    sendBroadcast(intent);
                    muteButton.setText("MUTE");
                };
            }
        });

        quitButton = this.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameFragment = (GameFragment)getSupportFragmentManager().findFragmentById(R.id.game_container);
                gameFragment.revealCards(true);

            }
        });

        scoreButton = this.findViewById(R.id.scoreText);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            String output = "";
            @Override
            public void onClick(View view) {
                AlertDialog.Builder scoreAlert = new AlertDialog.Builder((MainActivity.this));

                tv = loadData();


                scoreAlert.setView(tv);



                scoreAlert.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(getSharedPrefs(difficulty), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(MainActivity.this, "High scores for this game have been cleared.", Toast.LENGTH_SHORT).show();
                    }
                });
                scoreAlert.show();
            }
        });

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("edttext", Integer.toString(4));
            // set Fragmentclass Arguments
            gameFragment = new GameFragment();
            gameFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.game_container, gameFragment)
                    .addToBackStack(GameFragment.class.getName()).commit();

        }
        Toast.makeText(this, "Press on the Score to see the high scores for this game.", Toast.LENGTH_SHORT).show();
    }





    private String getSharedPrefs(int n) {
        n = difficulty;
        if(n == 4) {
            return "sharedPrefs4";
        }
        if(n == 6) {
            return "sharedPrefs6";
        }
        if(n == 8) {
            return "sharedPrefs8";
        }
        if(n == 10) {
            return "sharedPrefs10";
        }
        if(n == 12) {
            return "sharedPrefs12";
        }
        if(n == 14) {
            return "sharedPrefs14";
        }
        if(n == 16) {
            return "sharedPrefs16";
        }
        if(n == 18) {
            return "sharedPrefs18";
        }
        if(n == 20) {
            return "sharedPrefs20";
        }
        return"";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), BackgroundMusicService.class);
        //stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent();
        intent.setAction("musicPause");
        sendBroadcast(intent);
        //stopService(intent);
    }

    protected void onResume() {
        super.onResume();
        if (musicState) {
            Intent intent = new Intent();
            intent.setAction("musicPlay");
            sendBroadcast(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction("musicPause");
            sendBroadcast(intent);
        }
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String savedTextScore = (String) scoreButton.getText();
        String savedTextMutePlay = (String) muteButton.getText();
        int savedDifficulty = difficulty;
        outState.putInt("sD", savedDifficulty);
        outState.putString("s", savedTextScore);
        outState.putString("mp", savedTextMutePlay);
        outState.putBoolean("m", musicState);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String savedText = (String) scoreButton.getText();
        String x = savedInstanceState.getString("s");
        String y = savedInstanceState.getString("mp");
        difficulty = savedInstanceState.getInt("sD");
        musicState = savedInstanceState.getBoolean("m");
        scoreButton.setText(x);
        muteButton.setText(y);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_custom,null);
        builder.setView(view);
        difficultyBar = (SeekBar) view.findViewById(R.id.difficulty_bar);
        difficultyBar.setMax(8);
        difficultyBar.setKeyProgressIncrement(1);
        difficultyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                difficulty = difficultySettings[progress];
                DifficultyUtilities.displayDifficultyToast(getApplicationContext(), view, difficulty);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerScore = 0;
                scoreButton.setText("SCORE: " + playerScore);
                Bundle bundle = new Bundle();
                bundle.putString("edttext", Integer.toString(difficulty));
                // set Fragmentclass Arguments
                gameFragment = new GameFragment();
                gameFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.game_container, gameFragment)
                        .addToBackStack(GameFragment.class.getName()).commit();



            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        builder.show();

    }
    //Resource startup array...
    private ImageView[][] assignImages(ImageView[][] blankArray) {
        for(int i = 0; i < blankArray[0].length; i++) {
            for(int k = 0;  k < blankArray[1].length; k++) {
                //blankArray[i][k].setImageResource();
            }
        }
        return blankArray;
    }

    public void addScore(Boolean a) {
        if(a)
            playerScore += 2;
        else
            playerScore -=1;

        scoreButton.setText("SCORE: " + playerScore);
    }

    private TextView loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(getSharedPrefs(difficulty), MODE_PRIVATE);
        lastScore = sharedPreferences.getInt("score", 0);
        lastName = sharedPreferences.getString("name", GameFragment.userName);
        hs1 = sharedPreferences.getInt("hs1", 0);
        hs2 = sharedPreferences.getInt("hs2", 0);
        hs3 = sharedPreferences.getInt("hs3", 0);
        ns1 = sharedPreferences.getString("ns1", "ABC");
        ns2 = sharedPreferences.getString("ns2", "ABC");
        ns3 = sharedPreferences.getString("ns3", "ABC");
        scoreReadout =  "\n# of Cards: " + difficulty + "\n" +
                        "Last Score: " + lastScore + "\n" +
                        "-------------------------" + "\n" +
                        ns1 + "\t : \t " + hs1 + "\n" +
                        ns2 + "\t : \t " + hs2 + "\n" +
                        ns3 + "\t : \t " + hs3;
        scoreReadout = sharedPreferences.getString("scoreString", scoreReadout);
        TextView tv = new TextView(this);
        tv.setText(scoreReadout);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        return tv;
    }


    public void onBackPressed() {

    }



}
