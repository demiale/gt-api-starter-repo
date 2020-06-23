package tests.repositories.lifecycle;

import common.config.RequestConfiguration;
import com.demiale.starter.entities.Repo;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static common.util.RepoUtils.*;
import static io.restassured.RestAssured.requestSpecification;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;


public class CreateRepoTest extends RequestConfiguration {

    @DataProvider
    Object[] repoNameProvider() {
        return new Object[] {
            "repo_name", "456161", "https-api.github.com", "application-vnd.github.v3-json", "tr-nsfer"
        };
    }



    @Test(dataProvider = "repoNameProvider", groups = "nameCheckCleanup")
    void repoWithValidNameCreated(String repoName) {

        assertEquals(postRepo(repoName), SC_CREATED);

        assertEquals(getRepo(repoName), SC_OK);

    }


    @Test
    void privateRepoNotCreatedWithPublicScopeToken() {

        requestSpecification.auth().oauth2(token_public_repo);
        assertEquals(postPrivateRepoStatus(DEFAULT_REPO_NAME), SC_FORBIDDEN);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(DEFAULT_REPO_NAME), SC_NOT_FOUND);

    }


    @Test
    void publicRepoNotCreatedWithNoPublicRepoScopeToken() {

        requestSpecification.auth().oauth2(token_no_public_repo);
        assertEquals(postRepo(DEFAULT_REPO_NAME), SC_NOT_FOUND);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(DEFAULT_REPO_NAME), SC_NOT_FOUND);

    }


    @Test
    void repoNotCreatedWithInvalidToken() {
        String token_invalid = token_all + "_";

        requestSpecification.auth().oauth2(token_invalid);

        assertEquals(postRepo(DEFAULT_REPO_NAME), SC_UNAUTHORIZED);

        requestSpecification.auth().oauth2(token_all);
        assertEquals(getRepo(DEFAULT_REPO_NAME), SC_NOT_FOUND);

    }


    @Test
    void repoWithoutNameNotCreated() {

        Repo repo = new Repo.RepoBuilder().
                name(null).description("名称").isPrivate(true).build();

        assertEquals(postRepo(repo), SC_UNPROCESSABLE_ENTITY);

    }


    @Test(groups = "defaultCleanup")
    void defaultValuesSetCorrectly() {

        Repo repo = new Repo.RepoBuilder().name(DEFAULT_REPO_NAME).build();

        postRepo(repo);

        Repo retrievedRepo = getRepoEntity(DEFAULT_REPO_NAME);

        assertEquals(retrievedRepo.getName(), DEFAULT_REPO_NAME);
        assertFalse(retrievedRepo.isPrivate());
        assertNull(retrievedRepo.getDescription());
        assertEquals(retrievedRepo.getFullName(), user + "/" + DEFAULT_REPO_NAME);
        assertEquals(retrievedRepo.getOwner().getLogin(), user);

        for (boolean value : retrievedRepo.getPermissions().values()) {
            assertTrue(value);
        }

    }


    @Test(groups = "defaultCleanup")
    void valuesSetCorrectly() {

        String description = "名称";
        Repo repo = new Repo.RepoBuilder().name(DEFAULT_REPO_NAME)
                .description(description).isPrivate(true).build();

        postRepo(repo);

        Repo retrievedRepo = getRepoEntity(DEFAULT_REPO_NAME);

        assertEquals(retrievedRepo.getName(), DEFAULT_REPO_NAME);
        assertTrue(retrievedRepo.isPrivate());
        assertEquals(retrievedRepo.getDescription(), description);

    }


    @Test(groups = "defaultCleanup")
    void repoWithExistingNameNotCreated() {

        postPrivateRepoStatus(DEFAULT_REPO_NAME);

        assertEquals(postRepo(DEFAULT_REPO_NAME), SC_UNPROCESSABLE_ENTITY);

    }


    @Test
    void createRepoWithBrokenJsonFails() {
        assertEquals(postRepoWithBrokenBodyStatus(DEFAULT_REPO_NAME), SC_BAD_REQUEST);
        assertEquals(getRepo(DEFAULT_REPO_NAME), SC_NOT_FOUND);
    }


    @Test(groups = "defaultCleanup")
    void createRepoNonExistingJsonFieldIgnored() {

        assertEquals(postRepoWithNonExistingFieldStatus(DEFAULT_REPO_NAME), SC_CREATED);
        assertEquals(getRepo(DEFAULT_REPO_NAME), SC_OK);

    }


    @Test
    void createRepoWithPutFails() {
        assertEquals(postRepoWithPutStatus(DEFAULT_REPO_NAME), SC_NOT_FOUND);
    }


    @Test
    void createRepoWithPatchFails() {
        assertEquals(postRepoWithPatchStatus(DEFAULT_REPO_NAME), SC_NOT_FOUND);
    }


    /*TODO At the moment these clean-up methods are triggered after each test, though not in every test are needed.
       need to find right configuration to solve this issue. Leaving as is for now
    */
    @AfterMethod(alwaysRun = true, onlyForGroups = "defaultCleanup")
    void deleteRepoWithDefaultName() {
        deleteRepo(DEFAULT_REPO_NAME);
    }


    @AfterMethod(alwaysRun = true, onlyForGroups = "nameCheckCleanup")
    void deleteReposFromNameProviderGroup() {
        Object[] created = repoNameProvider();
        for (Object name : created) {
            deleteRepo(name.toString());
        }
    }

}

