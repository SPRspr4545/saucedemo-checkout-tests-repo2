package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class NavigationTest {

    private WebDriver driver; // configurer le driver en tant que variable membre privée de la classe de test
    private static final String SITE = "https://www.skillsoft.com/";
    private static final String ABOUT = SITE + "/about";
    private static final String LEADERSHIP = SITE + "/leadership-team";
    private static final String NEWSROOM = SITE + "/news";

    @BeforeTest // Avant chaque méthode @Test choisir comme argument le pilote de navigateur à instancier
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
    }

    private static void delay() { // Fonction retard de 5 secondes afin de voir ce qu'il se passe
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e    );
        }
    }

    @Test
    public void navigationTest() { //https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/WebDriver.html
        // notre test de navigagtion devient très simple (le pilote est déjà instancié) il suffit de naviguer vers le site à tester
        driver.get(SITE);
        driver.get(ABOUT);

        driver.navigate().back();

        Assert.assertEquals(driver.getCurrentUrl(), SITE);
        Assert.assertEquals(driver.getTitle(), "Employee Development: Online Training Solutions | Skillsoft");

        driver.navigate().forward();

        Assert.assertEquals(driver.getCurrentUrl(), ABOUT);
        Assert.assertEquals(driver.getTitle(), "About Us - Skillsoft");

        driver.navigate().refresh();

        Assert.assertEquals(driver.getCurrentUrl(), ABOUT);
        Assert.assertEquals(driver.getTitle(), "About Us - Skillsoft");

        driver.navigate().to(LEADERSHIP);

        Assert.assertEquals(driver.getCurrentUrl(), LEADERSHIP);
        Assert.assertEquals(driver.getTitle(), "The Skillsoft Leadership Team");

        driver.get(NEWSROOM);
        //delay();
        Assert.assertEquals(driver.getCurrentUrl(), NEWSROOM);
        Assert.assertEquals(driver.getTitle(), "Skillsoft Newsroom - Skillsoft");
    }

    @AfterTest // cette annotation entraine l'exécution de cette méthode après chaque méthode @Test
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
    }


}
