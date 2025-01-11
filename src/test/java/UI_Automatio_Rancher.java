import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class UI_Automatio_Rancher
{

  WebDriver driver;
  String url="https://10.52.90.0";
  @BeforeTest
  public void setup() throws IOException
  {
    Properties properties = new Properties();
    FileInputStream fileInputStream = new FileInputStream("./src/main/resources/configuation.properties");
    properties.load(fileInputStream);
    System.setProperty("webdriver.chrome.driver", properties.getProperty("chromeDriverPath"));
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--ignore-certificate-errors");
    options.addArguments("--disable-web-security");
    options.addArguments("--allow-insecure-localhost");
    driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  @Test
  public void loginPage()
  {
    driver.get(url);
    WebDriverWait wt = new WebDriverWait(driver, 60);
    wt.until(ExpectedConditions.elementToBeClickable(By.id("username")));
    driver.findElement(By.id("username")).sendKeys("admin");
    wt.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
    driver.findElement(By.xpath("//input[@type='password']")).sendKeys("SU4BnKvmZcWooZE5");
    wt.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
    driver.findElement(By.id("submit")).click();
    wt.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@data-testid='banner-title']"))));
    String homePage= driver.findElement(By.xpath("//*[@data-testid='banner-title']")).getText().trim();
    Assert.assertEquals("Welcome to Rancher", homePage);
  }
  @Test
  public void mainWebPage()
  {
    String murl=driver.getCurrentUrl();
    String sctUrl=url+"/dashboard/home";
    Assert.assertEquals(sctUrl, murl);
  }
  @Test
  public void titleOfThePage()
  {
    String title=driver.getTitle();
    Assert.assertEquals("Rancher", title);
  }

  @AfterTest
  public void tearDown()
  {
    driver.quit();
  }
}
