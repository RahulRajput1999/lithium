package com.dscddu.lithium;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class StartActivity extends AppCompatActivity {

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBarStart);
        setSupportActionBar(bottomAppBar);
        final HashMap<String,String> dataToPass = new HashMap<>();
        spinner = (Spinner)findViewById(R.id.eventSpinner);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("Events");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                final List<String> list = new ArrayList<String>();
                for (DocumentSnapshot document : docList) {
                    list.add(document.get("Name").toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        dataToPass.put("Event",list.get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                FloatingActionButton fab = findViewById(R.id.fabStart);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("passedData",dataToPass);
                        startActivity(intent);
                    }
                });

            }
        });
    }

}
