package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtEmail = findViewById(R.id.edtEnterEmail);
        edtPassword = findViewById(R.id.edtEnterPassword);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode ==KeyEvent.KEYCODE_ENTER &&
                event.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnSignUp);

                }

                return false;
            }


        });
        edtUsername = findViewById(R.id.edtUsername);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){

            transitionToTwitterUsers();
        }

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){


                case R.id.btnSignUp:
                    if(edtEmail.getText().toString().equals("")||edtUsername.getText().toString().equals("")||edtPassword.getText().toString().equals("")){

                        FancyToast.makeText(this,"Email, Username, Password is required!",FancyToast.LENGTH_LONG,FancyToast.INFO,true);
                    }else {

                        final ParseUser parseUser = new ParseUser();
                        parseUser.setEmail(edtEmail.getText().toString());
                        parseUser.setUsername(edtUsername.getText().toString());
                        parseUser.setPassword(edtPassword.getText().toString());

                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage(edtUsername.getText().toString() + " is signing up");
                        progressDialog.show();
                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(MainActivity.this, parseUser.getUsername() + " is signed up", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                } else {
                                    FancyToast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                                transitionToTwitterUsers();
                                progressDialog.dismiss();
                            }

                        });


                    }
                    break;
            case R.id.btnLogIn:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }

    }

    public void rootLayoutIsTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void transitionToTwitterUsers(){
        Intent intent = new Intent(MainActivity.this,TwitterUsers.class);
        startActivity(intent);
    }
}
