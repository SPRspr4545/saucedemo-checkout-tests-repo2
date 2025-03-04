package com.loonycorn.learningselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

//***************** initialiser PageFactory pour utiliser les annotations pour localiser les éléments dans le DOM HTML de la page
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class LoginPage3 {

    private WebDriver driver;

    @FindBy(id = "user-name") // cette annotation est l'équivalent de ".findElement(By.id())"
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    // Constructeur instancie LoginPage et passe l'instance WebDriver
    public LoginPage3(WebDriver driver) {
        this.driver = driver;
        // j'appelle "PageFactory.initElements" dans le Constructeur" et lui passe l'instance de l'objet Driver
        PageFactory.initElements(driver, this);
        // sans "PageFactory.initElements", l'annotation @FindBy() ne localisera pas les éléments
            // de plus, "PageFactory" permet le chargement différé des éléments Web
            // les éléments ne sont initialisés que lorsqu'ils sont utilisés pour une opération
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
