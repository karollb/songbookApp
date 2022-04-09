package pl.edu.wszib.songbookapp.services;

import android.content.Context;

import pl.edu.wszib.songbookapp.models.User;

public interface IUserGoogleService {

    User getLoggedInUser(final Context context);

    boolean isLoggedUser(final Context context);

}
