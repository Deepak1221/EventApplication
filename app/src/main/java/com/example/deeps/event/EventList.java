package com.example.deeps.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EventList extends AppCompatActivity {
RecyclerView relView;
    EvenListAdapter adapter ;
ProgressBar  progressbar;
    ArrayList<EventModel> list_event = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        progressbar = findViewById(R.id.progressbar);

        relView = (RecyclerView) findViewById(R.id.relView);
        adapter = new EvenListAdapter(list_event,EventList.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        relView.setLayoutManager(mLayoutManager);
        relView.setItemAnimator(new DefaultItemAnimator());
        relView.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference().child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    HashMap eventMap = (HashMap) dataSnapshot.getValue();
//                    Iterator iterator = mapListGroup.keySet().iterator();
                    eventMap.get("events");

//                    list_event = (ArrayList<EventModel>) eventMap.get("events");
//
//                    list_event.get(0).getAddress();
//
                    Log.w("events",": "+dataSnapshot.getValue());

//                    Toast.makeText(EventList.this, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();


                    Iterator listKey = eventMap.keySet().iterator();
                    while (listKey.hasNext()) {
                        String key = listKey.next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(key);

                        EventModel item = new EventModel();
                        item.name = (String) userMap.get("name");
                        item.mobile_no = (String) userMap.get("mobile_no");
                        item.address = (String) userMap.get("address");
                        item.date= (String)userMap.get("date");
                        item.time= (String)userMap.get("time");
//                        item.setName(key.);
                        Log.w("name",":"+item.getName());

//                        Toast.makeText(EventList.this, ""+item.getName(), Toast.LENGTH_SHORT).show();

                        list_event.add(item);
                    }

                    adapter.notifyDataSetChanged();
                    progressbar.setVisibility(View.GONE);
//                    while (iterator.hasNext()){
//                        String idGroup = (String) mapListGroup.get(iterator.next().toString());
//                        EventModel newGroup = new EventModel();
////                        newGroup.id = idGroup;
//                        listGroup.add(newGroup);
//                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
