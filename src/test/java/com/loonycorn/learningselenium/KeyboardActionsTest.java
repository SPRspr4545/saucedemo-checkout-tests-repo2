package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.checkerframework.checker.units.qual.K;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class KeyboardActionsTest {
    private WebDriver driver;
    private static final String FORM = "https://testpages.eviltester.com/styled/basic-html-form-test.html";

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
    public void keybordActionsTest() {
        driver.get(FORM);

        WebElement usernameEl = driver.findElement(By.name("username"));
        WebElement passwordEl = driver.findElement(By.name("password"));
        WebElement commentsEl = driver.findElement(By.name("comments"));
        commentsEl.clear();

        Actions actions = new Actions(driver);

        actions
                .sendKeys(usernameEl, "Sam Bott")
                .sendKeys(Keys.ARROW_LEFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT)
                .pause(Duration.ofSeconds(2))
                .keyDown(Keys.SHIFT) // la méthode keyDown pour appuyer sur Maj enfoncée
                .sendKeys("super ")
                .pause(Duration.ofSeconds(2))
                .keyUp(Keys.SHIFT) // je relache explicitement Maj avec la méthode keyUp
                .sendKeys(Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT)
                .sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE)
                .pause(Duration.ofSeconds(5))
                .sendKeys(passwordEl, "password123")
                .sendKeys(commentsEl, "Some comments here!")
                .perform(); // j'invoque la méthode d'exécution

        System.out.println("Username: " + usernameEl.getAttribute("value"));
        System.out.println("Password: " + passwordEl.getAttribute("value"));
        System.out.println("Comments: " + commentsEl.getAttribute("value"));

        delay();

        WebElement submitInputEl = driver.findElement(By.cssSelector("input[value='submit'"));
        submitInputEl.submit();

        Assert.assertEquals(driver.getCurrentUrl(), "https://testpages.eviltester.com/styled/the_form_processor.php");
        delay();

    }

    @Test
    public void keybordActionsTest2() {
        driver.get(FORM);

        WebElement usernameEl = driver.findElement(By.name("username"));
        WebElement passwordEl = driver.findElement(By.name("password"));
        WebElement commentsEl = driver.findElement(By.name("comments"));
        commentsEl.clear();

        Actions actions = new Actions(driver);
        //Keys cmdCtrl = Platform.getCurrent().is(Platform.MAC) ? Keys.COMMAND : Keys.CONTROL;
        actions
                .sendKeys(usernameEl, "Sam Bott") // le curseur se trouve à la fin
                .keyDown(Keys.SHIFT) // maintenir SHIFT enfoncée
                .sendKeys(Keys.ARROW_UP) // selectionne le nom
                .keyUp(Keys.SHIFT) // relache SHIFT
                //.keyDown(cmdCtrl) // pour macOS
                .keyDown(Keys.CONTROL) // maintenir Ctrl enfoncer
                .sendKeys("xvv") // Couper + Coller + Coller
                .keyUp(Keys.CONTROL) // relache Ctrl
                .sendKeys(passwordEl, "password123")
                .sendKeys(commentsEl, "Some comments here!")
                .perform(); // j'invoque la méthode d'exécution

        System.out.println("Username: " + usernameEl.getAttribute("value"));
        System.out.println("Password: " + passwordEl.getAttribute("value"));
        System.out.println("Comments: " + commentsEl.getAttribute("value"));

        delay();

        WebElement submitInputEl = driver.findElement(By.cssSelector("input[value='submit'"));
        submitInputEl.submit();

        Assert.assertEquals(driver.getCurrentUrl(), "https://testpages.eviltester.com/styled/the_form_processor.php");
        delay();

    }

    @AfterTest // cette annotation entraine l'exécution de cette méthode après chaque méthode @Test
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
    }
}
