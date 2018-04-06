package com.example.rizk.firebaseexamplelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Register extends Activity {
    MaterialEditText etEmail, etFname, etLname, etPwd, etRepwd;
    ActionProcessButton btnReg;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    LoginButton loginButton;
    DatabaseReference databaseReference;
    private static final String EMAIL = "email";
    final static String TAG = "";
    String[] names;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.et_email);
        etFname = findViewById(R.id.fName);
        etLname = findViewById(R.id.lName);
        etPwd = findViewById(R.id.pwd);
        etRepwd = findViewById(R.id.rePwd);
        callbackManager = CallbackManager.Factory.create();
        btnReg = findViewById(R.id.btnReg);

        mAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPwd.getText().toString().equals(etRepwd.getText().toString())) {
                    register();
                } else
                    Toast.makeText(Register.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
            }
        });


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity", response.toString());

                                try {
                                    email = object.getString("email");
                                    String name = object.getString("name");
                                    names = name.split(" ");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    RegisterModel fbData = new RegisterModel(email, names[0], names[1], "123456");
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                    databaseReference.push().setValue(fbData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent homeIntent = new Intent(Register.this, AddTrip.class);
                startActivity(homeIntent);
                Register.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Register.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", exception.getMessage());
            }

        });
    }


    public void register() {
        final String stEmail = etEmail.getText().toString();
        final String stFirstname = etFname.getText().toString();
        final String stLastname = etLname.getText().toString();
        final String stPassword = etPwd.getText().toString();

        mAuth.createUserWithEmailAndPassword(stEmail, stPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = task.getResult().getUser();
                    if (user != null) {
                        mAuth.signInWithEmailAndPassword(stEmail, stPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                    databaseReference = databaseReference.child(user.getUid());
                                    RegisterModel saveData = new RegisterModel(stEmail, stFirstname, stLastname, stPassword);
                                    databaseReference.push().setValue(saveData);

                                    Intent homeIntent = new Intent(Register.this, AddTrip.class);
                                    startActivity(homeIntent);
                                    Register.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
