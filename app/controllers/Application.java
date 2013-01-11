package controllers;

import models.User;
import play.data.Form;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.editform;
import views.html.index;
import views.html.newform;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static play.Logger.*;

public class Application extends Controller {

    /**
     * Displays home page
     *
     * @return Result
     */
    public static Result index() {

        if (forwardedFromHead()) {
            debug("This request was forwarded from HEAD to GET");
        } else {
            debug("This is direct call");
        }


        List<User> users = User.find.all();
        response().setHeader(LAST_MODIFIED, new Date().toString());
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


    /**
     * Tries to get headers of resource with WebServices and return headers only.
     *
     * @param originalPath Path of the resource
     * @return Headers for HEAD request
     * @throws IllegalAccessException
     */
    public static Result autoHead(String originalPath) throws IllegalAccessException {

        WS.WSRequestHolder forwardedRequest = WS.url("http://" + request().host() + request().path());
        // this header will allow you to make additional choice i.e. avoid tracking the request or something else
        // see condition in index() action
        forwardedRequest.setHeader("X_FORWARD_FROM_HEAD", "true");

        // Forward original headers
        for (String header : request().headers().keySet()) {
            forwardedRequest.setHeader(header, request().getHeader(header));
        }

        // Forward original queryString
        for (String key : request().queryString().keySet()) {
            for (String val : request().queryString().get(key)) {
                forwardedRequest.setQueryParameter(key, val);
            }
        }

        // Call the same path but with GET
        WS.Response wsResponse = forwardedRequest.get().get();

        // Set returned headers to the response
        for (Field f : Http.HeaderNames.class.getFields()) {
            String headerName = f.get(null).toString();
            if (wsResponse.getHeader(headerName) != null) {
                response().setHeader(headerName, wsResponse.getHeader(headerName));
            }
        }

        return status(wsResponse.getStatus());
    }

    /**
     * Checks if request if forwarded from HEAD request
     * @return true if 'X_FORWARD_FROM_HEAD' header exists and is set to true
     */
    public static boolean forwardedFromHead() {
        return (request().getHeader("X_FORWARD_FROM_HEAD") != null && "true".equals(request().getHeader("X_FORWARD_FROM_HEAD")));
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