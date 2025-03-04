package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class TableTest {

    private static final String SITE = "https://testpages.eviltester.com/styled/index.html";
    private static final String NESTED_TABLE = "file:///C:/Selenium/learning-selenium/src/test/java/templates/nested_table.html";
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
    public void testRowCount() {
        driver.get(SITE);

        WebElement tableLinkEl = driver.findElement(By.linkText("Table Test Page"));
        tableLinkEl.click();

        WebElement tableEl = driver.findElement(By.id("mytable")); // accède à l'élément TABLE par #mytable
        List<WebElement> rowsEl = tableEl.findElements(By.tagName("tr")); // trouver tous les élémentS "TR" dans tableEl
        rowsEl.forEach(System.out::println); // j'imprime une représentation de chaque éléments sous forme de chaîne

        Assert.assertEquals(rowsEl.size(), 5,
                "Number of rows in static table doesn't match"); // j'affirme que nous avons bien 5 ligne "TR"

        delay();
    }

    @Test
    public void testColumnCount() {
        driver.get(SITE);

        WebElement tableLinkEl = driver.findElement(By.linkText("Table Test Page"));
        tableLinkEl.click();

        WebElement tableEl = driver.findElement(By.id("mytable"));

        List<WebElement> rowsEl = tableEl.findElements(By.tagName("tr")); // accède à tous les éléments "TR"

        //skip the header: je commence la boucle for à "int i=1" ce qui signifie que je saute la ligne d'entête
        for (int i = 1; i < rowsEl.size(); i++) {
            List<WebElement> columnsEl = rowsEl.get(i).findElements(By.tagName("td")); // trouver les cellules "TD" dans "TR"
            Assert.assertEquals(columnsEl.size(), 2,
                    "Number of columns in row " + i + " doesn't match");
            // on affirme que la taille des colonnes = 2 pour toutes les lignes de données
        }
        delay();
    }

    @Test
    public void testHeaders() {
        driver.get(SITE);

        WebElement tableLinkEl = driver.findElement(By.linkText("Table Test Page"));
        tableLinkEl.click();

        WebElement tableEl = driver.findElement(By.id("mytable"));

        WebElement headerRowEl = tableEl.findElement(By.tagName("tr")); // findElement(sans S) récupère le 1er "TR" qu'il rencontre, et qui contient l'entête
        List<WebElement> headersEl = headerRowEl.findElements(By.tagName("th")); // findElementS récupère les éléments d'entête "TH" dans headerRowEl
        Assert.assertEquals(headersEl.size(), 2, "Number of headers in static table doesn't match"); // headersEl contient 2 colonnes
        Assert.assertEquals(headersEl.get(0).getText(), "Name", "Header name for column 1 doesn't match"); // get(0) accède de manière explicite à la 1ère entête
        Assert.assertEquals(headersEl.get(1).getText(), "Amount", "Header name for column 2 doesn't match"); // get(1) accède de manière explicite à la 2eme entête

        System.out.println("Headers: " + headersEl.get(0).getText() + ", " + headersEl.get(1).getText()); // j'imprime le texte des entêtes

    }

    @Test
    public void testSingleRow() {
        driver.get(SITE);

        WebElement tableLinkEl = driver.findElement(By.linkText("Table Test Page"));
        tableLinkEl.click();

        WebElement tableEl = driver.findElement(By.id("mytable"));

        // Skip the header row
        WebElement firstRowEl = tableEl.findElements(By.tagName("tr")).get(1); // .get(1) signifie que je saute la ligne d'entête .get(0)
        List<WebElement> cellsEl = firstRowEl.findElements(By.tagName("td")); // findElementS récupère les "TD" dans firstRowEl

        System.out.println("Number of cells in the row: " + cellsEl.size()); // j'imprime la taille de la liste des cellules
        Assert.assertEquals(cellsEl.size(), 2, "Number of cells in the row doesn't match"); // cellsEl contient 2 cellules

        System.out.println("---Text in cells");
        for (WebElement cellEl : cellsEl) {
            System.out.println(cellEl.getText()); // j'imprime le contenu textuel de chaque cellules
        }

        Assert.assertEquals(cellsEl.get(0).getText(), "Alan"); // get(0) accède de manière explicite à la 1ère cellule
        Assert.assertEquals(cellsEl.get(1).getText(), "12"); // get(1) accède de manière explicite à la 2eme cellule

        System.out.println("First line: " + cellsEl.get(0).getText() + ", " + cellsEl.get(1).getText()); // j'imprime

    }

    @Test
    public void testTableData() {
        driver.get(SITE);

        WebElement tableLinkEl = driver.findElement(By.linkText("Table Test Page"));
        tableLinkEl.click();

        WebElement tableEl = driver.findElement(By.id("mytable"));

        String[][] expectedData = { // tableau bidimensionnel pour les données que je pense voir
                {"Alan", "12"},
                {"Bob", "23"},
                {"Aleister", "33.3"},
                {"Douglas", "42"}
        };

        List<WebElement> rows = tableEl.findElements(By.tagName("tr")); // j'accède à tous les éléments "TR" du tableau

        // "int i=1" Skip the header row
        for (int i = 1; i < rows.size(); i++) { // parcourir chaque éléments "TR" sauf l'entête
            List<WebElement> cells = rows.get(i).findElements(By.tagName("td")); // j'accède aux cellules de chaque "TR" en appelant rows.get(i)
            Assert.assertEquals(cells.size(), 2, "Number of columns in row " + i + " doesn't match");

            System.out.print("Row " + i + ": ");
                for (int j = 0; j < cells.size(); j++) { // parcourir chaque éléments "td"
                    System.out.print(cells.get(j).getText() + ", "); // j'imprime le texte de la cellule
                    // j'affirme que la valeur de la cellule correspond aux attendus de mon tableau bidimensionnel
                    Assert.assertEquals(cells.get(j).getText(), expectedData[i - 1][j]); // remarquer que j'ai [i-1] pour tenir compte de l'entête absente dans expectedData
                    // nous devons donc diminuer de 1 pour obtenir la bonne ligne à des fins de comparison
                }

            System.out.println();
        }
        delay();
    }

    @Test
    public void testNumberOfRows() {
        driver.get(NESTED_TABLE);

        List<WebElement> outerTableRowsEl = driver.findElements(By.xpath(
                "//table[@id='employeeTable']/tbody/tr"));

        Assert.assertEquals(outerTableRowsEl.size(), 5,
                "Incorrect number of rows in the outer table");

        System.out.println("Outer table rows: " + outerTableRowsEl);

        delay();
    }

    @Test
    public void testFirstRowData() {
        driver.get(NESTED_TABLE);

        WebElement firstRowEl = driver.findElement(By.xpath(
                "//table[@id='employeeTable']/tbody/tr[1]")); // remarquez comment j'accède au 1er "TR" avec ".../tr[1]"
        List<WebElement> firstRowCellsEl = firstRowEl.findElements(By.xpath(
                ".//td"));

        Assert.assertEquals(firstRowCellsEl.get(0).getText(), "Picture",
                "Name in the first row doesn't match");
        Assert.assertEquals(firstRowCellsEl.get(1).getText(), "holder",
                "Position in the first row doesn't match");

    }

    @Test
    public void testNestedTableRowsData() {
        driver.get(NESTED_TABLE);

        WebElement nestedTableEl = driver.findElement(By.xpath(
                "//table[@id='employeeTable']/tbody/tr[1]/td[3]/table")); // je cherche la table dans la 3ème cellule du 1er "TR" du tableau extérieur

        List<WebElement> nestedRowsEl = nestedTableEl.findElements(By.tagName("tr")); // j'obtiens les "TR" du nestedRowsEl

        Assert.assertEquals(nestedRowsEl.get(0).getText(), "Email aaaaa", "Email in the first row nested table doesn't match");
        Assert.assertEquals(nestedRowsEl.get(1).getText(), "Phone 01010101", "Phone in the first row nested table doesn't match");
        Assert.assertEquals(nestedRowsEl.get(2).getText(), "Address 123", "Address in the first row nested table doesn't match");


        nestedTableEl = driver.findElement(By.xpath(
                "//table[@id='employeeTable']/tbody/tr[2]/td[3]/table")); // je cherche la table dans la 3ème cellule du 2eme "TR" du tableau extérieur
        nestedRowsEl = nestedTableEl.findElements(By.tagName("tr"));

        Assert.assertEquals(nestedRowsEl.get(0).getText(), "Email bbbbb", "Email in the first row nested table doesn't match");
        Assert.assertEquals(nestedRowsEl.get(1).getText(), "Phone 020202", "Phone in the first row nested table doesn't match");
        Assert.assertEquals(nestedRowsEl.get(2).getText(), "Address 456", "Address in the first row nested table doesn't match");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //driver.close(); // ferme les onglets individuellement
        }
    }
}
