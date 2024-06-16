package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Models.FSO;

public class FSOLogin extends AppCompatActivity { // this class is for users to login to their food stall owner account
    EditText edtUsername, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fso_login);

        edtUsername = (EditText) findViewById(R.id.FSOUsername);
        edtPassword = (EditText) findViewById(R.id.FSOPassword);

        btnSignIn = (Button) findViewById(R.id.btnFSOLogin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("FSO");

        // sign in to fso account
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    ProgressDialog pDialog = new ProgressDialog(FSOLogin.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //check if fso exist in database
                            try {
                                if (snapshot.child(edtUsername.getText().toString()).exists()) {
                                    //get fso info here
                                    pDialog.dismiss();
                                    FSO fso = snapshot.child(edtUsername.getText().toString()).getValue(FSO.class);
                                    if (fso.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent fsoHomeIntent = new Intent(FSOLogin.this, FSOHome.class);
                                        Common.currentFSO = fso;
                                        startActivity(fsoHomeIntent);
                                        finish();
                                        //Toast.makeText(FSOLogin.this, "Sign In success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(FSOLogin.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(FSOLogin.this, "FSO doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(FSOLogin.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}