package pl.edu.wszib.songbookapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import pl.edu.wszib.songbookapp.models.Team;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.TeamService;
import pl.edu.wszib.songbookapp.services.UserGoogleService;

public class TeamMaker extends AppCompatActivity {

    private EditText teamNameView, teamPasswordView;

    private Button submitBtn;


    private final TeamService teamService = new TeamService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_maker);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        setToolbar();
        setViews();

        submitBtn.setOnClickListener(v -> {
            String teamName = teamNameView.getText().toString();
            String teamPassword = teamPasswordView.getText().toString();

            if (teamName.isEmpty() || teamPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Wypełnij nazwę zespołu i hasło", Toast.LENGTH_SHORT).show();
            } else {
                teamService.createTeam(getApplicationContext(), this, teamName, teamPassword);
            }
        });
    }


    private void setViews() {
        this.teamNameView = findViewById(R.id.createTeamName);
        this.teamPasswordView = findViewById(R.id.createTeamPassword);
        this.submitBtn = findViewById(R.id.createBtn);

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("STWÓRZ ZESPÓŁ");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }
}