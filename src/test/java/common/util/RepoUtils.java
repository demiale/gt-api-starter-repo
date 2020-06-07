package common.util;

import entities.Repo;

import static common.config.RequestConfiguration.*;
import static io.restassured.RestAssured.given;

public class RepoUtils {


    public static int postRepo(String repoName) {

        String bodyTemplate = "{\"name\": \"%s\"}";

        return
                given().log().all().body(String.format(bodyTemplate, repoName))
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


    public static int postRepo(Repo repo) {

        return
                given().log().all().body(repo)
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().log().all().extract().statusCode();
    }


    public static int postPrivateRepoStatus(String repoName) {

        Repo repo = new Repo.RepoBuilder().name(repoName).isPrivate(true).build();

        return
                given().log().all().body(repo)
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().assertThat().extract().statusCode();

    }


    public static int getRepo(String repoName) {
        return
                given().log().all()
                        .get(REPO_URI_GET_PATCH_DELETE_SPECIFIC_USER, user, repoName).then().extract().statusCode();
    }

    public static Repo getRepoEntity(String repoName) {
        return
                given().log().all()
                        .get(REPO_URI_GET_PATCH_DELETE_SPECIFIC_USER, user, repoName).then().extract().response().as(Repo.class);
    }


    public static int deleteRepo(String repoName) {
        return
                given().log().all()
                        .delete(REPO_URI_GET_PATCH_DELETE_SPECIFIC_USER, user, repoName).then().extract().statusCode();
    }

    public static int postRepoWithPutStatus(String repoName) {

        String bodyTemplate = "{\"name\": \"%s\"}";

        return
                given().log().all().body(String.format(bodyTemplate, repoName))
                        .put(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }

    public static int postRepoWithPatchStatus(String repoName) {

        String bodyTemplate = "{\"name\": \"%s\"}";

        return
                given().log().all().body(String.format(bodyTemplate, repoName))
                        .patch(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }

    public static int postRepoWithBrokenBodyStatus(String repoName) {

        String bodyTemplate = "{\"name\" \"%s\"}";

        return
                given().log().all().body(String.format(bodyTemplate, repoName))
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


    public static int postRepoWithNonExistingFieldStatus(String repoName) {

        String bodyTemplate = "{\"name\": \"%s\", \"some_non_existing_field\": \"some_non_existing_field_value\"}";

        return
                given().log().all().body(String.format(bodyTemplate, repoName))
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


}
