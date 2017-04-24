package myprojects.automation.assignment4;

import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
    //By.id("form_step1_active");

    private String productName;
    private int productQty;
    private String productPrice;

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

    public void createProduct() {
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
        productName = ProductData.generate().getName();
        productPrice = ProductData.generate().getPrice();
        productQty = ProductData.generate().getQty();


        driver.findElement(nameProductLocator).sendKeys(productName);

//        WebElement productQtyField = driver.findElement(quantityProductLocator);
        driver.findElement(quantityProductLocator).clear();
        driver.findElement(quantityProductLocator).sendKeys(String.valueOf(productQty));

        WebElement productPriceField = driver.findElement(priceProductLocator);
        productPriceField.clear();
        productPriceField.sendKeys(productPrice);

        //activate the product on the site
        driver.findElement(activeSwitchLocator).click();
        //check that confirm message is displayed
        Assert.assertEquals(driver.findElement(By.className("growl-message")).getText(), "Настройки обновлены","wrong text of message");
    }
    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        // TODO implement generic method to wait until page content is loaded

        // wait.until(...);
        // ...
    }
}
