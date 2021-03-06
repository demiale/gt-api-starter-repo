package common.util;

import com.demiale.starter.entities.Repo;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static common.config.RequestConfiguration.*;
import static io.restassured.RestAssured.given;

public class RepoUtils {

    final static String repoBodyTemplate = "{\"name\": \"%s\"}";
    final static String repoBrokenBodyTemplate = "{\"name\" \"%s\"}";
    final static String repoBodyWrongFieldTemplate = "{\"name\": \"%s\", \"some_non_existing_field\": \"some_non_existing_field_value\"}";

    public static int postRepo(String repoName) {

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
                        .post(URI_CURRENT_USER_REPO).then().extract().statusCode();
    }


    public static int postRepo(Repo repo) {

        return
                given().log().all().body(repo)
                        .post(URI_CURRENT_USER_REPO).then().log().all().extract().statusCode();
    }


    public static int postPrivateRepoStatus(String repoName) {

        Repo repo = new Repo.RepoBuilder().name(repoName).isPrivate(true).build();

        return
                given().log().all().body(repo)
                        .post(URI_CURRENT_USER_REPO).then().assertThat().extract().statusCode();

    }


    public static int getRepo(String repoName) {

        int responseCode = sendGetRequest(repoName).statusCode();
        return responseCode == 200 ? responseCode : sendGetRequest(repoName).statusCode();

    }


    public static Repo getRepoEntity(String repoName) {
        ExtractableResponse<Response> response = sendGetRequest(repoName);
        return response.statusCode() == 200 ? response.as(Repo.class) : sendGetRequest(repoName).as(Repo.class);
    }


    public static int deleteRepo(String repoName) {

        return
                given().log().all()
                        .delete(URI_USER_SPECIFIC_REPO, user, repoName).then().extract().statusCode();
    }


    public static int postRepoWithPutStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
                        .put(URI_CURRENT_USER_REPO).then().extract().statusCode();
    }


    public static int postRepoWithPatchStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBodyTemplate, repoName))
                        .patch(URI_CURRENT_USER_REPO).then().extract().statusCode();
    }


    public static int postRepoWithBrokenBodyStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBrokenBodyTemplate, repoName))
                        .post(URI_CURRENT_USER_REPO).then().extract().statusCode();
    }


    public static int postRepoWithNonExistingFieldStatus(String repoName) {

        return
                given().log().all().body(String.format(repoBodyWrongFieldTemplate, repoName))
                        .post(URI_CURRENT_USER_REPO).then().extract().statusCode();
    }


    private static ExtractableResponse<Response> sendGetRequest(String repoName) {
        return given().log().all()
                .get(URI_USER_SPECIFIC_REPO, user, repoName).then().extract();
    }

}
