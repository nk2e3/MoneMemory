package com.monemobility.monememory;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int numOfCards;
    private int m_max, n_max;
    boolean gameOver;
    private ImageView[][] im;
    int firstCard, secondCard;
    int fcx1, fcy1, fcx2, fcy2;
    int[][] gridImageValues;
    boolean[][] isCorrect;
    String[][] isCorrectS;
    TextView scoreText;
    boolean revealed;
    boolean flag;
    int fragScore = 1;
    private int g = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TextView textview = (TextView)getActivity().findViewById(R.id.scoreText);
        //textview.setText("SCORE: " + Integer.toString(fragScore));
        scoreText = (TextView) getActivity().findViewById(R.id.scoreText);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (savedInstanceState != null) {
            int x = savedInstanceState.getInt("m");
            int y = savedInstanceState.getInt("n");
            int z = savedInstanceState.getInt("z");
            revealed = savedInstanceState.getBoolean("r");

            DifficultyUtilities.displayDifficultyToast(getContext(), getView(), z);
            im = new ImageView[x][y];
            String[][] savedState;
            savedState = (String[][]) savedInstanceState.getSerializable("answered");
            isCorrectS = savedState;
            int[][] a = GameLogic.assignImageResources(z);

            for(int m = 0; m < x; m++) {
                for(int n = 0; n < y; n++) {
                    im[m][n] = new ImageView(getActivity());
                    if(savedState[m][n].equals("true")) {
                        im[m][n].setVisibility(View.INVISIBLE);
                        im[m][n].setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        numOfCards = Integer.valueOf(getArguments().getString("edttext"));
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        LinearLayout parent = (LinearLayout) view;
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parent.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            parent.setOrientation(LinearLayout.VERTICAL);
        }

        /*This code assigns grid dimensions based on the number of cards and returns a one dimensi-
        onal array. Of size two variables, to represent the m by n grid.*/

        int[] dim = GameLogic.getDimensions(numOfCards);
        LinearLayout[] row = new LinearLayout[dim[0]];
        if(savedInstanceState == null) {
            im = new ImageView[dim[0]][dim[1]];
            isCorrectS = new String[dim[0]][dim[1]];
            for(int j = 0; j < dim[0]; j++) {
                for(int k = 0; k < dim[1]; k++) {
                    isCorrectS[j][k] = "false";
                }
            }
        }

        m_max = dim[0];
        n_max = dim[1];
        gridImageValues = GameLogic.assignImageResources(numOfCards);


        for(int m = 0; m < m_max; m++) {
            for(int n = 0; n < n_max; n++) {
                if(savedInstanceState == null) {
                    im[m][n] = new ImageView(getActivity());
                    im[m][n].setImageResource(R.drawable.ic_go);
                } else {
                    //Toast.makeText(getContext(),isCorrectS[0][0],Toast.LENGTH_SHORT).show();
                    if(revealed) {
                        im[m][n].setImageResource(gridImageValues[m][n]);
                        im[m][n].setEnabled(false);
                    } else {
                        if(isCorrectS[m][n].equals("true")) {
                            im[m][n].setImageResource(gridImageValues[m][n]);
                        } else if(isCorrectS[m][n].equals("false")) {
                            im[m][n].setImageResource(R.drawable.ic_go);
                        }
                    }


                }
                im[m][n].setTag(Integer.toString(gridImageValues[m][n]));
                int finalM = m;
                int finalN = n;
                //int fs = fragScore;
                im[m][n].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        im[finalM][finalN].setImageResource(gridImageValues[finalM][finalN]);
                        im[finalM][finalN].setEnabled(false);
                        //isCorrectS[finalM][finalN] = "true";

                        int x;
                        if(g % 2 == 0) {
                            secondCard = Integer.parseInt((String) im[finalM][finalN].getTag());
                            fcx1 = finalM;
                            fcy1= finalN;
                            x = 2;
                        } else {
                            firstCard = Integer.parseInt((String) im[finalM][finalN].getTag());
                            fcx2 = finalM;
                            fcy2 = finalN;
                            x= 1;
                        }
                        String eq;


                        if(g%2 == 0) {
                            disableButtons();
                            if ((secondCard == firstCard) && !((finalM == fcx2) && (finalN == fcy2))) {
                                fragScore += 2;
                                scoreText = (TextView) getActivity().findViewById(R.id.scoreText);
                                scoreText.setText("SCORE: " + fragScore);
                                im[fcx2][fcy2].setImageResource(gridImageValues[fcx2][fcy2]);
                                im[finalM][finalN].setImageResource(gridImageValues[finalM][finalN]);
                                isCorrectS[finalM][finalN] = "true";
                                isCorrectS[fcx2][fcy2] = "true";
                                im[fcx2][fcy2].setEnabled(false);
                                im[finalM][finalN].setEnabled(false);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        im[fcx2][fcy2].setImageResource(gridImageValues[fcx2][fcy2]);
                                        im[finalM][finalN].setImageResource(gridImageValues[finalM][finalN]);
                                        isCorrectS[finalM][finalN] = "true";
                                        isCorrectS[fcx2][fcy2] = "true";
                                        im[fcx2][fcy2].setVisibility(View.INVISIBLE);
                                        im[finalM][finalN].setVisibility(View.INVISIBLE);
                                        checkWinState();
                                    }
                                }, 1000);
                            } else if (secondCard != firstCard && !((finalM == fcx2) && (finalN == fcy2))) {
                                fragScore -= 1;
                                if(fragScore == 0) {
                                    revealCards(false);
                                } else {
                                    Log.i("STATE","!eq");
                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isCorrectS[finalM][finalN] = "false";
                                            isCorrectS[fcx2][fcy2] = "false";
                                            im[finalM][finalN].setImageResource(R.drawable.ic_go);
                                            im[fcx2][fcy2].setImageResource(R.drawable.ic_go);
                                            im[fcx2][fcy2].setEnabled(true);
                                            im[finalM][finalN].setEnabled(true);
                                            //disable buttons until timer is done...
                                        }
                                    }, 1000);
                                    scoreText = (TextView) getActivity().findViewById(R.id.scoreText);
                                    scoreText.setText("SCORE: " + fragScore);
                                }

                            }
                        }


                        g++;
                    }
                });
            }
        }
        for(int m = 0; m < m_max; m++) {
            row[m] = new LinearLayout(getActivity());
            row[m].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                row[m].setOrientation(LinearLayout.VERTICAL);
            } else {
                row[m].setOrientation(LinearLayout.HORIZONTAL);
            }

            parent.addView(row[m]);
            for(int n = 0; n < n_max; n++) {

                im[m][n].setScaleType(CENTER_INSIDE);

                im[m][n].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                row[m].addView(im[m][n]);
            }
        } parent.setWeightSum(m_max);
        return view;
        //return inflater.inflate(R.layout.fragment_game, container, false);
     }

    public void revealCards(boolean show) {
        revealed = true;
        gameOver = true;
        scoreText = (TextView) getActivity().findViewById(R.id.scoreText);
        scoreText.setText("GAME OVER");
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int m = 0; m < m_max; m++) {
                    for(int n = 0; n < n_max; n++) {
                        if(show) {
                            im[m][n].setVisibility(View.VISIBLE);
                            im[m][n].setImageResource(gridImageValues[m][n]);
                        }
                        isCorrectS[m][n] = "false";
                        im[m][n].setEnabled(false);

                    }
                }
            }
        }, 1000);



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("answered", isCorrectS);
        outState.putInt("m", m_max);
        outState.putInt("n", n_max);
        outState.putInt("z", numOfCards);
        outState.putBoolean("r", revealed);
    }


    private void checkWinState() {
        String[][] answerKey = new String[m_max][n_max];
        for(int m = 0; m < m_max; m++) {
            for(int n = 0; n < n_max; n++) {
                answerKey[m][n] = "true";
            }
        }
        String[][] solution = new String[m_max][n_max];

        if(Arrays.deepEquals(answerKey,isCorrectS)) {
            TextView textview = (TextView) getActivity().findViewById(R.id.scoreText);
            textview.setText("WIN");
            gameOver = true;
        }

        //BELOW IS WHERE THE CODE FOR SCORE STORAGE CAN GO...

    }

    private void disableButtons() {
        for(int i = 0; i < m_max; i++) {
            for(int k = 0; k < n_max; k++) {
                im[i][k].setEnabled(false);
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < m_max; i++) {
                    for(int k = 0; k < n_max; k++) {
                        if(isCorrectS.equals("true")) {
                            im[i][k].setEnabled(false);
                        } else {
                            im[i][k].setEnabled(true);
                        }


                    }
                }
            }
        },1000);// set time as per your requirement
    }

}

