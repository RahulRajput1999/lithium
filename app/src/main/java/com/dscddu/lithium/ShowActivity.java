package com.dscddu.lithium;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class ShowActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    ListView lv;
    SearchView sv;

    Bundle b;
    HashMap<String,String> dataToPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBarShow);
        setSupportActionBar(bottomAppBar);
        sv = (SearchView) findViewById(R.id.searchView);
        lv = (ListView) findViewById(R.id.userList);
        b = this.getIntent().getExtras();
        if(b!=null){
            dataToPass = (HashMap<String,String>)b.get("passedData");
            //savedInstanceState.putBundle("previousBundle",b);
        } else{
            b = savedInstanceState.getBundle("previousBundle");
            dataToPass = (HashMap<String, String>) b.get("passedData");
        }
        String collection = dataToPass.get("Event");
        final List<User> list = new ArrayList<>();
        final UserAdapter ua = new UserAdapter(getApplicationContext(), R.layout.list_item, list);
        lv.setAdapter(ua);
        lv.setTextFilterEnabled(true);
        lv.setTextFilterEnabled(true);

        sv.setIconified(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ua.getFilter().filter(s);
                Log.d("Search", s);
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabShow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection(collection);
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                list.clear();
                for (DocumentSnapshot document : docList) {
                    User u = new User();
                    u.setName(document.get("Name").toString());
                    u.setEmail(document.get("Email").toString());
                    u.setPhone(document.get("Phone").toString());
                    u.setStatus(document.get("Status").toString());
                    list.add(u);
                    ua.notifyDataSetChanged();
                }

            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBundle("previousBundle",b);
    }

}
