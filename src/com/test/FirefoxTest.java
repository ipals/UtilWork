package com.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

public class FirefoxTest {

	@Test
	public void testOK() throws InterruptedException {
		WebDriver driver = new FirefoxDriver();
		
		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("kohls coupons");

		Thread.sleep(2000);
		
		List<WebElement> resultsList = driver.findElements(By.className("r"));
		for(WebElement ele : resultsList){
			System.out.println(ele.getText());
		}
		Assert.assertEquals(resultsList.get(2).getText(), "20% Off Kohls Coupon: Promo Codes, Printable Coupons", "Expected 3rd result is wrong");
		driver.quit();
	}
	
	@Test
	public void testKO() throws InterruptedException {
		WebDriver driver = new FirefoxDriver();
		
		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("kohls coupons");

		Thread.sleep(2000);
		
		List<WebElement> resultsList = driver.findElements(By.className("r"));
		for(WebElement ele : resultsList){
			System.out.println(ele.getText());
		}
		Assert.assertNotEquals(resultsList.get(1).getText(), "20% Off Kohls Coupon: Promo Codes, Printable Coupons", "Expected 3rd result is wrong");
		driver.quit();
	}

}
