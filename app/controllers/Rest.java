package controllers;

import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


import java.util.List;

public class Rest extends Controller {

    /**
     * Gets single user
     *
     * @param id ID of user
     * @return XML/JSON
     */
    public static Result user(int id) {

        User user = User.find.byId(id);
        if (user == null) return notFound("User not found!");

        if (request().accepts("application/xml")) {
            return ok(views.xml.user.render(user).body().trim()).as("application/xml");
        }

        return ok(Json.toJson(user));
    }

    /**
     * Gets list of users
     *
     * @return XML/JSON
     */
    public static Result users() {
        List<User> users = User.find.all();
        if (users.size() == 0) return notFound("Users list empty!");

        if (request().accepts("application/xml")) {
            return ok(views.xml.users.render(users).body().trim()).as("application/xml");

        }

        return ok(Json.toJson(users));
    }

    /**
     * Creates user
     *
     * @return Status + message
     */
    public static Result createUser() {
        User newUser = form(User.class).bindFromRequest().get();
        newUser.save();

        return created("User created");
    }

    /**
     * Updates user
     *
     * @param id ID of user
     * @return Status + message
     */
    public static Result updateUser(int id) {
        User user = User.find.byId(id);
        if (user == null) return notFound("User not found!");

        user = form(User.class).bindFromRequest().get();
        user.update(id);

        return status(202, "User modified");
    }

    /**
     * Deletes user
     *
     * @param id ID of user
     * @return Status + message
     */
    public static Result deleteUser(int id) {
        User user = User.find.byId(id);
        if (user == null) return notFound("User not found!");

        user.delete();

        return status(202, "User deleted");
    }

}
