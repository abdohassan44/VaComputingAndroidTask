package com.example.vacomputingtask;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Equation.class}, version = 1, exportSchema = false)
abstract class EquationٍRoomDatabase extends RoomDatabase {


    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile EquationٍRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                EquationDao dao = INSTANCE.equationDao();

            });
        }
    };

    static EquationٍRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EquationٍRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EquationٍRoomDatabase.class, "equation_table")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    abstract EquationDao equationDao();

}
