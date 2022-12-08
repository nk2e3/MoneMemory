package com.monemobility.monememory;

import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;

public class GameLogic {
    /*Given a number of cards, this function returns the corresponding grid dimensions as a one-
    dimensional array of size two.*/
    public static int[] getDimensions(int cards) {
        int m_max = 5;
        int n_max = 4;
        if(cards == 20) {
            m_max = 5;
            n_max = 4;
        } else if(cards == 18) {
            m_max = 6;
            n_max = 3;
        } else if(cards == 16) {
            m_max = 4;
            n_max = 4;
        } else if(cards == 14) {
            m_max = 7;
            n_max = 2;
        } else if(cards == 12) {
            m_max = 4;
            n_max = 3;
        } else if(cards == 10) {
            m_max = 5;
            n_max = 2;
        } else if(cards == 8) {
            m_max = 4;
            n_max = 2;
        } else if(cards == 6) {
            m_max = 3;
            n_max = 2;
        } else if(cards == 4) {
            m_max = 2;
            n_max = 2;
        }
        int[] dim = {m_max, n_max};
        return dim;
    }

    public static int[][] assignImageResources(int cards) {
        int image101, image102, image103, image104, image105, image106, image107, image108, image109,
                image110, image201, image202, image203, image204, image205, image206, image207, image208,
                image209, image210;
        image101 = R.drawable.ic_image101;
        image102 = R.drawable.ic_image102;
        image103 = R.drawable.ic_image103;
        image104 = R.drawable.ic_image104;
        image105 = R.drawable.ic_image105;
        image106 = R.drawable.ic_image106;
        image107 = R.drawable.ic_image107;
        image108 = R.drawable.ic_image108;
        image109 = R.drawable.ic_image109;
        image110 = R.drawable.ic_image110;

        image201 = R.drawable.ic_image101;
        image202 = R.drawable.ic_image102;
        image203 = R.drawable.ic_image103;
        image204 = R.drawable.ic_image104;
        image205 = R.drawable.ic_image105;
        image206 = R.drawable.ic_image106;
        image207 = R.drawable.ic_image107;
        image208 = R.drawable.ic_image108;
        image209 = R.drawable.ic_image109;
        image210 = R.drawable.ic_image110;


        int[][] four = {{image101, image102},
                        {image102, image101}};
        int[][] six = {{image103, image102},
                       {image102, image101},
                       {image101, image103}};
        int[][] eight = {{image104, image105},
                {image102, image101},
                {image105, image101},
                {image102, image104}};
        int[][] ten = {{image104, image105},
                {image105, image102},
                {image107, image101},
                {image104, image102},
                {image107, image101}};
        int[][] twelve = {{image104, image105, image104},
                {image105, image102, image107},
                {image107, image110, image106},
                {image106, image102, image110}};
        int[][] fourteen = {{image109, image105},
                {image105, image102},
                {image107, image101},
                {image108, image102},
                {image107, image101},
                {image110, image108},
                {image109, image110}};
        int[][] sixteen = {{image101, image108, image105, image106},
                {image103, image105, image102, image104},
                {image106, image103, image107, image108},
                {image101, image102, image104, image107}};

        int[][] eighteen = {{image109, image105, image103},
                {image105, image102, image104},
                {image107, image101, image103},
                {image108, image102, image106},
                {image107, image101, image104},
                {image109, image108, image106},};
        int[][] twenty = {{image104, image105, image106, image109},
                {image105, image102, image109, image110},
                {image107, image101, image108, image103},
                {image104, image102, image108, image103},
                {image107, image101, image106, image110}};

        int[] dim = getDimensions(cards);

        int[][] complete = four;
        if (cards == 20) {
            complete = twenty;
        } else if (cards == 18) {
            complete = eighteen;
        } else if (cards == 16) {
            complete = sixteen;
        } else if (cards == 14) {
            complete = fourteen;
        } else if (cards == 12) {
            complete = twelve;
        } else if (cards == 10) {
            complete = ten;
        } else if (cards == 8) {
            complete = eight;
        } else if (cards == 6) {
            complete = six;
        } else if (cards == 4) {
            complete = four;
        }
        return complete;
    }


    public static boolean getSol(String[][] array) {
        boolean[][] solution = new boolean[array[0].length][array[1].length];

        boolean[][] answerKey = new boolean[array[0].length][array[1].length];


        for(int m = 0; m < array[0].length; m++) {
            for(int n = 0; n < array[1].length; n++) {

                Log.i("", " " + array[m][n] + " ");

                if(array[m][n].equals("true"))
                    solution[m][n] = true;
                else
                    solution[m][n] = false;
            }
        }

        for(int m = 0; m < array[0].length; m++) {
            for(int n = 0; n < array[1].length; n++) {
                answerKey[m][n] = true;
            }
        }

        boolean returnVar;
        if(Arrays.deepEquals(answerKey, solution)) {
            returnVar = true;
        } else {
            returnVar = false;
        }

        return returnVar;
    }


}
