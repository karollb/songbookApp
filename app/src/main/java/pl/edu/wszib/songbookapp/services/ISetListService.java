package pl.edu.wszib.songbookapp.services;

import android.content.Context;

import pl.edu.wszib.songbookapp.models.DedicationModel;

public interface ISetListService {

    void addSongToSetList(final Context context, final DedicationModel dedicationModel);

    void removeSongFromSetList(final Context context, final String dedicationId);
}
