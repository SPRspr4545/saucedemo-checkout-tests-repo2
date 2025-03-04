package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductPage {

    private WebDriver driver;

    // Constructeur instancie ProductPage et passe l'instance WebDriver
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public  void addToCart() {
        WebElement addToCartButton = driver.findElement(By.cssSelector(".btn_inventory"));
        addToCartButton.click(); // ajoute le produit au panier
    }

    public String getButtonText() {
        WebElement cartButton = driver.findElement(By.cssSelector(".btn_inventory"));
        return cartButton.getText(); // renvoi le texte du bouton "Add to cart"/"Remove"
    }
}
