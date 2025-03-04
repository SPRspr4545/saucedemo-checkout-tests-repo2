package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor; //********** !!!
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class MouseActionsTest {

    private WebDriver driver; // configurer le driver en tant que variable membre privée de la classe de test
    private static final String SITE = "https://demoqa.com/buttons";
    private static final String MENU = "https://demoqa.com/menu";
    private static final String DROP = "https://demoqa.com/droppable";

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
    public void clickTest() {
        driver.get(SITE);
        // Après avoir accédé au site, j'ai une attente explicite
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement buttonsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[h1[text()='Buttons']]")));

        // pour faire défiler la page vers le centre et voir les messages, j'utilise;
        // JavascriptExecutor, une interface implémentée par tous les drivers de navigateur
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight /3)");
        // window.scrollTo() est une commande JS qui fait défiler la page à un tiers de sa hauteur

        WebElement clickButton = buttonsDiv.findElement(By.xpath(
                "//button[text()='Click Me']"));

        // pour le click j'utilise l'API Actions qui permet des intéraction plus complexes avec la souris
        new Actions(driver) // j'instancie un nouvel objet Actions
                .click(clickButton)
                .perform(); // la méthode perform() effectue l'action de click(clickButton)
        // Après le click j'ai une attente explicite
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("dynamicClickMessage"))); // une fois le message visible, lance Assert
        Assert.assertEquals(messageElement.getText(), "You have done a dynamic click");
        delay();
    }

    @Test
    public void doubleClickTest() {
        driver.get(SITE);
        // Après avoir accédé au site, j'ai une attente explicite
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement buttonsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[h1[text()='Buttons']]")));

        // pour faire défiler la page vers le centre et voir les messages, j'utilise;
        // JavascriptExecutor, une interface implémentée par tous les drivers de navigateur
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight /3)");
        // window.scrollTo() est une commande JS qui fait défiler la page à un tiers de sa hauteur

        WebElement doubleClickButton = buttonsDiv.findElement(By.id("doubleClickBtn"));

        // pour le doubleClick j'utilise l'API Actions qui permet des intéraction plus complexes avec la souris
        new Actions(driver) // j'instancie un nouvel objet Actions
                .doubleClick(doubleClickButton)
                .perform(); // la méthode perform() effectue l'action de click(doubleClickButton)
        // Après le click j'ai une attente explicite
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("doubleClickMessage"))); // une fois le message visible, lance Assert
        Assert.assertEquals(messageElement.getText(), "You have done a double click");
        delay();
    }

    @Test
    public void rightClickTest() {
        driver.get(SITE);
        // Après avoir accédé au site, j'ai une attente explicite
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement buttonsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[h1[text()='Buttons']]")));

        // pour faire défiler la page vers le centre et voir les messages, j'utilise;
        // JavascriptExecutor, une interface implémentée par tous les drivers de navigateur
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight /3)");
        // window.scrollTo() est une commande JS qui fait défiler la page à un tiers de sa hauteur

        WebElement rightClickButton = buttonsDiv.findElement(By.id("rightClickBtn"));

        // pour le doubleClick j'utilise l'API Actions qui permet des intéraction plus complexes avec la souris
        new Actions(driver) // j'instancie un nouvel objet Actions
                .contextClick(rightClickButton)
                .perform(); // la méthode perform() effectue l'action de click(rightClickButton)
        // Après le click j'ai une attente explicite
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("rightClickMessage"))); // une fois le message visible, lance Assert
        Assert.assertEquals(messageElement.getText(), "You have done a right click");
        delay();
    }

    @Test
    public void hoverTest() {
        driver.get(MENU);

        WebElement mainItem2El = driver.findElement(
                By.xpath("//a[text()='Main Item 2']")); // j'accède au menu de 1er niveau

        Actions actions = new Actions(driver); // j'instancie l'objet Actions
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight / 3)");

        actions.moveToElement(mainItem2El).perform(); // Action déplacer la souris sur le menu
        delay(); // voir si le menu contextuel apparaît

        // j'accède aux 3 sous-éléments de ce menu
        WebElement subItem1El = driver.findElement(By.xpath("//a[text()='Sub Item']"));
        WebElement subItem2El = driver.findElement(By.xpath("//a[text()='Sub Item']"));
        WebElement subSubItemEl = driver.findElement(By.xpath("//a[text()='SUB SUB LIST »']"));
        // et je m'assure que ces sous-éléments sont bien affichés
        Assert.assertTrue(subItem1El.isDisplayed());
        Assert.assertTrue(subItem2El.isDisplayed());
        Assert.assertTrue(subSubItemEl.isDisplayed());

        System.out.println("********************** Main menu hovering is done ***************************");

        actions.moveToElement(subSubItemEl).perform(); // Action déplacer la souris sur le sous menu LIST
        delay();

        // j'accède aux 2 sous-éléments de ce menu
        WebElement subSubItem1El = driver.findElement(By.xpath("//a[text()='Sub Sub Item 1']"));
        WebElement subSubItem2El = driver.findElement(By.xpath("//a[text()='Sub Sub Item 2']"));
        // et je m'assure que ces sous-éléments sont bien affichés
        Assert.assertTrue(subSubItem1El.isDisplayed());
        Assert.assertTrue(subSubItem2El.isDisplayed());

        System.out.println("********************** Nested hovering is done ***************************");
    }

    @Test
    public void dragDropTest() {
        driver.get(DROP);

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight / 3)");

        new Actions(driver) // j'utilise l'API Actions pour le dragAndDrop()
                .dragAndDrop(draggable, droppable)
                .perform();
        Assert.assertEquals(droppable.getText(), "Dropped!");
        delay();
    }

    @AfterTest // cette annotation entraine l'exécution de cette méthode après chaque méthode @Test
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
    }
}
