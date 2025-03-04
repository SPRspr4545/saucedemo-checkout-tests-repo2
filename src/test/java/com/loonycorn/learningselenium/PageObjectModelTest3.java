package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// importations pour les différentes annotations
    //import io.qameta.allure.Description;
    //import io.qameta.allure.Epic;
    //import io.qameta.allure.Feature;
    //import io.qameta.allure.Story;
import io.qameta.allure.*;

// informations supplémentaires sur chaque scénarios de test
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;


// cette annotation est utilisée pour marquer une Class ou une méthode dans le cadre d'une plus grande épopée
// les épopées représentent de grands objectifs commerciaux (destiné à fournir une vue d'ensemble)
@Epic("Epic:Checkout flow Saucedemo")
public class PageObjectModelTest3 {

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
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME); // instance du Driver

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

    @Feature("Feature:Login flow") // Feature est utilisée pour marquer une Class ou une méthode de test dans le cadre d'une fonctionnalité spécifique de l'application
    @Story("Story:Login") // Story est utilisé pour relier les méthodes de test à des témoignages d'utilisateurs spécifiques
    @Description("Description:Test to verify login functionality")
    @Link("https://www.saucedemo.com/")
    @Tag("Login") // Tag permet de baliser les tests avec des tags arbitraires
    @Owner("Charles Darwin") // Propriétaire ou responsable d'un scénario de test
    @Severity(SeverityLevel.BLOCKER)
    @Step("Login ans verify")
    @Test
    public void testLogin() {
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");

        delay();
    }

    @Feature("Feature:Add products flow")
    @Story("Story:Add products")
    @Description("Description:Test to add a backpack to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("products"), @Tag("add product")})
    @Owner("Jenny Li")
    @Severity(SeverityLevel.NORMAL)
    @Step("Add product to cart") // Etape extérieure qui comprend 2 sous-étapes "Allure.step()"
    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        productsPage.navigateToProductPage("Sauce Labs Backpack");

        productPage.addToCart();

        Allure.step("Add item to cart"); // indique les étapes individuelles de ce scénario de test

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        delay();

        Allure.step("Navigate back ton products page"); // indique les étapes individuelles de ce scénario de test

        driver.navigate().back();
    }

    @Feature("Feature:Add products flow")
    @Story("Story:Add products")
    @Description("Description:Test to add a FleeceJacket to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("products"), @Tag("add product")})
    @Owner("Jenny Li")
    @Severity(SeverityLevel.NORMAL)
    @Step("Add product to cart") // Etape extérieure qui comprend 2 sous-étapes "Allure.step()"
    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");

        productPage.addToCart();

        Allure.step("Add item to cart"); // indique les étapes individuelles de ce scénario de test

        Assert.assertEquals(productPage.getButtonText(),"Remove",
                "Button text did not change");

        delay();

        Allure.step("Navigate back ton products page"); // indique les étapes individuelles de ce scénario de test

        driver.navigate().back();
    }

    @Feature("Feature:View cart flow")
    @Story("Story:View cart")
    @Description("Description:Test to verify the cart contents")
    @Link("https://www.saucedemo.com/cart.html")
    @Tags({@Tag("cart"), @Tag("checkout")})
    @Owner("Charles Darwin")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Validate items in cart")
    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart() {
        // la spécification des étapes est ici un peu plus complexe
        Allure.step("Navigate to cart and verify state", step -> { // Message d'étape
            // méthode dans la Class ProductsPage pour accéder au panier
            productsPage.mavigateToCart();

            Assert.assertTrue(cartPage.isPageOpened(),"Cart page not loaded"); // la page est ouverte
            Assert.assertEquals(cartPage.getCartItemCount(),"2","Incorrect number of items in the cart"); // nombre de paniers
            Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout",
                    "Incorrect button text on the cart page"); // bouton "checkout/Commander"

            // indique des informations supplémentaires pour le rapport Allure...
            Allure.addAttachment("Cart item count", cartPage.getCartItemCount()); // ...Nbre d'articles dans le panier
        });

        // autre spécification d'étape
        Allure.step("Verify products incart", step -> {
            Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack")); // vérifie si le produit est dans le panier
            Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket")); // vérifie si le produit est dans le panier
        });

        delay();
    }

    @Feature("Feature:Checkout flow")
    @Story("Story:Checkout")
    @Description("Description:Test to verify the Checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-one.html")
    @Tag("checkout")
    @Owner("Charles Darwin")
    @Flaky // Test connu pour produire des résultats incohérents
    @Severity(SeverityLevel.MINOR)
    @Step("Verify checkout page")
    @Test(dependsOnMethods = "testCart")
    public void testCheckout() {
        Allure.step("Navigate to checkout and enter detail", step -> {
            cartPage.continuesCheckout(); // accède à la page de paiement

            Assert.assertTrue(checkoutPage.isPageOpened(),"Checkout page not loaded"); // vérifie que la page est ouverte
            checkoutPage.enterDetails("Peter","Hank","45430"); // Saisie les nom, prénom et zip code
        });

        Allure.step("Verify entered details", step -> {
            // vérifie les valeurs des champs saisie précédemment
            Assert.assertEquals(checkoutPage.getFirstNameFieldValue(),"Peter",
                    "First name field value iis incorrect");
            Assert.assertEquals(checkoutPage.getlastNameFieldValue(),"Hank",
                    "Last name field value iis incorrect");
            Assert.assertEquals(checkoutPage.getzipCodeFieldValue(),"45430",
                    "Zip code field value iis incorrect");
        });

        delay();
    }

    @Feature("Feature:Checkout flow")
    @Story("Story:Checkout")
    @Description("Description:Test to verify the final Checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-two.html")
    @Tag("checkout")
    @Owner("Charles Darwin")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Verify final checkout page")
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

    @Feature("Feature:Checkout flow")
    @Story("Story:Order completion")
    @Description("Description:Test to verify the Order completion functionality")
    @Link("https://www.saucedemo.com/checkout-complete.html")
    @Tags({@Tag("order completion"), @Tag("checkout")})
    @Owner("Jackson Smith")
    @Flaky
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Verify order completion page")
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

