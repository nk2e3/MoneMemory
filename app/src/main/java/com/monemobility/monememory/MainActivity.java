package com.monemobility.monememory;

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
    SeekBar difficultyBar;
    GameFragment gameFragment;
    public int playerScore;
    Boolean musicState = true;
    ImageView[][] IVArray= new ImageView[length][width];

    Button muteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        startButton = (Button) findViewById(R.id.start_button);
        Intent intent = new Intent(getApplicationContext(), BackgroundMusicService.class);
        startService(intent);

        startButton = this.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        muteButton = this.findViewById(R.id.mute_button);
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
                gameFragment.revealCards();

            }
        });




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
        startButton.setText("SCORE: " + playerScore);
    }


}

