package com.ninjacode98.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText id;
    private EditText name;
    private EditText address;
    private EditText phone;

    private Button saveBtn;
    private Button showBtn;
    private Button updateBtn;
    private Button deleteBtn;

    private Student student;
    private DatabaseReference dbRef;

    private void clearControls(){
        id.setText("");
        name.setText("");
        address.setText("");
        phone.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = findViewById(R.id.idEditText);
        name = findViewById(R.id.nameEditText);
        address = findViewById(R.id.addressEditText);
        phone = findViewById(R.id.mobileEditText);

        saveBtn = findViewById(R.id.saveBtn);
        showBtn = findViewById(R.id.showBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        student = new Student();

        /**
         * SAVE DATA IN FIREBASE
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
                try {
                    if(TextUtils.isEmpty(id.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter an ID.",Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(name.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter a name.",Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(address.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter a address.",Toast.LENGTH_SHORT).show();
                    }else{
                        student.setId(id.getText().toString().trim());
                        student.setName(name.getText().toString().trim());
                        student.setAddress(address.getText().toString().trim());
                        student.setPhone(Integer.parseInt(phone.getText().toString().trim()));

                        dbRef.push().setValue(student);
                        Toast.makeText(getApplicationContext(),"Data saved successfully.",Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Invalid contact number.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * GET DTA FROM FIREBASE
         */
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Student").child("-MYroGfJRfG6Y5LEVDxf");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            id.setText(snapshot.child("id").getValue().toString());
                            name.setText(snapshot.child("name").getValue().toString());
                            address.setText(snapshot.child("address").getValue().toString());
                            phone.setText(snapshot.child("phone").getValue().toString());
                        }else{
                            Toast.makeText(getApplicationContext(),"No source to display.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        /**
         * UPDATE DATA
         */

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("Student");

                updateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("-MYroGfJRfG6Y5LEVDxf")){
                            try{
                                student.setId(id.getText().toString().trim());
                                student.setName(name.getText().toString().trim());
                                student.setAddress(address.getText().toString().trim());
                                student.setPhone(Integer.parseInt(phone.getText().toString().trim()));

                                dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("-MYroGfJRfG6Y5LEVDxf");
                                dbRef.setValue(student);
                                Toast.makeText(getApplicationContext(),"Data updated successfully.",Toast.LENGTH_SHORT).show();
                                clearControls();
                            }catch (NumberFormatException e){
                                Toast.makeText(getApplicationContext(),"Invalid contact number.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No source to update.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        /**
         * DELETE DATA
         */

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Student");
                deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("-MYroGfJRfG6Y5LEVDxf")){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("-MYroGfJRfG6Y5LEVDxf");
                            dbRef.removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(),"Data deleted successfully.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"No source to delete.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

}