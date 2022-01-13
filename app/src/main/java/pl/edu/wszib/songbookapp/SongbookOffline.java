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

public class SongbookOffline extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "message";

    private ArrayAdapter<String> arrayAdapter;

    private File directory;

    private TextView toolbarTextView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songbook_offline);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Intent intent = getIntent();

        setDirectory(intent.getStringExtra(EXTRA_MESSAGE));


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText(directory.getName());
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        ArrayList<String> fileNames = setFileList();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);

        ListView listView = findViewById(R.id.files_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (arrayAdapter.getItem(position).endsWith(".pdf")) {
                Intent openPDF = new Intent(this, PdfViewerOffline.class);
                openPDF.putExtra(PdfViewerOffline.EXTRA_MESSAGE, intent.getStringExtra(SongbookOffline.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                startActivity(openPDF);


            } else {
                Intent openDirectory = new Intent(SongbookOffline.this, SongbookOffline.class);
                openDirectory.putExtra(SongbookOffline.EXTRA_MESSAGE, intent.getStringExtra(SongbookOffline.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                startActivity(openDirectory);
            }
        });

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
                SongbookOffline.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }



        });

        return super.onCreateOptionsMenu(menu);
    }
}