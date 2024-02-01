package org.kaustav.majorproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {


    TextInputEditText editTextEmail,editTextPassword;
    TextInputEditText username, confirmPassword;
    Button buttonReg;
    FirebaseAuth mAuth;





    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();




        }
    }

@SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();

        editTextEmail=findViewById(R.id.emailReg);
        editTextPassword=findViewById(R.id.passwordReg);
        buttonReg=findViewById(R.id.registerToTheMain);
        username = findViewById(R.id.username);
        confirmPassword = findViewById(R.id.confirmPasswordReg);


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                String usernameText = String.valueOf(username.getText());
                String confirmPasswordText = String.valueOf(confirmPassword.getText());



                // Validate email and password
                if (TextUtils.isEmpty(usernameText)||TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPasswordText)) {
                    // Show an error or toast indicating empty fields
                    Toast.makeText(registerActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPasswordText)) {
                    // Show an error or toast indicating password mismatch
                    Toast.makeText(registerActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }






                if(TextUtils.isEmpty(email)) {


                    Toast.makeText(registerActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;

                }

                if(TextUtils.isEmpty(password)) {


                    Toast.makeText(registerActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

                    return;

                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(registerActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(getApplicationContext(),loginActivity.class);
                                    startActivity(intent);
                                    finish();



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(registerActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }
        });




        }




    public void redirectToLogin(View v){
        Intent i=new Intent(this, loginActivity.class);
        startActivity(i);
    }




}