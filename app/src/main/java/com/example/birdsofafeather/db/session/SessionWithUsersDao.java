package com.example.birdsofafeather.db.session;


import static androidx.room.OnConflictStrategy.REPLACE;

import android.widget.TextView;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class SessionWithUsersDao {

    public long insertSession(Session session)  {
       return _insertSession(session);
    }

    public void addUsersToSession(long sessionId, List<User> users) {
        for(User user : users) {
           user.setSessionId(sessionId);
           insertUser(user);
        }
    }

//    public List<User> getUsersForSessionId(long sessionId) {
//         SessionWithUsers session = getForId(sessionId);
//         return session.getUsers();
//    }

    @Query("SELECT * FROM sessions WHERE id=:sessionId")
    public abstract SessionWithUsers getForId(long sessionId);

    @Query("SELECT * FROM sessions")
    public abstract List<SessionWithUsers> getAllSessions();


    @Query("SELECT * FROM users WHERE sessionId=:sessionId")
    public abstract List<User> getUsersForSessionId(long sessionId);

    //returns the autogenerated id
    @Insert(onConflict = REPLACE)
    abstract long _insertSession(Session session);

    @Insert(onConflict = REPLACE)
    abstract void insertUsers(List<User> userWithCourses);

    @Insert(onConflict = REPLACE)
    abstract void insertUser(User user);

    @Delete
    abstract void delete(Session session);

    @Update
    abstract void _update(Session session);

}
