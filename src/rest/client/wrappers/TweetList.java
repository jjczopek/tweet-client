package rest.client.wrappers;

import rest.client.beans.Tweet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jerzy
 * Date: 28.06.11
 * Time: 22:55
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "tweetList")
public class TweetList {


    private List<Tweet> tweets;

    public TweetList(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public TweetList() {
    }


    public List<Tweet> getTweets() {
        return tweets;
    }

    @XmlElement(name = "tweet")
    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
