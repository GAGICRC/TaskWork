package pt.se_ulusofona.tarefeiros.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.backup.BackupHelper;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.se_ulusofona.tarefeiros.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        //after first opening, if registered, app enters with last user's last login credentials
        firebaseAuth = FirebaseAuth.getInstance();
        //check if user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);


        buttonLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);


        //invoke progress dialog
        progressDialog = new ProgressDialog(this);



    }
    private void userSignIn(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            //email is empty
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }

        if(TextUtils.isEmpty(password)){
            //email is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stop to execute further
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            if(activeNetwork == null){
                                Toast.makeText(LoginActivity.this, R.string.connect_internet, Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(LoginActivity.this, R.string.wrong_name_pass, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            userSignIn();
        }
        if (v == textViewRegister) {
            startActivity(new Intent(this, PopupSignUp.class));
        }
    }
}
