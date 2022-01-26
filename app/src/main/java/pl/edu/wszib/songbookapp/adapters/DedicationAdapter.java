package pl.edu.wszib.songbookapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.edu.wszib.songbookapp.R;
import pl.edu.wszib.songbookapp.models.DedicationModel;

public class DedicationAdapter extends ArrayAdapter<DedicationModel> {


    public DedicationAdapter(@NonNull Context context, @NonNull List<DedicationModel> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DedicationModel dedicationModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_team_set_list, parent, false);
        }

        TextView dedicationContent = convertView.findViewById(R.id.dedicationContentText);
        TextView songName = convertView.findViewById(R.id.songNameText);

        dedicationContent.setText("Dedykacja: "+dedicationModel.getDedicationContent());
        songName.setText(dedicationModel.getSongName());


        return convertView;
    }
}
