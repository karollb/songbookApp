package pl.edu.wszib.songbookapp.services;

import android.content.Context;

import java.io.File;

public interface ISongService {

    void setSongListener(Context context);

    void shareSong(final Context context, final File song);
}
