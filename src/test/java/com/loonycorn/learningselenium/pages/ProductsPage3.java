package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//***************** initialiser PageFactory pour utiliser les annotations pour localiser les éléments dans le DOM HTML de la page
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class ProductsPage3 {

    private WebDriver driver;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartButton;

    // Constructeur
    public ProductsPage3(WebDriver driver) {
        this.driver = driver;
        // j'appelle "PageFactory.initElements" dans le Constructeur" et lui passe l'instance de l'objet Driver
        PageFactory.initElements(driver, this);
        // sans "PageFactory.initElements", l'annotation @FindBy() ne localisera pas les éléments
            // de plus, "PageFactory" permet le chargement différé des éléments Web
            // les éléments ne sont initialisés que lorsqu'ils sont utilisés pour une opération
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

    public void navigateToProductPageXpath(String productXpath) { // pour Firefox
        WebElement productLink = driver.findElement(By.xpath(productXpath));
        productLink.click();
    }

    public void mavigateToCart() { // ouvre le lien du panier
        cartButton.click();
    }
}
