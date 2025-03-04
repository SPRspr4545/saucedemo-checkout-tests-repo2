package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.utils.DriverFactory;
import org.openqa.selenium.*;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Set;

public class CookiesTest {

    private static final String COOKIE = "https://testpages.eviltester.com/styled/cookies/adminlogin.html";

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
    public void testGetCookie() {
        driver.get(COOKIE);

        Set<Cookie> cookies = driver.manage().getCookies(); // donne un ensemble d'objets cookies

        System.out.println("Cookies: " + cookies);
        Assert.assertTrue(cookies.isEmpty(), // j'affirme que les cookies sont vides
                "Expected empty set of cookies!");

        delay();
    }

    @Test
    public void testAddCookie() { // Comment ajouter un cookie de manière explicite à un site
        driver.get(COOKIE);

        String cookieName = "session_id";
        String cookieValue = "abc123";

        Cookie newCookie = new Cookie(cookieName, cookieValue); // j'instancie un objet newCookie...
        driver.manage().addCookie(newCookie); // ...que j'ajoute au site

        Set<Cookie> cookies = driver.manage().getCookies(); // Après l'avoir ajouter au site, récupérons le...
        Assert.assertFalse(cookies.isEmpty(), "Cookies not found"); // ...pour voir s'il est présent

        System.out.println("Cookies: " + cookies); // j'imprime les cookies
        // >>>>>>>> Cookies: [session_id=abc123; path=/; domain=testpages.eviltester.com;secure;; sameSite=Lax]
        // l'attribut "sameSite=Lax" permet au serveur d'affirmer une authentification par cookie
        // fournit une protection contre la falsification des requêtes intersites et les attaques CSRF

        // puis j'affirme que le set de cookies contient le (newCookie)
        Assert.assertTrue(cookies.contains(newCookie), "Added cookie not found!");

        delay();
    }

    @Test
    public void testAddMultipleCookies() {
        driver.get(COOKIE);

        String[] cookieNames = {"session_id", "user_id", "auth_token"}; // liste des noms des cookies
        String[] cookieValues = {"abc123", "user123", "token123"}; // liste des valeurs correspondantes

        for (int i = 0; i < cookieNames.length; i++) {
            // j'instancie un newCookie avec le nom et la valeur de chaque position de l'index
            Cookie newCookie = new Cookie(cookieNames[i], cookieValues[i]);
            driver.manage().addCookie(newCookie); // j'ajoute chaque newCookie que j'ai créé au site
        }

        Set<Cookie> cookies = driver.manage().getCookies(); // Après l'avoir ajouter au site, récupérons les 3 cookies...
        Assert.assertFalse(cookies.isEmpty(), "Cookies not found"); // ...pour voir s'ils ne sont pas vide

        System.out.println("Cookies: " + cookies); // j'imprime les cookies
        System.out.println("========================ADDITIONAL DETAILS============================");

        for (Cookie cookie : cookies) { // on peut également accéder aux attributs individuels ou aux propriétés de chaque cookies
            System.out.println("name: " + cookie.getName() + ", Value: " + cookie.getValue()); // j'imprime le nom et la valeur
            // puis j'imprime les propriétés de ces cookies
            System.out.println("Cookie Domain: " + cookie.getDomain()); // le Domain
            System.out.println("Cookie Path: " + cookie.getPath()); // le chemin
            System.out.println("Cookie Same Site: " + cookie.getSameSite()); // cookie du même site ?
            System.out.println("Is Cookie Secure: " + cookie.isSecure()); // Sécurisé ?
            System.out.println("Is Cookie HttpOnly: " + cookie.isHttpOnly()); // HttpOnly ?
            System.out.println("--------------------------------------------");
        }

        delay();
    }

    @Test
    public void testGetSingleCookie() {
        driver.get(COOKIE);

        String[] cookieNames = {"session_id", "user_id", "auth_token"}; // liste des noms des 3 cookies
        String[] cookieValues = {"abc123", "user123", "token123"}; // liste des valeurs correspondantes

        for (int i = 0; i < cookieNames.length; i++) {
            // j'instancie un newCookie avec le nom et la valeur de chaque position de l'index
            Cookie newCookie = new Cookie(cookieNames[i], cookieValues[i]);
            driver.manage().addCookie(newCookie); // j'ajoute chaque newCookie que j'ai créé au site
        }

        //Set<Cookie> cookies = driver.manage().getCookies(); // Après l'avoir ajouter au site, récupérons les 3 cookies...
        Cookie singleCookie = driver.manage().getCookieNamed("auth_token");

        Assert.assertNotNull(singleCookie,"Cookie not found!");
        Assert.assertEquals(singleCookie.getName(), "auth_token");
        Assert.assertEquals(singleCookie.getValue(), "token123");

        System.out.println("========================SINGLE COOKIE DETAILS============================");

        System.out.println("Name: " + singleCookie.getName() + ", Value: " + singleCookie.getValue());
        // puis j'imprime les propriétés de ces cookies
        System.out.println("Cookie Domain: " + singleCookie.getDomain()); // le Domain
        System.out.println("Cookie Path: " + singleCookie.getPath()); // le chemin
        System.out.println("Cookie Same Site: " + singleCookie.getSameSite()); // cookie du même site ?
        System.out.println("Is Cookie Secure: " + singleCookie.isSecure()); // Sécurisé ?
        System.out.println("Is Cookie HttpOnly: " + singleCookie.isHttpOnly()); // HttpOnly ?

    }

    @Test
    public void testUpdateCookie() { // on ne met pas vraiment à jour un cookie, on le supprime pour le recréer
        driver.get(COOKIE);

        String cookieName = "session_id"; // nom
        String cookieValue = "abc123"; // valeur

        Cookie newCookie = new Cookie(cookieName, cookieValue); // je crée un nouveau cookie
        driver.manage().addCookie(newCookie); // je l'ajoute au site actuel

        Cookie existingCookie = driver.manage().getCookieNamed(cookieName); // je récupère ce cookie par son nom

        // je crée un nouveau cookie à partir de "existingCookie" avec les valeurs que je veux
        Cookie updatedCookie = new Cookie.Builder(existingCookie.getName(), "xyz123") // met à jour la value
                .domain(existingCookie.getDomain())
                .path(existingCookie.getPath())
                .isHttpOnly(existingCookie.isHttpOnly())
                .isSecure(existingCookie.isSecure())
                .sameSite("Strict") // met à jour la propriété sameSite
                .build(); // Crée un nouvel objet cookie

        // Delete and add the cookie atherwise wewill have two cookies with the same name and different values
        driver.manage().deleteCookie(existingCookie); // je supprime le cookie existant
        driver.manage().addCookie(updatedCookie); // ajouter le cookie avec les attributs mis à jour

        updatedCookie = driver.manage().getCookieNamed(cookieName); // récupère le cookie mis à jour par son nom

        System.out.println("Cookie Value (After Update):" + updatedCookie.getValue());
        System.out.println("Cookie Same site (After Update):" + updatedCookie.getSameSite());

        Assert.assertEquals(updatedCookie.getValue(), "xyz123");
        Assert.assertEquals(updatedCookie.getSameSite(), "Strict");
    }

    @Test
    public void testDeleteCookie() {
        driver.get(COOKIE);

        String[] cookieNames = {"session_id", "user_id", "auth_token"}; // liste des noms des 3 cookies
        String[] cookieValues = {"abc123", "user123", "token123"}; // liste des valeurs correspondantes

        for (int i = 0; i < cookieNames.length; i++) {
            // j'instancie un newCookie avec le nom et la valeur de chaque position de l'index
            Cookie newCookie = new Cookie(cookieNames[i], cookieValues[i]);
            driver.manage().addCookie(newCookie); // j'ajoute chaque newCookie que j'ai créé au site
        }

        //Set<Cookie> cookies = driver.manage().getCookies(); // Après l'avoir ajouter au site, récupérons les 3 cookies...
        //Cookie singleCookie = driver.manage().getCookieNamed("auth_token");
        Cookie sessionCookie = driver.manage().getCookieNamed("session_id"); // récupère le cookie "session_id"
        Assert.assertNotNull(sessionCookie); // affirme qu'il n'est pas nul

        driver.manage().deleteCookieNamed("session_id"); // supprimer nommément le cookie "session_id"

        sessionCookie = driver.manage().getCookieNamed("session_id"); // je le récupère à nouveau pour...
        Assert.assertNull(sessionCookie); // ...affirmer qu'in est nul car nous venos de le supprimer

        Assert.assertFalse(driver.manage().getCookies().isEmpty(), "Cookie not found!"); // j'affirme que le jeu de cookies n'est pas vide (il en reste 2)

        driver.manage().deleteAllCookies(); // Supprime tous les cookies

        Assert.assertTrue(driver.manage().getCookies().isEmpty(), "Cookies found!"); // puis j'affirme que isEmpty() est vrai

    }

    @Test
    public void testLogin() {
        driver.get(COOKIE);

        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login"));

        usernameInput.sendKeys("Admin"); // je vais d'abord me connecter en tant qu'Admin
        passwordInput.sendKeys("AdminPass");

        delay();

        loginButton.click();

        Assert.assertEquals(driver.getCurrentUrl(), // l'URL de la page est différente pour Admin
                "https://testpages.eviltester.com/styled/cookies/adminview.html",
                "Login failed");

        Cookie loginCookie = driver.manage().getCookieNamed("loggedin"); // après connexion Admin, le CookieNamed("loggedin") est créé sur ce site

        Assert.assertNotNull(loginCookie); // affirme que le cookie n'est pas nul
        Assert.assertEquals(loginCookie.getValue(),"Admin"); // j'affirme égelement que la value de ce cookie est "Admin"

        System.out.println("Login cookie: " + loginCookie);

        delay();
    }

    @Test
    public void testCorrectLinksEnabledForAdmin() {
        driver.get(COOKIE);

        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login"));

        usernameInput.sendKeys("Admin"); // je vais d'abord me connecter en tant qu'Admin
        passwordInput.sendKeys("AdminPass");

        delay();

        loginButton.click();

        Cookie loginCookie = driver.manage().getCookieNamed("loggedin"); // après connexion Admin, le CookieNamed("loggedin") est créé sur ce site

        Assert.assertNotNull(loginCookie); // affirme que le cookie n'est pas nul
        Assert.assertEquals(loginCookie.getValue(),"Admin"); // j'affirme égelement que la value de ce cookie est "Admin"

        System.out.println("Login cookie: " + loginCookie);

        Assert.assertNotNull(driver.findElement(By.id("navadminlogin")).getAttribute("href")); // balise d'ancrage vers la page connexion Admin
        Assert.assertNotNull(driver.findElement(By.id("navadminview")).getAttribute("href")); // lien vers la vue Administration
        Assert.assertNotNull(driver.findElement(By.id("navadminlogout")).getAttribute("href")); // lien de déconnexion de l'Admin

        Assert.assertNull(driver.findElement(By.id("navadminsuperview")).getAttribute("href")); // lien vers la vue SuperAdmin est NULL

    }

    @Test
    public void testLoginLogoutBySettingCookies() { // on peut connecter l'utilisateur simplement en installant le cookie
        driver.get(COOKIE);

        Assert.assertEquals(driver.getCurrentUrl(),
                "https://testpages.eviltester.com/styled/cookies/adminlogin.html");

        Cookie newCookie = new Cookie("loggedin","Admin"); // au lieu de me connecter, j'instancie un nouveau cookie "loggedin" avec la value "Admin"
        driver.manage().addCookie(newCookie); // ajoute le cookie au site

        delay();

        driver.navigate().refresh(); // rafraichir la page
        // En actualisant la page, le site verra que le cookie Admin connecté et redirigera vers la vue d'administration
        Assert.assertEquals(driver.getCurrentUrl(),
                "https://testpages.eviltester.com/styled/cookies/adminview.html",
                "Login failed"); // Affirme l'URL d'administration

        Cookie loginCookie = driver.manage().getCookieNamed("loggedin"); // Accède au CookieNamed("loggedin")

        Assert.assertNotNull(loginCookie); // affirme que le cookie n'est pas nul
        Assert.assertEquals(loginCookie.getValue(),"Admin"); // j'affirme également que la value de ce cookie est "Admin"

        System.out.println("Login cookie: " + loginCookie);

        delay();

        driver.manage().deleteCookieNamed("loggedin"); // déconnecte de la même manière en supprimant le cookie

        driver.navigate().refresh();

        Assert.assertEquals(driver.getCurrentUrl(),
                "https://testpages.eviltester.com/styled/cookies/adminlogin.html");

        delay();
    }

    @Test
    public void testCorrectLinksEnabledForSuperAdmin() {
        driver.get(COOKIE);

        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login"));

        usernameInput.sendKeys("SuperAdmin"); // je vais d'abord me connecter en tant que SuperAdmin
        passwordInput.sendKeys("AdminPass");

        delay();

        loginButton.click();

        Cookie loginCookie = driver.manage().getCookieNamed("loggedin"); // après connexion Admin, le CookieNamed("loggedin") est créé sur ce site

        Assert.assertNotNull(loginCookie); // affirme que le cookie n'est pas nul
        Assert.assertEquals(loginCookie.getValue(),"SuperAdmin"); // j'affirme égelement que la value de ce cookie est "Admin"

        System.out.println("Login cookie: " + loginCookie);

        Assert.assertNotNull(driver.findElement(By.id("navadminlogin")).getAttribute("href")); // balise d'ancrage vers la page connexion Admin
        Assert.assertNotNull(driver.findElement(By.id("navadminview")).getAttribute("href")); // lien vers la vue Administration
        Assert.assertNotNull(driver.findElement(By.id("navadminlogout")).getAttribute("href")); // lien de déconnexion de l'Admin
        Assert.assertNotNull(driver.findElement(By.id("navadminsuperview")).getAttribute("href")); // lien vers la vue SuperAdmin

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
