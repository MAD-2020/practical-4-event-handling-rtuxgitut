package sg.edu.np.WhackAMole;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /* Hint
        - The function setNewMole() uses the Random class to generate a random value ranged from 0 to 2.
        - The function doCheck() takes in button selected and computes a hit or miss and adjust the score accordingly.
        - The function doCheck() also decides if the user qualifies for the advance level and triggers for a dialog box to ask for user to decide.
        - The function nextLevelQuery() builds the dialog box and shows. It also triggers the nextLevel() if user selects Yes or return to normal state if user select No.
        - The function nextLevel() launches the new advanced page.
        - Feel free to modify the function to suit your program.
    */

    private static final String TAG = "Whack-A-Mole"; //Title of game

    private TextView resultViewer; //Allow program to see result of the whack a mole
    private Integer randomLocation;
    private Integer score;
    private Button firstbutton; //Individually declare all 3 buttons
    private Button secondbutton;
    private Button thirdbutton;
    private List<Button> holeList = new ArrayList<>(); //List to store all 3 buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultViewer = findViewById(R.id.resultViewer);
        firstbutton = findViewById(R.id.hole1);
        holeList.add(firstbutton);
        secondbutton = findViewById(R.id.hole2);
        holeList.add(secondbutton);
        thirdbutton = findViewById(R.id.hole3);
        holeList.add(thirdbutton);
        Log.v(TAG, "Finished Pre-Initialisation!");
    }

    @Override
    protected void onStart(){
        super.onStart();
        setNewMole();

        View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button pressedButton = (Button) v;
                Log.v(TAG,"Reached");
                switch (holeList.indexOf(pressedButton)) {
                    case 0:
                        Log.v(TAG,"Left Button Clicked!");
                        break;
                    case 1:
                        Log.v(TAG,"Middle Button Clicked!");
                        break;
                    case 2:
                        Log.v(TAG,"Right Button Clicked!");
                        break;
                    default:
                        Log.v(TAG,"No input found.");
                }

                score = Integer.parseInt(resultViewer.getText().toString());
                switch (pressedButton.getText().toString()) {
                    case "*": //
                        Log.v(TAG,"Successful, points added!");
                        score++;
                        resultViewer.setText(score.toString());
                        doCheck(pressedButton);
                        break;
                    case "O":
                        if (score <= 0)
                        {
                            Log.v(TAG,"Reminder: To score points hit the button with the '*' in it");
                            score = 0; //Remove the occurrence of a negative number if player fails to hit target while score is at 0
                        }
                        else
                        {
                            Log.v(TAG,"Unsuccessful, points deducted!");
                            score--; //Works as normal if user gained points beforehand
                        }
                        resultViewer.setText(score.toString());
                        break;
                    default: //'Starting game' instructions. Will be overridden after user hits target for the 1st time when he/she launches the program
                        Log.v(TAG,"To score points hit the button with the '*' in it!");
                }
                holeList.get(randomLocation).setText("O"); //Set all other holes as distractions
                setNewMole();
            }
        };
        firstbutton.setOnClickListener(clicker);
        secondbutton.setOnClickListener(clicker);
        thirdbutton.setOnClickListener(clicker);

        Log.v(TAG, "Starting GUI!");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG, "Paused Whack-A-Mole!");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.v(TAG, "Stopped Whack-A-Mole!");
        finish();
    }

    private void doCheck(Button buttonchecker) {
        if (score > 0 && score % 10 == 0) {
            nextLevelQuery();
        }
    }

    private void setNewMole() {
        Random ran = new Random();
        randomLocation = ran.nextInt(3);
        holeList.get(randomLocation).setText("*"); //Set mole in a random hole
    }

    private void nextLevelQuery(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning! Harder Whack-A-Mole Incoming!");
        builder.setMessage("Would you like to play in hard mode?");
        builder.setCancelable(false); //Must click either yes or no

        //If user choose yes
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Log.v(TAG, "User accepts!");
                advancedLevel();
            }
        });

        //If user choose no
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v(TAG, "User decline!");
            }
        });

        AlertDialog alert = builder.create();
        Log.v(TAG, "Advance option given to user!");
        alert.show();
    }

    private void advancedLevel(){
        /* Launch advanced page */
        Intent secondActivity;
        secondActivity = new Intent(this, Main2Activity.class);
        secondActivity.putExtra("Score: ", Integer.parseInt(resultViewer.getText().toString()));
        startActivity(secondActivity);
    }
}