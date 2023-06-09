import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Section09Code {

	public static void main(String[] args) {
		WebDriver webDriver = new ChromeDriver();
		// Implicit Wait - but it has been deprecated
		// webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		// Explicit Wait
		WebDriverWait waiting = new WebDriverWait(webDriver, Duration.ofSeconds(5));

		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/");
		String[] itemsNeeded = { "Cucumber", "Brocolli", "Beetroot" };

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		addItems(webDriver, itemsNeeded);
		webDriver.findElement(By.xpath("//img[@alt='Cart']")).click();
		webDriver.findElement(By.xpath("//button[contains(text(),'PROCEED TO CHECKOUT')]")).click();
		waiting.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.promoCode")));

		webDriver.findElement(By.cssSelector("input.promoCode")).sendKeys("rahulshettyacademy");
		webDriver.findElement(By.cssSelector("button.promoBtn")).click();

		waiting.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.promoInfo")));
		System.out.println(webDriver.findElement(By.cssSelector("span.promoInfo")).getText());

		// Demonstrate using a fluent wait
		webDriver.switchTo().newWindow(WindowType.TAB);
		webDriver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		waiting.until(ExpectedConditions.elementToBeClickable(By.id("start")));

		webDriver.findElement(By.cssSelector("[id='start'] button")).click();

		Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(3)).ignoring(NoSuchElementException.class);

		WebElement helloElement = waiting.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver webDriver) {
				if (webDriver.findElement(By.cssSelector("[id='finish'] h4")).isDisplayed()) {
					return webDriver.findElement(By.cssSelector("[id='finish'] h4"));
				} else {
					return null;
				}
			}
		});

		System.out.println("Hello World! Text is displayed: "
				+ webDriver.findElement(By.cssSelector("[id='finish'] h4")).isDisplayed());

	}

	public static void addItems(WebDriver webDriver, String[] itemsNeeded)

	{
		int j = 0;
		List<WebElement> products = webDriver.findElements(By.cssSelector("h4.product-name"));
		System.out.println("Number of products: " + products.size());
		for (int i = 0; i < products.size(); i++) {

			String formattedName = products.get(i).getText().split("-")[0].trim();
			System.out.println("Product name: " + formattedName);
			// format it to get actual vegetable name
			// convert array into array list for easy search
			// check whether name you extracted is present in arrayList or not-
			List<String> itemsNeededList = Arrays.asList(itemsNeeded);

			if (itemsNeededList.contains(formattedName)) {
				j++;
				// click on Add to cart
				webDriver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();

				if (j == itemsNeeded.length) {
					break;
				}
			}
		}
	}
}
