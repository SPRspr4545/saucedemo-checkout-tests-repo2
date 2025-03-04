package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class OrderCompletionPage3 {

    private WebDriver driver;

    @FindBy(css = "[data-test='complete-header']")
    private WebElement header;

    @FindBy(css = "[data-test='complete-text']")
    private WebElement text;

    // Constructeur
    public OrderCompletionPage3(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-complete.html");
        // Vérifie si l'URL actuelle est ok
    }

    public String getHeaderTest() {
        return header.getText();
    }

    public String getBodyText() {
        return text.getText();
    }
}
