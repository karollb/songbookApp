package pl.edu.wszib.songbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.Objects;

import pl.edu.wszib.songbookapp.models.DedicationModel;
import pl.edu.wszib.songbookapp.services.ISetListService;
import pl.edu.wszib.songbookapp.services.impl.SetListServiceImpl;

public class AddSongToSetList extends AppCompatActivity {

    private final ISetListService setListService = new SetListServiceImpl();

    private EditText dedicationContent;
    private TextView songNameTextView;
    private Button selectSongBtn, submitBtn;


    private String songName, songPath;


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == 78) {
                        Intent intent = result.getData();

                        if (intent != null) {
                            String data = intent.getStringExtra("result");

                            File file = new File(Environment.getExternalStorageDirectory() + "/Spiewnik/" + data);

                            songName = file.getName();
                            songPath = data;
                            songNameTextView.setText(songName);
                        }
                    }

                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_set_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setToolbar();
        setViews();

        selectSongBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SelectSong.class);
            intent.putExtra(SelectSong.EXTRA_MESSAGE, Environment.getExternalStorageDirectory() + "/Spiewnik");
            activityResultLauncher.launch(intent);
        });


        submitBtn.setOnClickListener(v -> {
            if (songName == null) {
                Toast.makeText(getApplicationContext(), "Wybierz utwór który chcesz dodać do setlisty", Toast.LENGTH_SHORT).show();
            } else {
                DedicationModel dedicationModel = new DedicationModel("0", dedicationContent.getText().toString(), songName.replace(".pdf", ""), songPath);
                setListService.addSongToSetList(getApplicationContext(), dedicationModel);
                finish();
            }
        });


    }

    private void setViews() {
        this.dedicationContent = findViewById(R.id.dedicationContent);
        this.songNameTextView = findViewById(R.id.songName);
        this.selectSongBtn = findViewById(R.id.selectSong_btn);
        this.submitBtn = findViewById(R.id.submitDedication_btn);
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("DODAWANIE UTWORU DO SETLISTY");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }
}