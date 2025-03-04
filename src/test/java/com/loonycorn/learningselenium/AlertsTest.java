package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class AlertsTest {

    private static final String ALERT = "https://testpages.eviltester.com/styled/alerts/alert-test.html";

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

    private boolean isAlertPresent() { //méthode utilitaire pour vérifier la présence d'alerte (popup js)
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testAlert() {
        driver.get(ALERT);
        delay();
        driver.findElement(By.id("alertexamples")).click();
        delay();
        boolean isAlertPresent = isAlertPresent();
        Assert.assertTrue(isAlertPresent,"Alert not present");
    }

    @Test
    public void testGetAlertTextAndAccept() {
        driver.get(ALERT);
        driver.findElement(By.id("alertexamples")).click();

        Alert alert = driver.switchTo().alert();

        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(alertText, "I am an alert box!",
                "Alert text mismatch");

        delay();

        alert.accept(); // cette alerte ne comporte qu'un button
        //alert.dismiss(); // pour un alerte avec un seul button, il n'y a pas de différence entre "Accepter" & "Rejeter"
        // une fois l'alerte levée, le message apparaît en dessous du button
        Assert.assertEquals(driver.findElement(By.id("alertexplanation")).getText(),
                "You triggered and handled the alert dialog");

        delay();
    }

    @Test
    public void testWaitForAlert() {
        driver.get(ALERT);
        driver.findElement(By.id("alertexamples")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Attente explicite
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(alertText, "I am an alert box!",
                "Alert text mismatch");

        delay();

        //alert.accept(); // cette alerte ne comporte qu'un button
        alert.dismiss(); // pour un alerte avec un seul button, il n'y a pas de différence entre "Accepter" & "Rejeter"
        // une fois l'alerte levée, le message apparaît en dessous du button
        Assert.assertEquals(driver.findElement(By.id("alertexplanation")).getText(),
                "You triggered and handled the alert dialog");

        delay();
    }

    @Test
    public void testGetAlertTextAndAccept2() {
        driver.get(ALERT);
        driver.findElement(By.id("confirmexample")).click(); // click sur le 2ème btn

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Attente explicite
        Alert confirm = wait.until(ExpectedConditions.alertIsPresent());

        String promptText = confirm.getText();
        System.out.println("Alert text: " + promptText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(promptText, "I am a confirm alert",
                "Alert text mismatch");

        delay();

        confirm.accept(); // Clique [OK] pour "Accepter" & "Rejeter"
        //confirm.dismiss(); // Clique [Annuler] pour "Rejeter"

        String resultMessage = driver.findElement(By.id("confirmexplanation")).getText();
        System.out.println("Acceptance message: " + resultMessage);
        // une fois l'alerte levée, le message apparaît en dessous du button
        Assert.assertTrue(resultMessage.contains("You clicked OK"),
                "Confirm acceptance message not found");

        delay();
    }

    @Test
    public void testGetAlertTextAndDismiss() {
        driver.get(ALERT);
        driver.findElement(By.id("confirmexample")).click(); // click sur le 2ème btn

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Attente explicite
        Alert confirm = wait.until(ExpectedConditions.alertIsPresent());

        String promptText = confirm.getText();
        System.out.println("Alert text: " + promptText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(promptText, "I am a confirm alert",
                "Alert text mismatch");

        delay();

        //confirm.accept(); // Clique [OK] pour "Accepter" & "Rejeter"
        confirm.dismiss(); // Clique [Annuler] pour "Rejeter"

        String resultMessage = driver.findElement(By.id("confirmexplanation")).getText();
        System.out.println("Acceptance message: " + resultMessage);
        // une fois l'alerte levée, le message apparaît en dessous du button
        Assert.assertTrue(resultMessage.contains("You clicked Cancel"),
                "Confirm acceptance message not found");

        delay();
    }

    @Test
    public void testInputPromptTextAndAccept() {
        driver.get(ALERT);
        driver.findElement(By.id("promptexample")).click(); // click sur le 3ème btn

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Attente explicite
        Alert prompt = wait.until(ExpectedConditions.alertIsPresent());

        String alertText = prompt.getText();
        System.out.println("Alert text: " + alertText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(alertText, "I prompt you",
                "Alert text mismatch");

        String prompteMessage = "XXXXXXXXXXXXXXX";
        prompt.sendKeys(prompteMessage);

        delay();

        prompt.accept(); // Clique [OK] pour "Accepter" & "Rejeter"
        //prompt.dismiss(); // Clique [Annuler] pour "Rejeter"

        WebElement promptButton = driver.findElement(By.id("promptexample")); // je reçois le Button du début
        ((JavascriptExecutor)driver).executeScript( // je transforme l'instance du DRIVER en un JavascriptExecutor pour...
                "arguments[0].scrollIntoView(true);", promptButton); // ...faire défiler le Button afin de voir le message

        Assert.assertEquals(driver.findElement(By.id("promptreturn")).getText(), // accéder au texte de l'élément promptreturn...
                prompteMessage, // pour s'assurer qu'il correspond au texte saisi dans le champ de l'alert
                "Prompt message not found");

        String resultMessage = driver.findElement(By.id("promptexplanation")).getText(); // accéder aux texte de l'élément promptexplanation
        System.out.println("Acceptance message: " + resultMessage); // imprime le texte de l'élément promptexplanation
        Assert.assertTrue(resultMessage.contains("You clicked OK"), // vérifie le message est OK
                "Prompt acceptance message not found");

        delay();
    }

    @Test
    public void testInputPromptTextAndCancel() {
        driver.get(ALERT);
        driver.findElement(By.id("promptexample")).click(); // click sur le 3ème btn

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Attente explicite
        Alert prompt = wait.until(ExpectedConditions.alertIsPresent());

        String alertText = prompt.getText();
        System.out.println("Alert text: " + alertText);
        // je vérifie le message indiqué dans l'alerte
        Assert.assertEquals(alertText, "I prompt you",
                "Alert text mismatch");

        String prompteMessage = "XXXXXXXXXXXXXXX";
        prompt.sendKeys(prompteMessage);

        delay();

        //prompt.accept(); // Clique [OK] pour "Accepter" & "Rejeter"
        prompt.dismiss(); // Clique [Annuler] pour "Rejeter"

        WebElement promptButton = driver.findElement(By.id("promptexample")); // je reçois le Button du début
        ((JavascriptExecutor)driver).executeScript( // je transforme l'instance du DRIVER en un JavascriptExecutor pour...
                "arguments[0].scrollIntoView(true);", promptButton); // ...faire défiler le Button afin de voir le message

        Assert.assertEquals(driver.findElement(By.id("promptreturn")).getText(), // accéder au texte de l'élément promptreturn...
                "", "Prompt message not found"); // pour s'assurer qu'il est vide

        String resultMessage = driver.findElement(By.id("promptexplanation")).getText(); // accéder aux texte de l'élément promptexplanation
        System.out.println("Acceptance message: " + resultMessage); // imprime le texte de l'élément promptexplanation
        Assert.assertTrue(resultMessage.contains("You clicked Cancel"), // vérifie le message est OK
                "Prompt Cancel message not found");

        delay();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
