package pt.se_ulusofona.tarefeiros.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.se_ulusofona.tarefeiros.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PopupSignUp extends Activity implements View.OnClickListener{

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRepeatPass;
    private Button buttonSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_signup_layout);

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_pass);
        editTextRepeatPass = (EditText) findViewById(R.id.editText_repeat_pass);
        buttonSignUp = (Button) findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(this);

        //invoke progress dialog
        progressDialog = new ProgressDialog(this);
        //invoke firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //used to receive the resolution of the screen and resizes it to the desidered size (by percentage)
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.9));
    }

    private void userSignUp(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repeatPass = editTextRepeatPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }
        if(TextUtils.isEmpty(repeatPass)){
            //repeat pass is empty
            Toast.makeText(this, "Please repeat password", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }
        if(password.length() < 6){
            //password smaller than 5 chars
            Toast.makeText(this, "Password must be minimum 6 characters", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }
        if(!password.equals(repeatPass)){
            //password does not match repeat pass
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }

        //if validations are ok
        progressDialog.setMessage("Registering user...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user registered successfully
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(PopupSignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PopupSignUp.this, "Email already in use. Please try a different one", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignUp){
            userSignUp();
        }
    }
}
