package common.util;

import common.constants.ResponseCode;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

import static common.constants.HeaderParam.LINK;
import static io.restassured.RestAssured.*;
import static org.apache.http.HttpStatus.SC_OK;

public final class ResultsRetriever {

    public static <T> List<T> retrieveEntities(Class<T> type, RequestSpecification requestSpec, String endpointURI, int pageLimit) {

        if (pageLimit < 1)
            throw new IllegalArgumentException("Page limit cannot be less than 1");

        String linkHeader = getLinkHeader(requestSpec, endpointURI);

        if (linkHeader == null)
            return retrieveSinglePageResult(type, requestSpec, endpointURI);
        else
            return retrieveMultiPageResult(type, requestSpec, linkHeader, pageLimit);

    }

    public static <T> List<T> retrieveEntities(Class<T> type, RequestSpecification requestSpec, String endpointURI) {

        return
                retrieveEntities(type, requestSpec, endpointURI, getPageCount(getLinkHeader(requestSpec, endpointURI)));

    }

    private static <T> List<T> retrieveMultiPageResult(Class<T> type, RequestSpecification requestSpec, String linkHeader, int pageLimit) {

        List<T> entities = new ArrayList<>();

        int currentPage = 1;

        while (currentPage <= pageLimit) {

            String response =
                    given()
                            .spec(requestSpec)

                            .when()
                            .get(constructCurrentPageLink(linkHeader, currentPage))

                            .then()

                            .assertThat().statusCode(SC_OK)

                            .extract().response().asString();

            JsonPath responsePath = new JsonPath(response);
            entities.addAll(responsePath.getList(DEFAULT_PATH, type));

            currentPage++;

        }

        return entities;
    }

    private static <T> List<T> retrieveSinglePageResult(Class<T> type, RequestSpecification requestSpec, String endpointURI) {

        String response =
                given()
                        .spec(requestSpec)

                        .when()
                        .get(endpointURI)

                        .then()
                        .assertThat().statusCode(ResponseCode.OK)
                        .extract().response().asString();

        JsonPath responsePath = new JsonPath(response);

        return responsePath.getList(DEFAULT_PATH, type);

    }

    private static String constructCurrentPageLink(String linkHeader, int currentPage) {
        return String.format(getBasePagePath(linkHeader) + "%d", currentPage);
    }

    private static String getBasePagePath(String linkHeader) {
        String basePagePath = getLastPageURI(linkHeader).replace(baseURI + "/", "");
        return basePagePath.substring(0, basePagePath.lastIndexOf("page=") + 5); // get rid of magic number
    }

    private static boolean isResultPaginated(RequestSpecification requestSpec, String endpointURI) {
        return retrieveLinkHeader(requestSpec, endpointURI) != null;
    }

    private static String getLinkHeader(RequestSpecification requestSpec, String endpointURI) {
        return retrieveLinkHeader(requestSpec, endpointURI);
    }

    private static String retrieveLinkHeader(RequestSpecification requestSpec, String endpointURI) {

        return
                given()
                        .spec(requestSpec)

                        .when()
                        .get(endpointURI)

                        .then()
                        .assertThat().statusCode(ResponseCode.OK)
                        .extract().response().header(LINK);

    }

    private static int getPageCount(String linkHeader) {
        String[] splitted = getLastPageURI(linkHeader).split("page=");
        return Integer.parseInt(splitted[splitted.length - 1]);
    }

    private static String getLastPageURI(String linkHeader) {
        return linkHeader.split(", ")[1].split(";")[0].replaceAll("[<>]", "");
    }

}
