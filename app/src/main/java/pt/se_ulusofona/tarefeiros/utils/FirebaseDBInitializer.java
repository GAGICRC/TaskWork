package pt.se_ulusofona.tarefeiros.utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JoãoPedro on 01/07/2017.
 */

public class FirebaseDBInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
