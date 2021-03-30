
package com.example.authenticationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.Normalizer;

public class PhysicalActivity extends AppCompatActivity {


    private Button addNewDataBtn;
    private TextView textViewHeight, textViewWeight, textViewBMI;
    private ProgressBar progressBar;



    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private String userID ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        addNewDataBtn = findViewById(R.id.addDataBtn);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewBMI = findViewById(R.id.textViewBmi);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");
        firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        refreshPage();

        addNewDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhysicalActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View view = layoutInflater.inflate(R.layout.dialog_physical,null,false);
                builder.setView(view);

                EditText etWeight = view.findViewById(R.id.etWeight);
                EditText etHeight = view.findViewById(R.id.etHeight);
                Button btnAdd = view.findViewById(R.id.addData);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String weight = etWeight.getText().toString().trim();
                        String height = etHeight.getText().toString();
                        UserPhysicalObject userPhysicalObject = new UserPhysicalObject(weight,height);

                        progressBar.setVisibility(View.VISIBLE);

                        // add data to the database

                        reference.child(userID).child("Physical").setValue(userPhysicalObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PhysicalActivity.this, "Done Adding", Toast.LENGTH_SHORT).show();
                                    refreshPage();
                                    progressBar.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(PhysicalActivity.this, "Not Added", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });

                AlertDialog alert =  builder.create();
                alert.show();
            }

        });


    }

    private void refreshPage() {
        reference.child(userID).child("Physical").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPhysicalObject physicalObject = snapshot.getValue(UserPhysicalObject.class);

                if (physicalObject != null){


                    String h = physicalObject.getHeight();
                    String w = physicalObject.getWeight();

                    double newWeight = Double.parseDouble(w);
                    double newHeight = Double.parseDouble(h);

                    double BMI = (newWeight/newHeight/newHeight)*10000;


                    textViewHeight.setText("Height : "+ h + " Cm");
                    textViewWeight.setText("Weight : " + w + " KG");
//                    textViewBMI.setText("BMI : "+ BMI);
                    textViewBMI.setText(String.format("BMI : %.2f",BMI));

                    Toast.makeText(PhysicalActivity.this, "Load Data Successfully ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PhysicalActivity.this, "Not Able to Load Data ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}