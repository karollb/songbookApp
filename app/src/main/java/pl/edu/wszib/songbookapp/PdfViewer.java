package pl.edu.wszib.songbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Objects;

import pl.edu.wszib.songbookapp.models.Team;
import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.SongService;
import pl.edu.wszib.songbookapp.services.UserGoogleService;

public class PdfViewer extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "message";

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");

    private final UserGoogleService userGoogleService = new UserGoogleService();

    private final SongService songService = new SongService();


    private File song;
    Intent intent;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        intent = getIntent();

        song = new File(intent.getStringExtra(EXTRA_MESSAGE));

        setToolbar();

        if (userGoogleService.isLoggedUser(getApplicationContext())) {
            songService.setSongListener(getApplicationContext());
        }




        PDFView pdfView = findViewById(R.id.pdfViewer);

        pdfView.fromFile(song)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(true) // toggle night mode
                .load();

    }



    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText(song.getName().substring(0, song.getName().length() - 4));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userGoogleService.isLoggedUser(getApplicationContext())) {
            getMenuInflater().inflate(R.menu.menu_share, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {

            DatabaseReference userRef = database.getReference("Users/" + Objects.requireNonNull(userGoogleService.getLoggedInUser(getApplicationContext())).getId());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    if (user.getTeamName().equals("")) {
                        Toast.makeText(getApplicationContext(), "Nie należysz do żadnego zespołu", Toast.LENGTH_SHORT).show();

                    } else {
                        DatabaseReference teamRef = database.getReference("Teams/" + user.getTeamName());
                        teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Team team = snapshot.getValue(Team.class);

                                    if (Objects.requireNonNull(song.getParentFile()).getName().equals("Spiewnik")) {
                                        assert team != null;
                                        team.setSongName(song.getName());
                                    } else {
                                        assert team != null;
                                        team.setSongName(song.getParentFile().getName() + "/" + song.getName());
                                    }
                                    teamRef.child("songName").setValue(team.getSongName());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        return super.onOptionsItemSelected(item);
    }


}