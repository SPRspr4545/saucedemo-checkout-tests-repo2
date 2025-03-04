package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductsPage {

    private WebDriver driver;

    // Constructeur
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("inventory.html");
        // Vérifie l'URL actuelle si c'est celle de la page produits
    }

    public void navigateToProductPage(String productName) { // prend le nom du produit sous forme d'argument
        WebElement productLink = driver.findElement(By.linkText(productName)); // trouve le lien vers ce produit
        productLink.click(); // et click dessus
        // permet d'accéder à la page détaillée du produit en question
    }

    public void mavigateToCart() { // ouvre le lien du panier
        WebElement cartButton = driver.findElement(By.cssSelector(".shopping_cart_link"));
        cartButton.click();
    }
}
