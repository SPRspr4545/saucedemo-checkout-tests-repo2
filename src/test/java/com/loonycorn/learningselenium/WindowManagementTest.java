package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WindowManagementTest {

    private static final String SITE2 = "https://the-internet.herokuapp.com/windows";
    private static final String WINDOW_SITE = "https://the-internet.herokuapp.com/windows";
    private static final String SCROLL_SITE = "https://the-internet.herokuapp.com/infinite_scroll";

    private static final String DYNAMIC_CONTENT_SITE = "https://the-internet.herokuapp.com/dynamic_content";
    private static final String SCREENSHOT_DIR = "screenshots";

    private WebDriver driver;
    private WebDriver driver1;
    private WebDriver driver2;
    private WebDriver driver3;

    private static void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeTest
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
        // je les commente car ils ouvre un navigateur à chaque test
        // driver1 = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
        //driver2 = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
        //driver3 = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
    }

    // chaque onglet est considéré comme une fenêtre de manière unique à l'aide d'un WindowHandle
    @Test
    public void testOpenMainWindow() {
        driver.get(SITE2);
        // garantit simplement que la fenêtre principale possède un ID valide
        Assert.assertNotNull(driver.getWindowHandle(), "Main window handle is null.");
    delay();
}

@Test(dependsOnMethods = "testOpenMainWindow")
public void testNewTabOpened() {
    // vérifier si on peut ouvrir un nouvel onglet avec JS
    ((JavascriptExecutor)driver).executeScript("window.open();");
    // vérifier maintenant qu'on a bien 2 WindowHandle
    // pour ça je prends le getWindowHandle et je calcul sa taille
    Assert.assertEquals(driver.getWindowHandles().size(),2,"Expected to find two tabs");
    delay();
}

@Test(dependsOnMethods = "testOpenMainWindow")
public void testOpenNewTab() {
    // Spécifier le type de fenêtre à ouvrir dans newWindow() -
    driver.switchTo().newWindow(WindowType.TAB); // TAB = nouvel onglet dans l'instance du Driver et non une nouvelle fenêtre
    // vérifier maintenant qu'on a bien 2 WindowHandle
    // pour ça je prends le getWindowHandle et je calcul sa taille
    Assert.assertEquals(driver.getWindowHandles().size(),2,"Expected to find two tabs");
    delay();
}

@Test
public void testOpenNewTab2() {
    driver.get(SITE2);
    Assert.assertEquals(driver.getWindowHandles().size(),1,
            "Main windows is not opened"); // vérifions que nous avons qu'une seule fenêtre principale

    WebElement linkEl = driver.findElement(By.cssSelector(".example a"));
    linkEl.click(); // on click sur le lien

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Attente explicite
    wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // pour s'assurer du nombre de fenêtres (2)

    Set<String> allWindowHandles = driver.getWindowHandles(); // on récupère toutes les WindowHandles(identifiants uniques)
    System.out.println("allWindowHandles: " + allWindowHandles); // et les imprimons à l'écran
    Assert.assertEquals(driver.getWindowHandles().size(), 2,
            "New tab not opened."); // et nous affirmons le nombre de WindowHandles

    delay();
}

@Test
public void testSwitchToTabAndBack() {
    driver.get(SITE2);
    Assert.assertEquals(driver.getWindowHandles().size(), 1,
            "Main window is not opened"); // nous n'avons qu'une seule fenêtre principale pour l'instant

    String mainWindowHandle = driver.getWindowHandle(); // récupère le WindowHandle de la fenêtre principale
    System.out.println("mainWindowHandle: " + mainWindowHandle); // et je l'imprime à l'écran

    WebElement linkEl = driver.findElement(By.cssSelector(".example a")); // on trouve ensuite le lien
    linkEl.click(); // et on click dessus

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // attendons ensuite que le nombre de fenêtre = 2

    Set<String> allWindowHandles = driver.getWindowHandles(); // pour obtenir tous les descripteurs de fenêtre actuels

    // Switch à l’onglet nouvellement ouvert et confirmez le titre
    for (String windowHandle : allWindowHandles) {
        if (!windowHandle.equals(mainWindowHandle)) { // si WindowHandle n'est pas égal au mainWindowHandle, c'est donc le nouvel onglet
            driver.switchTo().window(windowHandle); // je passe au nouvel onglet
            System.out.println("newWindowHandles: " + windowHandle); // j'imprime le WindowHandle du nouvel onglet
            break; // et je sort de la boucle for
        }
    }

    Assert.assertTrue(driver.getTitle().contains("New Window"), // vérifier que le "driver" soit "focus" sur la nouvelle fenêtre dont le titre est "New Window"
            "New window title is not as expected");

    delay();

    driver.switchTo().window(mainWindowHandle); // Switch à la page principale(mainWindowHandle)
    Assert.assertTrue(driver.getTitle().contains("The Internet"), //confirmer le titre
            "Main window title is not as expected");

    delay();
}

    @Test
    public void testSwitchToTabAndBack2() {
        driver.get(SITE2);
        Assert.assertEquals(driver.getWindowHandles().size(), 1,
                "Main window is not opened"); // nous n'avons qu'une seule fenêtre principale pour l'instant

        String mainWindowHandle = driver.getWindowHandle(); // récupère le WindowHandle de la fenêtre principale
        System.out.println("mainWindowHandle: " + mainWindowHandle); // et je l'imprime à l'écran

        WebElement linkEl = driver.findElement(By.cssSelector(".example a")); // on trouve ensuite le lien
        linkEl.click(); // et on click dessus

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // attendons ensuite que le nombre de fenêtre = 2
        Set<String> allWindowHandles = driver.getWindowHandles(); // pour obtenir tous les descripteurs de fenêtre actuels


        // Switch à l’onglet nouvellement ouvert et confirmez le titre
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) { // si WindowHandle n'est pas égal au mainWindowHandle, c'est donc le nouvel onglet
                driver.switchTo().window(windowHandle); // je passe au nouvel onglet
                System.out.println("newWindowHandles: " + windowHandle); // j'imprime le WindowHandle du nouvel onglet
                break; // et je sort de la boucle for
            }
        }
        String secondTabHandle = driver.getWindowHandle(); // stock l'id du nouvel onglet dans secondTabHandle
        System.out.println("secondTabHandle: " + secondTabHandle);

        Assert.assertTrue(driver.getTitle().contains("New Window"), // vérifier que le "driver" soit "focus" sur la nouvelle fenêtre dont le titre est "New Window"
                "New window title is not as expected");

        delay();

        driver.switchTo().window(mainWindowHandle); // j'utilise le mainWindowHandle pour Switch vers la page principale(Main)
        Assert.assertTrue(driver.getTitle().contains("The Internet"), //et confirmer le titre
                "Main window title is not as expected");
        delay();
        linkEl = driver.findElement(By.cssSelector(".example a")); // Click à nouveau sur le lien
        linkEl.click(); // et ouvre un 3ème onglet

        wait.until(ExpectedConditions.numberOfWindowsToBe(3)); // attendons ensuite que le nombre de fenêtre = 3
        Set<String> allWindowHandles3 = driver.getWindowHandles(); // pour obtenir tous les descripteurs des fenêtres actuels

        // Switch à l’onglet nouvellement ouvert et confirmez le titre
        for (String windowHandle : allWindowHandles3) {
            if (!windowHandle.equals(secondTabHandle) && !windowHandle.equals(mainWindowHandle)) { // si WindowHandle est différent des précédents WindowHandle, c'est donc le nouvel onglet
                driver.switchTo().window(windowHandle); // je passe au nouvel onglet
                System.out.println("newWindowHandles: " + windowHandle); // j'imprime le windowHandle du nouvel onglet
                break; // et je sort de la boucle for
            }
        }
        String thirdTabHandle = driver.getWindowHandle(); // stock l'id du nouvel onglet dans thirdTabHandle
        System.out.println("thirdTabHandle: " + thirdTabHandle);

        //delay();
        driver.switchTo().window(mainWindowHandle); // j'utilise le mainWindowHandle pour Switch vers la page principale(Main)
        Assert.assertTrue(driver.getTitle().contains("The Internet"),
                "Main window title is not as expected");
        //delay();
        driver.switchTo().window(secondTabHandle); // je switch sur le secondTabHandle
        Assert.assertTrue(driver.getTitle().contains("New Window"),
                "New window title is not as expected");
        //delay();
        driver.switchTo().window(thirdTabHandle); // je switch sur le thirdTabHandle
        Assert.assertTrue(driver.getTitle().contains("New Window"),
                "Main window title is not as expected");
        //delay();
    }

    @Test
    public void testOpenDifferentTabs() {
        driver.get(SITE2);
        Assert.assertEquals(driver.getWindowHandles().size(),1,
                "Main window is not opened.");

        driver.switchTo().newWindow(WindowType.TAB);

        Assert.assertEquals(driver.getWindowHandles().size(),2,
                "New tab is not opened.");

        driver.get("https://www.codeacademy.com");

        driver.switchTo().newWindow(WindowType.TAB);

        Assert.assertEquals(driver.getWindowHandles().size(),3,
                "New tab is not opened.");

        driver.get("http://www.skillsoft.com");

        delay();
    }

    @Test
    public void testClosingTabs() {
        driver.get(SITE2);
        Assert.assertEquals(driver.getWindowHandles().size(),1,
                "Main window is not opened.");

        WebElement linkEl = driver.findElement(By.cssSelector(".example a"));
        linkEl.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        delay();

        // Click on the link again and open a third tab
        linkEl.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));

        // Click on the link again and open a fourth tab
        linkEl.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(4));

        delay();

        driver.close();
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
        driver.close();

        delay();
    }

    @Test
    public void testOpenDifferentWindows() {
        driver1.get(SITE2);
        Assert.assertEquals(driver1.getWindowHandles().size(),1,
                "Main window is not opened.");

        driver2.get(SITE2);
        Assert.assertEquals(driver2.getWindowHandles().size(),1,
                "Main window is not opened.");

        driver3.get(SITE2);
        Assert.assertEquals(driver3.getWindowHandles().size(),1,
                "Main window is not opened.");

        delay();
    }

    @Test
    public void testOpenDifferentWindows2() {
        driver1.get(SITE2);
        Assert.assertEquals(driver1.getWindowHandles().size(),1,
                "Main window is not opened.");

            String firstWindow = driver.getWindowHandle(); // stock l'id de la nouvelle fenêtre
            System.out.println("firstWindow: " + firstWindow);

        delay();

        driver.switchTo().newWindow(WindowType.WINDOW); // j'ouvre une nouvelle fenêtre avec le même Driver
        Assert.assertEquals(driver.getWindowHandles().size(),2,
                "Main window is not opened."); // nous avons un seul Driver qui gère 2 windows

            String secondWindow = driver.getWindowHandle(); // stock l'id de la nouvelle fenêtre
            System.out.println("secondWindow: " + secondWindow);

        delay();

        driver.switchTo().newWindow(WindowType.WINDOW); // j'ouvre une nouvelle fenêtre avec le même Driver
        Assert.assertEquals(driver.getWindowHandles().size(),3,
                "Main window is not opened."); // nous avons un seul Driver qui gère 3 windows

            String thirdWindow = driver.getWindowHandle(); // stock l'id de la nouvelle fenêtre
            System.out.println("thirdWindow: " + thirdWindow);

        delay();
    }

    @Test
    public void testNavigation() {
        driver.get(WINDOW_SITE);
        System.out.println("Opened window: " + driver.getWindowHandle()); // un seul WindowHandle qu'on imprime à l'écran
        Assert.assertTrue(driver.getCurrentUrl().equals(WINDOW_SITE),
                "URL mismatch after opening window"); // je vérifie l'URL de WINDOW_SITE

        delay();

        driver.get(SCROLL_SITE); // j'utilise en suite la même fenêtre pour accéder au SCROLL_SITE
        Assert.assertTrue(driver.getCurrentUrl().equals(SCROLL_SITE),
                "URL mismatch after navigating"); // je vérifie l'URL de SCROLL_SITE

        delay();

        driver.navigate().back(); // accédons à l'historique du navigateur pour revenir en arrière
        Assert.assertTrue(driver.getCurrentUrl().equals(WINDOW_SITE),
                "URL mismatch after navigating back"); // je vérifie l'URL de WINDOW_SITE

        delay();

        driver.navigate().forward(); // accédons à l'historique du navigateur pour revenir en avant
        Assert.assertTrue(driver.getCurrentUrl().equals(SCROLL_SITE),
                "URL mismatch after navigating back"); // je vérifie l'URL de WINDOW_SITE

        delay();

        driver.navigate().refresh();
        Assert.assertTrue(driver.getCurrentUrl().equals(SCROLL_SITE),
                "URL mismatch after navigating back"); // je vérifie l'URL de WINDOW_SITE

        delay();
    }

    @Test
    public void testWindowSize() {
        driver.get(WINDOW_SITE);

        driver.manage().window().setSize(new Dimension(800, 600)); // setSize() définit les dimensions de la fenêtre
        // par défaut le navigateur démarre en mode maximisé
        String windowOneHandle = driver.getWindowHandle(); // stock l'id WindowHandle dans la var windowOneHandle

        delay();

        driver.switchTo().newWindow(WindowType.WINDOW); // créer une nouvelle fenêtre
        // le switchTo().newWindow() signifie que le DRIVER se concentre sur cette nouvelle fenêtre
        String windowTwoHandle = driver.getWindowHandle(); // stock l'id WindowHandle dans la var windowTwoHandle
        driver.get(SCROLL_SITE); // j'accède à SCROLL_SITE dans cette même fenêtre
        driver.manage().window().setSize(new Dimension(1000, 375)); // setSize() définit les dimensions de la nouvelle fenêtre

        driver.switchTo().window(windowOneHandle); // Focus du DRIVER sur la 1ère fenêtre windowOneHandle
        Dimension windowOneSize = driver.manage().window().getSize(); // getSize() Obtenir la taille de la 1ère fenêtre
        Assert.assertEquals(windowOneSize.getWidth(), 800); // vérifie que la taille est bien la même
        Assert.assertEquals(windowOneSize.getHeight(), 600); // vérifie que la taille est bien la même

        driver.switchTo().window(windowTwoHandle); // Focus du DRIVER sur la 2ème fenêtre windowTwoHandle
        Dimension windowTwoSize = driver.manage().window().getSize(); // getSize() Obtenir la taille de la 2ème fenêtre
        Assert.assertEquals(windowTwoSize.getWidth(), 1000); // vérifie que la taille est bien la même
        Assert.assertEquals(windowTwoSize.getHeight(), 375); // vérifie que la taille est bien la même

        delay();
    }

    @Test
    public void testWindowSizeAndPosition() {
        driver.get(WINDOW_SITE);

        driver.manage().window().setPosition(new Point(0,0)); // setPosition définit les coordonnées/position de windowOneHandle
        driver.manage().window().setSize(new Dimension(800, 600));
        String windowOneHandle = driver.getWindowHandle();

        delay();

        driver.switchTo().newWindow(WindowType.WINDOW);
        String windowTwoHandle = driver.getWindowHandle();
        driver.get(SCROLL_SITE);
        driver.manage().window().setSize(new Dimension(1000, 375));
        driver.manage().window().setPosition(new Point(0,250)); // setPosition définit les coordonnées/position de windowTwoHandle

        driver.switchTo().window(windowOneHandle);
        Dimension windowOneSize = driver.manage().window().getSize();
        Assert.assertEquals(windowOneSize.getWidth(), 800);
        Assert.assertEquals(windowOneSize.getHeight(), 600);

        Point windowOnePosition = driver.manage().window().getPosition(); // getPosition() obtenir la position de
        System.out.println("Window One Position: " + windowOnePosition);

        driver.switchTo().window(windowTwoHandle);
        Dimension windowTwoSize = driver.manage().window().getSize();
        Assert.assertEquals(windowTwoSize.getWidth(), 1000);
        Assert.assertEquals(windowTwoSize.getHeight(), 375);

        Point windowTwoPosition = driver.manage().window().getPosition(); // getPosition() obtenir la position de
        System.out.println("Window One Position: " + windowTwoPosition);

        delay();
    }

    @Test
    public void testWindowResezing() {
        driver.get(WINDOW_SITE);
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(800,600));
        String windowOneHandle = driver.getWindowHandle();

        delay();

        driver.manage().window().maximize(); // agrandit la fenêtre

        delay();

        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(SCROLL_SITE);
        driver.manage().window().setSize(new Dimension(1000,375));
        driver.manage().window().setPosition(new Point(250,250));

        delay();

        driver.switchTo().window(windowOneHandle);
        delay();
        driver.manage().window().fullscreen(); // la fenêtre passe en plein écran
        delay();
        driver.manage().window().minimize(); // minimize la fenêtre
        delay();
    }

    @Test
    public void testTakeFullScreenshot() throws IOException {
        driver.get(DYNAMIC_CONTENT_SITE);
        takeScreenshot(driver, "screenshot_one.png");
        driver.navigate().refresh();
        takeScreenshot(driver, "screenshot_two.png");
    }

    private static void takeScreenshot(WebDriver driver, String filename) throws IOException {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // TakesScreenshot est une interface implémentée par le driver
        // getScreenshotAs obtenir la capture

        Path directoryPath = Paths.get(SCREENSHOT_DIR); // chemin vers le répertoire des captures, dans le projet en cours

        if (!Files.exists(directoryPath)) { // si le fichier n'existe pas
            Files.createDirectories(directoryPath); // on le crée
        }

        Path destinationFilePath = FileSystems.getDefault().getPath(SCREENSHOT_DIR, filename); // cette méthode construit le chemin indépendamment du système d'exploit.

        Files.copy(screenshotFile.toPath(), destinationFilePath); // copier le fichier de capture depuis où il se trouve, vers sa destination
    }

    @Test
    public void testTakeElementcreenshot() throws IOException {
        driver.get(DYNAMIC_CONTENT_SITE);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // attente explicite pour ...
        WebElement contentEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("content"))); // ... s'assurer que contentEl soit visible

        List<WebElement> rowEls = contentEl.findElements(By.className("row")); // utiliser contentEl.findElements pour rechercher les éléments ".row" imbriqués
        System.out.println(rowEls.size()); // j'imprime le nombre d'éléments ".row" imbriqués

        int i = 0;
        for (WebElement rowEl : rowEls) { // boucle for() en itérant sur chaque rowEl
            takeScreenshot2(rowEl, "screenshot_row_" + i++ + ".png");
            // puis j'invoque la méthode takeScreenshot2(rowEl + nom de fichier incrémenté)
        }
        delay();
    }

    private static void takeScreenshot2(WebElement rowEl, String filename) throws IOException {
        // le 1er argument de la "function(1er,2ème)" n'est plus l'instance du Driver mais du WebElement à capturer: rowEl
        File screenshotFile = rowEl.getScreenshotAs(OutputType.FILE); // getScreenshotAs obtenir la capture

        Path directoryPath = Paths.get(SCREENSHOT_DIR);

        if (!Files.exists(directoryPath)) { // si le fichier n'existe pas
            Files.createDirectories(directoryPath); // on le crée
        }

        Path destinationFilePath = FileSystems.getDefault().getPath(SCREENSHOT_DIR, filename); // cette méthode construit le chemin indépendamment du système d'exploit.

        Files.copy(screenshotFile.toPath(), destinationFilePath); // copier le fichier de capture depuis où il se trouve, vers sa destination
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }

        // si on a instancié 3 Driver différents, il faut tous les fermer
        if (driver1 != null) {
            driver1.quit();
        }
        if (driver2 != null) {
            driver2.quit();
        }
        if (driver3 != null) {
            driver3.quit();
        }
    }
}
