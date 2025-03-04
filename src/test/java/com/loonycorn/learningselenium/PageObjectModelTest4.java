package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PageObjectModelTest4 {

    private static final String SITE = "https://www.saucedemo.com/";

    private WebDriver driver;

    // variables membres pour interagir avec les Class correspondantes aux pages du SITE de test
    private LoginPage3 loginPage;
    private ProductsPage4 productsPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME); // instance du Driver

        // transmet le Driver aux Constructeurs pour les Class correspondantes
        loginPage = new LoginPage3(driver);
        productsPage = new ProductsPage4(driver);

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
    public void testAllInventoryItemsUsingFindAll() {
        List<WebElement> allItems = productsPage.getAllInventoryItemsUsingFindAll();
        System.out.println("-----List of items using FindAll: " + allItems.size());

        for (WebElement item : allItems) {
            System.out.println(item.getText());
        }

        Assert.assertTrue(allItems.size() > 0,
                "At least one inventory item should be present");
    }

    @Test(dependsOnMethods = "testAllInventoryItemsUsingFindAll")
    public void testAllInventoryItemsUsingFindBys() {
        List<WebElement> allItems = productsPage.getAllInventoryItemsUsingFindBys();
        System.out.println("-----List of items using FindBys: " + allItems.size());

        for (WebElement item : allItems) {
            System.out.println(item.getText());
        }

        Assert.assertTrue(allItems.size() > 0,
                "At least one inventory item should be present");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
