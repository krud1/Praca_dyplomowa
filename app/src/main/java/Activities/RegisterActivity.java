package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebase_android.R;
import com.example.firebase_android.UserInformations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    String uid;
    EditText etNickname, etEmail,  etRegisterPassword, etRepeatPassword, etPhoneNumber;
    ProgressBar progressBar;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String email = etEmail.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String repeatPassword = etRepeatPassword.getText().toString().trim();
                final String nickname = etNickname.getText().toString().trim();
                final String phone_num = etPhoneNumber.getText().toString().trim();

                if(TextUtils.isEmpty(nickname)) {
                    etNickname.setError("Nickname can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    etEmail.setError("Email can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    etRegisterPassword.setError("Password can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(phone_num)) {
                    etPhoneNumber.setError("Phone number can't be empty");
                    return;
                }
                if(password.length()<8){
                    etRegisterPassword.setError("Password must be at least 8 characters");
                    return;
                }
                if(!password.equals(repeatPassword)){
                    etRepeatPassword.setError("Passwords are different");
                    return;
                }

                //Register to firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            uid = fUser.getUid();
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference().child("Users").child(uid).child("User Informations");
                            UserInformations userInfo = new UserInformations(nickname, email, phone_num, uid);
                            dbRef.setValue(userInfo);

                            Intent intent = new Intent(view.getContext(), LoginActivity.class);
                            startActivity(intent);

                            Toast.makeText(RegisterActivity.this, "User created, you may login", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Log.d(TAG, "createUserWithEmail:failure");
                            Toast.makeText(RegisterActivity.this, "Error - " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }
}
