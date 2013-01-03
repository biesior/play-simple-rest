package models;


import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends Model {

    @Id
    public Integer id;
    public String name;
    public String nick;


    public static Finder<Integer, User> find
            = new Finder<Integer, User>(Integer.class, User.class);

    public User(String name, String nick) {
        this.name = name;
        this.nick = nick;
    }

    public User() {

    }
}
