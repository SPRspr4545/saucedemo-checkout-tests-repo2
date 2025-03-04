package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.Comparator;
import java.util.List;

public class ProductsPage4 {

    private WebDriver driver;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartButton;

    //************************* Exemples d'annotations  @FindAll et @FindBys ****************/
    // l'annotation @FindAll() fonctionne de manière logique "OU" et trouvera les éléments correspondant aux localisateurs @FindBy()
    @FindAll({@FindBy(id = "item_0_title_link"), @FindBy(id = "item_1_title_link"),
            @FindBy(id = "item_2_title_link"), @FindBy(id = "item_3_title_link"),
            @FindBy(id = "item_4_title_link"), @FindBy(id = "item_5_title_link")})
    private List<WebElement> allInventoryItemsUsingFindAll; // variable membre contient List<WebElement> de tous les articles

    // l'annotation @FindBys() est un ET logique, les 2 conditions @FindBy() doivent être remplies pour localiser le/les élément/s
    // un élément qui correspond à l'une de ces conditions ne sera pas localisé
    @FindBys({
            @FindBy(css = ".inventory_item"), // remonte tout le texte (titre, prix, descriptio...) de l'ensemble des articles
            @FindBy(css = ".inventory_item_name ")
            //@FindBy(className = "inventory_item"),
            //@FindBy(className = "inventory_item_name ") // @FindBy(className) fait bugger l'appli >> choix du @FindBy(css = ".*")
    })
    private List<WebElement> allInventoryItemsUsingFindBys;
    //***************************************************************************************/

    // Constructeur
    public ProductsPage4(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
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
        cartButton.click();
    }

    //******************** Méthode correspondant à l'annotations @FindAll ****************/
    public List<WebElement> getAllInventoryItemsUsingFindAll() {
        return allInventoryItemsUsingFindAll;
    }

    //******************** Méthode correspondant à l'annotations @FindBys ****************/
    public List<WebElement> getAllInventoryItemsUsingFindBys() {
        return allInventoryItemsUsingFindBys;
    }
}
