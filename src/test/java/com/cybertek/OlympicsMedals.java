package com.cybertek;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class OlympicsMedals {

	static WebDriver driver;

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}

	@BeforeMethod
	public void bMethod() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
	}

	@Test
	public void sortTest() {
		List<WebElement> ranks = driver.findElements(By.xpath("//table[8]/tbody/tr/td[1]"));
		ranks.remove(10);
		boolean sorted = false;
		int[] k = new int[ranks.size()];
		for (int i = 0; i < ranks.size(); i++) {
			k[i] = Integer.parseInt(ranks.get(i).getText());
		}
		for (int j = 0; j < k.length - 1; j++) {
			if (k[j] < k[j + 1]) {
				sorted = true;
			} else {
				sorted = false;
			}
		}
		assertTrue(sorted);

		driver.findElement(By.xpath("(//th[@class='headerSort'])[8]")).click();
		List<WebElement> countries = driver.findElements(By.xpath("//table[8]/tbody/tr/th"));
		List<String> countS = new ArrayList<>();
		for (WebElement each : countries) {
			countS.add(each.getText());
		}
		boolean sortedC = false;
		for (int i = 1; i < countS.size(); i++) {
			if ((countS.get(i - 1).compareTo(countS.get(i)) > 0)) {
				sortedC = false;
			} else {
				sortedC = true;
			}
		}
		assertTrue(sortedC);

		List<WebElement> ranks1 = driver.findElements(By.xpath("//table[8]/tbody/tr/td[1]"));

		int[] n = new int[ranks1.size()];

		for (int i = 0; i < ranks1.size(); i++) {
			n[i] = Integer.parseInt(ranks1.get(i).getText());
		}

		for (int j = 0; j < k.length - 1; j++) {
			if (n[j] < n[j + 1]) {
				sorted = false;
			} else {
				sorted = true;
			}
		}
		assertTrue(sorted);
	}

	@Test
	public void theMost() {
		String actualGold = gold();
		String expectedGold = " United States (USA)";
		assertEquals(actualGold, expectedGold, "Gold assert failed");

		String actualSilver = silver();
		String expectedSilver = " United States (USA)";
		assertEquals(actualSilver, expectedSilver, "Silver assert failed");

		String actualBronze = bronze();
		String expectedBronze = " United States (USA)";
		assertEquals(actualBronze, expectedBronze, "Bronze assert failed");

		String actualMost = mostMedals();
		String expectedMost = " United States (USA)";
		assertEquals(actualMost, expectedMost, "Most medals assert failed");
	}

	@Test
	public void getIndex() {
		String actual = rowColumn(" Japan (JPN)");
		String expected = " Japan (JPN)";
		assertEquals(actual, expected, "Index didnt match");
	}

	@Test
	public void silverCount() {
		List<String> actual = silverCountries();
		List<String> expected = Arrays.asList(" China (CHN)", " France (FRA)");
		assertEquals(actual.toString(), expected.toString());
	}

	@Test
	public void getSum() {
		Set<String> actual = sum();
		List<String> expected = Arrays.asList(" Australia (AUS)", " Italy (ITA)");
		assertEquals(actual.toString(), expected.toString());
	}

	@AfterClass
	public void terminate() {
		driver.close();
	}

	public static Set<String> sum() {
		Set<String> result = new HashSet<>();
		List<WebElement> countries = driver.findElements(By.xpath("//table[8]/tbody/tr/th"));
		countries.remove(10);
		List<WebElement> bronze = driver.findElements(By.xpath("//table[8]/tbody/tr/td[4]"));

		int[] medals = new int[bronze.size()];
		for (int i = 0; i < bronze.size(); i++) {
			medals[i] = Integer.parseInt(bronze.get(i).getText());
		}
		for (int i = 0; i < medals.length; i++) {
			for (int j = 0; j < medals.length; j++) {
				if (medals[i] + medals[j] == 18) {
					if (countries.get(i).getText().equals(countries.get(j).getText())) {
						result.clear();

					} else {
						result.add(countries.get(i).getText());
						result.add(countries.get(j).getText());
					}
				}
			}
		}
		return result;
	}

	public static List<String> silverCountries() {
		driver.navigate().refresh();
		List<String> country = new ArrayList<>();
		List<WebElement> silver = driver.findElements(By.xpath("//table[8]/tbody/tr/td[3]"));
		silver.remove(10);
		List<WebElement> countries = driver.findElements(By.xpath("//table[8]/tbody/tr/th"));
		countries.remove(10);
		for (WebElement w : silver) {
			if (w.getText().equals("18")) {
				int index = silver.indexOf(w);
				country.add(countries.get(index).getText());
			}
		}

		return country;
	}

	public static String gold() {
		driver.navigate().refresh();
		List<WebElement> gold = driver.findElements(By.xpath("//table[8]/tbody/tr/td[2]"));
		int[] medals = new int[gold.size()];
		for (int i = 0; i < gold.size(); i++) {
			medals[i] = Integer.parseInt(gold.get(i).getText());
		}
		String countryName = "";
		for (int i = 0; i < medals.length - 1; i++) {
			if (medals[0] > medals[i]) {
				countryName = driver.findElement(By.xpath("//table[8]/tbody/tr[1]/th")).getText();
			}
		}
		return countryName;
	}

	public static String silver() {
		driver.navigate().refresh();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[4]")).click();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[4]")).click();

		List<WebElement> silver = driver.findElements(By.xpath("//table[8]/tbody/tr/td[3]"));
		int[] medals = new int[silver.size()];
		for (int i = 0; i < silver.size(); i++) {
			medals[i] = Integer.parseInt(silver.get(i).getText());
		}
		String countryName = "";
		for (int i = 0; i < medals.length - 1; i++) {
			if (medals[0] > medals[i]) {
				countryName = driver.findElement(By.xpath("//table[8]/tbody/tr[1]/th")).getText();
			}
		}
		return countryName;

	}

	public static String bronze() {
		driver.navigate().refresh();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[5]")).click();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[5]")).click();

		List<WebElement> bronze = driver.findElements(By.xpath("//table[8]/tbody/tr/td[4]"));
		int[] medals = new int[bronze.size()];
		for (int i = 0; i < bronze.size(); i++) {
			medals[i] = Integer.parseInt(bronze.get(i).getText());
		}
		String countryName = "";
		for (int i = 0; i < medals.length - 1; i++) {
			if (medals[0] > medals[i]) {
				countryName = driver.findElement(By.xpath("//table[8]/tbody/tr[1]/th")).getText();
			}
		}
		return countryName;
	}

	public static String mostMedals() {
		driver.navigate().refresh();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[6]")).click();
		driver.findElement(By.xpath("//table[8]/thead/tr/th[6]")).click();

		List<WebElement> mostMedals = driver.findElements(By.xpath("//table[8]/tbody/tr/td[5]"));
		int[] medals = new int[mostMedals.size()];
		for (int i = 0; i < mostMedals.size(); i++) {
			medals[i] = Integer.parseInt(mostMedals.get(i).getText());
		}
		String countryName = "";
		for (int i = 0; i < medals.length - 1; i++) {
			if (medals[0] > medals[i]) {
				countryName = driver.findElement(By.xpath("//table[8]/tbody/tr[1]/th")).getText();
			}
		}
		return countryName;
	}

	public static String rowColumn(String country) {
		driver.navigate().refresh();
		String element = null;
		List<WebElement> countries = driver.findElements(By.xpath("//table[8]/tbody/tr/th"));
		countries.remove(10);
		int index = 0;

		for (WebElement c : countries) {
			if (country.trim().equals(c.getText().trim())) {
				index = countries.indexOf(c);
			}
		}
		element = driver.findElement(By.xpath("(//table[8]/tbody/tr/th)[" + (index + 1) + "]")).getText();

		return element;

	}

}
