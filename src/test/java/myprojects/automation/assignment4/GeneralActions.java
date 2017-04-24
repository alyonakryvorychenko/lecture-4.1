package myprojects.automation.assignment4;

import com.sun.javafx.scene.KeyboardShortcutsHandler;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions extends BaseTest{
    private WebDriver driver;
    private WebDriverWait wait;
    private By catalogueLink = By.cssSelector("#subtab-AdminCatalog");
//    private By productLink = By.cssSelector("#subtab-AdminProducts");
    private By productLink = By.xpath("//nav//li[4]//li[1]/a");
    private By addProductButton = By.id("page-header-desc-configuration-add");
    private By nameProductLocator = By.id("form_step1_name_1");
    private By quantityProductLocator = By.id("form_step1_qty_0_shortcut");
    private By priceProductLocator = By.id("form_step1_price_shortcut");
    private By activeSwitchLocator = By.xpath("//div[@class='switch-input']");
    private By pageBody = By.xpath("//body");

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void login(String login, String password) {
        // TODO implement logging in to Admin Panel
        CustomReporter.log("Login as user" + login);

        driver.navigate().to(Properties.getBaseAdminUrl());
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData newProduct) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(catalogueLink));
        WebElement catalogueLink = driver.findElement(this.catalogueLink);
        WebElement productLink = driver.findElement(this.productLink);

        log ("Click the 'Product' item in main menu");
        Actions actions = new Actions(driver);
        actions.moveToElement(catalogueLink).perform();
        actions.moveToElement(productLink).perform();
        productLink.click();

        //check that product page is displayed
        wait.until(ExpectedConditions.presenceOfElementLocated(addProductButton));

        //create new product
        driver.findElement(addProductButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='form_content']")));

        // enter data of product

        WebElement productQtyField = driver.findElement(quantityProductLocator);
        log("cleare field");
        productQtyField.sendKeys(Keys.BACK_SPACE);
        log("Send keys");
        productQtyField.sendKeys(newProduct.getQty().toString());

        WebElement productNameField = driver.findElement(nameProductLocator);
        log("Send keys");
        productNameField.sendKeys(newProduct.getName());

        WebElement productPriceField = driver.findElement(priceProductLocator);
        log("cleare field");
        productPriceField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        productPriceField.sendKeys(Keys.BACK_SPACE);
        log("Send keys");
        productPriceField.sendKeys(newProduct.getPrice());

        //activate the product on the site
        log("check the 'Active product' switcher");
        driver.findElement(pageBody).sendKeys(Keys.chord(Keys.CONTROL, "o"));

        //check that confirm message is displayed
        Assert.assertEquals(driver.findElement(By.className("growl-message")).getText(), "Настройки обновлены.","wrong text of message");

        log("Save the product");
        driver.findElement(By.xpath("//button[contains(@class,'js-btn-save')]")).click();

        //check that confirm message is displayed
        Assert.assertEquals(driver.findElement(By.className("growl-message")).getText(), "Настройки обновлены.","wrong text of message");

    }

    public void waitForContentLoad(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
