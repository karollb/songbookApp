package pl.edu.wszib.songbookapp.services.impl;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.edu.wszib.songbookapp.models.Team;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.ITeamService;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;
import pl.edu.wszib.songbookapp.services.IUserService;

public class TeamServiceImpl implements ITeamService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");
    private final IUserGoogleService userGoogleService = new UserGoogleServiceImpl();

    private final IUserService userService = new UserServiceImpl();

    public void joinToTeam(final Context context, final Activity activity, final String teamName, final String teamPassword) {
        DatabaseReference reference = database.getReference("Teams").child(teamName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(context, "Podany zespół nie istnieje", Toast.LENGTH_SHORT).show();
                } else {
                    Team team = snapshot.getValue(Team.class);
                    assert team != null;
                    if (team.getTeamPassword().equals(teamPassword)) {
                        User user = userGoogleService.getLoggedInUser(context);
                        assert user != null;
                        user.setTeamName(teamName);
                        reference.child("Members").child(user.getId()).setValue(user);
                        userService.updateUserTeamName(user.getId(), teamName);
                        activity.finish();


                    } else {
                        Toast.makeText(context, "Błędne hasło", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void createTeam(final Context context, final Activity activity, final String teamName, final String teamPassword) {
        DatabaseReference teamRef = database.getReference().child("Teams").child(teamName);
        teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "Nazwa zespołu jest już zajęta", Toast.LENGTH_SHORT).show();
                } else {
                    Team team = new Team(teamName, teamPassword, "");
                    User user = userGoogleService.getLoggedInUser(context);
                    assert user != null;
                    user.setTeamName(teamName);
                    teamRef.setValue(team);
                    teamRef.child("Members").child(user.getId()).setValue(user);
                    userService.updateUserTeamName(user.getId(), teamName);
                    activity.finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void leaveTeam(final String teamName, final String userID, final Activity context) {

        userService.updateUserTeamName(userID, "");
        DatabaseReference teamRef = database.getReference("Teams").child(teamName).child("Members").child(userID);
        teamRef.removeValue().addOnCompleteListener(task -> {

            if (task.isComplete()) {
                context.recreate();
            }
        });


        removeTeamIfNoMembers(teamName);
    }


    private void removeTeamIfNoMembers(final String teamName) {
        DatabaseReference reference = database.getReference("Teams").child(teamName).child("Members");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    DatabaseReference myRef = database.getReference("Teams").child(teamName);
                    myRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
