package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderCompletionPage {

    private WebDriver driver;

    // Constructeur
    public OrderCompletionPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-complete.html");
        // Vérifie si l'URL actuelle est ok
    }

    public String getHeaderTest() {
        return driver.findElement(By.cssSelector(
                "[data-test='complete-header']")).getText();
    }

    public String getBodyText() {
        return driver.findElement(By.cssSelector(
                "[data-test='complete-text']")).getText();
    }
}
