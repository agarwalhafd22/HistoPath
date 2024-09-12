package com.example.histopath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import kotlin.Unit;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;


public class FemaleReproductiveSystem extends AppCompatActivity {

    CardView uterusCardView, breastCardView, quizTopicCardView;
    ImageView whitebgImage;
    Button quizButton;
    TextView backTextView, nextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_reproductive_system);

        uterusCardView = findViewById(R.id.uterusCardView);
        breastCardView = findViewById(R.id.breastCardView);
        quizTopicCardView = findViewById(R.id.quizTopicCardView);
        whitebgImage = findViewById(R.id.whitebgImage);
        quizButton = findViewById(R.id.quizButton);
        backTextView = findViewById(R.id.backTextView);
        nextTextView = findViewById(R.id.nextTextView);

        nextTextView.setVisibility(View.INVISIBLE);

        whitebgImage.setVisibility(View.INVISIBLE);
        quizTopicCardView.setVisibility(View.INVISIBLE);

        ThemedToggleButtonGroup themedButtonGroup = findViewById(R.id.quizTopic);

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uterusCardView.setClickable(false);
                whitebgImage.setVisibility(View.VISIBLE);
                quizTopicCardView.setVisibility(View.VISIBLE);


            }
        });

        themedButtonGroup.setOnSelectListener((ThemedButton btn)->{
            if(btn.getText().equals("Uterus")){
                nextTextView.setVisibility(View.VISIBLE);
            }
            else if(btn.getText().equals("Breast")){
                nextTextView.setVisibility(View.VISIBLE);
            }

            return Unit.INSTANCE;
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uterusCardView.setClickable(true);
                whitebgImage.setVisibility(View.INVISIBLE);
                quizTopicCardView.setVisibility(View.INVISIBLE);
                nextTextView.setVisibility(View.INVISIBLE);
            }
        });

        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemedButton selectedButton=themedButtonGroup.getSelectedButtons().get(0);
                if(selectedButton.getText().equals("Uterus")){
                    Intent intent=new Intent(FemaleReproductiveSystem.this, uterusQuiz.class);
                    startActivity(intent);
                }
                else if(selectedButton.getText().equals("Breast")){
                    Intent intent = new Intent(FemaleReproductiveSystem.this, breastQuiz.class);
                    startActivity(intent);
                }
            }
        });



    }
}