package pl.edu.wszib.songbookapp.services.impl;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import pl.edu.wszib.songbookapp.TeamSetList;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;
import pl.edu.wszib.songbookapp.services.IUserService;

public class UserServiceImpl implements IUserService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");
    private final IUserGoogleService userGoogleService = new UserGoogleServiceImpl();

    public void addUserToFirebase(final User user) {
        DatabaseReference reference = database.getReference("Users");
        reference.child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    reference.child(user.getId()).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateUserInFirebase(final User user) {
        DatabaseReference reference = database.getReference("Users").child(user.getId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUserTeamName(final String userId, final String teamName) {
        DatabaseReference reference = database.getReference("Users").child(userId).child("teamName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.setValue(teamName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startActivityIfUserHasTeam(final Context context, final String path) {
        DatabaseReference reference = database.getReference("Users/" + Objects.requireNonNull(userGoogleService.getLoggedInUser(context)).getId());

        reference.child("teamName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.getValue(String.class), "")) {
                    Toast.makeText(context, "Nie należysz do żadnego zespołu", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, TeamSetList.class);
                    intent.putExtra(TeamSetList.EXTRA_MESSAGE, path);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
