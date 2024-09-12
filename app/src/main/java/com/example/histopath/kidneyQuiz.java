package com.example.histopath;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class kidneyQuiz extends AppCompatActivity {

    AnyChartView anyChartView;

    private CardView[] cardViews;
    private RadioGroup[] radioGroups; // Array of RadioGroups
    private Button startQuizButtonKidney, nextButton, backButton, submitButtonKidney;

    private int currentCardIndex = 0; // To keep track of the current CardView

    String[] correctAnswers = {"Renal Corpuscle", "Simple cuboidal with brush border"};
    String[] givenAnswers = new String[2]; // Store user's answers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidney_quiz);

        startQuizButtonKidney = findViewById(R.id.startQuizButtonKidney);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        submitButtonKidney = findViewById(R.id.submitButtonKidney); // Initialize submit button
        anyChartView=findViewById(R.id.anyChartView);

        // Initially hide the submit button
        submitButtonKidney.setVisibility(View.INVISIBLE);

        // Initialize CardViews and RadioGroups
        cardViews = new CardView[]{
                findViewById(R.id.kidneyQuizCardView1),
                findViewById(R.id.kidneyQuizCardView2),
                // Add more CardViews if you have more questions
        };

        radioGroups = new RadioGroup[]{
                findViewById(R.id.kidneyQuizRadioGroup1),
                findViewById(R.id.kidneyQuizRadioGroup2),
                // Add more RadioGroups if you have more questions
        };

        // Initially, set all CardViews to invisible
        for (CardView cardView : cardViews) {
            cardView.setVisibility(View.INVISIBLE);
        }

        nextButton.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);

        // Set OnClickListener for Start Quiz Button
        startQuizButtonKidney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                // Show the first CardView
                showCardView(0);
            }
        });

        // Set OnClickListener for Next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the answer from the current CardView
                saveAnswer(currentCardIndex);

                if (currentCardIndex < cardViews.length - 1) {
                    showCardView(currentCardIndex + 1);
                }
            }
        });

        // Set OnClickListener for Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCardIndex > 0) {
                    showCardView(currentCardIndex - 1);
                }
            }
        });

        // Set OnClickListener for Submit Button
        submitButtonKidney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitButtonKidney.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);

                startQuizButtonKidney.setVisibility(View.INVISIBLE);
                anyChartView.setVisibility(View.VISIBLE);

                cardViews[currentCardIndex].setVisibility(View.GONE);
                // Save the answer from the last CardView
                saveAnswer(currentCardIndex);

                int correctCount = compareAnswers();
                int wrongCount = correctAnswers.length - correctCount;

                // Compare answers and show result using Toast
                int score = compareAnswers();

//                ProgressBar progressBar = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);

                Pie pie = AnyChart.pie();

                List<DataEntry> data=new ArrayList<>();
                data.add(new ValueDataEntry("Correct", correctCount));
                data.add(new ValueDataEntry("Wrong", wrongCount));

                pie.data(data);
                anyChartView.setChart(pie);

            }
        });
    }

    private void saveAnswer(int index) {
        // Get selected RadioButton from the RadioGroup
        int selectedRadioButtonId = radioGroups[index].getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) { // If a RadioButton is selected
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            givenAnswers[index] = selectedRadioButton.getText().toString(); // Save the answer
        }
    }

    private void showCardView(int index) {
        // Hide all CardViews
        for (CardView cardView : cardViews) {
            cardView.setVisibility(View.INVISIBLE);
        }

        // Show the selected CardView
        if (index >= 0 && index < cardViews.length) {
            cardViews[index].setVisibility(View.VISIBLE);
            currentCardIndex = index;
            updateButtons();
        }
    }

    private void updateButtons() {
        // Show the Submit button only on the last CardView
        if (currentCardIndex == cardViews.length - 1) {
            nextButton.setVisibility(View.INVISIBLE);
            submitButtonKidney.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            submitButtonKidney.setVisibility(View.INVISIBLE);
        }

        // Update Back Button visibility
        backButton.setVisibility(currentCardIndex > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    private int compareAnswers() {
        // Compare correctAnswers and givenAnswers
        int score = 0;
        for (int i = 0; i < correctAnswers.length; i++) {
            if (correctAnswers[i].equals(givenAnswers[i])) {
                score++;
            }
        }
        return score; // Return the score
    }

}
