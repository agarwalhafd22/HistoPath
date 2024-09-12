package com.example.histopath;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.alexvasilkov.gestures.views.GestureImageView;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.PhotoView;


public class Kidney extends AppCompatActivity {

    private PhotoView photoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidney); // Your layout

        GestureImageView gestureImageView = findViewById(R.id.gesture_image_view);
        gestureImageView.setImageResource(R.drawable.kidney_one_unlabelled);




//        photoView = findViewById(R.id.photo_view);
//
//        // Load the image into PhotoView
//        photoView.setImageResource(R.drawable.kidney_one_unlabelled);

    }
}
