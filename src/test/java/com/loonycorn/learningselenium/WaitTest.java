package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait; //*****************
import org.openqa.selenium.support.ui.Wait; //**************
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration; //*********************************!!!
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class WaitTest {

    private static final String SITE = "https://testpages.eviltester.com/styled/index.html";
    private static final String SITE2 = "https://the-internet.herokuapp.com/";
    private WebDriver driver;
    // Suivre le temps d'exécution des scénarios de test:
    private long startTime; // variables primitives long: Valeur numérique entière
    private double duration; // variables primitives double: Valeur flottante à double précision

    private static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // cette function attend qu'un devienne cliquable
    private Function<WebDriver, WebElement> buttonBecomesClickable(By locator) {
        return driver -> {
            WebElement element = driver.findElement(locator);
            String disabled = element.getAttribute("disabled");
            return  disabled == null || !disabled.equals("true") ? element : null;
        };
    }

    @BeforeTest
    public void setUp() {
        // Créer l'instance du DriverFactory pour communiquer avec le navigateur
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
        startTime = System.currentTimeMillis();

    }

    @Test
    public void buttonClickTest() {
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(By.linkText("Dynamic Buttons Challenge 01"));
        dynamicButtonsLinkEl.click();

        WebElement startButtonsLinkEl = driver.findElement(By.id("button00"));
        startButtonsLinkEl.click();
        delay(1000);

        WebElement oneButtonsLinkEl = driver.findElement(By.id("button01"));
        oneButtonsLinkEl.click();
        delay(2000);

        WebElement twoButtonsLinkEl = driver.findElement(By.id("button02"));
        twoButtonsLinkEl.click();
        delay(4000);

        WebElement treeButtonsLinkEl = driver.findElement(By.id("button03"));
        treeButtonsLinkEl.click();
        delay(500);

        WebElement allButtonsClickedMessagLinkEl = driver.findElement(By.id("buttonmessage"));
        String message = allButtonsClickedMessagLinkEl.getText();

        Assert.assertEquals(message, "All Buttons Clicked",
                "Message after clicking all buttons is incorrect.");

    }

    @Test
    public void buttonClickTest2() {

        System.out.println("Implicit wait timeout (Before): " +
                driver.manage().timeouts().getImplicitWaitTimeout());
        // Méthode attente implicite applicable pendant toute la durée de l'objet WebDriver ************!!!
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));
        // la méthode implicitlyWait() s'applique à chaque appel de localisation d'élément à l'aide de findElement()

        System.out.println("Implicit wait timeout (After): " +
                driver.manage().timeouts().getImplicitWaitTimeout());
        //**********************************//
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(By.linkText("Dynamic Buttons Challenge 01"));
        dynamicButtonsLinkEl.click();

        WebElement startButtonsLinkEl = driver.findElement(By.id("button00"));
        startButtonsLinkEl.click();

        WebElement oneButtonsLinkEl = driver.findElement(By.id("button01"));
        oneButtonsLinkEl.click();

        WebElement twoButtonsLinkEl = driver.findElement(By.id("button02"));
        twoButtonsLinkEl.click();

        WebElement treeButtonsLinkEl = driver.findElement(By.id("button03"));
        treeButtonsLinkEl.click();

        WebElement allButtonsClickedMessagLinkEl = driver.findElement(By.id("buttonmessage"));
        String message = allButtonsClickedMessagLinkEl.getText();

        Assert.assertEquals(message, "All Buttons Clicked",
                "Message after clicking all buttons is incorrect.");
    }

    @Test
    public void buttonClickTest3() {
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(By.linkText("Dynamic Buttons Challenge 01"));
        dynamicButtonsLinkEl.click();

        WebElement startButtonsLinkEl = driver.findElement(By.id("button00"));
        startButtonsLinkEl.click();

        // Attente Explicite en instanciant l'objet WebDriverWait()
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // maintenant j'invoque la méthode WAIT qui prend la fonction .UNTIL() comme argument d'entrée,
        // pour évaluer si la condition visibilityOfElementLocated() est True ou False
        // la class ExpectedConditions contient de nombreuses méthodes d'assistance

        // Essai j'attend un élément qui n'existe pas...
        //      try {
        //          WebElement doesNotExistButtonEl = wait.until(
        //                  ExpectedConditions.visibilityOfElementLocated(By.id("doesNotExistButton")));
        //          doesNotExistButtonEl.click();
        //      } catch (Exception e) {
        //          e.printStackTrace();
        //      }

        WebElement oneButtonEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("button01")));
        oneButtonEl.click();

        WebElement twoButtonEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("button02")));
        twoButtonEl.click();

        WebElement treeButtonEl =  wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("button03")));
        treeButtonEl.click();

        WebElement allButtonsClickedMessagLinkEl = driver.findElement(By.id("buttonmessage"));
        String message = allButtonsClickedMessagLinkEl.getText();

        Assert.assertEquals(message, "All Buttons Clicked",
                "Message after clicking all buttons is incorrect.");

    }

    @Test
    public void buttonClickTest4() {
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(
                By.linkText("Dynamic Buttons Challenge 02"));
        dynamicButtonsLinkEl.click();

        // Attente Explicite en instanciant l'objet WebDriverWait()
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // presenceOfElementLocated() attend le button de démarrage
        WebElement startButtonEl = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("button00")));
        startButtonEl.click();

        // elementToBeClickable() attend que les buttons soient cliquable
        WebElement oneButtonEl = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button01")));
        oneButtonEl.click();

        WebElement twoButtonEl = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button02")));
        twoButtonEl.click();

        WebElement treeButtonEl =  wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button03")));
        treeButtonEl.click();

        WebElement allButtonsClickedMessagLinkEl = driver.findElement(By.id("buttonmessage"));
        String message = allButtonsClickedMessagLinkEl.getText();

        Assert.assertEquals(message, "All Buttons Clicked",
                "Message after clicking all buttons is incorrect.");

    }

    @Test
    public void buttonClickTest5() {
        driver.get(SITE2);

        WebElement dynamicControlsLinkEl = driver.findElement(
                By.linkText("Dynamic Controls"));
        dynamicControlsLinkEl.click();

        WebElement checkboxEl = driver.findElement(
                By.id("checkbox"));
        Assert.assertTrue(checkboxEl.isDisplayed());

        WebElement removeButtonEl = driver.findElement(
                By.cssSelector("button[onclick='swapCheckbox()']"));
        removeButtonEl.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(checkboxEl));

        List<WebElement> elements = driver.findElements(By.id("checkbox"));
        Assert.assertTrue(elements.isEmpty());

        WebElement addButtonEl = driver.findElement(By.cssSelector("button[onclick='swapCheckbox()']"));
        addButtonEl.click();

        checkboxEl = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkbox")));
        Assert.assertTrue(checkboxEl.isDisplayed());

    }

    @Test
    public void buttonClickTest6() {
        driver.get(SITE2);

        WebElement dynamicControlsLinkEl = driver.findElement(
                By.linkText("Dynamic Controls"));
        dynamicControlsLinkEl.click();

        WebElement textboxEl = driver.findElement(By.cssSelector("input[type='text']"));
        Assert.assertTrue(textboxEl.isDisplayed());
        Assert.assertFalse(textboxEl.isEnabled());

        WebElement enableButtonEl = driver.findElement(
                By.cssSelector("button[onclick='swapInput()']"));
        enableButtonEl.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(textboxEl)); // on attend que la zone de texte soit cliquable

        Assert.assertTrue(textboxEl.isDisplayed());
        Assert.assertTrue(textboxEl.isEnabled());

        // le button enableButtonEl devient maintenant disableButtonEl
        WebElement disableButtonEl = driver.findElement(
                By.cssSelector("button[onclick='swapInput()']"));
        disableButtonEl.click();

        // les conditions d'attente explicites sont un peu différentes dans le cas présente:
        // on passe elementToBeClickable() à ExpectedConditions.not()
        // cela signifie que l'attente explicite attend que l'élément ne soit plus elementToBeClickable(), ni activé
        wait.until(ExpectedConditions.not(
                ExpectedConditions.elementToBeClickable(textboxEl)));
        Assert.assertTrue(textboxEl.isDisplayed());
        Assert.assertFalse(textboxEl.isEnabled());

    }

    @Test
    public void testProgressBars() {
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(By.linkText("Multiple Progress Bars"));
        dynamicButtonsLinkEl.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // instancions l'objet WebDriverWait

        // 3 attentes explicites que le STATUS des 3 barres de progression atteigne 100%
        wait.until(ExpectedConditions.attributeToBe(By.id("progressbar0"), "value", "100")); // j'attend que l'attribut VALUE
        wait.until(ExpectedConditions.attributeToBe(By.id("progressbar1"), "value", "100")); // de l'élément progressbarXXX
        wait.until(ExpectedConditions.attributeToBe(By.id("progressbar2"), "value", "100")); // soit égale à 100

        // vérifier ensuite la présence du texte "Stopped"
        wait.until(ExpectedConditions.textToBe(By.id("status"), "Stopped"));
    }

    @Test
    public void buttonClickTest7() {
        driver.get(SITE);

        WebElement dynamicButtonsLinkEl = driver.findElement(
                By.linkText("Dynamic Buttons Challenge 02"));
        dynamicButtonsLinkEl.click();

        // au lieu d'utiliser l'attente WebDriverWait, utilisons l'objet FluentWait pour l'attente Fluide
        // FluentWait contient les méthode enchaînées pour config les param de l'attente
        // 1. le délai d'attente est de 10 secondes max
        // 2. avec un sondage toutes les 2 secondes (ce qui réduit le nombre par défaut de 500 millisecondes)
        // 3. config le message si exception au bout de ofSeconds(10)
        // 4. configurer explicitement pour ignorer NoSuchElementException jusqu'à la fin du délai imparti
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .withMessage("Action timed out!")
                .ignoring(NoSuchElementException.class);

        WebElement startButtonEl = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("button000")));
        startButtonEl.click();

        // elementToBeClickable() attend que les buttons soient cliquable
        WebElement oneButtonEl = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button01")));
        oneButtonEl.click();

        WebElement twoButtonEl = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button02")));
        twoButtonEl.click();

        WebElement treeButtonEl =  wait.until(
                ExpectedConditions.elementToBeClickable(By.id("button03")));
        treeButtonEl.click();

        WebElement allButtonsClickedMessagLinkEl = driver.findElement(By.id("buttonmessage"));
        String message = allButtonsClickedMessagLinkEl.getText();

        Assert.assertEquals(message, "All Buttons Clicked",
                "Message after clicking all buttons is incorrect.");

    }

    @AfterTest
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
        // Ensuite nous calculons la valeur EndTime du scénario de test pour calculer la durée du scénario de test
        long endTime = System.currentTimeMillis();
        duration = (endTime - startTime) / 1000.0;
        System.out.println("Total time taken: " + duration + " seconds");
    }
}
