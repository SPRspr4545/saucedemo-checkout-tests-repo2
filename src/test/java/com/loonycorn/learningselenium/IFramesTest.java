package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.security.interfaces.XECKey;
import java.time.Duration;
import java.util.List;

public class IFramesTest {

    private static final String SITE = "https://omayo.blogspot.com/";
    private static final String IFRAME = "file:///C:/Selenium/learning-selenium/src/test/java/templates/multiple_iframes.html";
    private static final String NESTED_IFRAME = "file:///C:/Selenium/learning-selenium/src/test/java/templates/nested_iframes.html";

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
    public void testNumberOfIFrames1() {
        driver.get(SITE);

        int numberOfIFrames = driver.findElements(By.tagName("iframe")).size(); // findElementS (au pluriel)

        System.out.println("Number of iframes on page: " + numberOfIFrames);

        Assert.assertEquals(numberOfIFrames, 3, "Number of iframes is equal to three.");
    }

    @Test
    public void testInputValue() {
        driver.get(SITE);

        WebElement iframeContent = driver.findElement(By.id("navbar-iframe"));
        driver.switchTo().frame(iframeContent); // accéder à l'iframe pour intéragir avec son contenu

        // WebElement inputBox = driver.findElement(By.id("b-query")); // fonctionne pas, l'id a disparu !?
        WebElement inputBox = driver.findElement(By.cssSelector("input[name='q']"));

        String inputText = "Selenium Testing";
        inputBox.sendKeys(inputText);

        delay();

        String inputValue = inputBox.getAttribute("value"); // obtenir l'attribut "value" de la zone de texte de saisie

        System.out.println("Input value: " + inputValue);

        Assert.assertEquals(inputValue, inputText, "Input value does not match"); // affirme qu'inputValue = inputText

    }

    @Test
    public void testSearch() {
        driver.get(SITE);

        WebElement iframeContent = driver.findElement(By.id("navbar-iframe"));
        driver.switchTo().frame(iframeContent); // accéder à l'iframe pour intéragir avec son contenu

        // WebElement inputBox = driver.findElement(By.id("b-query")); // fonctionne pas, l'id a disparu !?
        WebElement inputBox = driver.findElement(By.cssSelector("input[name='q']"));

        String inputText = "Selenium Testing";
        inputBox.sendKeys(inputText);

        delay();

        //WebElement submitButton = driver.findElement(By.id("d-query-icon")); // fonctionne pas, l'id a disparu !?
        WebElement submitButton = driver.findElement(By.cssSelector("span[aria-label='Search']"));
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // attente explicite
        wait.until(ExpectedConditions.urlContains("q=Selenium+Testing")); // j'attends que l'URL contienne cette sous-chaîne

        Assert.assertTrue(driver.getCurrentUrl().contains("q=Selenium+Testing"), // simple vérification
                "URL after clicking search button is incorrect");

        delay();
    }

    @Test
    public void testClickBackOnMainPage() {
        driver.get(SITE);

        WebElement iframeContent = driver.findElement(By.id("navbar-iframe"));
        driver.switchTo().frame(iframeContent); // accéder à l'iframe pour intéragir avec son contenu

        // WebElement inputBox = driver.findElement(By.id("b-query")); // fonctionne pas, l'id a disparu !?
        WebElement inputBox = driver.findElement(By.cssSelector("input[name='q']"));
        inputBox.sendKeys("Selenium Testing");

        delay();

        //WebElement submitButton = driver.findElement(By.id("d-query-icon")); // fonctionne pas, l'id a disparu !?
        WebElement submitButton = driver.findElement(By.cssSelector("span[aria-label='Search']"));
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // attente explicite
        wait.until(ExpectedConditions.urlContains("q=Selenium+Testing")); // j'attends que l'URL contienne cette sous-chaîne

        delay();

        // si on travail dans le contexte d'un iframe et que l'on souhaite revenir à la fenêtre principale
        driver.switchTo().defaultContent(); // revenir à la fenêtre principale

        WebElement homePageTitle = driver.findElement(By.linkText("omayo (QAFox.com)")); // obtenir le lien/titre du site et...
        homePageTitle.click(); // ...clicker dessus pour revenir à l'accueil et...

        wait.until(ExpectedConditions.urlToBe(SITE)); // ... j'attends que l'URL soit identique au "driver.get(SITE)"

        delay();
    }

    @Test
    public void testNumberOfIFrames2() {
        driver.get(IFRAME);
        int numberOfIFrames = driver.findElements(By.tagName("iframe")).size(); // findElementS (au pluriel)
        Assert.assertEquals(numberOfIFrames, 2, "Number of iframes is not as expected");
    }

    @Test
    public void testCopyH1FromIframe1ToTextarea() {
        driver.get(IFRAME);

        driver.switchTo().frame("iframe1"); // accéder au 1er iframe
        WebElement h1Element = driver.findElement(By.tagName("h1")); // récupère l'élément h1
        String h1Text = h1Element.getText(); // récupère le texte de l'élément h1

        System.out.println("Iframe1 heading: " + h1Text);

        driver.switchTo().defaultContent(); // je retourne à la page principale

        WebElement textarea = driver.findElement(By.tagName("textarea")); // accède au textarea pour...
        textarea.clear();
        textarea.sendKeys(h1Text); // ...y coller le texte

        Assert.assertEquals(textarea.getAttribute("value"), h1Text,
                "Text in textarea does not match h1 text from iframe1");

        delay();

    }

    @Test
    public void testCopyH1FromIframesToTextarea() {
        driver.get(IFRAME);

        // accéder au 1er iframe
        driver.switchTo().frame("iframe1");
        WebElement pElement1 = driver.findElement(By.tagName("p")); // récupère l'élément p
        String pText1 = pElement1.getText(); // récupère le texte de l'élément p

        System.out.println("Iframe1 text: " + pText1);

        driver.switchTo().defaultContent(); // je retourne à la page principale pour accéder à l'iframe2 !!!

        // accéder au 2eme iframe
        driver.switchTo().frame("iframe2");
        WebElement pElement2 = driver.findElement(By.tagName("p")); // récupère l'élément p
        String pText2 = pElement2.getText(); // récupère le texte de l'élément p

        System.out.println("Iframe2 text: " + pText2);

        driver.switchTo().defaultContent(); // je retourne à la page principale

        WebElement textarea = driver.findElement(By.tagName("textarea")); // accède au textarea pour...
        textarea.clear(); // efface le contenu du textarea
        textarea.sendKeys(pText1 + " " + pText2); // ...y coller les 2 textes provenant des 2 iframes

        delay();

        String expectedText = pText1 + " " + pText2;
        Assert.assertEquals(textarea.getAttribute("value"), expectedText,
                "Text in textarea does not match h1 text from iframe1");

    }

    @Test
    public void testNumberOfIFrames3() {
        driver.get(NESTED_IFRAME);
        //driver.get(SITE);

        int numberOfIFrames = countNestedIframes(driver.findElement(By.tagName("body"))); // findElementS (au pluriel)
        System.out.println("Number of iframes: " + numberOfIFrames);
        Assert.assertEquals(numberOfIFrames, 3, "Number of iframes is not as expected");
    }

    private int countNestedIframes(WebElement element) {
        int count = 0;

        for (WebElement iframe : element.findElements(By.tagName("iframe"))) { // FOR parcours tous les éléments iframe qui se trouvent imbriqués dans WebElement
            count++; // pour chaque iframe trouvé on incrémente count
            driver.switchTo().frame(iframe); // accède à l'iframe pour trouver les iframes imbriqués dans cet iframe

            // appel récursif avec countNestedIframes() pour parcourir le DOM HTML en...
            count += countNestedIframes(driver.findElement(By.tagName("body"))); // ...comptant tous les iframes imbriqués dans l'iframe supérieur
            driver.switchTo().parentFrame(); // en sortant de l'appel récursif on passe au parentFrame pour revenir au contexte de l'iframe actuel
            // cela continuera tant que des iframes seront imbriquées
        }
        return count;
    }

    @Test
    public void testCopyH1FromIframeCToTextarea() {
        driver.get(NESTED_IFRAME);

        driver.switchTo().frame("iframeA"); // accéder au 1er iframe "A"
        driver.switchTo().frame("iframeB"); // accéder à l'iframeB qui est intégré à l'iframeA
        driver.switchTo().frame("iframeC"); // accéder à l'iframeC qui est intégré à l'iframeB

        WebElement h1Element = driver.findElement(By.tagName("h1")); // récupère l'élément h1
        String h1Text = h1Element.getText(); // récupère le texte de l'élément h1

        System.out.println("IframeC heading: " + h1Text);

        driver.switchTo().defaultContent(); // je retourne à la page principale

        WebElement textarea = driver.findElement(By.tagName("textarea")); // accède au textarea pour...
        textarea.clear();
        textarea.sendKeys(h1Text); // ...y coller le texte

        Assert.assertEquals(textarea.getAttribute("value"), h1Text,
                "Text in textarea does not match h1 text from iframeC");

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
