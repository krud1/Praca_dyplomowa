package Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase_android.NotificationBroadcastReceiver;
import com.example.firebase_android.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class GroupsPositionItemActivity extends AppCompatActivity {

    TextView tvItemName, tvAdditionalInfo, tvNumber, tvDate;
    CheckBox phone, popup;
    ImageView imgImage;
    String tableId, photoPath;
    Long uniqueId;
    Button cancelEvent;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_position_item);
        tvItemName = findViewById(R.id.tvItemName);
        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
        tvNumber = findViewById(R.id.tvNumber);
        tvDate = findViewById(R.id.tvDate);
        phone = findViewById(R.id.chbPhone);
        popup = findViewById(R.id.chbNotification);
        imgImage = findViewById(R.id.imgImage);
        cancelEvent = findViewById(R.id.btnCancel);


        popup.setEnabled(false);
        phone.setEnabled(false);
        
        tableId = getIntent().getStringExtra("tableId");


        
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvItemName.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("item_name").getValue(String.class));
                tvAdditionalInfo.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("additional_info").getValue(String.class));
                tvNumber.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("phone_number").getValue(String.class));
                tvDate.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("notificationDate").getValue(String.class));
                photoPath = snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("photo_path").getValue(String.class);
                uniqueId = snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("uniqueId").getValue(Long.class);
                try{
                    getImage();
                } catch (IOException e){
                    e.printStackTrace();
                }
                boolean popupBool = (Boolean) snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("popup").getValue();
                boolean phoneBool = (Boolean) snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("phone").getValue();
                    if(popupBool){ popup.setChecked(true); }
                    if(phoneBool){ phone.setChecked(true); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEvent();
                cancelSms();
                deleteTable();
                Intent intent = new Intent(GroupsPositionItemActivity.this, GroupsActivity.class);
                startActivity(intent);
                Toast.makeText(GroupsPositionItemActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
    }

    public void getImage() throws IOException{

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("images").child(getCurrentUserId()).child(photoPath);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(getApplicationContext()).load(String.valueOf(uri)).placeholder(R.drawable.baseline_add_a_photo_24).into(imgImage);
                Log.d("xxxxxxxxxxxxxxxxxxx", uri.toString());
            }
        });



    }

    private void cancelEvent(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.valueOf(Long.toString(uniqueId)) , intent, PendingIntent.FLAG_NO_CREATE);
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelSms(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.valueOf(Long.toString(uniqueId+10)) , intent, PendingIntent.FLAG_NO_CREATE);
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void deleteTable() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query removeQuery = ref.child("Users").child(getCurrentUserId()).child("User Database").child(tableId);
        removeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private String getCurrentUserId() {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d("GroupsPositionItemActivity", "Current user uid: " + currentUser.getUid());
            } else {
                Log.d("GroupsPositionItemActivity", "No user logged in");
            }
            return currentUser.getUid();
        }
    }

