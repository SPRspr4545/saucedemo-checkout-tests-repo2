package com.loonycorn.learningselenium.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.firefox.FirefoxDriverService;
import java.time.Duration;

public class DriverFactory {

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
    }

    public static WebDriver createDriver(BrowserType browserType) {
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                // WARNING: The chromedriver version (131.0.6778.108) detected in PATH at C:\Users\sam.pelletier\AppData\Roaming\npm\chromedriver.cmd might not be compatible with the detected chrome version (133.0.6943.54); currently, chromedriver 133.0.6943.53 is recommended for chrome 133.*, so it is advised to delete the driver in PATH and retry
                //System.setProperty("webdriver.chrome.driver", "/Users/sam.pelletier/chromedriver-win64/chromedriver.exe"); // modifie manuellement la version du chromedriver
                ChromeOptions chromeOptions = new ChromeOptions(); // Instancie la Classe ChromeOptions()
                chromeOptions.addArguments("--start-maximized"); // Ajoute un argument choisi
                driver = new ChromeDriver(chromeOptions); // Instancie le ChromeDriver(à l'aide de ces Options)
                // driver.manage().window().maximize(); // si je souhaite que le navigateur soit maximisé après ouverture
                //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); //implicitlyWait fait attendre le navigateur pour trouver un élément s'il n'est pas disponible immédiatement
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions); // Instancie le Pilote du navigateur
                break;
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions); // Instancie le Pilote du navigateur
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        return driver; // et je renvoie le pilote à instancier
    }
}
