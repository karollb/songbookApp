package pl.edu.wszib.songbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import pl.edu.wszib.songbookapp.services.SongService;
import pl.edu.wszib.songbookapp.services.UserGoogleService;

public class Songbook extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "message";

    private final UserGoogleService userGoogleService = new UserGoogleService();
    private final SongService songService = new SongService();

    private Intent intent;

    private ArrayAdapter<String> arrayAdapter;

    private File directory;

    private TextView toolbarTextView;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songbook);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        intent = getIntent();

        setDirectory(intent.getStringExtra(EXTRA_MESSAGE));

        setToolbar();

        if (userGoogleService.isLoggedUser(getApplicationContext())) {
            songService.setSongListener(getApplicationContext());
        }


        ArrayList<String> fileNames = setFileList();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);

        setListView();


    }

    private void setDirectory(String path) {
        this.directory = new File(path);
    }

    private ArrayList<String> setFileList() {
        ArrayList<String> arrayList = new ArrayList<>();


        File[] files = this.directory.listFiles();

        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            arrayList.add(files[i].getName());
        }
        Collections.sort(arrayList, String.CASE_INSENSITIVE_ORDER);

        return arrayList;
    }

    private void setListView() {

        ListView listView = findViewById(R.id.files_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (arrayAdapter.getItem(position).endsWith(".pdf")) {
                Intent openPDF = new Intent(this, PdfViewer.class);
                openPDF.putExtra(PdfViewer.EXTRA_MESSAGE, intent.getStringExtra(Songbook.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                startActivity(openPDF);


            } else {
                Intent openDirectory = new Intent(Songbook.this, Songbook.class);
                openDirectory.putExtra(Songbook.EXTRA_MESSAGE, intent.getStringExtra(Songbook.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                startActivity(openDirectory);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnSearchClickListener(v -> toolbarTextView.setVisibility(View.GONE));

        searchView.setOnCloseListener(() -> {
            toolbarTextView.setVisibility(View.VISIBLE);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Songbook.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }


        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText(directory.getName());
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }


}