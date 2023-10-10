package cai.peter.test.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.peppermint.vision.test.model.JacksonUtil;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FxRestAssured {
    protected String sessionId;
    protected Cookie cookie;

    protected Supplier<RestAssuredConfig> configurator =
            () -> {
                return RestAssuredConfig.config()
                        .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation())
                        .and()
                        .redirect(RedirectConfig.redirectConfig().followRedirects(false))
                        .httpClient(
                                HttpClientConfig.httpClientConfig()
                                        .setParam("http.socket.timeout", Integer.valueOf(10000)))
                        .httpClient(
                                HttpClientConfig.httpClientConfig()
                                        .setParam("http.connection.timeout", Integer.valueOf(10000)));
            };

    protected Supplier<ObjectMapper> jacksonMapper = Suppliers.memoize(() -> {
        return JacksonUtil.createMapper();
    });

    private static PrintStream logger = new PrintStream(
            new TeeOutputStream(System.out, new ByteArrayOutputStream()));

    private static RequestLoggingFilter reqLogFilter =
            new RequestLoggingFilter(logger) {
                @Override
                public Response filter(
                        FilterableRequestSpecification requestSpec,
                        FilterableResponseSpecification responseSpec,
                        FilterContext ctx) {
                    logger.println(
                            ">>--------- Request: "
                                    + requestSpec.getMethod()
                                    + " "
                                    + requestSpec.getUserDefinedPath()
                                    + " -------------------------------------------");

                    return super.filter(requestSpec, responseSpec, ctx);
                }
            };
    private static ResponseLoggingFilter resLogFilter =
            new ResponseLoggingFilter(logger) {

                @Override
                public Response filter(
                        FilterableRequestSpecification requestSpec,
                        FilterableResponseSpecification responseSpec,
                        FilterContext ctx) {
                    logger.println("<<---------- Response ----------------------------------------");
                    return super.filter(requestSpec, responseSpec, ctx);
                }
            };

    protected void flushLogger() throws IOException {
        logger.flush();
    }

    protected void logInit() {
        RestAssured.replaceFiltersWith(reqLogFilter, resLogFilter);
    }

    protected void logTeardown() throws IOException {
        flushLogger();
    }

    protected RequestSpecification buildJSONRequest() {
        return RestAssured.given()
                .config((RestAssuredConfig) configurator.get())
                .contentType(ContentType.JSON);
    }


    protected RequestSpecification buildAuthenticatedRequest() {
        return RestAssured.given()
                .config((RestAssuredConfig) configurator.get())
                .contentType(ContentType.JSON)
                .header("Authorization", sessionId);
    }

    protected RequestSpecification buildCookieRequest() {
        return RestAssured.given()
                .config((RestAssuredConfig) configurator.get())
                .cookie(cookie)
                .contentType(ContentType.JSON);
    }

    protected RequestSpecification buildAuthenticatedRequest(String sessionId) {
        return RestAssured.given()
                .config((RestAssuredConfig) configurator.get())
                .contentType(ContentType.JSON)
                .header("Authorization", sessionId);
    }


    protected String feedLogin(String user, String pwd, String base) throws JsonProcessingException {
        ImmutableMap<String, String> credential = ImmutableMap.of("name", user, "password", pwd);
        String json = JacksonUtil.createMapper().writeValueAsString(credential);
        RestAssured.baseURI = base;
        Response auth = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("user/login");
        cookie = auth.then()
                .statusCode(200).extract().response().getDetailedCookie("JSESSIONID");
        return cookie.getValue();
    }

    protected String formLogin(String user, String pwd, String base) {
        RestAssured.baseURI = base;
        Response auth = RestAssured.given().urlEncodingEnabled(true)
                .param("username", user)
                .param("password", pwd)
                .when()
                .post("/auth");
        auth.then()
                .statusCode(200);

        sessionId = (String) auth.path("sessionId");
        return sessionId;
    }

    public <T> T fromJson(String content, Class<T> type) throws Exception {
        return jacksonMapper.get().readValue(content, type);
    }

    public <T> void saveJson(List<T> objects) throws Exception {
        saveJson(objects, null);
    }

    public <T> void saveJson(List<T> objects, String filename) throws Exception {
        String json = jacksonMapper.get().writerWithDefaultPrettyPrinter().writeValueAsString(objects);
        saveToFile(json, filename);
    }

    public void saveJson(String raw) throws Exception {
        saveJson(raw, null);
    }

    public void saveJson(String raw, String filename) throws Exception {
        ObjectMapper mapper = jacksonMapper.get();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(raw));
        saveToFile(json, filename);
    }

    private void saveToFile(String json, String filename) throws Exception {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
        Path path = Paths.get("assured/" + time + Optional.ofNullable(filename).map(f -> "-" + f).orElse("") + ".json");
        Files.createDirectories(path.getParent());
        Files.write(path, json.getBytes());
    }

    @SneakyThrows
    public String readJsonFile(String filename) {
        Path path = Paths.get("json/" + filename);
        String s = Files.readString(path);
        return s;
    }
}
