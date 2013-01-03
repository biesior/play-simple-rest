package controllers;

import models.User;


import play.data.Form;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

import views.html.*;
import views.html.editform;

import java.util.List;

public class Application extends Controller {

    /**
     * Displays home page
     *
     * @return Result
     */
    public static Result index() {
        List<User> users = User.find.all();
        return ok(index.render("Home page...", users));
    }

    /**
     * Displays new form
     *
     * @return Result
     */
    public static Result newForm() {
        Form<User> userForm = form(User.class).fill(new User());
        return ok(newform.render("Add new user", userForm));
    }

    /**
     * Displays edit form
     *
     * @return Result
     */
    public static Result editForm(int id) {
        Form<User> userForm = form(User.class).fill(User.find.byId(id));
        return ok(editform.render("Edit user", userForm, id));
    }




    /*
     *  API REQUESTS SIMULATIONS
     *
     */

    /**
     * Sends DELETE request
     *
     * @param id ID of user to delete
     * @return API's answer
     */
    public static Result simulateDeleteRequest(int id) {
        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.deleteUser(id).absoluteURL(request()).toString()).delete();

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }

    /**
     * Bind data from HTML form and send as params to the REST api
     *
     * @param id ID of user to update
     * @return API's answer
     */
    public static Result simulatePutRequest(int id) {

        Form<User> userForm = form(User.class);
        User user = userForm.bindFromRequest().get();

        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.updateUser(id).absoluteURL(request()).toString())
                .setQueryParameter("name", user.name)
                .setQueryParameter("nick", user.nick)
                .put("");

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }

    /**
     * Gets data of single user in JSON
     *
     * @param id ID of user
     * @return API's answer
     */
    public static Result simulateGetUserJson(int id) {
        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.user(id).absoluteURL(request()).toString())
                .setHeader(ACCEPT, "application/json")
                .get();

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }

    /**
     * Gets data of single user in XML
     *
     * @param id ID of user
     * @return API's answer
     */
    public static Result simulateGetUserXml(int id) {
        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.user(id).absoluteURL(request()).toString())
                .setHeader(ACCEPT, "application/xml")
                .get();

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }

    /**
     * Gets list of users in JSON
     *
     * @return API's answer
     */
    public static Result simulateListUsersJson() {
        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.users().absoluteURL(request()).toString())
                .setHeader(ACCEPT, "application/json")
                .get();

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }

    /**
     * Gets list of users in  XML
     *
     * @return API's answer
     */
    public static Result simulateListUsersXml() {
        F.Promise<WS.Response> wsCall = WS.url(routes.Rest.users().absoluteURL(request()).toString())
                .setHeader(ACCEPT, "application/xml")
                .get();

        WS.Response res = wsCall.get();
        return status(res.getStatus(), res.getBody()).as(res.getHeader("Content-Type"));
    }


}