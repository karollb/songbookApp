package pl.edu.wszib.songbookapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import pl.edu.wszib.songbookapp.R;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.ITeamService;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;
import pl.edu.wszib.songbookapp.services.impl.TeamServiceImpl;
import pl.edu.wszib.songbookapp.services.impl.UserGoogleServiceImpl;

public class UserTeam extends AppCompatActivity {

    private TextView teamTextView;
    private Button leaveBtn, teamCreateBtn, teamJoinBtn;



    private String userID;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");

    private final IUserGoogleService userGoogleService = new UserGoogleServiceImpl();
    private final ITeamService teamService = new TeamServiceImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_team);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        User user = userGoogleService.getLoggedInUser(getApplicationContext());


        if (user != null) {
            userID = user.getId();
        }

        setToolbar();
        setViews();
        updateUi(userID);


        teamCreateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TeamMaker.class);
            startActivity(intent);
        });

        teamJoinBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TeamJoin.class);
            startActivity(intent);
        });


        leaveBtn.setOnClickListener(v -> {


            String teamName = teamTextView.getText().toString();
            teamService.leaveTeam(teamName, userID, this);

        });

    }

    private void updateUi(String userID) {
        DatabaseReference reference = database.getReference().child("Users").child(userID).child("teamName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.getValue(), "")) {
                    teamTextView.setText("Dołącz do zespołu lub stwórz zespół");
                    leaveBtn.setVisibility(View.GONE);
                    teamCreateBtn.setVisibility(View.VISIBLE);
                    teamJoinBtn.setVisibility(View.VISIBLE);
                } else {
                    teamTextView.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                    leaveBtn.setVisibility(View.VISIBLE);
                    teamCreateBtn.setVisibility(View.GONE);
                    teamJoinBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setViews() {
        this.teamTextView = findViewById(R.id.teamTextView);
        this.leaveBtn = findViewById(R.id.leave_btn);
        this.teamCreateBtn = findViewById(R.id.teamCreate_btn);
        this.teamJoinBtn = findViewById(R.id.teamJoin_btn);


    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("TWÓJ ZESPÓŁ");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }


}