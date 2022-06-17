package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText usernameTF;
    private EditText passwordTF;
    private Button loginButton;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));


        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        } else {
            setContentView(binding.getRoot());
        }


        usernameTF = binding.usernameTextField;
        passwordTF = binding.passwordTextField;
        loginButton = binding.loginButton;
        progressBar = binding.loginProgressBar;;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTF.getText().toString();
                String password = passwordTF.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password){
        showProgressBar();
        Log.i(TAG, "Attempting to log in user: "+username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                hideProgressBar();
                if (e != null){
                    Log.e(TAG, "issue with login: "+e.getMessage(), e);
                    Toast.makeText(LoginActivity.this, "Issue with login: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, "Success!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}