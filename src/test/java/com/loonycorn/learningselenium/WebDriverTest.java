package com.loonycorn.learningselenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class WebDriverTest {

    // l'annotation @Test de TestNG indique qu'il s'agit d'un scénario de test à exécuter lors de l'exécution de cette class
    @Test
    public void navigateToPageUsingChrome() {
        // époque révolue où chromedriver devait être maj manuellement à chque évolution du navigateur
        // aujourd'hui la gestion des pilotes est confiée à Selenium Manager
        // System.setProperty("webdriver.chrome.driver", "C://Selenium/chromedriver-win64/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");

        driver.quit();
    }

    @Test
    public void navigateToPageUsingEdge() {
        WebDriver driver = new EdgeDriver();
        driver.get("https://bing.com");

        driver.quit();
    }

    @Test
    public void navigateToPageUsingFirefox() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://duckduckgo.com");

        driver.quit();
    }
}