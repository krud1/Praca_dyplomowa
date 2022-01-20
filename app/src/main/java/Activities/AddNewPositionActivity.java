package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.firebase_android.AddPosition;
import com.example.firebase_android.NotificationBroadcastReceiver;
import com.example.firebase_android.R;
import com.example.firebase_android.SmsBroadcastReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddNewPositionActivity extends AppCompatActivity  {

    private final static String TAG = "AddNewPosition";
    int uploadCode;
    Uri imageUri;
    String photo_path, getPhotoPath, userId, randomKey, tableId, status = "pending", notificationDate;
    int uniqueId = createUniqueID();
    boolean phone = false;
    boolean popup = false;
    Bitmap bitmap;
    Calendar calendar;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mRef;
    EditText item_name, additional_info, phone_number;
    Button add_photo, contact_book, select_date, add_to_list, btn_chooseGallery;
    CheckBox chb_phone, chb_popup;
    ImageView photo_View;
    TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_position);

        item_name = findViewById(R.id.et_item_name);
        additional_info = findViewById(R.id.et_additional_info);
        phone_number = findViewById(R.id.et_addPhoneNumber);
        add_photo = findViewById(R.id.imgbtn_add_photo);
        contact_book = findViewById(R.id.imgbtn_contactbook);
        select_date = findViewById(R.id.btn_select_date);
        add_to_list = findViewById(R.id.btn_add_to_list);
        btn_chooseGallery = findViewById(R.id.btn_chooseGallery);
        chb_phone = findViewById(R.id.chb_phone);
        chb_popup = findViewById(R.id.chb_popup);
        photo_View = findViewById(R.id.photo_View);
        tv4 = findViewById(R.id.tv4);

        setNewAlarm();
        changeStatus();
        connectToBase();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        randomKey = UUID.randomUUID().toString();
        photo_path = "not defined";

        contact_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactBook();
            }
        });

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDateTimeDialog();
            }
        });

        btn_chooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { choosePhotoFromGallery();
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddNewPositionActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(AddNewPositionActivity.this, "Allow camera usage to continue", Toast.LENGTH_SHORT).show();
                            //Todo ask for permission
                }
                else{
                    choosePhotoFromCamera();  
                }
            }
        });

        add_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPosition(mRef);
                uploadPicture(uploadCode);
                Intent intent = new Intent(AddNewPositionActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
    }

    private String getCurrentUserId(){

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){
            Log.d("GroupsPositionItemActivity", "Current user uid: " + currentUser.getUid());
        }
        else{
            Log.d("GroupsPositionItemActivity", "No user logged in");
        }
        return currentUser.getUid();
    }

    private void changeStatus(){

        final String tableId = getIntent().getStringExtra("tableId2");
        final String status = getIntent().getStringExtra("finishedStatus");
        if(tableId == null && status ==null){ } else{
            FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("status").setValue(status);
            Intent intent = new Intent(AddNewPositionActivity.this, GroupsActivity.class);
            startActivity(intent);
        }

    }

    private void setNewAlarm() {
        final String tableId = getIntent().getStringExtra("tableId3");

        if (tableId == null) { } else {
            Log.d("=======", tableId);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    item_name.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("item_name").getValue(String.class));
                    additional_info.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("additional_info").getValue(String.class));
                    phone_number.setText(snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("phone_number").getValue(String.class));
                    getPhotoPath = snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("photo_path").getValue(String.class);
                    boolean popupBool = (Boolean) snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("popup").getValue();
                    boolean phoneBool = (Boolean) snapshot.child("Users").child(getCurrentUserId()).child("User Database").child(tableId).child("phone").getValue();
                    if (popupBool) { chb_popup.setChecked(true); }
                    if (phoneBool) { chb_phone.setChecked(true); }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


    private void getImage(String photoPath) throws IOException {
        if(!photoPath.equals("not defined")){
            storageRef.child("images").child(getCurrentUserId()).child(photoPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    photo_View.setImageURI(Uri.parse(imageUrl));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageUri = data.getData();
                    photo_View.setImageURI(imageUri);
                    uploadCode = 1;
                    photo_path = randomKey;
                }
                break;
            case 2:
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                photo_View.setImageBitmap(bitmap);
                uploadCode = 2;
                photo_path = randomKey;
                break;
            case 3:
                if(resultCode == Activity.RESULT_OK){
                    Uri contactNumber = data.getData();
                    Cursor c = getContentResolver().query(contactNumber, null, null, null, null);
                    if(c.moveToFirst()){
                        int i = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = c.getString(i);
                        Toast.makeText(this, number, Toast.LENGTH_LONG).show();
                        phone_number.setText(number);
                    }
                }
                break;
        }
    }

    private void choosePhotoFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void choosePhotoFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    private void uploadPicture(int x) {
        StorageReference storageRef = storageReference.child("images/" + userId + "/" + randomKey);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading to database");
        if(x == 1 && imageUri != null) {
            pd.show();
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Uploaded!", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(AddNewPositionActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Percentage:" + (int) progressPercent + "%");
                        }
                    });
        }
        if(x == 2){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            mRef = FirebaseDatabase.getInstance().getReference();
            storageRef.putBytes(b)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Uploaded!", Snackbar.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(AddNewPositionActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Percentage:" + (int) progressPercent + "%");
                        }
                    });
                }
        else {
           photo_path = "not defined";
        }
    }

    private void showDateTimeDialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        tv4.setText(simpleDateFormat.format(calendar.getTime()));
                        status += "_" + simpleDateFormat.format(calendar.getTime());
                        notificationDate = simpleDateFormat.format(calendar.getTime());
                        Log.d("xxxxxxxxxx",notificationDate);
                        tv4.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(AddNewPositionActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(AddNewPositionActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void addPosition(DatabaseReference mRef){

        Date createDate = new Date();

        if(tv4.toString().equals("Select date")){
            Toast.makeText(this, "Date mustn't empty", Toast.LENGTH_LONG).show();

        } else {
            if(chb_phone.isChecked()){
                phone = true;
                smsSchedule(calendar, phone_number.getText().toString(), item_name.getText().toString(), additional_info.getText().toString());
            }
            if(chb_popup.isChecked()){
                popup = true;
                startAlarm(calendar);
            }

            AddPosition addpos = new AddPosition(
                item_name.getText().toString(),
                additional_info.getText().toString(),
                phone_number.getText().toString(),
                tv4.getText().toString(),
                phone, popup, createDate.toString(), photo_path, tableId, status, uniqueId);

        mRef.setValue(addpos);
        }
        Log.d(TAG, "Created item");
    }

    private void connectToBase(){
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userId = fUser.getUid();
        mRef = database.getReference().child("Users").child(userId).child("User Database").push();
        tableId = mRef.getKey();

    }

    private void openContactBook () {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 3);
    }

    public int createUniqueID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

    private void smsSchedule(Calendar c, String phone_number, String item_name, String additional_info){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SmsBroadcastReceiver.class);
        intent.putExtra("number", phone_number);
        intent.putExtra("message", "Control: "+ item_name + "\n " + additional_info);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, createUniqueID()+10, intent, 0);
        Log.d("startSmsSchedule", "starting smsSchedule :" + item_name + c.getTime());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        intent.putExtra("title", item_name.getText().toString());
        intent.putExtra("text", additional_info.getText().toString());
        intent.putExtra("tableId", tableId);
        intent.putExtra("notificationDate", notificationDate);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueId, intent, 0);
        Log.d("startAlarm", "starting alarm :" + item_name.getText().toString() + c.getTime());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


}