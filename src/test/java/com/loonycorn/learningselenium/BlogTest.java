package com.loonycorn.learningselenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.service.DriverFinder;
import org.testng.annotations.Test;
import java.nio.file.Path;

public class BlogTest {

    @Test
    public void chromeOptionsTest() {
        ChromeOptions options = new ChromeOptions(); // on instancie la classe ChromeOptions
        options.setBrowserVersion("stable");

        WebDriver driver = new ChromeDriver(options);
        // j'accède au chemin d'installation de Chrome en local
        // DriverFinder permet d'obtenir le chemin d'installation
        // ChromeDriverService fait le lien entre Selenium WebDriver et Chrome
        // Path chromePath = Path.of(DriverFinder.getPath(ChromeDriverService.createDefaultService(), options).getBrowserPath());

        //Méthode Magalie en créant une variable pour remplacer la méthode getPath()
        // Path chromePath = Path.of(DriverFinder.getPath(ChromeDriverService.createDefaultService(), options).getBrowserPath());
        DriverFinder path = new DriverFinder(ChromeDriverService.createDefaultService(), options);
        //path.getBrowserPath();
        Path chromePath = Path.of(path.getBrowserPath());

        System.out.println("Chrome binary location: " + chromePath);
        System.out.println("Méthode Magalie: " + path);

        driver.get("https://omayo.blogspot.com/");
        delay(5000); // on appel la méthode/fonction delay()
        driver.quit();
    }

    @Test
    public void edgeOptionsTest() {
        EdgeOptions options = new EdgeOptions();
        options.setBrowserVersion("stable");

        WebDriver driver = new EdgeDriver(options);

        //Méthode Magalie en créant une variable pour remplacer la méthode getPath()
        // Path edgePath = Path.of(DriverFinder.getPath(EdgeDriverService.createDefaultService(), options).getBrowserPath());
        DriverFinder path = new DriverFinder(EdgeDriverService.createDefaultService(), options);
        path.getBrowserPath();
        Path edgePath = Path.of(path.getBrowserPath());

        System.out.println("Edge binary location: " + edgePath);
        System.out.println("Méthode Magalie: " + path);

        driver.get("https://omayo.blogspot.com/");
        delay(5000); // on appel la méthode/fonction delay()
        driver.quit();
    }

    @Test
    public void navigateToPageUsingChrome() {
        ChromeOptions options = new ChromeOptions(); // on instancie la classe ChromeOptions
        options.addArguments("--start-maximized"); // puis on ajoute un argument --start-maximized
        options.addArguments("--incognito"); // puis on ajoute un second argument pour la navigation en mode privée
        // options.addArguments("--start-maximized", "--incognito"); // on peut spécifier les arguments en une fois
        WebDriver driver = new ChromeDriver(options); // je transmets ces options lorque j'instancie le ChromeDriver

        driver.get("https://omayo.blogspot.com/");
        delay(5000); // on appel la méthode/fonction delay()
        driver.quit();
    }

    private void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e    );
        }
    }

}
