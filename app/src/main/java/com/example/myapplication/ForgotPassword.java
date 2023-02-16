package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button sendemail;
    private ProgressDialog PD;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.etMobile);
        sendemail = (Button) findViewById(R.id.btnSendEmail);
        PD=new ProgressDialog(ForgotPassword.this);
        firebaseAuth=FirebaseAuth.getInstance();

        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                if (Email.isEmpty()) {
                    email.setError("Field can`t be Empty");
                }
                else if (!isValidEmail(Email)) {
                    email.setError("Enter Valid Email");
                }
                else {
                    PD.setMessage("Please Wait ....");
                    PD.show();
                    firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                PD.dismiss();
                                Toast.makeText(ForgotPassword.this, "Password Reset Email Sent !", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                PD.dismiss();
                                Toast.makeText(ForgotPassword.this, "Error in sending Email !", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });
    }


    public boolean isValidEmail(String Email) {
        String EMAIL_PATTERN = "^[0-9a-zA-Z]+@charusat.edu.in+$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }
}
