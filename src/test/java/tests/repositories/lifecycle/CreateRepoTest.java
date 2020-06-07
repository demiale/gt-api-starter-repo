package tests.repositories.lifecycle;

import common.config.RequestConfiguration;
import entities.Repo;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static common.constants.ResponseCode.*;
import static common.util.RepoUtils.*;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CreateRepoTest extends RequestConfiguration {

    @DataProvider
    Object[] repoNameProvider() {
        return new Object[] {
            "repo_name", "456161", "https-api.github.com", "application-vnd.github.v3-json", "tr-nsfer"
        };
    }



    @Test(dataProvider = "repoNameProvider", groups = "nameCheckCleanup")
    void repoWithValidNameCreated(String repoName) {

        assertThat(postRepo(repoName), equalTo(CREATED));

        assertThat(getRepo(repoName), equalTo(OK));

    }


    @Test
    void privateRepoNotCreatedWithPublicScopeToken() {

        requestSpecification.auth().oauth2(token_public_repo);
        assertThat(postPrivateRepoStatus(DEFAULT_REPO_NAME), equalTo(FORBIDDEN));

        requestSpecification.auth().oauth2(token_all);
        assertThat(getRepo(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));

    }


    @Test
    void publicRepoNotCreatedWithNoPublicRepoScopeToken() {

        requestSpecification.auth().oauth2(token_no_public_repo);
        assertThat(postRepo(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));

        requestSpecification.auth().oauth2(token_all);
        assertThat(getRepo(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));

    }


    @Test
    void repoNotCreatedWithInvalidToken() {
        String token_invalid = token_all + "_";

        requestSpecification.auth().oauth2(token_invalid);

        assertThat(postRepo(DEFAULT_REPO_NAME), equalTo(UNAUTHORIZED));

        requestSpecification.auth().oauth2(token_all);
        assertThat(getRepo(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));

    }


    @Test
    void repoWithoutNameNotCreated() {

        Repo repo = new Repo.RepoBuilder().
                name(null).description("名称").isPrivate(true).build();

        assertThat(postRepo(repo), equalTo(UNPROCESSABLE_ENTITY));

    }


    @Test(groups = "defaultCleanup")
    void defaultValuesSetCorrectly() {

        Repo repo = new Repo.RepoBuilder().name(DEFAULT_REPO_NAME).build();

        postRepo(repo);

        Repo retrievedRepo = getRepoEntity(DEFAULT_REPO_NAME);

        assertThat(retrievedRepo.getName(), equalTo(DEFAULT_REPO_NAME));
        assertThat(retrievedRepo.isPrivate(), equalTo(false));
        assertThat(retrievedRepo.getDescription(), equalTo(null));
        assertThat(retrievedRepo.getFullName(), equalTo(user + "/" + DEFAULT_REPO_NAME));
        assertThat(retrievedRepo.getOwner().getLogin(), equalTo(user));

        for (boolean value : retrievedRepo.getPermissions().values()) {
            assertThat(value, equalTo(true));
        }

    }


    @Test(groups = "defaultCleanup")
    void valuesSetCorrectly() {

        String description = "名称";
        Repo repo = new Repo.RepoBuilder().name(DEFAULT_REPO_NAME)
                .description(description).isPrivate(true).build();

        postRepo(repo);

        Repo retrievedRepo = getRepoEntity(DEFAULT_REPO_NAME);

        assertThat(retrievedRepo.getName(), equalTo(DEFAULT_REPO_NAME));
        assertThat(retrievedRepo.isPrivate(), equalTo(true));
        assertThat(retrievedRepo.getDescription(), equalTo(description));

    }


    @Test(groups = "defaultCleanup")
    void repoWithExistingNameNotCreated() {

        postPrivateRepoStatus(DEFAULT_REPO_NAME);

        assertThat(postRepo(DEFAULT_REPO_NAME), equalTo(UNPROCESSABLE_ENTITY));

    }


    @Test
    void createRepoWithBrokenJsonFails() {
        assertThat(postRepoWithBrokenBodyStatus(DEFAULT_REPO_NAME), equalTo(BAD_REQUEST));
        assertThat(getRepo(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));
    }


    @Test(groups = "defaultCleanup")
    void createRepoNonExistingJsonFieldIgnored() {

        assertThat(postRepoWithNonExistingFieldStatus(DEFAULT_REPO_NAME), equalTo(CREATED));
        assertThat(getRepo(DEFAULT_REPO_NAME), equalTo(OK));

    }


    @Test
    void createRepoWithPutFails() {
        assertThat(postRepoWithPutStatus(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));
    }


    @Test
    void createRepoWithPatchFails() {
        assertThat(postRepoWithPatchStatus(DEFAULT_REPO_NAME), equalTo(NOT_FOUND));
    }


    /*TODO At the moment these clean-up methods are triggered after each test, though not in every test are needed.
       need to find right configuration to solve this issue. Leaving as is for now
    */
    @AfterMethod(alwaysRun = true, onlyForGroups = "defaultCleanup")
    void deleteRepoWithDefaultName() {

        System.out.println("default cleanup");
        deleteRepo(DEFAULT_REPO_NAME);
    }


    @AfterMethod(alwaysRun = true, onlyForGroups = "nameCheckCleanup")
    void deleteReposFromNameProviderGroup() {
        System.out.println("deleting repos from provider - groups");
        Object[] created = repoNameProvider();
        for (Object name : created) {
            deleteRepo(name.toString());
        }
    }

}

