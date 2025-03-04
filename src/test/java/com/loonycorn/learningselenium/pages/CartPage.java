package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {

    private WebDriver driver;

    // Constructeur
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("cart.html");
        // Vérifie l'URL actuelle si c'est celle du panier
    }

    public String getCartItemCount() {
        return driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        // trouve l'élément où le nombre de paniers est affiché et renvoie son texte
    }

    public boolean productInCart(String productName) {
        // vérifier si un produit en particulier est dans le panier
        WebElement product = driver.findElement(By.xpath(
                String.format("//div[@class='inventory_item_name'][text()='%s']", productName)));
        // https://waytolearnx.com/2020/03/java-string-format.html
            // "String.format()" = concaténation de 2 chaînes
            // le placeholder "%s" est un espace réservé à la valeur de "productName"

        return product != null;
    }

    public String getContinueButtonText() {
        return driver.findElement(By.cssSelector(".btn_action")).getText();
        // renvoit le texte du bouton "checkout/Commander"
    }

    public void continuesCheckout() {
        driver.findElement(By.cssSelector(".btn_action")).click();
        // click sur le bouton "checkout/Commander"
    }
}
