package com.example.tahir.owlchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    EditText Username,Email,Password;
    Button   btn_register;
    private ProgressDialog progressdialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Username=findViewById(R.id.txt_username);
        Email=findViewById(R.id.txt_email);
        Password=findViewById(R.id.txt_password);
        btn_register=findViewById(R.id.btn_register);
        firebaseAuth = FirebaseAuth.getInstance();

        progressdialog=new ProgressDialog(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                register_user(username,email,password);

            }
        });
    }

    private  void register_user(String username,String email,String password)
    {
        if(TextUtils.isEmpty(username)){
            Username.setError("Geçerli bir kullanıcı Adı Girin");
        }
        else if(TextUtils.isEmpty(email))
            {
                Email.setError("Geçerli Bir Email Adresi Girin");
            }
        else if(TextUtils.isEmpty(password) ||password.length()<6 )
        {
            Password.setError("En az 6 Karakterli Geçerli Bir Şifre Girin");
        }
        else
        {

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser current_user=firebaseAuth.getInstance().getCurrentUser();
                            String User_id=current_user.getUid();
                            // Write a message to the database
                            DatabaseReference Database= FirebaseDatabase.getInstance().getReference().child("Users").child(User_id);

                            HashMap<String,String>userMap=new HashMap<>();
                            userMap.put("name",username);
                            userMap.put("status","Merhabaaa");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            Database.setValue(userMap);

                            Database.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        progressdialog.setTitle("Yeni Kayıt");
                                        progressdialog.setTitle("Hesabınız Oluşurken Lütfen Bekleyin");
                                        progressdialog.setCanceledOnTouchOutside(false);
                                        progressdialog.show();
                                        progressdialog.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
                        else{
                            progressdialog.hide();
                            Toast.makeText(getApplicationContext(),"Kayıt Olunamadı Lütfen tekrar Deneyin",Toast.LENGTH_SHORT).show();

                        }
                    }
        });
    }}

}
