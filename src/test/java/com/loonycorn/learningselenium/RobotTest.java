package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*; // ******************************* la Classe Robot ne fait pas partie de Selenium
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent; //******************* elle fait partie du Kit d'outils de fenêtres abstraites de Java AWT

public class RobotTest {

    private static final String ROBOT = "file:///C:/Selenium/learning-selenium/src/test/java/templates/testing_robot.html";
    private static final String UPLOAD = "https://formy-project.herokuapp.com/fileupload";

    private WebDriver driver;
    private Robot robot; //*****************!

    @BeforeTest
    public void setUp() throws AWTException { // ajout de l'exception "AWTException" à la signature de la méthode
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);

        //ChromeOptions Options = new ChromeOptions(); // Instancie la Classe ChromeOptions()
        //Options.addArguments("--headless"); // Ajoute un argument choisi
        //driver = new ChromeDriver(Options); // Instancie le ChromeDriver(Options)
        //driver.manage().window().maximize(); // si je souhaite que le navigateur soit maximisé après ouverture

        robot = new Robot(); // l'instance de la Classe Robot peut générer l'exception AWTException
    }

    private static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // méthode d'aide à la saisie qui prend un texte comme argument de saisie...
    public void type1(String text) {
        // ... et parcourt chaque caractère du texte spécifié
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c); // pour chaque caractère on obtient le keyCode de ce caractère
            robot.keyPress(keyCode); // puis appuis sur cette touche en appelant "robot.keyPress"
            robot.keyRelease(keyCode); // et relâcher la touche avec "robot.keyRelease"

            delay(100);
        }
    }

    @Test
    public void testNameFieldUsingRobot1() {
        driver.get(ROBOT);
        // on accède au site et nous passons à la fenêtre principale
        driver.switchTo().window(driver.getWindowHandle()); // "getWindowHandle" définit le contexte du Driver sur la fenêtre actuelle

        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.click(); // on click sur le "nameInput" pour focus sur cette zone de texte

        robot.delay(3000); // j'utilise la Classe Robot.delay juste pour voir ce qui se passe

        type1("Mia Raymond"); // la function type contrôle le clavier

        String actualValue = nameInput.getAttribute("value"); // récupère l'attribut value pour...
        Assert.assertEquals(actualValue, "mia raymond"); // ...vérifier la valeur de "actualValue"
    }

    // un capsLock booléen à TRUE verrouille la touche majuscule avant de commencer à taper
    public void type2(String text, boolean capsLock) {
        if (capsLock) { // si capsLock booléen à TRUE
            robot.keyPress(KeyEvent.VK_CAPS_LOCK); // appel robot.keyPress pour verrouiller la touche MAJ
        }
        // lançons ensuite la boucle for en parcourant chaque caractère du tableau
        for (char c : text.toCharArray()) {
            if (capsLock) { // si capsLock booléen à TRUE
                robot.keyPress(KeyEvent.VK_CAPS_LOCK);
            }

            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c); // pour chaque caractère on obtient le keyCode de ce caractère
            robot.keyPress(keyCode); // puis appuis sur cette touche en appelant "robot.keyPress"
            robot.keyRelease(keyCode); // et relâcher la touche avec "robot.keyRelease"

            delay(100);
        }
        if (capsLock) {
            robot.keyRelease(KeyEvent.VK_CAPS_LOCK); // relache la touche MAJ une fois la saisie terminée
        }
    }

    @Test
    public void testNameFieldUsingRobot2() {
        driver.get(ROBOT);
        // on accède au site et nous passons à la fenêtre principale
        driver.switchTo().window(driver.getWindowHandle()); // "getWindowHandle" définit le contexte du Driver sur la fenêtre actuelle

        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.click(); // click sur "nameInput" pour focus sur cette zone de texte

        robot.delay(3000); // j'utilise la Classe Robot.delay juste pour voir ce qui se passe

        type2("Mia Raymond", true); // passe à la function type2: le texte et le capsLock=TRUE
        // le texte entier sera en majuscule

        String actualValue = nameInput.getAttribute("value"); // récupère l'attribut value pour...
        Assert.assertEquals(actualValue, "MIA RAYMOND"); // ...vérifier la valeur de "actualValue"
    }

    public void type3(String text, boolean capsLock) {
        if (capsLock) { // si capsLock booléen à TRUE
            robot.keyPress(KeyEvent.VK_CAPS_LOCK); // appel robot.keyPress pour verrouiller la touche MAJ
        }
        // lançons ensuite la boucle for en parcourant chaque caractère du tableau
        for (char c : text.toCharArray()) {
            if (capsLock) { // si capsLock booléen à TRUE
                robot.keyPress(KeyEvent.VK_CAPS_LOCK);
            }

            int keyCode;
            if (c == '@') {
                robot.keyPress(KeyEvent.VK_ALT_GRAPH);
                keyCode = KeyEvent.VK_0;
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
            } else if (c == '.'){
                robot.keyPress(KeyEvent.VK_SHIFT); // press la touch "SHIFT"
                keyCode = KeyEvent.VK_SEMICOLON; // correspond au ";" pour avoir le "."
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }else {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(c); // pour chaque caractère on obtient le keyCode de ce caractère
                robot.keyPress(keyCode); // puis appuis sur cette touche en appelant "robot.keyPress"
                robot.keyRelease(keyCode); // et relâcher la touche avec "robot.keyRelease"
            }

            delay(100);
        }
        if (capsLock) {
            robot.keyRelease(KeyEvent.VK_CAPS_LOCK); // relache la touche MAJ une fois la saisie terminée
        }
    }

    @Test
    public void testEmailFieldUsingRobot() {
        driver.get(ROBOT);
        // on accède au site et nous passons à la fenêtre principale
        driver.switchTo().window(driver.getWindowHandle()); // "getWindowHandle" définit le contexte du Driver sur la fenêtre actuelle

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.click(); // click sur "emailInput" pour focus sur cette zone de texte

        robot.delay(3000); // j'utilise la Classe Robot.delay juste pour voir ce qui se passe

        type3("mia.raymond@mail.geek", false); // passe à la function type2: le texte et le capsLock=false
        // le texte entier sera en minuscule

        String actualValue = emailInput.getAttribute("value"); // récupère l'attribut value pour...
        Assert.assertEquals(actualValue, "mia.raymond@mail.geek"); // ...vérifier la valeur de "actualValue"
    }

    @Test
    public void testScrollToBottomAndUp() {
        driver.get(ROBOT);

        for (int i = 0; i < 6; i++) { // pendant 6 itérations
            // "+3" fait référence au nombre d'encoches à faire défiler sur la molette de la souris
            // "+3" indique que la page défile vers le bas
            robot.mouseWheel(+3);
            robot.delay(1000);
        }

        for (int i = 0; i < 6; i++) { // pendant 6 itérations
            // "-3" fait référence au nombre d'encoches à faire défiler sur la molette de la souris
            // "-3" indique que la page défile vers le haut
            robot.mouseWheel(-3);
            robot.delay(1000);
        }
    }

    @Test
    public void testRightClickAndReload() {
        driver.get(ROBOT);

        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement emailInput = driver.findElement(By.id("email"));

        delay(1000);

        nameInput.sendKeys("Name");
        emailInput.sendKeys("Email");
        delay(2000);

        robot.mouseMove(800,300); // déplace la souris à un endroit précis sur le navigateur

        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // click sur le bouton DROIT...
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); // ...puis relâche
        robot.delay(1000);

        robot.keyPress(KeyEvent.VK_DOWN); // j'appuis une première fois sur la touche BAS
        robot.keyRelease(KeyEvent.VK_DOWN); // pour accéder à la 1ère option du menu contextuel
        robot.delay(500);

        robot.keyPress(KeyEvent.VK_DOWN); // puis une seconde fois sur la touche BAS
        robot.keyRelease(KeyEvent.VK_DOWN); // pour accéder à la 2ème option du menu contextuel
        robot.delay(500);

        robot.keyPress(KeyEvent.VK_ENTER); // j'appuis sur ENTER
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(3000);

        nameInput = driver.findElement(By.id("name"));
        emailInput = driver.findElement(By.id("email"));

        // j'affirme que les 2 inputs sont vides "isEmpty()"
        Assert.assertTrue(nameInput.getAttribute("value").isEmpty(),
                "Name field is not blank after reload");
        Assert.assertTrue(emailInput.getAttribute("value").isEmpty(),
                "Email field is not blank after reload");

    }

    @Test
    public void testFileUpload() {
        driver.get(UPLOAD);

        driver.findElement(By.cssSelector(".btn-choose")).click();
        robot.delay(2000);

        // ne permet pas de mettre le Focus sur la fenêtre d'exploration (il y est déjà
        //driver.switchTo().window(driver.getWindowHandle());
        //robot.delay(2000);

        // maintenant je navigue vers le volet gauche pour accéder au dossier
        //robot.keyPress(KeyEvent.VK_CAPS_LOCK); // appel robot.keyPress pour verrouiller la touche MAJ
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        //robot.keyRelease(KeyEvent.VK_CAPS_LOCK); // relache la touche MAJ une fois la saisie terminée
        robot.delay(2000);

        robot.keyPress(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_END);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(2000);


        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(2000);

        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(2000);

        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(2000);

        WebElement fileUploadInput = driver.findElement(By.id("file-upload-field"));
        Assert.assertTrue(fileUploadInput.getAttribute("value").contains("Epiphone.webp"),
                "File 'Epiphone.webp' is not upload.");
        robot.delay(2000);
    }

    @Test
    public void testFileUploadWithoutRobot() {
        // test sans ROBOT, que du Selenium pour tester sans navigateur apparent via l'argument dans le SetUp: "Options.addArguments("--headless");"
        driver.get(UPLOAD);

        WebElement fileUploadInput = driver.findElement(By.id("file-upload-field"));

        // je trouve le chemin du fichier sur ma machine locale
        String filePath = System.getProperty("user.home") + "/Downloads/Secret Folder/Epiphone.webp";
        fileUploadInput.sendKeys(filePath); // Spécifie le (filePath)

        delay(5000);

        Assert.assertTrue(fileUploadInput.getAttribute("value").contains("Epiphone.webp"),
                "File 'Epiphone.webp' is not uploaded");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
