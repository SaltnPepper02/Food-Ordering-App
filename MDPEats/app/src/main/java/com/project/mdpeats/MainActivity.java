package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity { // starting class

    Button btnStudent,btnFSO;
    TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variable
        btnStudent = (Button) findViewById(R.id.btnStudent);
        btnFSO = (Button) findViewById(R.id.btnFSO);

        txtSlogan = (TextView) findViewById(R.id.txtSlogan);

        // this button goes to food stall owner login
        btnFSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FSOSignIn = new Intent(MainActivity.this, FSOLogin.class);
                startActivity(FSOSignIn);
            }
        });

//        FirebaseMessaging.getInstance().getToken() // gets phone token
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        System.out.println("TOKEN: " + token);
//
//                    }
//                });

        // this button goes to student login
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StudentSignIn = new Intent(MainActivity.this, StudentLogin.class);
                startActivity(StudentSignIn);
            }
        });


    }
}