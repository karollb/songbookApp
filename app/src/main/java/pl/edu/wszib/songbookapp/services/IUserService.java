package pl.edu.wszib.songbookapp.services;

import android.content.Context;

import pl.edu.wszib.songbookapp.models.User;

public interface IUserService {

    void addUserToFirebase(final User user);

    void updateUserInFirebase(final User user);

    void updateUserTeamName(final String userId, final String teamName);

    void startActivityIfUserHasTeam(final Context context, final String path);
}
