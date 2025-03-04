package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DatePickerTest {

    private static final String DATEPICKER = "https://jqueryui.com/datepicker/";

    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);
    }

    private static void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simplePickDateTest() {
        driver.get(DATEPICKER);

        // l'élément datepicker est intégré dans un iframe
        // il faut donc passer cet iframe pour pouvoir manipuler le datepicker
        driver.switchTo().frame(0); // .switchTo().frame(0) permet de passer au 1er iframe de la page

        WebElement datePickerInput = driver.findElement(By.id("datepicker")); // l'input datepicker porte l'id "datepicker"
        datePickerInput.click(); // click dessus pour faire apparaître le datepicker

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // attente explicite pour le datepicker
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div"))); // j'attends le div[id='ui-datepicker-div']

        delay();

        // pour trouver la dateCell j'utilise XPATH, un moyen simple d'identifier le bon dateCell du mois en cours
        WebElement dateCell = driver.findElement(By.xpath("//a[text()='24']"));
        dateCell.click();

        String selectedDate = datePickerInput.getAttribute("value"); // on obtient la date sélectionnée dans l'attribut value de l'input
        System.out.println("Selected date: " + selectedDate);

        delay();
    }

    @Test
    public void pickDateFromPreviousMonthTest() {
        driver.get(DATEPICKER);
        driver.switchTo().frame(0); // .switchTo().frame(0) permet de passer au 1er iframe de la page

        WebElement datePickerInput = driver.findElement(By.id("datepicker")); // input "datepicker"
        datePickerInput.click(); // fait apparaître le datepicker

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // attente explicite
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div"))); // j'attends le div[id='ui-datepicker-div']

        delay();

        WebElement prevMonthButton = driver.findElement(By.xpath("//span[text()='Prev']"));
        prevMonthButton.click(); // revenir le mois précédent

        WebElement dateCell = driver.findElement(By.xpath("//a[text()='10']"));
        dateCell.click();

        String selectedDate = datePickerInput.getAttribute("value"); // on obtient la date sélectionnée dans l'attribut "value" de l'input
        System.out.println("Selected date: " + selectedDate);

        delay();
    }

    @Test
    public void pickDateInJanuaryTest() {
        driver.get(DATEPICKER);
        driver.switchTo().frame(0);

        WebElement datePickerInput = driver.findElement(By.id("datepicker"));
        datePickerInput.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));

        delay();

        WebElement prevMonthButton; // j'initialise le WebElement
        while (!driver.findElement(By.className("ui-datepicker-month")).getText().equals("February")) {
            // récupère le texte de cet élément et regarde si le mois est égal à "February" sinon on réitère la boucle
            prevMonthButton = driver.findElement(By.xpath("//span[text()='Prev']"));
            prevMonthButton.click();
            delay();
        }

        WebElement dateCell = driver.findElement(By.xpath("//a[text()='10']"));
        dateCell.click();

        String selectedDate = datePickerInput.getAttribute("value");
        System.out.println("Selected date: " + selectedDate);

        delay();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
