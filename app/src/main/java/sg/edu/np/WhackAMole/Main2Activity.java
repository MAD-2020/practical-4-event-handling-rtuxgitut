package sg.edu.np.WhackAMole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
    /* Hint
        - The function setNewMole() uses the Random class to generate a random value ranged from 0 to 8.
        - The function doCheck() takes in button selected and computes a hit or miss and adjust the score accordingly.
        - The functions readTimer() and placeMoleTimer() are to inform the user X seconds before starting and loading new mole.
        - Feel free to modify the function to suit your program.
    */
    private TextView resultViewer;
    private Integer ranLocation;
    private Integer score = 0;
    private CountDownTimer cdTimer;
    private final List<Button> holeList = new ArrayList<>();
    private static final String TAG = "Whack-A-Mole!";

    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.hole1, R.id.hole2, R.id.hole3, R.id.hole4, R.id.hole5,
            R.id.hole6, R.id.hole7, R.id.hole8, R.id.hole9
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares the existing score brought over.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Get Intent
        Intent previousScore = getIntent();
        score = previousScore.getIntExtra("Score", 0);
        Log.v(TAG, "Current User Score: " + score.toString());

        //Set current score value
        resultViewer = findViewById(R.id.resultViewer);
        resultViewer.setText(score.toString());

        //Initialise listener
        View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button buttonChecker = (Button) v;
                doCheck(buttonChecker);
            }
        };

        //Initialise buttons
        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            Button hole = (Button) findViewById(id);
            hole.setOnClickListener(clicker);
            holeList.add(hole);
        }

        //Once all initialized, start timer
        readyTimer();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onStop(){
        super.onStop();
        Log.v(TAG, "Stopped Whack-A-Mole!");
    }

    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, "Hit, score added!");
            Log.v(TAG, "Missed, point deducted!");
            belongs here.
        */
        switch (checkButton.getText().toString()) {
            case "0":
                if (score <= 0) {
                    Log.v(TAG, "Hit the mole to score points!");
                    score = 0;
                    resultViewer.setText(score.toString());
                    break;
                }
                else{
                    Log.v(TAG, "Missed, score deducted!");
                    score -= 1;
                    resultViewer.setText(score.toString());
                    break;
                }
            case "*":
                Log.v(TAG, "Hit, score added!");
                score += 1;
                resultViewer.setText(score.toString());
                holeList.get(ranLocation).setText("0");
                setNewMole();
                cdTimer.cancel();
                placeMoleTimer(); //Reset timer.
                break;
            default:
                Log.v(TAG, "Please hit one of the buttons!");
        }
    }

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        cdTimer = new CountDownTimer(10*1000, 1000) {
            @Override
            public void onTick(long ms) {
                //Calculate time remaining
                Long timeRemain = ms/1000;
                Log.v(TAG, "Ready CountDown!" + ms/ 1000);
                String msg = "Get ready in " + timeRemain.toString() + " seconds!";

                //Toast is a small pop up which length is the same as the text length.
                final Toast cdMsg = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                cdMsg.show();

                //Set timer to delete toast
                Timer delcdMsg = new Timer();
                delcdMsg.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        cdMsg.cancel();
                    }
                }, 1000);
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                setNewMole();
                placeMoleTimer();
            }
        };

        cdTimer.start();
    }

    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        cdTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long ms) {
                //Calculate time remaining
                Long timeRemain = ms/1000;
                Log.v(TAG, "New Mole Location!");
                holeList.get(ranLocation).setText("0");
                setNewMole();
            }

            @Override
            public void onFinish() {
                cdTimer.start();
            }
        };

        cdTimer.start();
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole.
         */
        Random ran = new Random();
        ranLocation = ran.nextInt(9);
        holeList.get(ranLocation).setText("*");
    }
}


