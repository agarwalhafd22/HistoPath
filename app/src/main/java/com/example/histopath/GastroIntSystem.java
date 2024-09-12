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


public class GastroIntSystem extends AppCompatActivity {

    CardView colonCardView, appendixCardView, liverCardView, quizTopicCardView;
    ImageView whitebgImage;
    Button quizButton;
    TextView backTextView, nextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastro_int_system);

        colonCardView = findViewById(R.id.colonCardView);
        appendixCardView = findViewById(R.id.appendixCardView);
        liverCardView = findViewById(R.id.liverCardView);
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
                whitebgImage.setVisibility(View.VISIBLE);
                quizTopicCardView.setVisibility(View.VISIBLE);


            }
        });

        themedButtonGroup.setOnSelectListener((ThemedButton btn)->{
            if(btn.getText().equals("Colon")){
                nextTextView.setVisibility(View.VISIBLE);
            }
            else if(btn.getText().equals("Appendix")){
                nextTextView.setVisibility(View.VISIBLE);
            }
            else if(btn.getText().equals("Liver")){
                nextTextView.setVisibility(View.VISIBLE);
            }

            return Unit.INSTANCE;
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whitebgImage.setVisibility(View.INVISIBLE);
                quizTopicCardView.setVisibility(View.INVISIBLE);
                nextTextView.setVisibility(View.INVISIBLE);
            }
        });

        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemedButton selectedButton=themedButtonGroup.getSelectedButtons().get(0);
//                if(selectedButton.getText().equals("Colon")){
//                    Intent intent=new Intent(GastroIntSystem.this, colonQuiz.class);
//                    startActivity(intent);
//                }
//                else if(selectedButton.getText().equals("Breast")){
//                    Intent intent = new Intent(GastroIntSystem.this, appendixQuiz.class);
//                    startActivity(intent);
//                }
            }
        });



    }
}