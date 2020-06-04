package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class Repo {

    private int id;
    private String nodeId;
    private String name;
    private String fullName;
    @JsonProperty("private")
    private boolean isPrivate;
    private String description;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime pushedAt;
    private User owner;
    private Map<String, Boolean> permissions;

    private Repo(){}

    private Repo(RepoBuilder repoBuilder) {

        this.id = repoBuilder.id;
        this.nodeId = repoBuilder.nodeId;
        this.name = repoBuilder.name;
        this.fullName = repoBuilder.fullName;
        this.isPrivate = repoBuilder.isPrivate;
        this.description = repoBuilder.description;
        this.language = repoBuilder.language;
        this.createdAt = repoBuilder.createdAt;
        this.updatedAt = repoBuilder.updatedAt;
        this.pushedAt = repoBuilder.pushedAt;
        this.owner = repoBuilder.owner;
        this.permissions = repoBuilder.permissions;

    }


    public int getId() {
        return id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getPushedAt() {
        return pushedAt;
    }

    private void setCreatedAt(String createdAt) {
        this.createdAt = parseDateFromString(createdAt);
    }

    private void setUpdatedAt(String updatedAt) {
        this.updatedAt = parseDateFromString(updatedAt);
    }

    private void setPushedAt(String pushedAt) {
        this.pushedAt = parseDateFromString(pushedAt);
    }


    private LocalDateTime parseDateFromString(String strToParse) {
        return LocalDateTime.parse(strToParse.substring(0, strToParse.length()-1), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {

        return String
                .format("%n Repo name: %s%n Id: %s%n Is private: %s%n Node: %s%n " +
                                "Full Name: %s%n Permissions: %s%n Description: %s%n Language: %s%n Owner: %s",
                        name, id, isPrivate, nodeId, fullName, permissions, description, language, owner);

    }



    ////////////////////////////////

    public static class RepoBuilder {
        private int id;
        private String nodeId;
        private String name;
        private String fullName;
        private boolean isPrivate;
        private String description;
        private String language;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime pushedAt;
        private User owner;
        private Map<String, Boolean> permissions;


        public Repo build() {
            return new Repo(this);
        }

        public RepoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public RepoBuilder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public RepoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RepoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public RepoBuilder isPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public RepoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public RepoBuilder language(String language) {
            this.language = language;
            return this;
        }

        public RepoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RepoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RepoBuilder pushedAt(LocalDateTime pushedAt) {
            this.pushedAt = pushedAt;
            return this;
        }

        public RepoBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public RepoBuilder permissions(Map<String, Boolean> permissions) {
            this.permissions = permissions;
            return this;
        }

    }

}
