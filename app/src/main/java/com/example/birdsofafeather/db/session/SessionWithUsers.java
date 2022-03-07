package com.example.birdsofafeather.db.session;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.List;

public class SessionWithUsers {
    @Embedded
    private Session session;

    @Relation(parentColumn = "id",
            entityColumn = "id",
            entity = User.class
    )
    private List<User> users;

    public void setSession(Session session) {
        this.session = session;
    }

    public void setSessionName(String name) {
        this.session.setName(name);
    }

    public String getSessionName() {
        return this.session.name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Session getSession() {
        return this.session;
    }

    public List<User> getUsers() {
       return this.users;
    }

    public void addUsers(List<User> users) {
        this.users.addAll(users);
    }


}
