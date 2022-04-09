package pl.edu.wszib.songbookapp.services.impl;



import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import pl.edu.wszib.songbookapp.models.User;
import pl.edu.wszib.songbookapp.services.IUserGoogleService;

public class UserGoogleServiceImpl implements IUserGoogleService {



    public User getLoggedInUser(final Context context) {
        GoogleSignInAccount googleSignIn = GoogleSignIn.getLastSignedInAccount(context);

        if (googleSignIn != null) {
            return new User(googleSignIn.getId(), googleSignIn.getGivenName(), googleSignIn.getFamilyName(), googleSignIn.getEmail(), "");
        }
        return null;
    }

    public boolean isLoggedUser(final Context context) {
        return getLoggedInUser(context) != null;
    }


}
