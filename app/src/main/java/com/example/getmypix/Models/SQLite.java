package com.example.getmypix.Models;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.getmypix.GetMyPixApplication;

import java.util.Date;

@Database(entities = {Posts.class}, version = 23)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostsDao postsDao();
}
public class SQLite {

    static  public AppLocalDbRepository db = Room.databaseBuilder(GetMyPixApplication.getContext(),
            AppLocalDbRepository.class,
            "getmypix.db")
            .fallbackToDestructiveMigration()
            .build();
}

class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}