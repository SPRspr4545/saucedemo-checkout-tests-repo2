package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//************* GRID
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
// implementation de l'API WebDriver conçue pour contrôler les drivers à distance via le serveur Selenium
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class PageObjectModelTest3Grid1 {

    private static final String SITE = "https://www.saucedemo.com/";

    private WebDriver driver;

    // variables membres pour interagir avec les Class correspondantes aux pages du SITE de test
    private LoginPage3 loginPage;
    private ProductsPage3 productsPage;
    private ProductPage3 productPage;
    private CartPage3 cartPage;
    private CheckoutPage3 checkoutPage;
    private FinalCheckoutPage3 finalCheckoutPage;
    private OrderCompletionPage3 orderCompletionPage;

    @parameters("browser")
    @BeforeClass
    public void setUp(String browser)throws MalformedURLException {
        // instancie un objet de la classe DesiredCapabilities pour définir les propriétés des navigateurs et les environnements des sessions WebDriver
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // permet de spécifier des attributs spécifiques au navigateur pour contrôler la façon dont WebDriver intéragit avec l'instance du navigateur

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless"); // recommandé lorsque les tests sont exécutés sur Selenium GRID, car il se peut que l'écran de la machine distante ne soit pas connecté
                capabilities.setBrowserName("chrome");
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                capabilities.setBrowserName("firefox");
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                capabilities.setBrowserName("edge");
                capabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browser);
        }

        String gridUrl = "http://192.168.1.67:4444"; // l'URL de Selenium GRID "java -jar selenium-server-4.28.1.jar standalone"

        driver = new RemoteWebDriver(new URL(gridUrl), capabilities); // instancie le RemoteWebDriver et précise la gridUrl et les fonctionnalités du navigateur
        driver.manage().window().maximize();

        // transmet le Driver aux Constructeurs pour les Class correspondantes
        loginPage = new LoginPage3(driver);
        productsPage = new ProductsPage3(driver);
        productPage = new ProductPage3(driver);
        cartPage = new CartPage3(driver);
        checkoutPage = new CheckoutPage3(driver);
        finalCheckoutPage = new FinalCheckoutPage3(driver);
        orderCompletionPage = new OrderCompletionPage3(driver);

        driver.get(SITE);
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogin() throws InterruptedException {
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");

        Thread.sleep(20000);
    }

    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        //productsPage.navigateToProductPage("Sauce Labs Backpack");
        productsPage.navigateToProductPageXpath("//*[@id=\"item_4_title_link\"]/div");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        driver.navigate().back();
    }

    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        //productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");
        productsPage.navigateToProductPageXpath("//*[@id=\"item_5_title_link\"]/div");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(),"Remove",
                "Button text did not change");

        driver.navigate().back();
    }

    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart() {
        // méthode dans la Class ProductsPage pour accéder au panier
        productsPage.mavigateToCart();

        Assert.assertTrue(cartPage.isPageOpened(),"Cart page not loaded"); // la page est ouverte
        Assert.assertEquals(cartPage.getCartItemCount(),"2","Incorrect number of items in the cart"); // nombre de paniers
        Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout",
                "Incorrect button text on the cart page"); // bouton "checkout/Commander"

        Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack")); // vérifie si le produit est dans le panier
        Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket")); // vérifie si le produit est dans le panier
    }

    @Test(dependsOnMethods = "testCart")
    public void testCheckout() {
        cartPage.continuesCheckout(); // accède à la page de paiement

        Assert.assertTrue(checkoutPage.isPageOpened(),"Checkout page not loaded"); // vérifie que la page est ouverte
        checkoutPage.enterDetails("Peter","Hank","45430"); // Saisie les nom, prénom et zip code

        // vérifie les valeurs des champs saisie précédemment
        Assert.assertEquals(checkoutPage.getFirstNameFieldValue(),"Peter",
                "First name field value iis incorrect");
        Assert.assertEquals(checkoutPage.getlastNameFieldValue(),"Hank",
                "Last name field value iis incorrect");
        Assert.assertEquals(checkoutPage.getzipCodeFieldValue(),"45430",
                "Zip code field value iis incorrect");
    }

    @Test(dependsOnMethods = "testCheckout")
    public void testFinalCheckout() {
        checkoutPage.continueCheckout(); // la Class CheckoutPage contient la méthode continueCheckout() pour accéder à la "checkout-step-two.html"

        Assert.assertTrue(finalCheckoutPage.isPageOpened(),
                "Checkout page not loaded");
        Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(),
                "SauceCard #31337");
        Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(),
                "Free Pony Express Delivery!");
        Assert.assertEquals(finalCheckoutPage.getTotalLabel(),
                "Total: $86.38");
    }

    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        finalCheckoutPage.finishCheckout();

        Assert.assertEquals(orderCompletionPage.getHeaderTest(),
                "Thank you for your order!");
        Assert.assertEquals(orderCompletionPage.getBodyText(),
                "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}

