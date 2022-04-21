package pl.edu.wszib.songbookapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import pl.edu.wszib.songbookapp.R;

public class SelectSong extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "message";

    private Intent intent;

    private ArrayAdapter<String> arrayAdapter;

    private File directory;

    private String dirName;

    private TextView toolbarTextView;

    private static int resultCode = 78;


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == 80) {
                    Intent intent = result.getData();

                    if (intent != null) {
                        String data = intent.getStringExtra("result");
                        Intent selectSong = new Intent();
                        selectSong.putExtra("result", data);
                        setResult(78, selectSong);
                        finish();
                    }
                }

            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song);
        intent = getIntent();

        setDirectory(intent.getStringExtra(EXTRA_MESSAGE));

        setToolbar();


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

        ListView listView = findViewById(R.id.files_list_view_2);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (arrayAdapter.getItem(position).endsWith(".pdf")) {
                Intent selectSong = new Intent();
                File file = new File(intent.getStringExtra(SelectSong.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                if (Objects.requireNonNull(file.getParentFile()).getName().equals("Spiewnik")) {
                    selectSong.putExtra("result", file.getName());
                } else {
                    selectSong.putExtra("result", file.getParentFile().getName() + "/" + file.getName());
                }
                setResult(resultCode, selectSong);
                resultCode = 78;
                finish();

            } else {
                dirName = arrayAdapter.getItem(position);
                Intent openDirectory = new Intent(SelectSong.this, SelectSong.class);
                openDirectory.putExtra(SelectSong.EXTRA_MESSAGE, intent.getStringExtra(SelectSong.EXTRA_MESSAGE) + "/" + arrayAdapter.getItem(position));
                resultCode = 80;
                activityResultLauncher.launch(openDirectory);

            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        resultCode = savedInstanceState.getInt("resultCode", 78);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("resultCode", resultCode);
        super.onSaveInstanceState(outState);
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
                SelectSong.this.arrayAdapter.getFilter().filter(newText);
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