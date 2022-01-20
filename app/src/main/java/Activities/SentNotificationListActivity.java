package Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.firebase_android.GroupsPosition;
import com.example.firebase_android.R;
import com.example.firebase_android.RecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SentNotificationListActivity extends AppCompatActivity {


    String currentUserId;
    Context context;
    RecyclerView rv;
    RecyclerViewAdapter recyclerViewAdapter;
    FirebaseAuth mAuth;
    FirebaseDatabase fDb;
    DatabaseReference myRef;
    Button btn_pending, btn_add, btn_sent;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_notifs_list);
        rv = findViewById(R.id.rv_sent_list);
        btn_add = findViewById(R.id.btn_add2);
        btn_pending = findViewById(R.id.btn_pending2);
        btn_sent = findViewById(R.id.btn_sent2);
        searchView = findViewById(R.id.svFinished);
        getCurrentUserId();
        loadDataByDate();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentNotificationListActivity.this, AddNewPositionActivity.class);
                startActivity(intent);
            }
        });

        btn_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentNotificationListActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });

        btn_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentNotificationListActivity.this, SentNotificationListActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
    }


    public void loadDataByDate() {
        fDb = FirebaseDatabase.getInstance();
        myRef = fDb.getReference();
        rv.setLayoutManager(new LinearLayoutManager(context));

        FirebaseRecyclerOptions<GroupsPosition> x =
                new FirebaseRecyclerOptions.Builder<GroupsPosition>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("status").startAt("finished_1021-01-01 17:55").endAt("finished_3021-01-01 17:55"), GroupsPosition.class).build();


        recyclerViewAdapter = new RecyclerViewAdapter(x);
        recyclerViewAdapter.startListening();
        rv.setAdapter(recyclerViewAdapter);

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
                                            child("Users").child(mAuth.getCurrentUser().getUid()).child("User Database").orderByChild("item_name").startAt(s).endAt(s + "\uf8ff"), GroupsPosition.class).build();

                    recyclerViewAdapter = new RecyclerViewAdapter(x);
                    recyclerViewAdapter.startListening();
                    rv.setAdapter(recyclerViewAdapter);
                }
                return false;
            }
        });
    }

    public void getCurrentUserId(){
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().toString();
        }
}



