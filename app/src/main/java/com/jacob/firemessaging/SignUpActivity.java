package com.jacob.firemessaging;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @BindView(R.id.signupBtn) TextView signupBtn;
    @BindView(R.id.userNameInput) EditText userNameInput;
    @BindView(R.id.passwordInput) EditText passwordInput;
    @BindView(R.id.passwordInputConfirm) EditText passwordInputConfirm;
    @BindView(R.id.emailInput) EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordInput.getText().toString().trim().equals(passwordInputConfirm.getText().toString().trim())) {
                    registerUser();
                    return;
                } else {
                    passwordInput.setError("Password doesn't match");
                    passwordInput.requestFocus();
                    return;

                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void registerUser(){
        final String username_content, password_content, email_content;

        username_content = userNameInput.getText().toString().trim();
        password_content = passwordInput.getText().toString().trim();
        email_content = emailInput.getText().toString().trim();

        if(!TextUtils.isEmpty(username_content) && !TextUtils.isEmpty(password_content) && !TextUtils.isEmpty(email_content)){
            mAuth.createUserWithEmailAndPassword(email_content, password_content)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user_id);
                                current_user_db.child("username").setValue(username_content);
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }


                        }
                    });
        }
    }

    public static void start(Context context) {
        Intent startSignUpActivity = new Intent(context, SignUpActivity.class);
        context.startActivity(startSignUpActivity);
    }

}
