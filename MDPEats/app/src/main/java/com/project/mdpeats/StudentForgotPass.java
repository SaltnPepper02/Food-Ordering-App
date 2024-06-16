package com.project.mdpeats;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Models.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentForgotPass extends AppCompatActivity { // may not implement

    EditText edtPassword, edtConfirmPassword;

    String studentID = " ";
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_forgot_pass);

        edtPassword = (EditText) findViewById(R.id.FNewPass);
        edtConfirmPassword = (EditText) findViewById(R.id.FComfirmNewPass);

        btnSignup = (Button) findViewById(R.id.btnStudentForgotPass);



        if (getIntent() != null) {
            studentID = getIntent().getStringExtra("StudentID");

        }

        System.out.println(studentID);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                    changePassword(edtPassword.getText().toString());
                }
                else{
                    Toast.makeText(StudentForgotPass.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private void changePassword(String password) {
        Map<String, Object> passwordUpdate = new HashMap<>();
        passwordUpdate.put("password", password);

        //Initiate Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("Student");

        table_user.child(studentID).updateChildren(passwordUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(StudentForgotPass.this, "Password changed", Toast.LENGTH_SHORT).show();
                        Intent BackToLogin = new Intent(StudentForgotPass.this, StudentLogin.class);
                        startActivity(BackToLogin);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentForgotPass.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}