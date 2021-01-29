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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

   private EditText editTextUserMail;
   private EditText editTextUserPass;
   private EditText editTextConformPassword;
   private CheckBox checkBoxShowPassword;
   private ProgressBar progressBarSignUp;
   private FirebaseAuth firebaseAuth;
   private Button buttonSignUpToSignIn;
   private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUserMail = findViewById(R.id.edit_text_register_mail);
        editTextUserPass = findViewById(R.id.edit_text_register_pass);
        editTextConformPassword = findViewById(R.id.edit_text_register_pass_again);
        checkBoxShowPassword = findViewById(R.id.checkbox_register);
        progressBarSignUp = findViewById(R.id.progress_bar_register);
        buttonSignUpToSignIn = findViewById(R.id.button_signUp_to_login);
        buttonSignUp = findViewById(R.id.button_register_signUp);
        firebaseAuth = FirebaseAuth.getInstance();


        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false){
                    editTextUserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextConformPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    editTextUserPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editTextConformPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        buttonSignUpToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextUserMail.getText().toString();
                String password = editTextUserPass.getText().toString();
                String conformPassword = editTextConformPassword.getText().toString();

                if(!TextUtils.isEmpty(mail) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(conformPassword)){
                    if(password.equals(conformPassword)){
                        progressBarSignUp.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   sendToSignIn();
                                   progressBarSignUp.setVisibility(View.INVISIBLE);
                               } else{
                                   progressBarSignUp.setVisibility(View.INVISIBLE);
                                   String error = task.getException().getMessage();
                                   Toast.makeText(RegisterActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
                    }else{
                        progressBarSignUp.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Confirm right password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Fields Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendToSignIn(){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}