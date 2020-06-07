package common.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;

import static common.constants.HeaderParam.ACCEPT_API_VERSION;
import static common.config.RequestConfigUtils.props;
import static io.restassured.RestAssured.oauth2;

public class RequestConfiguration {

    // TODO 11-Jun-2020 Move to .properties
    // TODO 11-Jun-2020 Add encrypt/decrypt methods

    public static final String user = props.getProperty("user");
    public static final String password = props.getProperty("password");
    public static final String token_all = props.getProperty("token_all");
    public static final String token_private_repo = props.getProperty("token_private_repo");
    public static final String token_public_repo = props.getProperty("token_public_repo");
    public static final String token_delete_repo = props.getProperty("token_delete_repo");
    public static final String token_no_public_repo = props.getProperty("token_no_public_repo");

    public static final String REPOS_URI_GET_POST_CURRENT_USER = "/user/repos"; // both GET and POST
    public static final String REPOS_URI_GET_SPECIFIC_USER = "/users/{username}/repos";
    public static final String REPOS_URI_GET_PUBLIC = "/repositories";
    public static final String REPO_URI_GET_PATCH_DELETE_SPECIFIC_USER = "/repos/{owner}/{repo}"; // GET, PATCH, DELETE

    public static final String CREATE_REPO_URI_FROM_TEMPLATE = "/repos/{template_owner}/{template_repo}/generate";

    public static final String BASE_URI = "https://api.github.com";

    public static final String DEFAULT_REPO_NAME = "dummy";

    @BeforeMethod
    public static void configure() {

        RestAssured.requestSpecification =
                new RequestSpecBuilder().setBaseUri(BASE_URI).setAuth(oauth2(token_all))
                        .setAccept(ACCEPT_API_VERSION).setContentType(ContentType.JSON)
                        .build();

    }

}
