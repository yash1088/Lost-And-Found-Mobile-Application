package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity{

    private EditText fname;
    private EditText lname;
    private EditText user;
    private EditText pass;
    private EditText email1;
    private EditText mobile;
    private Button register;
    private ProgressDialog PD;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";
    public String Fname,Lname,Userid,Mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        fname=(EditText) findViewById(R.id.etFname);
        lname=(EditText) findViewById(R.id.etLname);
        user=(EditText) findViewById(R.id.etUserid);
        pass=(EditText) findViewById(R.id.etPassword);
        mobile=(EditText) findViewById(R.id.etMobile);
        email1=(EditText) findViewById(R.id.etEmail);
        register=(Button) findViewById(R.id.btnRegister);
        PD=new ProgressDialog(SignUpPage.this);
        firebaseAuth=FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fname=fname.getText().toString().trim();
                Lname=lname.getText().toString().trim();
                Userid=user.getText().toString().trim();
                String password=pass.getText().toString().trim();
                Mobile=mobile.getText().toString().trim();
                String email=email1.getText().toString().trim();

                if(Fname.isEmpty()){
                    fname.setError("Field can`t be empty");
                }
                else if(!isValidFname(Fname)){
                    fname.setError("Enter Valid First-Name");
                }
                else if(Lname.isEmpty()){
                    lname.setError("Field can`t be empty");
                }
                else if(!isValidLname(Lname)){
                    lname.setError("Enter Valid Last-Name");
                }
                else if(Userid.isEmpty()){
                    user.setError("Field can`t be empty");
                }
                else if (!isValidUser(Userid)) {
                    user.setError("Enter Valid User-Name");
                }
                else if(password.isEmpty()){
                    pass.setError("Field can`t be empty");
                }
                else if (!isValidPass(password)) {
                    pass.setError("Enter six character");
                }
                else if(Mobile.isEmpty()){
                    mobile.setError("Field can`t be empty");
                }
                else if (!isValidMobile(Mobile)) {
                    mobile.setError("Enter 10 digits only");
                }
                else if(email.isEmpty()){
                    email1.setError("Field can`t be empty");
                }
                else if (!isValidEmail(email)) {
                    email1.setError("Enter Valid Email");
                }
                else {
                    PD.setMessage("Please Wait ....");
                    PD.show();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            PD.dismiss();
                        }
                    }).start();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpPage.this, "Registration Successfull !", Toast.LENGTH_SHORT).show();
                                        SendData();
                                    } else {
                                        Toast.makeText(SignUpPage.this, "Registration UnSuccessfull", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public boolean isValidFname(String Fname) {
        String FnamePATTERN = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(FnamePATTERN);
        Matcher matcher = pattern.matcher(Fname);
        return matcher.matches();
    }
    public boolean isValidLname(String Lname) {
        String LnamePATTERN = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(LnamePATTERN);
        Matcher matcher = pattern.matcher(Lname);
        return matcher.matches();
    }

    public boolean isValidUser(String username) {
        String USER_PATTERN = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    public boolean isValidPass(String pass) {
        String PASS_PATTERN = "^[0-9a-zA-Z@]{6}";
        Pattern pattern = Pattern.compile(PASS_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    public boolean isValidMobile(String mobile) {
        String MOBILE_PATTERN = "^[0-9]{10}";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public boolean isValidEmail(String Email) {
        String EMAIL_PATTERN = "^[0-9a-zA-Z]+@charusat.edu.in+$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    public void SendData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference Ref=firebaseDatabase.getReference(firebaseAuth.getUid());
        User userprofile=new User(Fname,Lname,Userid,Mobile);
        Ref.setValue(userprofile);
    }
}
