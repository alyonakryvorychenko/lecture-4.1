package myprojects.automation.assignment4;

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

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions extends BaseTest{
    private WebDriver driver;
    private WebDriverWait wait;
    private By loginLocator = By.id("email");
    private By passwordLocator = By.id("passwd");
    private By catalogueLink = By.cssSelector("#subtab-AdminCatalog");
    private By productLink = By.xpath("//nav//li[4]//li[1]/a");
    private By addProductButton = By.id("page-header-desc-configuration-add");
    private By nameProductLocator = By.id("form_step1_name_1");
    private By quantityProductLocator = By.id("form_step1_qty_0_shortcut");
    private By priceProductLocator = By.id("form_step1_price_shortcut");
    private By pageBody = By.xpath("//body");
    private By frontendAllProductLink = By.xpath("//a[contains(@class, 'all-product')]");
    private static ProductData actionsProductData;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void login(String login, String password) {
        // TODO implement logging in to Admin Panel
        CustomReporter.log("Login as user" + login);

        driver.navigate().to(Properties.getBaseAdminUrl());
        WebElement inputLogin = driver.findElement(loginLocator);
        inputLogin.sendKeys(login);
        WebElement inputPassword = driver.findElement(passwordLocator);
        inputPassword.sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData newProduct) throws InterruptedException {
        actionsProductData = newProduct;
        waitForContentLoad(catalogueLink);
        WebElement catalogueLink = driver.findElement(this.catalogueLink);

        log ("Click the 'Product' item in main menu");
        Actions actions = new Actions(driver);
        actions.moveToElement(catalogueLink).perform();

        log ("Click the 'Product Link' item in main menu");
        WebElement productLink = driver.findElement(this.productLink);
        actions.moveToElement(productLink).perform();
        productLink.click();

        //check that product page is displayed
        waitForContentLoad(addProductButton);

        //create new product
        driver.findElement(addProductButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='form_content']")));

        // enter data of product

        WebElement productQtyField = driver.findElement(quantityProductLocator);
        productQtyField.sendKeys(Keys.BACK_SPACE);
        productQtyField.sendKeys(newProduct.getQty().toString());

        WebElement productNameField = driver.findElement(nameProductLocator);
        productNameField.sendKeys(newProduct.getName());

        WebElement productPriceField = driver.findElement(priceProductLocator);
        productPriceField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        productPriceField.sendKeys(Keys.BACK_SPACE);
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

    public void checkProductAttributes (){
        //Check the product on the site
        log("log in the the frontend");
        driver.navigate().to(Properties.getBaseUrl());

        //wait to loading the page
        waitForContentLoad(frontendAllProductLink);

        log("click the All product link");
        WebElement allProductLink = driver.findElement(frontendAllProductLink);
        allProductLink.click();

        //wait to loading page
        waitForContentLoad(By.id("search_filters"));

        log("find the created product on the site");
        WebElement searchField = driver.findElement(By.className("ui-autocomplete-input"));
        log("enter name");
        searchField.sendKeys(actionsProductData.getName());
        log("press enter");
        //searchField.sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//button/i")).click();

        log("wait the title of page");
        waitForContentLoad(By.className("h2"));

        log("Make sure that search page is displayed");
        WebElement searchPageTitle = driver.findElement(By.className("h2"));
        Assert.assertEquals(searchPageTitle.getText(), "РЕЗУЛЬТАТЫ ПОИСКА", "Search page isn't opened");

        log("Check the attributes of product");
        //check the attributes of product
        Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='h3 product-title']/a")).getText(), actionsProductData.getName(), "Wrong name of product");
        WebElement priceAttribute = driver.findElement(By.xpath("//div[@class='product-price-and-shipping']/span"));
        String priceAttribute1 = priceAttribute.getText();

        Assert.assertEquals(cutString(priceAttribute1), actionsProductData.getPrice(), "Wrong product price");
    }
    public void waitForContentLoad(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public String cutString (String price){
        return price.substring(0, (price.length() - 2));
    }
}
