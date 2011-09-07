package rest.client.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created by IntelliJ IDEA.
 * User: Jerzy
 * Date: 28.06.11
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class User {

    //@XmlElement
    public long id;

    //@XmlElement
    public String login;

    //@XmlElement
    public String fullName;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
