package com.loonycorn.learningselenium;

import com.google.common.collect.ImmutableList;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;

// ************************************************************!
import org.openqa.selenium.devtools.DevTools;
// j'importe la réponse aux requêtes réseau et la sécurité depuis l'API Selenium DevTools, en fonction de la version du Driver utilisé:
// C:\Users\sam.pelletier\.cache\selenium\chromedriver\win64\131.0.6778.204>chromedriver --version
// ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7})
// les importations sont spécifiques à la version:
// Ce qui compte ici, c'est la version majeure: 131. le reste est secondaire
import org.openqa.selenium.devtools.v131.network.Network;
import org.openqa.selenium.devtools.v131.network.model.Request;
import org.openqa.selenium.devtools.v131.network.model.Response;
import org.openqa.selenium.devtools.v131.security.Security;
// ************************************************************!

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChromeDevToolsTest {

    private static final String SITE = "https://www.saucedemo.com/";
    private static final String EXPIRED = "https://expered.badssl.com/";
    private WebDriver driver;
    private DevTools devTools; //*****************!

    @BeforeTest
    public void setUp() throws AWTException { // ajout de l'exception "AWTException" à la signature de la méthode
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);

        // je convertie l'instance WebDriver pour ChromeDriver...
        devTools = ((ChromeDriver) driver).getDevTools(); // ...et j'invoque la méthode getDevTools() en référence aux DevTools Chrome
        devTools.createSession(); // pour pouvoir travailler dans une session DevTools
    }

    private static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCaptureRequestTraffic() {
        // initialisation liste requêtes capturées
        List<Request> capturedRequests = new CopyOnWriteArrayList<>(); // type particulier de liste tableau pour éviter l'exception de modification simultanée

        // active l'onglet Réseau sur DevTools
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

        // afin de saisir les requêtes envoyées par la page, j'ajoute un écouteur "addListener()" à DevTools
        // permet de capturer les requêtes envoyée par la page en écoutant "Network.requestWillBeSent()"
        devTools.addListener(Network.requestWillBeSent(), request -> {
            capturedRequests.add(request.getRequest());
        });

        driver.get(SITE); // j'accède au site

        Assert.assertFalse(capturedRequests.isEmpty(), // Affirme que "capturedRequests" ne sont plus vides
                "No requests were captured.");

        // je lance une boucle for pour parcourir les requêtes capturées
        for (Request request : capturedRequests) {
            System.out.println("Request URL: " + request.getUrl());
            System.out.println("Request Method: " + request.getMethod());
            System.out.println("------------------------------------------");
        }
    }

    @Test
    public void testCaptureResponseTraffic() {
        // initialisation liste Réponses capturées
        List<Response> capturedResponses = new CopyOnWriteArrayList<>(); // type particulier de liste tableau pour éviter l'exception de modification simultanée

        // active l'onglet Réseau sur DevTools
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

        // afin de saisir les Réponses envoyées par la page, j'ajoute un écouteur "addListener()" à DevTools
        // permet de capturer les Réponses envoyée par la page en écoutant "Network.responseReceived()"
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            capturedResponses.add(responseReceived.getResponse());
        });

        driver.get(SITE); // j'accède au site

        Assert.assertFalse(capturedResponses.isEmpty(), // Affirme que "capturedResponses" ne sont plus vides
                "No requests were captured.");

        // je lance une boucle for pour parcourir les réponses capturées
        for (Response response : capturedResponses) {
            System.out.println("Response URL: " + response.getUrl());
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Headers: " + response.getHeaders());
            System.out.println("------------------------------------------");
        }
    }

    @Test
    public void testCaptureConsoleLogs() {
        driver.get(SITE);

        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("Wrong_password"); // je saisie un mauvais password pour provoquer une erreur
        loginButton.click();

        // je ne peux pas utiliser d'attente explicite ici car les journeaux ne font pas partie du DOM HTML de la page web
        delay(20000);// pause de 20 secondes car les journeaux de la console n'apparaissent pas immédiatement

        // je précise à "LogEntries" les journeaux qui m'intéressent ".get(LogType.BROWSER)" ...
        LogEntries entries = driver.manage().logs().get(LogType.BROWSER); // ...qui font partie de Chrome DevTools
        List<LogEntry> logEntries = entries.getAll(); // j'obtient la liste de toutes les entrées de journal "getAll()"

        // je parcourt chaque "LogEntry" à l'aide d'une boucle FOR
        for (LogEntry entry : logEntries) {
            //System.out.println(entry);
            System.out.println("Level: " + entry.getLevel());
            System.out.println("Message: " + entry.getMessage());
            System.out.println("Timestamp: " + entry.getTimestamp());
            System.out.println("----------------------------------------------");
        }
    }

    @Test
    public void testBlockImageLoading() {
        driver.get(SITE);

        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.clear();
        usernameField.sendKeys("standard_user");
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        delay(5000);

        // j'active le suivi du réseau dans DevTools
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

        // je précise une liste de modèles d'expressions régulières pour les URLs que je souhaite bloquer
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg","*.jpeg","*.png")));

        driver.navigate().refresh();
        delay(5000);
    }

    @Test
    public void testBlockCSSResources() {
        driver.get(SITE);

        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.clear();
        usernameField.sendKeys("standard_user");
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        delay(5000);

        // j'active le suivi du réseau dans DevTools
        devTools.send(Network.enable(
                Optional.of(100000000), // j'ai spécifier une valeur pour "MaxBufferSize", on pouvait aussi utiler "Optional.empty()"
                Optional.empty(),
                Optional.empty()));

        // je précise une liste de modèles d'expressions régulières pour les URLs que je souhaite bloquer
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.css")));

        driver.navigate().refresh();
        delay(5000);

        // pour consulter la page sans CSS, faire défiler vers le bas de page:
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver; // lance le Driver en JavascriptExecutor
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight / 2);"); // lance le script
        delay(5000);
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight / 2);"); // relance le script
        delay(5000);
    }

    @Test
    public void testHandlingInsecureWebsite() { // ne fonctionne pas sur le poste car bloqué par CGI

        devTools.send(Security.enable()); // active la configuration de sécurité sur DevTools
        devTools.send(Security.setIgnoreCertificateErrors(true)); // demande à DevTools d'ignorer les erreurs de certification

        driver.get(EXPIRED);
        delay(5000);

        Assert.assertTrue(driver.getTitle().equals("expired.badssl.com"),"The title does not equal to 'expired.badssl.com'.");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
