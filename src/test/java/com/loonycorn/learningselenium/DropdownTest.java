package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class DropdownTest {

    private static final String SITE = "https://testpages.eviltester.com/styled/index.html";
    private static final String STATIC_DROPDOWN = "file:///C:/Selenium/learning-selenium/src/test/java/templates/static_dropdown.html";
    private static final String DYNAMIC_DROPDOWN = "file:///C:/Selenium/learning-selenium/src/test/java/templates/dynamic_dropdown.html";
    private static final String DYNAMIC_MULTISELECT_DROPDOWN = "file:///C:/Selenium/learning-selenium/src/test/java/templates/dynamic_multiselect.html";
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
    private void simpleDropdownTtests() {
        driver.get(STATIC_DROPDOWN);

        WebElement dropdown = driver.findElement(By.id("staticDropdown"));
        Assert.assertTrue(dropdown.isDisplayed(), "Dropdown is not displayed");
        Assert.assertEquals(dropdown.findElements(By.tagName("option")).size(), 5, "Number of options in dropdown doesn't match");

        String[] expectedOptionsText = {"Open this select menu", "One", "Two", "Three", "Fourth"};
        for (int i = 0; i < expectedOptionsText.length; i++) {
            WebElement option = dropdown.findElement(By.cssSelector("option:nth-of-type(" + (i + 1) + ")")); // j'accède aux options avec "nth-of-type(1)"

            Assert.assertEquals(option.getText(), expectedOptionsText[i], "Option text doesn't match");

            System.out.println("Option txt: " + option.getText());
        }
        delay();
    }

    @Test
    private void testSelectOptionUsingLocators() { // cette technique est plutôt maladroite voir la suivante si nécessaire
        driver.get(STATIC_DROPDOWN);

        WebElement dropdown = driver.findElement(By.id("staticDropdown"));

        delay();

        dropdown.findElement(By.cssSelector("option[value='option2']")).click();

        String selectedOption = dropdown.getAttribute("value"); // pour vérifier l'option sélectionnée j'appel "dropdown.getAttribute("value)"

        Assert.assertEquals(selectedOption, "option2", "Selected option is not expected");

        delay();
    }

    @Test
    private void testSelectOptionUsingVisibleText() { // SELENIUM offre l'Objet SELECT(), un meilleur moyen d'interagir avec les listes déroulantes :
        driver.get(STATIC_DROPDOWN);

        WebElement dropdown = driver.findElement(By.id("staticDropdown"));

        delay();

        // l'Objet SELECT() propose plusieurs méthodes pour interagir avec la liste déroulante
        Select select = new Select(dropdown); // j'instancie l'Objet SELECT() et lui transmet l'élément comme argument
        select.selectByContainsVisibleText("Three"); // on sélectionne le texte visible à l'écran

        WebElement selectedOption = select.getFirstSelectedOption(); // obtenir l'(es)option(s) actuellement sélectionnée(s)

        Assert.assertEquals(selectedOption.getText(), "Three",
                "Selected option is not expected"); // pour vérifier le texte sélectionné j'appel "dropdown.getText()"
        Assert.assertEquals(selectedOption.getAttribute("value"), "option3",
                "Selected option is not expected"); // pour vérifier l'option sélectionnée j'appel "dropdown.getAttribute("value)"

        delay();
    }

    @Test
    private void testSelectOptionUsingIndex() { // SELENIUM offre l'Objet SELECT(), un meilleur moyen d'interagir avec les listes déroulantes :
        driver.get(STATIC_DROPDOWN);

        WebElement dropdown = driver.findElement(By.id("staticDropdown"));

        delay();

        Select select = new Select(dropdown); // j'instancie l'Objet SELECT() et lui transmet l'élément comme argument
        select.selectByIndex(2); // on sélectionne l'option par son index

        WebElement selectedOption = select.getFirstSelectedOption(); // obtenir l'(es)option(s) actuellement sélectionnée(s)

        Assert.assertEquals(selectedOption.getText(), "Two",
                "Selected option is not expected"); // pour vérifier le texte sélectionné j'appel "dropdown.getText()"
        Assert.assertEquals(selectedOption.getAttribute("value"), "option2",
                "Selected option is not expected"); // pour vérifier l'option sélectionnée j'appel "dropdown.getAttribute("value)"

        delay();
    }

    @Test
    private void testSelectOptionUsingValue() { // SELENIUM offre l'Objet SELECT(), un meilleur moyen d'interagir avec les listes déroulantes :
        driver.get(STATIC_DROPDOWN);

        WebElement dropdown = driver.findElement(By.id("staticDropdown"));

        delay();

        Select select = new Select(dropdown); // j'instancie l'Objet SELECT() et lui transmet l'élément comme argument
        select.selectByValue("option4"); // on sélectionne l'option par l'attribut VALUE

        WebElement selectedOption = select.getFirstSelectedOption(); // obtenir l'(es)option(s) actuellement sélectionnée(s)

        Assert.assertEquals(selectedOption.getText(), "Fourth",
                "Selected option is not expected"); // pour vérifier le texte sélectionné j'appel "dropdown.getText()"
        Assert.assertEquals(selectedOption.getAttribute("value"), "option4",
                "Selected option is not expected"); // pour vérifier l'option sélectionnée j'appel "dropdown.getAttribute("value)"

        delay();
    }

    @Test
    private void initialStateDropdownTests() {
        driver.get(DYNAMIC_DROPDOWN);

        WebElement categoryDropdown = driver.findElement(By.id("categoryDropdown"));
        Assert.assertTrue(categoryDropdown.isDisplayed(), "Category dropdown is not displayed");

        Select categorySelect = new Select(categoryDropdown); // j'instancie l'Objet SELECT() et lui transmet l'élément comme argument
        Assert.assertEquals(categorySelect.getFirstSelectedOption().getText(),
                "-- Select a Category --",
                "Incorrect default option in category dropdown");

        WebElement subcategoryDropdown = driver.findElement(By.id("subcategoryDropdown"));
        Assert.assertFalse(subcategoryDropdown.isEnabled(),
                "Subcategory dropdown should not be enabled"); // assertFalse affirmer que subcategoryDropdown n'est pas actif

        delay();
    }

    @Test
    private void testDynamicSubcategories() {
        driver.get(DYNAMIC_DROPDOWN);

        WebElement categoryDropdown = driver.findElement(By.id("categoryDropdown"));
        Select categorySelect = new Select(categoryDropdown); // j'instancie l'Objet SELECT() et lui transmet categoryDropdown comme argument
        categorySelect.selectByVisibleText("Electronics"); // je selectionne explicitement le texte visible "Electronics"
        Assert.assertEquals(categorySelect.getFirstSelectedOption().getText(), // je trouve la 1ère option sélectionnée dans categorySelect
                "Electronics", // et je m'assure que le texte est = à "Electronics"
                "Incorrect default option in category dropdown");

        WebElement subcategoryDropdown = driver.findElement(By.id("subcategoryDropdown")); // j'accède maintenant à la subcategoryDropdown
        Select subcategorySelect = new Select(subcategoryDropdown); // j'instancie l'Objet SELECT() et lui transmet subcategoryDropdown comme argument
        Assert.assertTrue(subcategoryDropdown.isEnabled(),
                "Subcategory dropdown should be enabled"); // j'affirme que subcategoryDropdown est actif

        Assert.assertEquals(subcategorySelect.getOptions().size(), 5, // la liste déroulante subcategoryDropdown doit contenir 5 options
                "Incorrect number of options in subcategory dropdown");

        subcategorySelect.selectByIndex(2); // sélectionne l'index 2 correspondant à l'option "Smartphones"
        Assert.assertEquals(subcategorySelect.getFirstSelectedOption().getText(),
                "Smartphones",
                "Incorrect default option in subcategory dropdown");

        delay();
    }

    @Test
    private void testDynamicMultiselect() {
        driver.get(DYNAMIC_MULTISELECT_DROPDOWN);

        WebElement categoryDropdown = driver.findElement(By.id("categoryDropdown"));
        Select categorySelect = new Select(categoryDropdown); // j'instancie l'Objet SELECT() et lui transmet categoryDropdown comme argument
        categorySelect.selectByVisibleText("Books"); // je selectionne explicitement la catégorie "Books"
        Assert.assertEquals(categorySelect.getFirstSelectedOption().getText(), // je trouve la 1ère option sélectionnée dans categorySelect
                "Books", // et je m'assure que le texte est = à "Books"
                "Incorrect default option in category dropdown");

        WebElement subcategoryDropdown = driver.findElement(By.id("subcategoryDropdown")); // j'accède maintenant à la subcategoryDropdown
        Select subcategorySelect = new Select(subcategoryDropdown); // j'instancie l'Objet SELECT() et lui transmet subcategoryDropdown comme argument
        Assert.assertTrue(subcategoryDropdown.isEnabled(), // j'affirme que subcategoryDropdown est actif
                "Subcategory dropdown should be enabled");

        Assert.assertEquals(subcategorySelect.getOptions().size(), 5, // la liste déroulante subcategoryDropdown doit contenir 5 options
                "Incorrect number of options in subcategory dropdown");

        subcategorySelect.selectByVisibleText("Fiction"); // je selectionne explicitement le texte visible "Fiction"
        subcategorySelect.selectByVisibleText("Non-Fiction"); // et le texte visible "Non-Fiction"

        Assert.assertEquals(subcategorySelect.getAllSelectedOptions().size(), 2,
                "Incorrect number of selected subcategories"); // revient à la même chose que les 2 lignes ci-dessous

        // accéder à toutes les options sélectionnées en appelant getAllSelectedOptions()
        List<WebElement> selectedOptions = subcategorySelect.getAllSelectedOptions();

        Assert.assertEquals(selectedOptions.size(), 2,
                "Incorrect number of selected subcategories");
        Assert.assertTrue(selectedOptions.stream().anyMatch(option -> option.getText().equals("Fiction")),
                "Fiction is not selected");
        Assert.assertTrue(selectedOptions.stream().anyMatch(option -> option.getText().equals("Non-Fiction")),
                "Non-Fiction is not selected");

        delay();
    }

    @Test
    private void testDynamicMultiselectDeselect() {
        driver.get(DYNAMIC_MULTISELECT_DROPDOWN);

        WebElement categoryDropdown = driver.findElement(By.id("categoryDropdown"));

        Select categorySelect = new Select(categoryDropdown); // j'instancie l'Objet SELECT() et lui transmet categoryDropdown comme argument
        categorySelect.selectByVisibleText("Books"); // je selectionne explicitement la catégorie "Books"

        WebElement subcategoryDropdown = driver.findElement(By.id("subcategoryDropdown")); // j'accède maintenant à la subcategoryDropdown
        Select subcategorySelect = new Select(subcategoryDropdown); // j'instancie l'Objet SELECT() et lui transmet subcategoryDropdown comme argument

        subcategorySelect.selectByVisibleText("Fiction");
        subcategorySelect.selectByVisibleText("Non-Fiction");
        subcategorySelect.selectByVisibleText("Comics");

        delay();

        Assert.assertEquals(subcategorySelect.getAllSelectedOptions().size(), 3, // j'affirme avoir sélectionné 3 options à l'aide de getAllSelectedOptions().size()
                "Incorrect number of selected subcategories");

        subcategorySelect.deselectByVisibleText("Fiction");

        Assert.assertEquals(subcategorySelect.getAllSelectedOptions().size(), 2,  // revient à la même chose que les 2 lignes ci-dessous
                "Incorrect number of selected subcategories");
        // accéder à toutes les options sélectionnées en appelant getAllSelectedOptions()
        List<WebElement> selectedOptions = subcategorySelect.getAllSelectedOptions();
        Assert.assertEquals(selectedOptions.size(), 2,  // revient à la même chose que le précédent Assert
                "Incorrect number of selected subcategories");

        Assert.assertTrue(selectedOptions.stream().anyMatch(option -> option.getText().equals("Comics")),
                "Comics is not selected");
        Assert.assertTrue(selectedOptions.stream().anyMatch(option -> option.getText().equals("Non-Fiction")),
                "Non-Fiction is not selected");

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

