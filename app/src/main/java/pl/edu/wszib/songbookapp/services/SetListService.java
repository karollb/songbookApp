package pl.edu.wszib.songbookapp.services;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import pl.edu.wszib.songbookapp.adapters.DedicationAdapter;
import pl.edu.wszib.songbookapp.models.DedicationModel;

public class SetListService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");
    private final UserGoogleService userGoogleService = new UserGoogleService();


    public void addSongToSetList(final Context context, final DedicationModel dedicationModel) {
        DatabaseReference reference = database.getReference("Users/" + Objects.requireNonNull(userGoogleService.getLoggedInUser(context)).getId() + "/teamName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String teamName = snapshot.getValue(String.class);

                DatabaseReference teamRef = database.getReference("Teams/" + teamName + "/teamSetList");
                teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long childrenCount = snapshot.getChildrenCount();
                        dedicationModel.setId(Long.toString(childrenCount));
                        teamRef.child(dedicationModel.getId()).setValue(dedicationModel);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void removeSongFromSetList(final Context context, final String dedicationId) {
        DatabaseReference reference = database.getReference("Users/" + Objects.requireNonNull(userGoogleService.getLoggedInUser(context)).getId() + "/teamName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String teamName = snapshot.getValue(String.class);

                DatabaseReference teamRef = database.getReference("Teams/" + teamName + "/teamSetList/" + dedicationId);
                teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        teamRef.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

