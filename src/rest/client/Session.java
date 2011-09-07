package rest.client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by IntelliJ IDEA.
 * User: Jerzy
 * Date: 28.06.11
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
@Path("/session")
public interface Session {

    @POST
    @Path("/login")
    public String login(@QueryParam("login") String login, @QueryParam("passwordHash") String passwordHash);

    @POST
    @Path("/logout/{sessionId}")
    public String logout(@PathParam("sessionId") String sessionId);

    @Path("/valid/{sessionId}")
    @POST
    public Boolean isSessionValid(@PathParam("sessionId") String sessionId);

}
