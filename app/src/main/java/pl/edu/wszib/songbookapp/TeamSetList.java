package pl.edu.wszib.songbookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.edu.wszib.songbookapp.adapters.DedicationAdapter;
import pl.edu.wszib.songbookapp.models.DedicationModel;
import pl.edu.wszib.songbookapp.services.SetListService;
import pl.edu.wszib.songbookapp.services.UserGoogleService;

public class TeamSetList extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "message";

    private final UserGoogleService userGoogleService = new UserGoogleService();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://songbookapp-d0156-default-rtdb.europe-west1.firebasedatabase.app/");

    private final SetListService setListService = new SetListService();


    private List<DedicationModel> dedicationModelList;

    private ListView listView;

    private DedicationAdapter adapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_set_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setToolbar();

        listView = findViewById(R.id.setList_view);
        dedicationModelList = new ArrayList<>();


        setListView(this);


        registerForContextMenu(listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), PdfViewer.class);
            intent.putExtra(PdfViewer.EXTRA_MESSAGE, Environment.getExternalStorageDirectory() + "/Spiewnik/" + adapter.getItem(position).getSongPath());
            startActivity(intent);

        });

    }

    private void setListView(Activity activity) {


        adapter = new DedicationAdapter(this, dedicationModelList);

        DatabaseReference reference = database.getReference("Users/" + userGoogleService.getLoggedInUser(getApplicationContext()).getId() + "/teamName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String teamName = snapshot.getValue(String.class);

                DatabaseReference setListRef = database.getReference("Teams/" + teamName + "/teamSetList");
                setListRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        dedicationModelList.add(snapshot.getValue(DedicationModel.class));
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        dedicationModelList.remove(snapshot.getValue(DedicationModel.class));
                        adapter.notifyDataSetChanged();
                        activity.recreate();


                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTextView = toolbar.findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("DEDYKACJE / SETLISTA");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.setList_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.set_list_context_menu, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.delete) {
            DedicationModel dedicationModel = adapter.getItem(info.position);
            setListService.removeSongFromSetList(getApplicationContext(), dedicationModel.getId());

            return true;
        }
        return super.onContextItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_list, menu);
        menu.findItem(R.id.add_song_to_setList).setVisible(userGoogleService.isLoggedUser(getApplicationContext()));


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_song_to_setList) {
            Intent intent = new Intent(getApplicationContext(), AddSongToSetList.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }

}