package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.GeneralActions;
import myprojects.automation.assignment4.model.ProductData;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    GeneralActions generalActions;

    @DataProvider(name = "credentials")
    public Object[][] loginToAdmin() {
        return new Object[][] {
                { "webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw"}
        };
    }

    @Test(dataProvider = "credentials")
    public void createNewProduct(String login, String password) throws InterruptedException {

        log("log in with real credentials");
        actions.login(login, password);
        Assert.assertTrue(driver.findElement(By.className("page-title")).getText().contains("Пульт"),"wrong title page");

        //create new product
        actions.createProduct(ProductData.generate());
    }

    @Test (dependsOnMethods = {"createNewProduct"})
    public void checkProductFrontend (){
        generalActions.checkProductAttributes();
    }


}
