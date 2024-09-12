package com.example.histopath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextView headerUserEmailTextView, headerUserNameTextView, headerTextView16;

    CardView gastroIntCardView, renalSystemCardView, femaleRepCardView, respiratoryCardView, nervousCardView;

    String email;
    String[] userData = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        gastroIntCardView=findViewById(R.id.gastroIntCardView);
        renalSystemCardView=findViewById(R.id.renalSystemCardView);
        femaleRepCardView=findViewById(R.id.femaleRepCardView);
        respiratoryCardView=findViewById(R.id.RespiratoryCardView);
        nervousCardView=findViewById(R.id.NervousCardView);

        navigationView.bringToFront();

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerUserEmailTextView = headerView.findViewById(R.id.userEmailTextView);
        headerUserNameTextView = headerView.findViewById(R.id.userNameTextView);
        headerTextView16=headerView.findViewById(R.id.textView16);

        // Get the current user's email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        if (email != null) {
            String formattedEmail = email.replace(".", ",");

            // Use the callback to get the user data from Firebase
            getUserData(formattedEmail, new FirebaseCallback() {
                @Override
                public void onCallback(String[] data) {
                    // Update the TextViews once data is fetched
                    if (data != null) {
                        userData=data;
                        headerUserNameTextView.setText(userData[0]);  // User name
                        headerUserEmailTextView.setText(email);       // User email
                        headerTextView16.setText("Student");
                    }
                }
            });
        }

        gastroIntCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GastroIntSystem.class);
                startActivity(intent);
            }
        });

        renalSystemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RenalSystem.class);
                startActivity(intent);
            }
        });

        femaleRepCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FemaleReproductiveSystem.class);
                startActivity(intent);
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), StudentLogin.class);
            startActivity(intent);
            finish();
            item.setChecked(false);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }











    // FirebaseCallback interface to handle asynchronous data retrieval
    public interface FirebaseCallback {
        void onCallback(String[] userData);
    }

    // Modified getUserData function with FirebaseCallback
    private void getUserData(String email, FirebaseCallback callback) {
        String[] returnString = new String[4];  // Array to hold user data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StudentDB").child(email);

        // Use the Firebase database to fetch user data asynchronously
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String college = snapshot.child("college").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    // Populate the returnString array with user data
                    returnString[0] = name;
                    returnString[1] = college;
                    returnString[2] = phone;
                    returnString[3] = password;

                    // Pass the data back through the callback
                    callback.onCallback(returnString);
                } else {
                    // Handle case where snapshot doesn't exist (e.g., user not found)
                    callback.onCallback(null);  // Pass null to indicate no data found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors here (e.g., log or show a message)
                callback.onCallback(null);
            }
        });
    }
}
