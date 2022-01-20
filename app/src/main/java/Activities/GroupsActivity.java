package Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.firebase_android.R;
import com.example.firebase_android.RecyclerViewAdapter;
import com.example.firebase_android.SearchBarAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class GroupsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final String TAG = "GroupsActivity";

    String currentUserId;
    SearchBarAdapter sba;
    FirebaseAuth mAuth;
    FirebaseDatabase fDb;
    DatabaseReference myRef;
    Spinner spinner;
    RecyclerViewAdapter rvAdapter;
    RecyclerView rvList;
    Button btn_add, btn_sent, btn_pending, btn_speechToText;
    SearchView searchView;
    String[] sortOptions = {"Sort alphabetically","Sort by date"};



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        rvList = findViewById(R.id.rv_list);
        btn_speechToText = findViewById(R.id.btn_speechToText);
        btn_add = findViewById(R.id.btn_add);
        btn_sent = findViewById(R.id.btn_sent);
        btn_pending = findViewById(R.id.btn_pending);
        searchView = findViewById(R.id.svFinished);
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, sortOptions);
        spinner.setAdapter(aa);


        getCurrentUserId();

        rvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        sba = new SearchBarAdapter(mAuth, fDb, myRef, rvAdapter, rvList, this, searchView, spinner);

        btn_speechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(GroupsActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    checkPermission();
                }
               voiceRecognition();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, AddNewPositionActivity.class); //todo
                startActivity(intent);
            }
        });

        btn_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, SentNotificationListActivity.class);
                startActivity(intent);
            }
        });
        btn_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void voiceRecognition(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 10);
    }

    public void getCurrentUserId(){

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().toString();

        if(currentUser != null){
            Log.d(TAG, "Current user uid: " + currentUser.getUid());
        }
        else{
            Log.d(TAG, "No user logged in");
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Log.d("voice results", result.toString());
                searchView.setQuery(result.get(0), true);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCurrentUserId();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            sba.loadDataByItemName();
        }
        if(i==1){
            sba.loadDataByDate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        sba.loadDataByItemName();
    }
}

