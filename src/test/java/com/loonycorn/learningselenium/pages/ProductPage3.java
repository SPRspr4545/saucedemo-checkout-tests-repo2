package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class ProductPage3 {

    private WebDriver driver;

    @FindBy(css = ".btn_inventory")
    private WebElement cartButton;

    // Constructeur
    public ProductPage3(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public  void addToCart() {
        cartButton.click(); // ajoute le produit au panier
    }

    public String getButtonText() {
        return cartButton.getText(); // renvoi le texte du bouton "Add to cart"/"Remove"
    }
}
