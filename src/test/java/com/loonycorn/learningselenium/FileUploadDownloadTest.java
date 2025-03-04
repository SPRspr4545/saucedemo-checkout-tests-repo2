package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileUploadDownloadTest {

    private static final String UPLOAD = "https://the-internet.herokuapp.com/upload";
    private static final String DOWNLOAD = "https://the-internet.herokuapp.com/download";

    private WebDriver driver;

    @BeforeTest
    public void setUp() { // ici je n'utilise pas "DriverFactory" car je veux configurer certaines préférences spécifiques
        Map<String, Object> prefs = new HashMap<>(); // HashMap permet de spécifier le répertoire de téléchargement
        prefs.put("download.default_directory", "/Users/sam.pelletier/Desktop/"); // choix d'un répertoire spécifique pour télécharger le fichier
        //prefs.put("download.default_directory", ""); // la chaîne vide "" signifie que l'emplacement par défaut sera utilisé pour télécharger le fichier

        ChromeOptions options = new ChromeOptions(); // j'instancie l'objet ChromeOptions
        options.setExperimentalOption("prefs", prefs); // j'appel "setExperimentalOption" pour lui transmettre les "prefs"
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options); // j'utilise ces options pour instancier le ChromeDriver
    }

    private static void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFileUpload() {
        driver.get(UPLOAD);

        // j'accède à l'input qui permet de charger le fichier à télécharger...
        WebElement fileInput = driver.findElement(By.id("file-upload")); // ...et je le stocke dans la variable "fileInput"


        // il ne faut pas clicker sur le Button pour choisir le fichier à télécharger
        String filePath = "C:/Selenium/learning-selenium/poissons.jpg"; // il faut trouver le chemin du fichier à télécharger...
        fileInput.sendKeys(filePath); // ...et passer ce chemin au "fileInput"
        // avec cela, nous n'affichons pas la boite de dialogue du système !!

        WebElement uploadButton = driver.findElement(By.id("file-submit")); // j'accède ensuite au Button qui permet de télécharger le fichier...
        delay();
        uploadButton.click(); // ... puis clicker dessus

        delay(); // nous sommes redirigés vers la page où nous recevrons le message de réussite

        WebElement uploadedFiles = driver.findElement(By.id("uploaded-files")); // où nous devrions obtenir le nom du fichier qui vient d'être téléchargé

        String uploadedFileName = uploadedFiles.getText().trim();
        Assert.assertEquals(uploadedFileName, "poissons.jpg",
                "Uploaded file name doesn't match expected");

        String actualTitle = driver.findElement(By.tagName("h3")).getText(); // j'accède au message de réussite
        Assert.assertEquals(actualTitle,"File Uploaded!",
                "File upload was unsuccessful");

        delay();
    }

    @Test
    public void testFileDowload() {
        driver.get(DOWNLOAD);

        WebElement downloadLink = driver.findElement(By.linkText("LambdaTest.txt"));
        downloadLink.click();

        delay();

        // je sais que l'emplacement par défaut se trouve dans mon dossier Téléchargement
        //File downloadedFile = new File("/Users/sam.pelletier/Downloads/LambdaTest.txt"); // j'instancie un Objet "File"
        File downloadedFile = new File("/Users/sam.pelletier/Downloads/LambdaTest.txt"); // j'instancie un Objet "File"
        Assert.assertTrue(downloadedFile.exists(),"File download failed");
    }

    @Test
    public void testAllJPGFilesDownload() {
        driver.get(DOWNLOAD);

        List<WebElement> downloadLinks = driver.findElements(By.cssSelector("a[href$='.jpg']")); // l'attribut "href" se termine par $= signifie qu'il se termine par ".jpg"
        for (WebElement link : downloadLinks) { // je lance une boucle sur chacun de ces liens...
            link.click(); // ...et j'appel link.click()
            //delay();
        }

        File[] downloadedFiles = new File("C:/Users/sam.pelletier/Downloads/") // je regarde ensuite tous les fichiers dans le répertoire...
                .listFiles((dir, name) -> name.endsWith(".jpg")); // ...et je filtre tous les fichiers qui terminent par ".jgp"

        Assert.assertNotNull(downloadedFiles, "No files were downloaded.");
        Assert.assertTrue(downloadedFiles.length > 0, "No JPG files were downloaded.");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
