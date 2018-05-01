package com.jacob.firemessaging;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @BindView(R.id.loginBtn) TextView loginBtn;
    @BindView(R.id.signupBtn) TextView signupBtn;
    @BindView(R.id.userNameInput) EditText userNameInput;
    @BindView(R.id.passwordInput) EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (UserClass.isUserSignedIn(this)) {
            // Show error dialog
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameInput.getText().toString();
                String password = passwordInput.getText().toString();

                //send it to setter
                setUsersInfo(username, password);
            }
        });

        signupBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void setUsersInfo(String usersInfo, String passwordInfo) {
        Boolean userExists = false;
        Log.e("Username: ",usersInfo);
        UserClass.setUsername(this, usersInfo);

        if(!TextUtils.isEmpty(usersInfo) && !TextUtils.isEmpty(passwordInfo)){
            mAuth.signInWithEmailAndPassword(usersInfo, passwordInfo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkUserExist();
                    }
                }
            });
        }


        // TODO add authentication function that iterate thru firebase username field and match it
        if(userExists == true){
            MessagingActivity.start(this);
            finish();
        }

    }

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){
                    Intent loginIntent = new Intent(LoginActivity.this, MessagingActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signupBtn:
                startActivity(new Intent(this, SignUpActivity.class));

        }
    }
}
