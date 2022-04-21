package pl.edu.wszib.songbookapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.Objects;

import pl.edu.wszib.songbookapp.R;
import pl.edu.wszib.songbookapp.services.ISongService;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;
import pl.edu.wszib.songbookapp.services.impl.SongServiceImpl;
import pl.edu.wszib.songbookapp.services.impl.UserGoogleServiceImpl;

public class PdfViewer extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "message";

    private final IUserGoogleService userGoogleService = new UserGoogleServiceImpl();
    private final ISongService songService = new SongServiceImpl();


    private File song;
    public static boolean nightMode=true;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Intent intent = getIntent();

        song = new File(intent.getStringExtra(EXTRA_MESSAGE));


        setToolbar();


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
                .nightMode(nightMode) // toggle night mode
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
        getMenuInflater().inflate(R.menu.menu_pdf_viewer, menu);
        menu.findItem(R.id.share).setVisible(userGoogleService.isLoggedUser(getApplicationContext()));

        MenuItem changeThemeItem = menu.findItem(R.id.changeTheme);
        if (nightMode) {
            changeThemeItem.setIcon(R.drawable.ic_baseline_wb_sunny_24);
        } else {
            changeThemeItem.setIcon(R.drawable.ic_baseline_nights_stay_24);
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            songService.shareSong(getApplicationContext(), song);
        }

        if (item.getItemId() == R.id.changeTheme) {
            nightMode = !nightMode;
            recreate();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        nightMode = savedInstanceState.getBoolean("nightMode", true);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("nightMode", nightMode);
        super.onSaveInstanceState(outState);
    }
}