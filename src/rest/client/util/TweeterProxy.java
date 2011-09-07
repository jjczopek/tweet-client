package rest.client.util;

import org.jboss.resteasy.client.ProxyFactory;
import rest.client.Session;
import rest.client.Tweet;

/**
 * Class for holding references to proxies for accessing tweeter application RESTful API
 */
public class TweeterProxy {

    private final Session sessionProxy;
    private final Tweet tweetProxy;

    /**
     * Creates new instacne of TweeterProxy, initializing proxies for tweeter application.
     * Url is composed of the application url and {@code /rest} suffix, which points to the REST api of tweeter application,
     * e.g. {@code http://localhost:9000/rest}
     * Proxies are created, so they point to the URL given as the parameter.
     *
     * @param URL url of tweeter application
     */
    public TweeterProxy(String URL) {
        this.tweetProxy = ProxyFactory.create(Tweet.class, URL);
        this.sessionProxy = ProxyFactory.create(Session.class, URL);
    }

    /**
     * Returns reference to session proxy
     *
     * @return session proxy
     */
    public Session getSessionProxy() {
        return sessionProxy;
    }

    /**
     * Returns reference to the tweet's proxy
     *
     * @return tweet's proxy
     */
    public Tweet getTweetProxy() {
        return tweetProxy;
    }

    /**
     * Creates new instance of TweeterProxy pointing to default URL: http://localhost:9000/rest
     */
    public TweeterProxy() {
        this("http://localhost:9000/rest");
    }
}
