package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.sql.Driver;

public class PageObjectModelTest2 {

    private static final String SITE = "https://www.saucedemo.com/";

    private WebDriver driver;

    // variables membres pour interagir avec les Class correspondantes aux pages du SITE de test
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private FinalCheckoutPage finalCheckoutPage;
    private OrderCompletionPage orderCompletionPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME); // instance du Driver

        // transmet le Driver aux Constructeurs pour les Class correspondantes
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        finalCheckoutPage = new FinalCheckoutPage(driver);
        orderCompletionPage = new OrderCompletionPage(driver);

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
    public void testLogin() {
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");

        delay();
    }

    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        productsPage.navigateToProductPage("Sauce Labs Backpack");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        delay();

        driver.navigate().back();
    }

    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(),"Remove",
                "Button text did not change");

        delay();

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

        delay();
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

        delay();
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

        delay();
    }

    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        finalCheckoutPage.finishCheckout();

        Assert.assertEquals(orderCompletionPage.getHeaderTest(),
                "Thank you for your order!");
        Assert.assertEquals(orderCompletionPage.getBodyText(),
                "Your order has been dispatched, and will arrive just as fast as the pony can get there!");

        delay();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
