package pl.edu.wszib.songbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.UserGoogleService;
import pl.edu.wszib.songbookapp.services.UserService;

public class UserProfile extends AppCompatActivity {

    private TextView givenNameView, familyNameView, mailView;
    private Button teamBtn, logoutBtn, songbookBtn;

    private final String path = Environment.getExternalStorageDirectory() + "/Spiewnik";

    private final UserGoogleService userGoogleService = new UserGoogleService();
    private final UserService userService = new UserService();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        setViews();
        setToolbar();

        User user = userGoogleService.getLoggedInUser(getApplicationContext());

        if (user != null) {
            userService.addUserToFirebase(user);

            this.givenNameView.setText(user.getGivenName());
            this.familyNameView.setText(user.getFamilyName());
            this.mailView.setText(user.getEmail());
        }

        songbookBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Songbook.class);
            intent.putExtra(Songbook.EXTRA_MESSAGE, path);
            startActivity(intent);
        });

        teamBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserTeam.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });


    }

    private void setViews() {
        this.givenNameView = findViewById(R.id.givenName_view);
        this.familyNameView = findViewById(R.id.lastName_view);
        this.mailView = findViewById(R.id.mail_view);
        this.teamBtn = findViewById(R.id.team_btn);
        this.logoutBtn = findViewById(R.id.logout_btn);
        this.songbookBtn = findViewById(R.id.songbook_btn);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("TWOJE KONTO");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }


}