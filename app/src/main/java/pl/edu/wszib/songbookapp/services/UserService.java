package pl.edu.wszib.songbookapp.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.edu.wszib.songbookapp.models.User;

public class UserService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");

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
}
