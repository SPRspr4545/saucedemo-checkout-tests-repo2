package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class CartPage3 {

    private WebDriver driver;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartItemCount;

    @FindBy(css = ".btn_action")
    private WebElement continueButton;

    // Constructeur
    public CartPage3(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("cart.html");
        // Vérifie l'URL actuelle si c'est celle du panier
    }

    public String getCartItemCount() {
        return cartItemCount.getText();
        // trouve l'élément où le nombre de paniers est affiché et renvoie son texte
    }

    public boolean productInCart(String productName) {
        // vérifier si un produit en particulier est dans le panier
        WebElement product = driver.findElement(By.xpath(
                String.format("//div[@class='inventory_item_name'][text()='%s']", productName)));
        // https://waytolearnx.com/2020/03/java-string-format.html
            // "String.format()" = concaténation de 2 chaînes
            // avec un placeholder "%s" est un espace réservé à la valeur de "productName"

        return product != null;
    }

    public String getContinueButtonText() {
        return continueButton.getText();
        // renvoit le texte du bouton "checkout/Commander"
    }

    public void continuesCheckout() {
        continueButton.click();
        // click sur le bouton "checkout/Commander"
    }
}
