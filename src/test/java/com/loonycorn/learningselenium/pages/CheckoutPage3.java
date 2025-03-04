package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage3 {

    private WebDriver driver;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement zipCodeField;

    @FindBy(css = ".cart_button")
    private WebElement continueCheckoutButton;

    // Constructeur
    public CheckoutPage3(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-step-one.html");
        // Vérifie si l'URL actuelle est ok
    }

    public void  enterDetails(String firstName, String lastName, String zipCode) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        zipCodeField.sendKeys(zipCode);
    }

    public String getFirstNameFieldValue() {
        return  firstNameField.getAttribute("value");
    }

    public String getlastNameFieldValue() {
        return  lastNameField.getAttribute("value");
    }

    public String getzipCodeFieldValue() {
        return  zipCodeField.getAttribute("value");
    }

    public void continueCheckout() {
        continueCheckoutButton.click();
        // click sur le bouton continuer pour le paiement pour passer à la page suivante
    }
}
