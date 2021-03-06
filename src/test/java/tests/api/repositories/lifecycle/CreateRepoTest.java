package tests.api.repositories.lifecycle;

import com.demiale.starter.entities.Repo;
import common.config.RequestConfiguration;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static common.util.RepoUtils.*;
import static io.restassured.RestAssured.requestSpecification;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;


public class CreateRepoTest extends RequestConfiguration {

    private static final Random rand = new Random(100);
    private static final List<String> repoNames = new ArrayList<>();

    @DataProvider
    Object[] repoNameProvider() {
        return new Object[]{
            "repo_name", "456161", "https-api.github.com", "application-vnd.github.v3-json", "tr-nsfer"
        };
    }


    @Test(dataProvider = "repoNameProvider")
    void repoWithValidNameCreated(String repoName) {

        saveRepoNameIntoTestContext(repoName);

        assertEquals(postRepo(repoName), SC_CREATED);

        assertEquals(getRepo(repoName), SC_OK);

    }


    @Test
    void privateRepoNotCreatedWithPublicScopeToken() {

        requestSpecification.auth().oauth2(token_public_repo);

        String repoName = getDummyRepoName();
        assertEquals(postPrivateRepoStatus(repoName), SC_FORBIDDEN);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(repoName), SC_NOT_FOUND);

    }


    @Test
    void publicRepoNotCreatedWithNoPublicRepoScopeToken() {

        requestSpecification.auth().oauth2(token_no_public_repo);
        String repoName = getDummyRepoName();
        assertEquals(postRepo(repoName), SC_NOT_FOUND);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(repoName), SC_NOT_FOUND);

    }


    @Test
    void repoNotCreatedWithInvalidToken() {
        String token_invalid = token_all + "_";

        requestSpecification.auth().oauth2(token_invalid);

        String repoName = getDummyRepoName();
        assertEquals(postRepo(repoName), SC_UNAUTHORIZED);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(repoName), SC_NOT_FOUND);

    }


    @Test
    void repoWithoutNameNotCreated() {

        Repo repo = new Repo.RepoBuilder().
                name(null).description("名称").isPrivate(true).build();

        assertEquals(postRepo(repo), SC_UNPROCESSABLE_ENTITY);

    }


    @Test
    void defaultValuesSetCorrectly() {

        String repoName = getDummyRepoName();
        Repo repo = new Repo.RepoBuilder().name(repoName).build();

        postRepo(repo);

        saveRepoNameIntoTestContext(repoName);

        Repo retrievedRepo = getRepoEntity(repoName);

        assertEquals(retrievedRepo.getName(), repoName);
        assertFalse(retrievedRepo.isPrivate());
        assertNull(retrievedRepo.getDescription());
        assertEquals(retrievedRepo.getFullName(), user + "/" + repoName);
        assertEquals(retrievedRepo.getOwner().getLogin(), user);

        for (boolean value : retrievedRepo.getPermissions().values()) {
            assertTrue(value);
        }

    }


    @Test
    void valuesSetCorrectly() {

        String description = "名称";

        String repoName = getDummyRepoName();
        Repo repo = new Repo.RepoBuilder().name(repoName)
                .description(description).isPrivate(true).build();

        postRepo(repo);

        saveRepoNameIntoTestContext(repoName);

        Repo retrievedRepo = getRepoEntity(repoName);

        assertEquals(retrievedRepo.getName(), repoName);
        assertTrue(retrievedRepo.isPrivate());
        assertEquals(retrievedRepo.getDescription(), description);

    }


    @Test
    void repoWithExistingNameNotCreated() {

        String repoName = getDummyRepoName();

        postPrivateRepoStatus(repoName);

        saveRepoNameIntoTestContext(repoName);

        assertEquals(postRepo(repoName), SC_UNPROCESSABLE_ENTITY);

    }


    @Test
    void createRepoWithBrokenJsonFails() {
        String repoName = getDummyRepoName();
        assertEquals(postRepoWithBrokenBodyStatus(repoName), SC_BAD_REQUEST);
        assertEquals(getRepo(repoName), SC_NOT_FOUND);
    }


    @Test
    void createRepoNonExistingJsonFieldIgnored() {

        String repoName = getDummyRepoName();

        saveRepoNameIntoTestContext(repoName);

        assertEquals(postRepoWithNonExistingFieldStatus(repoName), SC_CREATED);

        assertEquals(getRepo(repoName), SC_OK);

    }


    @Test
    void createRepoWithPutFails() {
        String repoName = getDummyRepoName();
        assertEquals(postRepoWithPutStatus(repoName), SC_NOT_FOUND);
    }


    @Test
    void createRepoWithPatchFails() {
        String repoName = getDummyRepoName();
        assertEquals(postRepoWithPatchStatus(repoName), SC_NOT_FOUND);
    }


    @BeforeClass
    private void updateTestContext(ITestContext context) {
        context.setAttribute("repoNames", repoNames);
    }

    @AfterClass
    void repoCleanup() {

        for (String name : repoNames) {
            deleteRepo(name);
        }

    }

    // TODO Move to RepoUtils + add a map containing all existing names and do lookup to ensure no name conflicts can occur
    private String getDummyRepoName() {
        return DEFAULT_REPO_NAME + rand.nextInt(100);
    }

    private void saveRepoNameIntoTestContext(String repoName) {
        repoNames.add(repoName);
    }

}

