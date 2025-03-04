package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator; //********** !!!
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class RelativeLocatorTest {

    private WebDriver driver; // configurer le driver en tant que variable membre privée de la classe de test
    private static final String SITE = "https://testpages.eviltester.com/styled/index.html";

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
    public void relativeLocatorsTest() {
        driver.get(SITE);

        WebElement basicFormLinkEl = driver.findElement(By.linkText("HTML Form Example"));
        basicFormLinkEl.click();
        delay();

        WebElement  usernameInputEl = driver.findElement(By.name("username"));
        usernameInputEl.sendKeys("Bob Baker");

        // Les locators Relatifs permettent d'identifier les éléments en dessous(below), au dessus(above), à gauche(toLeftOf) ou à droite(toRightOf)
        // on utilise les locators relatifs quand on à pas le choix, il est préférable d'utiliser les locators Absolus (voir LocatorTests.java)
        WebElement passwordInputEl = driver.findElement(
                RelativeLocator.with(By.tagName("input")).below(usernameInputEl));
        passwordInputEl.sendKeys("password123");

        WebElement commentsInputEl = driver.findElement(
                RelativeLocator.with(By.tagName("textarea")).below(passwordInputEl));
        commentsInputEl.clear(); // Par défaut, la zone de texte contient déjà du texte
        commentsInputEl.sendKeys("Some comments here");

        delay();
        WebElement submitInputEl = driver.findElement(By.cssSelector("input[value='submit']"));
        submitInputEl.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("_valueusername")));

        WebElement usernameListItemEl = driver.findElement(By.id("_valueusername"));
        Assert.assertEquals(usernameListItemEl.getText(), "Bob Baker"); // vérifie que le nom affiché est celui en entrée

        WebElement passwordListItemEl = driver.findElement(
                RelativeLocator.with(By.tagName("li")).below(usernameListItemEl));
        Assert.assertEquals(passwordListItemEl.getText(),"password123");

        WebElement commentsListItemEl = driver.findElement(
                RelativeLocator.with(By.tagName("li")).below(passwordListItemEl));
        Assert.assertEquals(commentsListItemEl.getText(),"Some comments here");

        delay();

        WebElement backToFormEl = driver.findElement(By.cssSelector("a[id='back_to_form']"));
        backToFormEl.click();

        delay();

        WebElement submitInputEl1 = driver.findElement(By.cssSelector("input[value='submit']"));
        WebElement cancelInputEl = driver.findElement(
                RelativeLocator.with(By.tagName("input")).toLeftOf(submitInputEl1));
        cancelInputEl.click();
        delay();


    }

    @AfterTest // cette annotation entraine l'exécution de cette méthode après chaque méthode @Test
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
    }

}
