package com.example.firebase_android;

import android.content.Context;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchBarAdapter {
    Context context;
    SearchView searchView;
    FirebaseAuth mAuth;
    FirebaseDatabase fDb;
    DatabaseReference myRef;
    RecyclerViewAdapter rvAdapter;
    RecyclerView rvList;
    Spinner spinner;

    public SearchBarAdapter(FirebaseAuth mAuth, FirebaseDatabase fDb, DatabaseReference myRef,
                            RecyclerViewAdapter rvAdapter, RecyclerView rvList, Context context,
                            SearchView searchView, Spinner spinner) {

        this.mAuth = mAuth;
        this.fDb = fDb;
        this.myRef = myRef;
        this.rvAdapter = rvAdapter;
        this.rvList = rvList;
        this.context = context;
        this.searchView = searchView;
        this.spinner = spinner;
    }

    public void loadDataByItemName(){
        fDb = FirebaseDatabase.getInstance();
        myRef = fDb.getReference();
        rvList.setLayoutManager(new LinearLayoutManager(context));

        FirebaseRecyclerOptions<GroupsPosition> x =
                new FirebaseRecyclerOptions.Builder<GroupsPosition>()
                    .setQuery(FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("item_name"), GroupsPosition.class).build();

        rvAdapter = new RecyclerViewAdapter(x);
        rvAdapter.startListening();
        rvList.setAdapter(rvAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s!=null){
                    FirebaseRecyclerOptions<GroupsPosition> x =
                            new FirebaseRecyclerOptions.Builder<GroupsPosition>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().
                                            child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("item_name").startAt(s).endAt(s+"\uf8ff"), GroupsPosition.class).build();

                    rvAdapter = new RecyclerViewAdapter(x);
                    rvAdapter.startListening();
                    rvList.setAdapter(rvAdapter);
                }

                return false;
            }
        });
    }
    public void loadDataByDate() {
        fDb = FirebaseDatabase.getInstance();
        myRef = fDb.getReference();
        rvList.setLayoutManager(new LinearLayoutManager(context));

        FirebaseRecyclerOptions<GroupsPosition> x =
                new FirebaseRecyclerOptions.Builder<GroupsPosition>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("notificationDate"), GroupsPosition.class).build();

        rvAdapter = new RecyclerViewAdapter(x);
        rvAdapter.startListening();
        rvList.setAdapter(rvAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null) {
                    FirebaseRecyclerOptions<GroupsPosition> x =
                            new FirebaseRecyclerOptions.Builder<GroupsPosition>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().
                                            child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("notificationDate").startAt(s).endAt(s + "\uf8ff"), GroupsPosition.class).build();
                    rvAdapter = new RecyclerViewAdapter(x);
                    rvAdapter.startListening();
                    rvList.setAdapter(rvAdapter);
                }
                return false;
            }
        });
    }
}
