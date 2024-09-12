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


public class RenalSystem extends AppCompatActivity {

    CardView kidneyCardView, ureterCardView, quizTopicCardView;
    ImageView whitebgImage;
    Button quizButton;
    TextView backTextView, nextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renal_system);

        kidneyCardView = findViewById(R.id.kidneyCardView);
        ureterCardView=findViewById(R.id.ureterCardView);
        quizTopicCardView=findViewById(R.id.quizTopicCardView);
        whitebgImage=findViewById(R.id.whitebgImage);
        quizButton=findViewById(R.id.quizButton);
        backTextView=findViewById(R.id.backTextView);
        nextTextView=findViewById(R.id.nextTextView);

        nextTextView.setVisibility(View.INVISIBLE);

        whitebgImage.setVisibility(View.INVISIBLE);
        quizTopicCardView.setVisibility(View.INVISIBLE);

        ThemedToggleButtonGroup themedButtonGroup = findViewById(R.id.quizTopic);




        kidneyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenalSystem.this, Kidney.class);
                startActivity(intent);
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kidneyCardView.setClickable(false);
                whitebgImage.setVisibility(View.VISIBLE);
                quizTopicCardView.setVisibility(View.VISIBLE);


            }
        });


        themedButtonGroup.setOnSelectListener((ThemedButton btn)->{
            if(btn.getText().equals("Kidney")){
                nextTextView.setVisibility(View.VISIBLE);
            }
            else if(btn.getText().equals("Ureter")){
                nextTextView.setVisibility(View.VISIBLE);
            }

            return Unit.INSTANCE;
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kidneyCardView.setClickable(true);
                whitebgImage.setVisibility(View.INVISIBLE);
                quizTopicCardView.setVisibility(View.INVISIBLE);
                nextTextView.setVisibility(View.INVISIBLE);
            }
        });

        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemedButton selectedButton=themedButtonGroup.getSelectedButtons().get(0);
                if(selectedButton.getText().equals("Kidney")){
                    Intent intent=new Intent(RenalSystem.this, kidneyQuiz.class);
                    startActivity(intent);
                }
                else if(selectedButton.getText().equals("Ureter")){
                    Intent intent = new Intent(RenalSystem.this, ureterQuiz.class);
                    startActivity(intent);
                }
            }
        });



    }
}