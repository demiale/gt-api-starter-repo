package common.util;

import entities.Repo;

import static common.config.RequestConfiguration.*;
import static io.restassured.RestAssured.given;

public class RepoUtils {

    final static String repoBodyTemplate = "{\"name\": \"%s\"}";
    final static String repoBrokenBodyTemplate = "{\"name\" \"%s\"}";
    final static String repoBodyWrongFieldTemplate = "{\"name\": \"%s\", \"some_non_existing_field\": \"some_non_existing_field_value\"}";

    public static int postRepo(String repoName) {

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
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

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
                        .put(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


    public static int postRepoWithPatchStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
                        .patch(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


    public static int postRepoWithBrokenBodyStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBrokenBodyTemplate, repoName))
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


    public static int postRepoWithNonExistingFieldStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBodyWrongFieldTemplate, repoName))
                        .post(REPOS_URI_GET_POST_CURRENT_USER).then().extract().statusCode();
    }


}
