package com.example.birdsofafeather.db.session;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {

    // will autogenerate key when added to DB (insert method will return long id)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    long id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "filters")
    String[] filters;

    @ColumnInfo(name = "sort_options")
    String[] sort_options;

    @ColumnInfo(name = "nearby_users")
    String[] nearby_users;
}
