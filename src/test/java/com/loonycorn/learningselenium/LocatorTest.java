package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class LocatorTest {

    private WebDriver driver; // configurer le driver en tant que variable membre privée de la classe de test
    private static final String SITE = "https://www.demoblaze.com/";
    private static final String CART = SITE + "cart.html";
    private static final String SITE2 = "https://www.saucedemo.com/";

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
    public void idLocatorsTest1() {
        driver.get(SITE);
        driver.get(CART);

        // WebElement est une fonctionnalité de base fournie par Selenium
        WebElement logoEl = driver.findElement(By.id("nava")); // findElement permet de localiser les éléments de la page
        System.out.println("Element text: " + logoEl.getText()); // getText fonctionne sur les éléments HTML contenant du texte

        Assert.assertTrue(logoEl.isDisplayed()); // isDisplayed méthode pour vérifier l'état "est affiché"
        Assert.assertTrue(logoEl.isEnabled()); // isEnabled méthode pour vérifier l'état "est activé"
        Assert.assertFalse(logoEl.isSelected()); // je m'assure qu'il n'est pas sélectionné car ce n'est pas un élément sélectionnable

        //logoEl.clear(); // clear est une méthode à utiliser avec une zone de saisie pur effacer le contenu d'une zone de texte
        //logoEl.submit(); // Soumettre pour envoyer le formulaire
        logoEl.click();
        delay();
        Assert.assertEquals(driver.getCurrentUrl(), SITE + "index.html");
    }

    @Test
    public void idLocatorsTest2() {
        driver.get(SITE);

        WebElement footerEl = driver.findElement(By.id("footc"));
        Assert.assertNotNull(footerEl);
        Assert.assertTrue(footerEl.isDisplayed());
        Assert.assertTrue(footerEl.isEnabled());

        WebElement cartEl = driver.findElement(By.id("cartur"));
        cartEl.click();
        Assert.assertEquals(driver.getCurrentUrl(), CART);

        footerEl = driver.findElement(By.id("footc")); // besoin d'obtenir cet élément pour cette page
        Assert.assertNotNull(footerEl);
        Assert.assertTrue(footerEl.isDisplayed());
        Assert.assertTrue(footerEl.isEnabled());

        delay();

        WebElement logoEl = driver.findElement(By.id("nava"));
        logoEl.click();
        Assert.assertEquals(driver.getCurrentUrl(), SITE + "index.html");

        delay();
    }

    @Test
    public void classLocatorsTest1() {
        driver.get(SITE);

        WebElement samsungGalaxyEL = driver.findElement(By.className("hrefch"));
        Assert.assertTrue(samsungGalaxyEL.isDisplayed());
        Assert.assertTrue(samsungGalaxyEL.isEnabled());
        samsungGalaxyEL.click();

        WebElement addToCartButtonEl = driver.findElement(By.className("btn-success"));
        addToCartButtonEl.click();

        // Attente explicite de la popup d'alerte avant de poursuivre
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent()); // wait.until permet de spécifier l'état explicite recherché (la popup: alertIsPresent)

        Alert alert = driver.switchTo().alert();
        alert.accept();

        WebElement cartEl = driver.findElement(By.id("cartur"));
        cartEl.click();

        WebElement cartItemEl = driver.findElement(By.className("success"));
        Assert.assertTrue(cartItemEl.isDisplayed());
        Assert.assertTrue(cartItemEl.isEnabled());
    }

    @Test
    public void tagNameLocatorsTest1() {
        driver.get(SITE);

        WebElement imgEl = driver.findElement(By.tagName("img"));

        String srcAttr = imgEl.getAttribute("src");
        Assert.assertNotNull(srcAttr);
        System.out.println("Image source: " + srcAttr);
    }

    @Test
    public void tagNameLocatorsTest2() {
        driver.get(SITE);

        List<WebElement> imgEls = driver.findElements(By.tagName("img")); // notez le pluriel à imgEls et à findElements
        for (WebElement imgEl : imgEls) {
            String srcAttr = imgEl.getAttribute("SRC");
            Assert.assertNotNull(srcAttr);

            System.out.println("Image source: " + srcAttr);
        }
    }

    @Test
    public void tagNameLocatorsTest3() {
        driver.get(SITE);

        List<WebElement> categoryEls = driver.findElements(By.id("itemc")); // notez le pluriel à imgEls et à findElements
        for (WebElement categoryEl : categoryEls) {
            System.out.println("-----------------");
            System.out.println("Category clicked: " + categoryEl.getText());

            categoryEl.click();
            delay();

            WebElement conteinerEl = driver.findElement(By.id("tbodyid"));
            List<WebElement> linkEls = conteinerEl.findElements(By.tagName("a"));
            for (WebElement linkEl : linkEls) {
                String hrefAttr = linkEl.getAttribute("href");
                Assert.assertNotNull(hrefAttr);
                System.out.println("URL: " + hrefAttr);
            }


        }

    }

    @Test
    public void NameLocatorsTest() {
        driver.get(SITE);

        WebElement formEl = driver.findElement(By.name("frm")); // <form name="frm" ...
        Assert.assertTrue(formEl.isEnabled());
        Assert.assertTrue(formEl.isDisplayed());

        List<WebElement> buttonsEl = formEl.findElements(By.tagName("button")); // <button ...
        Assert.assertEquals(buttonsEl.size(), 2);
        Assert.assertTrue(buttonsEl.get(0).isDisplayed());
        Assert.assertTrue(buttonsEl.get(1).isDisplayed());

        System.out.println("button text: " + buttonsEl.get(0).getText());
        System.out.println("button text: " + buttonsEl.get(1).getText());

    }

    @Test
    public void cssSelectorsLocatorsTest() {
        driver.get(SITE2);

        WebElement loginEl = driver.findElement(By.cssSelector("div.login-box")); // par optimisation je ne fais pas la recherche dans l'intégralité du DOM
        Assert.assertTrue(loginEl.isEnabled());
        Assert.assertTrue(loginEl.isDisplayed());

        WebElement userNameEl = loginEl.findElement(By.cssSelector("#user-name")); // le préfixe # indique au selecteur css que nous identifions son ID
        WebElement passwordEl = loginEl.findElement(By.cssSelector("input#password"));

        userNameEl.sendKeys("standard_user");
        passwordEl.sendKeys("secret_sauce");

        delay();
        WebElement submitButtonEl = loginEl.findElement(By.cssSelector(".submit-button")); // le Point de préfixe indique une class css
        submitButtonEl.submit();
        delay();
        Assert.assertEquals(driver.getCurrentUrl(), SITE2 + "inventory.html");

    }

    @Test
    public void cssSelectorsLocatorsTest2() {
        driver.get(SITE2);

        WebElement loginEl = driver.findElement(By.cssSelector("div[class='login-box']")); // la spécification des différents attributs entre crochets
        Assert.assertTrue(loginEl.isEnabled());
        Assert.assertTrue(loginEl.isDisplayed());

        WebElement userNameEl = loginEl.findElement(By.cssSelector("input[id='user-name']")); // la spécification des différents attributs entre crochets
        WebElement passwordEl = loginEl.findElement(By.cssSelector("input[id='password']"));
        userNameEl.sendKeys("standard_user");
        passwordEl.sendKeys("secret_sauce");
        delay();
        WebElement submitButtonEl = loginEl.findElement(By.cssSelector("input[data-test='login-button']")); // la spécification des différents attributs entre crochets
        submitButtonEl.submit();
        delay();
        Assert.assertEquals(driver.getCurrentUrl(), SITE2 + "inventory.html");

        // Add first product to cart
        WebElement addToCartBackpackEl = driver.findElement(By.cssSelector(
                "button.btn_inventory[name='add-to-cart-sauce-labs-backpack']"));
        addToCartBackpackEl.click();

        // Add second product to cart
        WebElement addToCartOnesieEl = driver.findElement(By.cssSelector(
                "button#add-to-cart-sauce-labs-onesie[data-test='add-to-cart-sauce-labs-onesie']"));
        addToCartOnesieEl.click();
        delay();

        // Click on shopping cart link using a prefix of an attribute
        WebElement shoppingCartLinkEl = driver.findElement(By.cssSelector(
                "a[class^='shopping_cart']")); // le signe ^ indique que la class commence par le préfixe shopping_cart... même si elle est : shopping_cart_link
        shoppingCartLinkEl.click();
        delay();

        // Click on the continue shopping button using the suffix of an attribute
        WebElement continueShoppingButtonEl = driver.findElement(By.cssSelector(
                "button[id$='inue-shopping']")); // le signe $ signifie que l'id fini par le suffixe ...inue-shopping même s'il est : continue-shopping
        continueShoppingButtonEl.click();
        delay();
        Assert.assertEquals(driver.getCurrentUrl(), SITE2 + "inventory.html");

    }

    @Test
    public void xpathLocatorsTest() {
        driver.get(SITE2);

        WebElement loginEl = driver.findElement(By.cssSelector("div[class='login-box']"));
        Assert.assertTrue(loginEl.isDisplayed());
        Assert.assertTrue(loginEl.isDisplayed());

        WebElement userNameEl = driver.findElement(By.xpath(
                "/html/body/div/div/div[2]/div[1]/div/div/form/div[1]/input")); // l'accès aux éléments en utilisant le XPath complet n'est pas recommandé
        WebElement passwordEl = driver.findElement(By.xpath("//*[@id=\"password\"]")); // copier uniquement XPath, et non le complet qui lui est instable
        userNameEl.sendKeys("standard_user");
        passwordEl.sendKeys("secret_sauce");
        delay();
        WebElement submitButtonEl = loginEl.findElement(By.xpath("//*[@id=\"login-button\"]"));
        submitButtonEl.submit();
        delay();
        Assert.assertEquals(driver.getCurrentUrl(), SITE2 + "inventory.html");

        // SELENIUM propose 2 autres localisateurs très utiles :
        WebElement backpackEl = driver.findElement(By.linkText("Sauce Labs Backpack")); // linkText permet de spécifier le texte du lien
        backpackEl.click();
        delay();
        driver.navigate().back();
        WebElement onesieEl = driver.findElement(By.partialLinkText("Onesie")); // partialLinkText permet de ne spécifier qu'une partie du texte du lien
        onesieEl.click();
        delay();
        driver.navigate().back();


    }

    @AfterTest // cette annotation entraine l'exécution de cette méthode après chaque méthode @Test
    public void tearDown() {
        if (driver != null) { // si driver a été instancié n'est pas null
            driver.quit(); // supprime le pilote
        }
    }

}
