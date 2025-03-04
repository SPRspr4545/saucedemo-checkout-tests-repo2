package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//**************************************
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PageObjectModelTest3Slf4j {

    private Logger logger;

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

    @BeforeClass
    public void setUp() {
        // LoggerFactory.getLogger pour instancier l'enregistreur SLF4J
        logger = LoggerFactory.getLogger(PageObjectModelTest3Slf4j.class);

        logger.trace("Setting up Webdriver...");

        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME); // instance du Driver

        logger.trace("Setting up page classes for the Page Object Model...");

        // transmet le Driver aux Constructeurs pour les Class correspondantes
        loginPage = new LoginPage3(driver);
        productsPage = new ProductsPage3(driver);
        productPage = new ProductPage3(driver);
        cartPage = new CartPage3(driver);
        checkoutPage = new CheckoutPage3(driver);
        finalCheckoutPage = new FinalCheckoutPage3(driver);
        orderCompletionPage = new OrderCompletionPage3(driver);

        logger.trace("Completed set up of Webdriver and page classes...");

        driver.get(SITE);

        // les {} sont un espace réservé dans lequel on peut saisir des paramètres de logging...
        logger.debug("Navigate to site {}", SITE); // ...ici ce paramètre est le nom du SITE
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogin() {
        logger.debug("********** Starting testLogin **********");

        loginPage.login("standard_user", "secret_sauce");

        if (!productsPage.isPageOpened()) {
            logger.error("Login failed! Incorrect username or password.");
        }

        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");

        logger.info("User logger in successfully");
        // SLF4j ne prend pas en charge les arguments de saisie complexes :
        // logger.info(Map.of("user", "standard_user", "password", "secret_sauce"));
        logger.info("Username: {}, Password: {}", "standard_user", "secret_sauce");

    }

    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        logger.debug("********** Starting testAddBackpackToCart **********");

        productsPage.navigateToProductPage("Sauce Labs Backpack");

        productPage.addToCart();

        if (!productPage.getButtonText().equals("Remove")) {
            logger.warn("Failed to Sauce Labs Backpack to cart.");
        }

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        logger.info("Added Sauce Labs Backpack to cart");

        driver.navigate().back();

        logger.info("Navigated back to the products page after adding product");
    }

    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        logger.debug("********** Starting testAddFleeceJacketToCart **********");

        productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");

        productPage.addToCart();

        if (!productPage.getButtonText().equals("Remove")) {
            logger.warn("Failed to Sauce Fleece Jacket to cart.");
        }

        Assert.assertEquals(productPage.getButtonText(),"Remove",
                "Button text did not change");

        logger.info("Added Sauce Labs Fleece Jacket to cart");

        driver.navigate().back();

        logger.info("Navigated back to the products page after adding the second product");
    }

    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart() {
        logger.debug("********** Starting testCart **********");

        // méthode dans la Class ProductsPage pour accéder au panier
        productsPage.mavigateToCart();

        if (!cartPage.isPageOpened()) {
            // SLF4J n'a pas de niveau FATAL
            //logger.fatal("Cart page not loaded!");
            logger.error("Cart page not loaded!");
        }

        Assert.assertTrue(cartPage.isPageOpened(),"Cart page not loaded"); // la page est ouverte
        Assert.assertEquals(cartPage.getCartItemCount(),"2","Incorrect number of items in the cart"); // nombre de paniers
        Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout",
                "Incorrect button text on the cart page"); // bouton "checkout/Commander"

        if (!cartPage.productInCart("Sauce Labs Backpack") || !cartPage.productInCart("Sauce Labs Fleece Jacket")) {
            logger.warn("Either {} or {} not found in cart","Sauce Labs Backpack", "Sauce Labs Fleece Jacket");
        }

        Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack")); // vérifie si le produit est dans le panier
        Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket")); // vérifie si le produit est dans le panier

        logger.info("Validated items in cart");
    }

    @Test(dependsOnMethods = "testCart")
    public void testCheckout() {
        logger.debug("********** Starting testCheckout **********");

        cartPage.continuesCheckout(); // accède à la page de paiement

        if (!checkoutPage.isPageOpened()) {
            logger.error("First checkout page not loaded!");
        }

        Assert.assertTrue(checkoutPage.isPageOpened(),"Checkout page not loaded"); // vérifie que la page est ouverte
        checkoutPage.enterDetails("Peter","Hank","45430"); // Saisie les nom, prénom et zip code

        // vérifie les valeurs des champs saisie précédemment
        Assert.assertEquals(checkoutPage.getFirstNameFieldValue(),"Peter",
                "First name field value iis incorrect");
        Assert.assertEquals(checkoutPage.getlastNameFieldValue(),"Hank",
                "Last name field value iis incorrect");
        Assert.assertEquals(checkoutPage.getzipCodeFieldValue(),"45430",
                "Zip code field value iis incorrect");

        logger.info("Completed first checkout page");
    }

    @Test(dependsOnMethods = "testCheckout")
    public void testFinalCheckout() {
        logger.debug("********** Starting testFinalCheckout **********");

        checkoutPage.continueCheckout(); // la Class CheckoutPage contient la méthode continueCheckout() pour accéder à la "checkout-step-two.html"

        if (!finalCheckoutPage.isPageOpened()) {
            logger.error("Second checkout page not loaded!");
        }

        Assert.assertTrue(finalCheckoutPage.isPageOpened(),
                "Checkout page not loaded");
        Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(),
                "SauceCard #31337");
        Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(),
                "Free Pony Express Delivery!");
        Assert.assertEquals(finalCheckoutPage.getTotalLabel(),
                "Total: $86.38");

        logger.info("Second checkout page");
    }

    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        logger.debug("********** Starting testOrderCompletion **********");

        finalCheckoutPage.finishCheckout();

        if (!orderCompletionPage.isPageOpened()) {
            logger.error("Final checkout page not loaded!");
        }

        Assert.assertEquals(orderCompletionPage.getHeaderTest(),
                "Thank you for your order!");
        Assert.assertEquals(orderCompletionPage.getBodyText(),
                "Your order has been dispatched, and will arrive just as fast as the pony can get there!");

        logger.info("Completed order");
    }

    @AfterClass
    public void tearDown() {
        logger.trace("Quitting WebDriver...");

        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }

        logger.trace("Quit WebDriver...");
    }
}

