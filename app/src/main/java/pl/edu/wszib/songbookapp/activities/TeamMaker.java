package pl.edu.wszib.songbookapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import pl.edu.wszib.songbookapp.R;
import pl.edu.wszib.songbookapp.services.ITeamService;
import pl.edu.wszib.songbookapp.services.impl.TeamServiceImpl;

public class TeamMaker extends AppCompatActivity {

    private EditText teamNameView, teamPasswordView;

    private Button submitBtn;


    private final ITeamService teamService = new TeamServiceImpl();

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