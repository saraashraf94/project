package com.example.rizk.firebaseexamplelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends Activity {
    MaterialEditText etUname, etPwd;
    ActionProcessButton btnLogin;
    TextView tvReg;
    FirebaseAuth lAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUname = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btnLogin);
        tvReg = findViewById(R.id.tv_register);
        lAuth = FirebaseAuth.getInstance();
        currentUser = lAuth.getCurrentUser();
//        if(currentUser!=null){
//            finish();
//            startActivity(new Intent(LoginActivity.this,AddTrip.class));
//        }
        String udata = "Register here";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
        tvReg.setText(content);
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regster = new Intent(LoginActivity.this, Register.class);
                startActivity(regster);
                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lAuth.signInWithEmailAndPassword(etUname.getText().toString(), etPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            if (user != null) {
                                Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(LoginActivity.this, ListActivity.class);
                                startActivity(home);
                                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }

                        } else {

                            Toast.makeText(LoginActivity.this, "emad" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
