package com.dscddu.lithium;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import javax.annotation.Nullable;

public class SelectActivity extends AppCompatActivity {

    HashMap<String,String> dataToPass;
    TextView name,email,phone,status;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    DocumentReference tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        bottomAppBar  = (BottomAppBar)findViewById(R.id.bottomAppBarSelect);
        setSupportActionBar(bottomAppBar);
        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        if(b!=null)
            dataToPass = (HashMap<String,String>)b.get("passedData");

        String collection = dataToPass.get("Event");
        String id=dataToPass.get("userID");
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        status = (TextView) findViewById(R.id.status);
        fab = (FloatingActionButton) findViewById(R.id.fabSelect);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection(collection).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.get("Name").toString());
                        email.setText(document.get("Email").toString());
                        phone.setText(document.get("Phone").toString());
                        status.setText(document.get("Status").toString());
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap<String,Object> obj = new HashMap();
                                obj.put("Status","present");
                                docRef.update(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        SelectActivity.this.finish();
                                    }
                                });
                            }
                        });
                    } else {
                    }
                } else {
                }
            }
        });

        /*db.collection("users").whereEqualTo("Email",userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                final DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                name.setText(document.get("Name").toString());
                                email.setText(document.get("Email").toString());
                                phone.setText(document.get("Phone").toString());
                                status.setText(document.get("Status").toString());
                            } else {
                                Log.e("TAG", "No such document");
                            }
                        } else {
                            Log.e("TAG", "get failed with ", task.getException());
                        }
                    }
                });
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String,Object> obj = new HashMap();
                        obj.put("Status","present");
                        docRef.update(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SelectActivity.this.finish();
                            }
                        });
                    }
                });
            }
        });*/




    }
}
