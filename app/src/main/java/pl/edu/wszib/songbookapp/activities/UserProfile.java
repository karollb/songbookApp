package pl.edu.wszib.songbookapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import pl.edu.wszib.songbookapp.R;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.ISongService;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;
import pl.edu.wszib.songbookapp.services.IUserService;
import pl.edu.wszib.songbookapp.services.impl.SongServiceImpl;
import pl.edu.wszib.songbookapp.services.impl.UserGoogleServiceImpl;
import pl.edu.wszib.songbookapp.services.impl.UserServiceImpl;

public class UserProfile extends AppCompatActivity {

    private TextView givenNameView, familyNameView, mailView;
    private Button teamBtn, logoutBtn, songbookBtn, setListBtn;

    private final String path = Environment.getExternalStorageDirectory() + "/Spiewnik";

    private final IUserGoogleService userGoogleService = new UserGoogleServiceImpl();
    private final IUserService userService = new UserServiceImpl();
    private final ISongService songService = new SongServiceImpl();


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

        //  songService.setSongListener(getApplicationContext());
        if (userGoogleService.isLoggedUser(getApplicationContext())) {
            songService.setSongListener(getApplicationContext());
        }

        songbookBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Songbook.class);
            intent.putExtra(Songbook.EXTRA_MESSAGE, path);
            startActivity(intent);
        });

        setListBtn.setOnClickListener(v -> {
            userService.startActivityIfUserHasTeam(getApplicationContext(), path);
//                Intent intent = new Intent(getApplicationContext(), TeamSetList.class);
//                intent.putExtra(TeamSetList.EXTRA_MESSAGE, path);
//                startActivity(intent);
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

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }

    private void setViews() {
        this.givenNameView = findViewById(R.id.givenName_view);
        this.familyNameView = findViewById(R.id.lastName_view);
        this.mailView = findViewById(R.id.mail_view);
        this.teamBtn = findViewById(R.id.team_btn);
        this.logoutBtn = findViewById(R.id.logout_btn);
        this.songbookBtn = findViewById(R.id.songbook_btn);
        this.setListBtn = findViewById(R.id.setlist_btn);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("TWOJE KONTO");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }


}