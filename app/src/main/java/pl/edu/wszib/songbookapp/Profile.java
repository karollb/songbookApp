package pl.edu.wszib.songbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Profile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("TWOJE KONTO");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        TextView lastName = findViewById(R.id.last_name_view);
        TextView name = findViewById(R.id.name_view);
        TextView mail = findViewById(R.id.mail_view);
        Button logoutBtn = findViewById(R.id.logout_btn);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);




        if (signInAccount != null) {
            lastName.setText(signInAccount.getFamilyName());
            name.setText(signInAccount.getGivenName());
            mail.setText(signInAccount.getEmail());

        }

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
//            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(logout);
            finish();
        });


    }
}