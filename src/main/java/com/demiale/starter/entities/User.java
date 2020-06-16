package com.demiale.starter.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.net.MalformedURLException;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {

    private String login;
    private int id;
    private String nodeId;
    private URL url;
    private String type;

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public URL getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public User setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public User setUrl(String url) throws MalformedURLException {
        this.url = new URL(url);
        return this;
    }

    public User setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return String
                .format("%n\t Login: %s%n\t Id: %s%n\t URL: %s%n\t Type: %s%n\t Node: %s", login, id, url, type, nodeId);
    }
}
