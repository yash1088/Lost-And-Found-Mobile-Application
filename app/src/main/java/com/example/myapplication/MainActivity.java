package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private Button Signup;
    private TextView Tv;
    private ProgressDialog PD;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Name=(EditText) findViewById(R.id.etUserid);
         Password=(EditText) findViewById(R.id.etPassword);
         Login =(Button) findViewById(R.id.btnLogin);
         Signup=(Button)findViewById(R.id.btnSignUp);
         Tv=(TextView)findViewById(R.id.tv);
         PD=new ProgressDialog(this);
         firebaseAuth=FirebaseAuth.getInstance();
         FirebaseUser user=firebaseAuth.getCurrentUser();
         if(user!=null){
            finish();
            Intent intent = new Intent(MainActivity.this, UploadPage.class);
            startActivity(intent);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=Name.getText().toString().trim();
                String pass=Password.getText().toString().trim();

                if(username.isEmpty() && pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "User-Name or Password is Empty  !", Toast.LENGTH_SHORT).show();
                }
                else{
                    isValidate(username,pass);
                }
            }

        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SignUpPage.class);
                startActivity(intent);
            }
        });

        Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void isValidate(String username,String pass) {
        PD.setMessage("Signing in...");
        PD.show();
        firebaseAuth.signInWithEmailAndPassword(username, pass)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            PD.dismiss();
                            Toast.makeText(MainActivity.this, "Login Successfull!",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,UploadPage.class);
                            startActivity(intent);
                        } else {
                            PD.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Login failed !",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void onBackPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
