package com.example.mateologist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerify extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        auth = FirebaseAuth.getInstance();
        FirebaseUser User = auth.getCurrentUser();
        toastEmailOpen();
    }

    private void toastEmailOpen() {
        Toast.makeText(EmailVerify.this, R.string.if_open_email, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            openEmail();
        }, 2000);
        new CountDownTimer(Integer.MAX_VALUE, 3000) {
            public void onTick(long millisUntilFinished) {
                Intent intent = getIntent();
                String password = "";
                if(intent!=null){
                    password = intent.getStringExtra("Password");
                }
                auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                Toast.makeText(EmailVerify.this, "Task is successful", Toast.LENGTH_SHORT).show();
                if (auth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(EmailVerify.this, "Is verified", Toast.LENGTH_SHORT).show();
                    cancel();
                    startActivity(new Intent(EmailVerify.this, LoginActivity.class));
                    finish();
                }
            }

                });

    }
            public void onFinish() {
            }
        }.start();

    }
    private void openEmail(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.if_open_email)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    finish();
                    Uri webpage = Uri.parse("https://mail.google.com/");
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(Intent.createChooser(webIntent, "Email"));
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                    dialog.cancel();
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.if_open_email);
        alert.show();
    }
}
