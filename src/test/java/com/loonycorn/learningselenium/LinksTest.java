package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LinksTest {

    private static final String LINKS = "file:///C:/Selenium/learning-selenium/src/test/java/templates/broken_links.html";

    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
    }

    private static void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTotalNumberOfLinks() {
        driver.get(LINKS);

        List<WebElement> links = driver.findElements(By.tagName("a")); // trouve tous les liens
        int actualCount = links.size(); // on compte le nombre de liens

        Assert.assertEquals(actualCount, 10, "Total number of links doesn't match expected count");

    }

    @Test
    public void testBrokenLinks() {
        driver.get(LINKS);

        List<WebElement> links = driver.findElements(By.tagName("a")); // obtenir la liste des liens
        for (WebElement link : links) {
            String url = link.getAttribute("href"); // obtenir l'url de chaque liens

            if (url != null && !url.isEmpty()) { // assure que toutes les urls non nulles et non vides sont valides
                try {
                    // établis une HttpURLConnection à cette URL, et ouvre cette connection
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(10000); // spécifie un délai de connection de 10 secondes, au delà on considère que le lien est rompu
                    connection.connect(); // maintenant que la connexion est ouverte, appel la méthode connect()...

                    int responseCode = connection.getResponseCode(); // ...et reçoit le responseCode à partir de la connexion

                    if (responseCode == 200) {
                        System.out.println("Link is working fine: " + url);
                    } else {
                        System.out.println("Broken link found: " + url);
                    }
                } catch (Exception e) { // l'établissement d'une connection HTTP à une URL peut provoquer une exception
                    System.out.println("Exception occured while checking link: " + url);
                    e.printStackTrace(); // Si je détecte une Exception, j'imprime simplement le StackTrace
                    //throw new RuntimeException(e);
                }
            }
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
