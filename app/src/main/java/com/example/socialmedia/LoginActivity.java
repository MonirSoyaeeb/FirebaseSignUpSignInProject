package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserMail;
    private EditText editTextUserPassword;
    private Button buttonSignIn;
    private Button buttonSignInToSignUp;
    private CheckBox checkBoxShowPassword;
    private ProgressBar progressBarSignIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserMail = findViewById(R.id.edit_text_login_mail);
        editTextUserPassword = findViewById(R.id.edit_text_login_pass);
        buttonSignIn = findViewById(R.id.button_login_signIn);
        buttonSignInToSignUp = findViewById(R.id.button_login_to_signUp);
        checkBoxShowPassword = findViewById(R.id.checkbox_login);
        progressBarSignIn = findViewById(R.id.progress_bar_login);

        firebaseAuth = FirebaseAuth.getInstance();

        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false){
                    editTextUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    editTextUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

        buttonSignInToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextUserMail.getText().toString();
                String password = editTextUserPassword.getText().toString();

                if(!TextUtils.isEmpty(mail) || !TextUtils.isEmpty(password)){
                    progressBarSignIn.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               sendToMain();
                           }else{
                               progressBarSignIn.setVisibility(View.INVISIBLE);
                               String error = task.getException().getMessage();
                               Toast.makeText(LoginActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                           }
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Empty field not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}