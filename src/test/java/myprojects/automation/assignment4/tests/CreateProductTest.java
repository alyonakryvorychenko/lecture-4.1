package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @Test
    public void createNewProduct() {
        // TODO implement test for product creation

        // actions.login(login, password);
        log("log in with real credentials");
        actions.login("webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw");
        Assert.assertTrue(driver.findElement(By.className("page-title")).getText().contains("Пульт"),"wrong title page");

        //create new product
        actions.createProduct();

        // ...
    }

    // TODO implement logic to check product visibility on website
}
