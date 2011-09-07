package rest.client;

import rest.client.wrappers.TweetList;

import javax.ws.rs.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jerzy
 * Date: 28.06.11
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public interface Tweet {
    @POST
    @Path("/tweet")
    public String create(@QueryParam("sessionId") String sessionId, @QueryParam("content") String content);

    @GET
    @Path("/tweets/all")
    @Produces("application/xml")
    public TweetList all(@QueryParam("page") @DefaultValue("1") Integer page);

    @GET
    @Path("/tweets/{userId}")
    @Produces("application/xml")
    //@Wrapped(element = "tweets")
    public TweetList userTweets(@PathParam("userId") Long userId);


    @GET
    @Path("/tweets/followed")
    @Produces("application/xml")
    //@Wrapped(element = "tweets")
    public TweetList followingUsersTweets(@QueryParam("sessionId") String sessionId);
}
