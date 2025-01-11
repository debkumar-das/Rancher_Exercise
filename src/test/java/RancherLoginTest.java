import  io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class RancherLoginTest {

  private String rancherUrl = "https://10.52.90.0"; // Adjust this to your Rancher installation URL
  private String apiPath = "/v3-public/localProviders/local?action=login";  // Rancher login API endpoint

  private String username = "admin";      // Replace with your Rancher admin username
  private String password = "SU4BnKvmZcWooZE5";  // Replace with your Rancher admin password

  @BeforeTest
  public void setUp() {
    // Disable SSL certificate validation in RestAssured (for testing purposes only)
    RestAssured.useRelaxedHTTPSValidation();
    RestAssured.baseURI = rancherUrl;
  }

  @Test
  public void testLoginToRancher() {
    // Create the login payload
    String loginPayload = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

    // Send the POST request to login
     RestAssured.given()
      .contentType("application/json")
      .body(loginPayload)
      .post(apiPath)
      .then()
      .statusCode(201)  // Assert the status code is 201 OK
      .assertThat()
      .log().all();
  }

  @Test
  public void testFailedLoginWithInvalidCredentials() {
    // Create invalid login payload
    String loginPayload = "{ \"username\": \"invalid_user\", \"password\": \"invalid_password\" }";

    RestAssured.given()
      .contentType("application/json")
      .body(loginPayload)
      .post(apiPath)
      .then()
      .statusCode(401)  // Assert the status code is 401 Unauthorized
      .body("message", containsString("authentication failed"))
      .log().all();
  }
}
