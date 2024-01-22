package com.virtual.software.mybuddy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtPredictedResult;
    TextView txtSkip, txtHand, txtPlayerHandCount, txtbankerHandCount;

    Button btnSkip, btnUndo;
    LinearLayout winAndLossLinearLayout;
    TableLayout tableLayout;
    boolean isSkip = false;
    boolean isWaiting = false;
    HorizontalScrollView winAndLossResultHorizontalSccrollView;


    //BEGIN------OCR Money Management-------------------------------------------------------
    private static final int BASE = 0;
    private static final int POS1 = 1;
    private static final int STEP1 = 2;
    private static final int STEP2 = 3;
    private static final int STEP3 = 4;
    private static final int STEP4 = 5;
    private static final int STEP5 = 6;

    private static String currentPosition = "Base";
    private static int consecutiveWins = 0;
    private static int consecutiveLose = 0;
    private static int winRequirement = 2;
    private static int loseRequirement = 3;
    private static String[] levels;

    TextView txtPos1, txtBase, txtStep1, txtStep2, txtStep3, txtStep4, txtStep5;
    LinearLayout moneyManagementPanel;


    //END--------------OCR Money Management-------------------------------------------------------

    double initialBetAmount = 0.0;
    int hand = 0;
    int playerHandCount = 0;
    int bankerHandCount = 0;
    int requiredHandToStart = 0;
    List<String> listOfItemFromRoad;// = Arrays.asList("B", "B", "B", "B", "P", "P", "P", "B", "B", "P", "P");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPredictedResult = findViewById(R.id.txtPredictedResult);
        txtSkip = findViewById(R.id.txtSkip);
        txtHand = findViewById(R.id.txtHand);
        txtPlayerHandCount = findViewById(R.id.txtPlayerHandCount);
        txtbankerHandCount = findViewById(R.id.txtbankerHandCount);

        btnSkip = findViewById(R.id.btnSkip);
        btnUndo = findViewById(R.id.btnUndo);
        winAndLossLinearLayout = findViewById(R.id.winAndLossLinearLayout);
        winAndLossResultHorizontalSccrollView = findViewById(R.id.winAndLossResultHorizontalSccrollView);
        winAndLossLinearLayout.removeAllViews();

//        BEGIN--------ORC---------------
        txtPos1 = findViewById(R.id.txtPo1);
        txtBase = findViewById(R.id.txtBase);
        txtStep1 = findViewById(R.id.txtStep1);
        txtStep2 = findViewById(R.id.txtStep2);
        txtStep3 = findViewById(R.id.txtStep3);
        txtStep4 = findViewById(R.id.txtStep4);
        txtStep5 = findViewById(R.id.txtStep5);
        moneyManagementPanel = findViewById(R.id.moneyManagementLinearPanel);
        tableLayout = findViewById(R.id.tableLayoutRoad);


        requiredHandToStart = Orc.getHandRequiredToStart();
        initialBetAmount = 0.2;
        levels = Orc.levels;
        Orc.setBetAmount(initialBetAmount);
//  END--------------ORC--------------------

        List<String> customList = Arrays.asList("P", "B", "B","P", "B", "B","P", "B", "B","P", "B", "B");
        setBigRoadView(customList);

        printAllTextViewNameFromtableLayout();

    }

    private void printAllTextViewNameFromtableLayout() {


        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View view = tableLayout.getChildAt(i);

            if (view instanceof TableRow) {
                TableRow tableRow = (TableRow) view;

                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View childView = tableRow.getChildAt(j);

                    if (childView instanceof TextView) {
                        TextView textView = (TextView) childView;
                        String textViewName = textView.getTag().toString(); // Or use any other property you want
                        System.out.println("TextView Name: " + textViewName);
                    }
                }
            }
        }

    }

    private void setBigRoadView(List<String> listOfItems) {
        // Create a TableLayout

//        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.MATCH_PARENT));

        // Add rows and cells to the TableLayout
        for (int i = 0; i < 6; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 20; j++) {
                TextView textView = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(60, 60);
                params.setMargins(1, 1, 1, 1);
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(8, 8, 8, 8);
                textView.setTag("r"+i+"c"+j);
                textView.setText("r"+i+"c"+j);
                textView.setTextSize(10);
//                textView.setBackgroundResource(R.drawable.road_item_color_red);
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
        }

    }



    private void generateResult() {
        Random random = new Random();
        int randomValue = random.nextInt(2); // Generates 0 or 1

        String result;
        if (randomValue == 0) {
            result = "Player";
        } else {
            result = "Banker";
        }
        listOfItemFromRoad.add(result);
        setPredictionView(result);
    }

    private void setPredictionView(String result) {
        txtPredictedResult.setText(result);
        if (result.equals("Player")) {
            txtPredictedResult.setBackgroundColor(getResources().getColor(R.color.player_button));
        } else {
            txtPredictedResult.setBackgroundColor(getResources().getColor(R.color.banker_button));
        }

    }

    private void validateAndDisplayTheResult(String txtOfButtonClicked) {

        String predictedOutCome = txtPredictedResult.getText().toString().toLowerCase();
        String userPressed = getButtonTextClicked(txtOfButtonClicked);
        if (predictedOutCome.equals(userPressed)) {
            createWinAndLossView("W", R.drawable.button_player);

        } else {
            createWinAndLossView("L", R.drawable.button_banker);
        }
        isSkip = false;
        setSkipView();
    }

    private void setSkipView() {


        if (isSkip) {
            txtSkip.setText("Yes");
            // Set background color
            txtSkip.setBackgroundColor(getResources().getColor(R.color.parity));
        } else {
            txtSkip.setText("No");
            // Set background color
            txtSkip.setBackgroundColor(getResources().getColor(R.color.white));
        }

    }

    private void OrcMoneyManagement(String response) {


        if (!isWaiting) {
            System.out.println("Current Position: " + currentPosition + " Bet Amount: " + Orc.getBetAmount(currentPosition));
            System.out.print("Did you win in " + currentPosition + "? (w/l): ");
//            String response = scanner.nextLine().toLowerCase();

            if (response.equals("w") || response.equals("l")) {
                switch (currentPosition) {
                    case "Pos1":
                        if (WinLoseCondition(levels[POS1], response)) {
                            currentPosition = levels[BASE];
                            consecutiveLose = 0;
                        } else {
                            currentPosition = levels[STEP1];
                        }
                        break;

                    case "Base":
                        consecutiveWins = 0;
                        consecutiveLose = 0;
                        if (WinLoseCondition(levels[BASE], response)) {
                            currentPosition = levels[POS1];
                        } else {
                            currentPosition = levels[STEP1];
                            consecutiveLose++;
                        }
                        break;

                    case "Step1":
                        if (WinLoseCondition(levels[STEP1], response)) {
                            currentPosition = levels[BASE];
                            consecutiveLose = 0;
                        } else {
                            currentPosition = levels[STEP2];
                            consecutiveLose++;
                        }
                        break;

                    case "Step2":
                        if (WinLoseCondition(levels[STEP2], response)) {
                            consecutiveWins++;
                            consecutiveLose = 0;
                            if (consecutiveWins == winRequirement) {
                                currentPosition = levels[BASE];
                            } else {
                                currentPosition = levels[STEP1];
                            }
                        } else {
                            currentPosition = levels[STEP3];
                            consecutiveLose++;
                        }
                        break;

                    case "Step3":
                        if (WinLoseCondition(levels[STEP3], response)) {
                            consecutiveWins++;
                            consecutiveLose = 0;
                            if (consecutiveWins == winRequirement) {
                                currentPosition = levels[BASE];
                            } else {
                                currentPosition = levels[STEP2];
                            }
                        } else {
                            currentPosition = levels[STEP4];
                            if (consecutiveWins > 0) {
                                consecutiveWins--;
                            }
                            consecutiveLose++;
                        }
                        break;

                    case "Step4":
                        if (WinLoseCondition(levels[STEP4], response)) {
                            consecutiveWins++;
                            consecutiveLose = 0;
                            if (consecutiveWins == winRequirement) {
                                currentPosition = levels[BASE];
                            } else {
                                currentPosition = levels[STEP3];
                            }
                        } else {
                            currentPosition = levels[STEP5];
                            if (consecutiveWins > 0) {
                                consecutiveWins--;
                            }
                            consecutiveLose++;
                        }
                        break;

                    case "Step5":
                        if (WinLoseCondition(levels[STEP5], response)) {
                            currentPosition = levels[STEP4];
                            consecutiveWins++;
                            consecutiveLose = 0;
                        } else {
                            currentPosition = levels[BASE];
                            consecutiveLose++;
                        }
                        break;
                }
            }

            if (consecutiveLose == loseRequirement) {
                isWaiting = true;
            }
        } else {
            System.out.print("Did you win while waiting? (w/l): ");
//            String response = scanner.nextLine().toLowerCase();

            if (response.equals("w")) {
                consecutiveWins++;
            }

            if (consecutiveWins == winRequirement) {
                isWaiting = false;
                consecutiveLose = 0;
                consecutiveWins = 0;
            }
        }

        System.out.println("Consecutive Lose: " + consecutiveLose);
        System.out.println("is waiting: " + isWaiting);

        validateAndDisplayTheResult(response);
        setWaitView();
        setHandView();
        setMoneyManagementCurrentLelvelVIew(currentPosition);

    }

    private void setHandView() {
        txtHand.setText(String.valueOf(hand));
    }

    private void setMoneyManagementCurrentLelvelVIew(String currentPosition) {


        TextView[] textViews = new TextView[]{txtPos1, txtBase, txtStep1, txtStep2, txtStep3, txtStep4, txtStep5};
        for (TextView txtView : textViews) {
            if (txtView.getText().toString().contains(currentPosition)) {
                txtView.setBackgroundColor(getResources().getColor(R.color.purple)); // Replace with the color you want
                txtView.setTextColor(getResources().getColor(R.color.white));
            } else {
                txtView.setBackgroundColor(getResources().getColor(R.color.white));
                txtView.setTextColor(getResources().getColor(R.color.light_black));
            }
        }

    }


    private static boolean WinLoseCondition(String currentPosition, String response) {
        if (response.equals("w")) {
            return true;
        } else if (response.equals("l")) {
            return false;
        } else {
            return WinLoseCondition(currentPosition, response);
        }
    }

    private void setWaitView() {


        if (isWaiting) {
            txtSkip.setText("Wait");

            txtSkip.setBackgroundColor(getResources().getColor(R.color.wait));
        } else {
//            Toast.makeText(this, "isWait is true", Toast.LENGTH_LONG).show();
        }

    }

    private String getButtonTextClicked(String txtOfButtonClicked) {


        if (txtOfButtonClicked.equals("P")) {
            return "player";
        } else {
            return "banker";

        }


    }

    private void createWinAndLossView(String userInput, int drawble) {


        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                50, // width
                50);
        layoutParams.setMargins(1, 1, 1, 1);
        textView.setLayoutParams(layoutParams);

        if (isSkip) {
            textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
        } else if (isWaiting) {
            textView.setBackground(getResources().getDrawable(R.color.wait));
        } else {
            textView.setBackground(getResources().getDrawable(drawble));
        }


        textView.setGravity(android.view.Gravity.CENTER);
        textView.setText(userInput);
        textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
        textView.setTextSize(15);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);

        winAndLossLinearLayout.addView(textView);
        scrollToEnd();
    }

    private void removeLastItemFromLayout(LinearLayout layout) {
        int childCount = layout.getChildCount();
        if (childCount > 0) {
            View lastChild = layout.getChildAt(childCount - 1);

            if (lastChild instanceof TextView) {
                layout.removeView(lastChild);
            }
        }
        scrollToEnd();
    }

    public void btnBanker_Clicked(View view) {

        hand++;
        playClickedSound();
        String bankerButtonText = ((Button) view).getText().toString();
        OrcMoneyManagement(bankerButtonText);
        generateResult();
        setHandCountView(bankerButtonText);
//        setBigRoadView();

    }


    public void btnPlayer_Clicked(View view) {


        hand++;
        playClickedSound();
        String playerButtonText = ((Button) view).getText().toString();
        OrcMoneyManagement(playerButtonText);
        generateResult();
        setHandCountView(playerButtonText);
//        setBigRoadView();

    }

    private void setHandCountView(String hand) {

        if (hand.equals("P")) {
            playerHandCount++;
        } else if (hand.equals("B")) {
            bankerHandCount++;
        }

        txtPlayerHandCount.setText(String.valueOf(playerHandCount));
        txtbankerHandCount.setText(String.valueOf(bankerHandCount));

    }


    public void btnUndo_Clicked(View view) {
        playClickedSound();
        removeLastItemFromLayout(winAndLossLinearLayout);
    }

    private void scrollToEnd() {
        // Scroll to the end
        winAndLossResultHorizontalSccrollView.post(new Runnable() {
            @Override
            public void run() {
                winAndLossResultHorizontalSccrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    private void ResetAll() {
        winAndLossLinearLayout.removeAllViews();
        isWaiting = false;
        isSkip = false;
        setSkipView();

        hand = 0;
        setHandView();
        playerHandCount = 0;
        bankerHandCount = 0;
        setHandCountView("");

    }

    private void playClickedSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clicked_sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Release the MediaPlayer object after the sound has finished playing
            }
        });
    }


    public void btnReset_Clicked(View view) {
        playClickedSound();
        ResetAll();
    }

    public void btnSkip_Clicked(View view) {

        playClickedSound();
        isSkip = true;

        setSkipView();

    }




}