package com.example.authenticationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class UserActivity extends AppCompatActivity {

    private Button minus;
    private Button plus;
    TextView name, age, email, movie, progress_text;
    EditText etMovie;

    ProgressBar progressBar;

    String n, a, mail, film;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button signOut = findViewById(R.id.signOut);


        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("User");
        user = mAuth.getCurrentUser();
        uId = user.getUid();
        progressBar = findViewById(R.id.progressBarUser);


        name = findViewById(R.id.tvName);
        age= findViewById(R.id.tvAge);
        email = findViewById(R.id.tvEmail);
        movie = findViewById(R.id.tvMovie);
        etMovie =  findViewById(R.id.etMovie);
        Button btnAddMovie = findViewById(R.id.btnAddMovie);
        Button physicalInfoBtn = findViewById(R.id.physicalInfoBtn);
        plus = findViewById(R.id.plusProgress);
        minus = findViewById(R.id.minusProgress);
        progress_text = findViewById(R.id.progress_text);

        registerForContextMenu(name);
        refreshPAge();
        progress_text.setText(0 + "%");
        progressBar.setProgress(0);
        progressBar.setMax(100);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pro = progressBar.getProgress();
                if ( pro < progressBar.getMax()){
                progressBar.setProgress(pro+20);
                progress_text.setText(pro + 20 + "%");
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pro = progressBar.getProgress();
                if (pro == 0){
                    Toast.makeText(UserActivity.this, "Can not minus from empty progress bar ", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setProgress(pro-20);
                    progress_text.setText(pro - 20 + "%");
                }

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(UserActivity.this, "sign out successful " , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                finish();
            }
        });


        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = etMovie.getText().toString().trim();


                if (movieName.isEmpty()) {
                    etMovie.setError("Enter movie name please ");
                    etMovie.requestFocus();
                    return;
                }
                        
                UserObject newData = new UserObject(n,a,mail,movieName);
                ref.child(uId).child("Personal").setValue(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserActivity.this, "Added Successfully ", Toast.LENGTH_SHORT).show();
                            refreshPAge();
                        }
                        else{
                            Toast.makeText(UserActivity.this, "not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        physicalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,PhysicalActivity.class));

            }
        });

//        name.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
//                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//                View view = inflater.inflate(R.layout.dialog_user_info,null,false);
//                builder.setView(view);
//
//                EditText etNameDialog = view.findViewById(R.id.etNameDialog);
//                Button btnUpdate = view.findViewById(R.id.updateName);
//                etNameDialog.setText(n); // setting user name
//
//                btnUpdate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // get data from edit text
//                       String newName = etNameDialog.getText().toString().trim();
//
//                       // creating object to pass to the database
//                        UserObject newData = new UserObject();
//                        newData.setName(newName);
//
//                       ref.child(uId).child("Personal").setValue(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                           @Override
//                           public void onComplete(@NonNull Task<Void> task) {
//                               if (task.isSuccessful()) {
//                                   Toast.makeText(UserActivity.this, "Data Updated Successfully ", Toast.LENGTH_SHORT).show();
//                               }
//                               else{
//                                   Toast.makeText(UserActivity.this, "Sorry Can not Update Data ", Toast.LENGTH_SHORT).show();
//                               }
//                           }
//                       });
//
//
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//


                //Toast.makeText(UserActivity.this, "Name " + name.getText().toString(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });




    }





    public void refreshPAge() {
        ref.child(uId).child("Personal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserObject profile = snapshot.getValue(UserObject.class);
                Log.d("TAG", "onDataChange: "+ profile);
                if (profile != null) {
                     n = profile.getName();
                     a = profile.getAge();
                     mail = profile.getEmail();
                     film = profile.getMovie();


                    name.setText("Welcome :" + n);
                    age.setText("Your age : " + a);
                    email.setText("Email : " + mail);
                    movie.setText("Movie Name : " + film);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.tvName) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_screen, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.firstItem:
                // add stuff here
                Toast.makeText(this, "you click me ", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.firstItem:
                Toast.makeText(this, "hello you click me ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}