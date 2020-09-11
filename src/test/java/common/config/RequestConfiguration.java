package common.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;

import static common.config.EncodeUtils.decode;
import static common.config.RequestConfigUtils.props;
import static io.restassured.RestAssured.oauth2;

public abstract class RequestConfiguration {

    public static final String user = decode(props.getProperty("user"));
    public static final String password = decode(props.getProperty("password"));
    public static final String token_all = decode(props.getProperty("token_all"));
    public static final String token_private_repo = decode(props.getProperty("token_private_repo"));
    public static final String token_public_repo = decode(props.getProperty("token_public_repo"));
    public static final String token_delete_repo = decode(props.getProperty("token_delete_repo"));
    public static final String token_no_public_repo = decode(props.getProperty("token_no_public_repo"));

    public static final String BASE_URI = props.getProperty("base_uri");

    public static final String URI_CURRENT_USER_REPO = props.getProperty("uri_current_user_repo"); // both GET and POST
    public static final String URI_USER_REPOS_ = props.getProperty("uri_user_repos");
    public static final String URI_PUBLIC_REPOS = props.getProperty("uri_public_repos"); // list public repos
    public static final String URI_USER_SPECIFIC_REPO = props.getProperty("uri_user_specific_repo"); // GET, PATCH, DELETE

    public static final String URI_REPO_FROM_TEMPLATE = props.getProperty("uri_repo_from_template");

    public static final String API_VERSION_3 = props.getProperty("api_version_3");

    public static final String DEFAULT_REPO_NAME = "dummy";

    @BeforeMethod
    public static void configure() {

        RestAssured.requestSpecification =
                new RequestSpecBuilder().setBaseUri(BASE_URI).setAuth(oauth2(token_all))
                        .setAccept(API_VERSION_3).setContentType(ContentType.JSON)
                        .build();

    }

}
