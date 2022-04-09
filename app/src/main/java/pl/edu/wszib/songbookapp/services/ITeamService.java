package pl.edu.wszib.songbookapp.services;

import android.app.Activity;
import android.content.Context;

public interface ITeamService {

    void joinToTeam(final Context context, final Activity activity, final String teamName, final String teamPassword);

    void createTeam(final Context context, final Activity activity, final String teamName, final String teamPassword);

    void leaveTeam(final String teamName, final String userID, final Activity context);

}
