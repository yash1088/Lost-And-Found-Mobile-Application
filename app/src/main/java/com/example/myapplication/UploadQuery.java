package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadQuery extends AppCompatActivity {

    public EditText Fitem;
    public EditText Place;
    private Spinner spinner;
    private EditText Image;
    private Button Upload;
    private ImageView IV;
    private ProgressDialog PD;
    private Firebase mroot;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE=123;
    Uri imagePath;
    public String found_item,place;
    private Firebase firebaseRef;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode== RESULT_OK && data.getData() !=null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                IV.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_query);

        //firebaseRef=new Firebase("https://my-app-2a0e6.firebaseio.com/1iAw2NH42KbAu4gupkmulcLGSp92");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();


        Fitem=(EditText) findViewById(R.id.etFItem);
        Place=(EditText) findViewById(R.id.etPlace);
        IV=(ImageView) findViewById(R.id.iv);
        spinner=(Spinner) findViewById(R.id.Spinner);

        List<String> Categories =new ArrayList<>();
        Categories.add(0,"Choose Category");
        Categories.add("Pendrive");
        Categories.add("Mouse");
        Categories.add("Earphones");
        Categories.add("Laptop-Charger");
        Categories.add("Mobile-Charger");
        Categories.add("HeadPhone");
        Categories.add("Memory-Card");
        Categories.add("Mouse-Bluetooth");
        Categories.add("Bike-Key");
        Categories.add("Car-Key");
        Categories.add("Other");
        ArrayAdapter<String> dataAdapter;
        dataAdapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item, Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Category")){
                }
                else{
                    String item=parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected item is:" +item ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadQuery.this, "Please Select Item !", Toast.LENGTH_SHORT).show();
            }
        });

        Image = (EditText) findViewById(R.id.etImage);
        Upload=(Button) findViewById(R.id.btnUpload);
        PD=new ProgressDialog(UploadQuery.this);

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
            }
        });



        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                found_item =Fitem.getText().toString().trim();
                place=Place.getText().toString().trim();

                if(found_item.isEmpty()){
                    Fitem.setError("Field can`t be Empty");
                }
                else if (!isValidFitem(found_item)) {
                    Fitem.setError("Enter Valid Item");
                }
                else if (place.isEmpty()) {
                    Place.setError("Field can`t be empty");
                }
                else if (!isValidPlace(place)) {
                    Place.setError("Enter Valid Place");
                }

                else if(imagePath==null){
                    Toast.makeText(UploadQuery.this,"Please Enter Image",Toast.LENGTH_SHORT).show();
                }

                else{
                    PD.setMessage("Uploading ....");
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
                    SendData();
                    Toast.makeText(UploadQuery.this, "Submited Data!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadQuery.this,UploadQuery.class);
                    startActivity(intent);

                }
            }
        });
    }

        public boolean isValidFitem(String found_item) {
            String Found_PATTERN = "^[a-zA-Z0-9-,_]+$";
            Pattern pattern = Pattern.compile(Found_PATTERN);
            Matcher matcher = pattern.matcher(found_item);
            return matcher.matches();
        }

        public boolean isValidPlace(String place) {
            String Place_PATTERN = "^[a-zA-Z0-9-,_]+$";
            Pattern pattern = Pattern.compile(Place_PATTERN);
            Matcher matcher = pattern.matcher(place);
            return matcher.matches();
        }

    public void SendData(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference Ref=firebaseDatabase.getReference(firebaseAuth.getUid());
        try {
            String str1="Founded-Item";
            String str2="Place";
            Ref.child("").child(str1).setValue(found_item);
            Ref.child("").child(str2).setValue(place);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StorageReference imageref=storageReference.child(firebaseAuth.getUid()).child("images").child("Profile");
        UploadTask uploadTask=imageref.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadQuery.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadQuery.this, "uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
