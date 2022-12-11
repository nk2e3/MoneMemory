package com.monemobility.monememory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

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
    public int playerScore = 1;
    Boolean musicState;
    ImageView[][] IVArray= new ImageView[length][width];

    Button muteButton;

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
                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                TreeMap<String, ?> keys = new TreeMap<String, Object>(prefs.getAll());
                List<Pair<Object, String>> sortedByValue = new LinkedList<Pair<Object,String>>();
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    Pair<Object, String> e = new Pair<Object, String>(entry.getValue(), entry.getKey());
                    sortedByValue.add(e);
                }
                Collections.sort(sortedByValue, new Comparator<Pair<Object, String>>() {
                    public int compare(Pair<Object, String> lhs, Pair<Object, String> rhs) {
                        String sls = String.valueOf(lhs.first);
                        String srs = String.valueOf(rhs.first);
                        int res = sls.compareTo(srs);
                        // Sort on value first, key second
                        return res == 0 ? lhs.second.compareTo(rhs.second) : res;
                    }
                });
                for (Pair<Object, String> pair : sortedByValue) {
                    Log.i("map values", pair.first + "/" + pair.second);
                }
                Map<String, ?> allEntries = prefs.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    output += entry.getKey() + "\t\t..........\t\t" + entry.getValue().toString() + "\n";
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("TOP SCORES:\n" + output);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        /*
        Bundle bundle = new Bundle();
        bundle.putString("edttext", Integer.toString(4));
        // set Fragmentclass Arguments
        gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.game_container, gameFragment)
                .addToBackStack(GameFragment.class.getName()).commit();

*/
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
        outState.putString("s", savedTextScore);
        outState.putString("mp", savedTextMutePlay);
        outState.putBoolean("m", musicState);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String savedText = (String) scoreButton.getText();
        String x = savedInstanceState.getString("s");
        String y = savedInstanceState.getString("mp");
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
                playerScore = 1;
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


}

