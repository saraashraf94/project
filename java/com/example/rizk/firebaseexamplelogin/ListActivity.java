package com.example.rizk.firebaseexamplelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView rv;
    Button btn;
    List<SaveData> tripList;
    RvAdapter adapter;
    DatabaseReference mPostReference;
    LinearLayoutManager llm;
    String status="" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
//        if(savedInstanceState==null)
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Trips/" + user.getUid());
        rv = (RecyclerView) findViewById(R.id.rv);
        tripList = new ArrayList<>();
        llm = new LinearLayoutManager(this);
        btn = findViewById(R.id.floatingActionButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, AddTrip.class));
                ListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        adapter = new RvAdapter(tripList);

        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    SaveData trip = data.getValue(SaveData.class);
                    trip.setKey(data.getKey());
                    if(trip.getStatus().equals("upComming"))
                    tripList.add(trip);

                }
                adapter.notifyDataSetChanged();
                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);
                Log.v("tav", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.aaaa, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                startActivity(new Intent(ListActivity.this,DoneAct.class));
                ListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.up:
                startActivity(new Intent(ListActivity.this,ListActivity.class));
                ListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
         }
        return super.onOptionsItemSelected(item);
    }
}
