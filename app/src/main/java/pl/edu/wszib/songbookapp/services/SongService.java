package pl.edu.wszib.songbookapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Objects;

import pl.edu.wszib.songbookapp.PdfViewer;

public class SongService {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");
    private final UserGoogleService userGoogleService = new UserGoogleService();

    public void setSongListener(Context context) {
        DatabaseReference reference = database.getReference("Users/" + Objects.requireNonNull(userGoogleService.getLoggedInUser(context)).getId());
        reference.child("teamName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String teamName = snapshot.getValue(String.class);

                        DatabaseReference teamRef = database.getReference("Teams/" + teamName);
                        teamRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (Objects.equals(snapshot.getKey(), "songName")) {
                                    String songName = snapshot.getValue(String.class);

                                    File file = new File(Environment.getExternalStorageDirectory() + "/Spiewnik/" + songName);
                                    if (!file.exists()) {
                                        Toast.makeText(context, "Użytkownik próbował udostępnić plik, którego nie posiadasz, lub masz go w innej lokalizacji", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(context, PdfViewer.class);
                                        intent.putExtra(PdfViewer.EXTRA_MESSAGE, Environment.getExternalStorageDirectory() + "/Spiewnik/" + songName);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }
                                }


                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
