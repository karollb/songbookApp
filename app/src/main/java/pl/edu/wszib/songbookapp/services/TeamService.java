package pl.edu.wszib.songbookapp.services;

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

public class TeamService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");
    private final UserGoogleService userGoogleService = new UserGoogleService();

    private final UserService userService = new UserService();

    public void joinToTeam(final Context context,final Activity activity ,final String teamName, final String teamPassword) {
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
                        userService.updateUserTeamName(user.getId(),teamName);
                        //userService.updateUserInFirebase(user);
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
                    userService.updateUserTeamName(user.getId(),teamName);
                   // userService.updateUserInFirebase(user);
                    activity.finish();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void leaveTeam(final String teamName, final String userID) {

        DatabaseReference teamRef = database.getReference("Teams").child(teamName).child("Members").child(userID);
        teamRef.removeValue();
        removeTeamIfNoMembers(teamName);

        userService.updateUserTeamName(userID, "");



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
