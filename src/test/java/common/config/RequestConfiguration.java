package common.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeMethod;

import static common.constants.HeaderParam.ACCEPT_API_VERSION;
import static io.restassured.RestAssured.oauth2;

public class RequestConfiguration {

    public static final String user = "marinerro";
    public static final String password = "m@4ineRro";

    public static final String token_all = "41b25ee0963cbbaf5151843afdd6ee0b206d1fcd";
    public static final String token_private_repo = "6f256fe3371636d86d24076ec65c79490cf7da28";
    public static final String token_public_repo = "95b89844fe17c6d731f1f8895f1f0070503d7497";
    public static final String token_delete_repo = "bdeb35713bd399395419b56c9ade1b0b706a0b99";

    public static final String REPOS_URI_CURRENT_USER = "/user/repos"; // both GET and PUT
    public static final String REPOS_URI_SPECIFIC_USER = "/users/{username}/repos";
    public static final String REPOS_PUBLIC = "/repositories";
    public static final String REPO_URI_SPECIFIC_USER = "/repos/{owner}/{repo}"; // GET, PATCH, DELETE

    public static final String CREATE_REPO_URI_FROM_TEMPLATE = "/repos/{template_owner}/{template_repo}/generate";

    public static final String BASE_URI = "https://api.github.com";

    @BeforeMethod
    public static void configure() {

        RestAssured.requestSpecification =
                new RequestSpecBuilder().setBaseUri(BASE_URI).setAuth(oauth2(token_all)).setAccept(ACCEPT_API_VERSION)
                        .build();

    }

}
