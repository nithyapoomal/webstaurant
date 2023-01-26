package com.webstaurant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebstaurantDemo {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "c:/tools/driver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		boolean pagination = true;
		driver.get("https://www.webstaurantstore.com/");
		driver.findElement(By.xpath("//input[@id='searchval']")).sendKeys("stainless work table");
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();
		WebElement addCartEle = null;
		boolean nextPage = true;

		while (pagination) {
			List<WebElement> allTableEleList = driver.findElements(By.xpath("//div[@id='product_listing']/div"));
			System.out.println("The number of elements on the first page:" + allTableEleList.size());
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			List<WebElement> nextPageEleList = driver.findElements(By.xpath("//li[@class=\"rc-pagination-next\"]"));

			if (nextPageEleList.size() > 0) {
				nextPage = true;
			} else {
				nextPage = false;
			}

			if (allTableEleList.size() > 0) {

				for (WebElement eachTableEle : allTableEleList) {
					if (nextPage) {
						try {

							Assert.assertTrue(eachTableEle.getText().contains("Table"));
						} catch (AssertionError e) {
							Assert.assertFalse(eachTableEle.getText().contains("Table"));
							System.out.println("This Product do not have the word Table in the description: "
									+ eachTableEle.getText());
							continue;
						}
					} else {
						// This try block will run only in the last page.
						try {

							Assert.assertTrue(eachTableEle.getText().contains("Table"));
							try {
								/*
								 * Finding the element for the "Add To Cart" is very slow. Hence the code takes to
								 * finish lot of time in the last page.
								 */
								addCartEle = eachTableEle.findElement(By.xpath(".//input[@name='addToCartButton']"));
							} catch (Exception e) {
								System.out.println("This Item has the correct string Table however it is out of stock :"
										+ eachTableEle.getText());
							}
						} catch (AssertionError e) {
							Assert.assertFalse(eachTableEle.getText().contains("Table"));
							System.out.println("This Product do not have the word Table in the description: "
									+ eachTableEle.getText());
							continue;
						}
					}
				}
				if (nextPage) {
					nextPageEleList.get(0).click();
				} else {
					// Break the while Loop when the Next Page pagination link is disabled.
					break;
				}

			} else {
				System.out.println("Stainless Work Table Search returned no results");
				pagination = false;
			}

		} // end of while loop

		// Add the last item that has the word "Table" to Cart and Empty the cart
		addCartEle.click();

		String s1 = driver.getWindowHandle();
		System.out.println("The window name is :" + s1);
		driver.switchTo().window(s1);
		driver.findElement(By.xpath("//button[contains(text(),'Add To Cart')]")).click();
		Thread.sleep(2000);
		driver.switchTo().window(s1);
		driver.findElement(By.xpath("//a[contains(text(),'View Cart')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//a[contains(text(),'Empty Cart')]")).click();
		Thread.sleep(2000);
		driver.switchTo().window(s1);
		driver.findElement(By.xpath("//button[normalize-space()='Empty Cart']")).click();
		Thread.sleep(2000);
		driver.close();

	}

}

