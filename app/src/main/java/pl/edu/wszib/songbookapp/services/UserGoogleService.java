package pl.edu.wszib.songbookapp.services;



import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import pl.edu.wszib.songbookapp.models.User;

public class UserGoogleService {



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
