package com.example.tahir.owlchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private TextView txt_email;
    private TextView txt_password;
    private TextView btn_new_account;
    private Button btn_login;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btn_new_account=(TextView) findViewById(R.id.btn_new_account);
        btn_login=(Button) findViewById(R.id.btn_login);
        txt_email=(TextView) findViewById(R.id.txt_email) ;
        txt_password=(TextView) findViewById(R.id.txt_password);
        auth = FirebaseAuth.getInstance();



        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(register_intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txt_email.getText().toString();
                String password=txt_password.getText().toString();
                LoginUser(email,password);
            }
        });

    }

    public  void LoginUser(String email,String password)
    {
        if(TextUtils.isEmpty(email))
        {
            txt_email.setError("Email Adresini Kontrol Ediniz");
        }
        else if(TextUtils.isEmpty(password))
        {
            txt_password.setError("Şifrenizi Kontrol Ediniz");
        }else {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   Intent intent=new Intent(StartActivity.this, MainActivity.class) ;
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                   finish();

                }
                else {
                    Toast.makeText(StartActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }
}
