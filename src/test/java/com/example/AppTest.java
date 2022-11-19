package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppTest {

  WebDriver driver;
  ArrayList<FlightObject> flighttList = new ArrayList<FlightObject>();

  /**
   * setup
   *
   * This method is used to setup the environment needed for each test
   * It sends two city names, clicks the relevant buttons to choose them
   * and presses the "Search flights" button.
   *
   */
  @BeforeEach
  public void setup() throws Exception {

    String origin = "Kalamata";
    String destination = "Thessaloniki";
    driver = new ChromeDriver();
    WebElement element;

    driver.get("https://www.flightnetwork.com/");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchForm-singleBound-origin-input")));

    driver.findElement(By.id("searchForm-singleBound-origin-input")).sendKeys(origin);
    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-select-2-option-0")));
    element.click();

    driver.findElement(By.id("searchForm-singleBound-destination-input")).sendKeys(destination);
    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-select-3-option-0")));
    element.click();

    driver.findElement(By.xpath("//button[contains(.,\'Search flights\')]")).click();

  }

  /**
   * loadData
   *
   * This method loads the flight data, creating a flightObject with the
   * information that is present for each flight.
   * 
   * That includes the departure and arrival times, the duration of each flight
   * the locations of each flight, the price and the stops.
   * 
   * In this version of the method only the original and final airlines are saved
   * for increased testing speed.
   * For the tests that require all the layover flight information
   * the loadAllData method is used instead.
   *
   */
  public void loadData() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-asrj15")));

    JavascriptExecutor jE = (JavascriptExecutor) driver;

    /*
     * This part scrolls to the end of the page so that all flight information would
     * be loaded on our page.
     */
    while (true) {

      ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

      jE.executeScript("return document.body.scrollHeight");
      if (driver.findElements(By.cssSelector(".css-1affg1x")).size() != 0) {
        break;
      }

    }

    // Flight information
    List<WebElement> flights = driver.findElements(By.className("e1ygvaur0"));

    /*
     * This part collects all the relevant information for each flight and then
     * creates a custom FlightObject.
     * This is done for all flights shown on the page.
     * It is used on all tests for easier testing.
     */
    for (WebElement flight : flights) {

      // Airlines
      List<WebElement> airlines = flight.findElements(By.className("e1ngmrql4"));
      List<String> airlinesStrLst = new ArrayList();

      for (WebElement element2 : airlines) {
        airlinesStrLst.add(element2.getText());
      }

      // Times
      List<WebElement> times = flight.findElements(By.className("eovrwni2"));
      String originDepartureTime = times.get(0).getText();
      String originArrivalTime = times.get(1).getText();
      String destinationDepartureTime = times.get(2).getText();
      String destinationArrivalTime = times.get(3).getText();

      // Durations
      List<WebElement> durations = flight.findElements(By.className("e1n8f0fp0"));
      String originDuration = durations.get(0).getText();
      String destinationDuration = durations.get(1).getText();

      // Locations
      List<WebElement> locations = flight.findElements(By.className("eovrwni1"));
      String originDepartureLocation = locations.get(0).getText();
      String originArrivalLocation = locations.get(1).getText();
      String destinationDepartureLocation = locations.get(2).getText();
      String destinationArrivalLocation = locations.get(3).getText();

      // Prices
      List<WebElement> prices = flight.findElements(By.className("ed2an4d3"));
      String priceStandard = prices.get(0).getText();
      String priceFlexible = "";
      if (prices.size() > 1) {
        priceFlexible = prices.get(1).getText();
      }

      // Stops
      List<WebElement> stops = flight.findElements(By.className("exysb4p0"));
      int maxStops = 0;

      for (WebElement stop : stops) {
        String stopTxt = stop.getText().replaceAll("\\D*", "");

        if (Integer.parseInt(stopTxt) > maxStops) {
          maxStops = Integer.parseInt(stopTxt);
        }
      }

      FlightObject fO = new FlightObject(airlinesStrLst, originDepartureTime, originArrivalTime,
          destinationDepartureTime, destinationArrivalTime, originDuration, destinationDuration,
          originDepartureLocation, originArrivalLocation, destinationDepartureLocation, destinationArrivalLocation,
          priceStandard, priceFlexible, maxStops);
      flighttList.add(fO);

    }

  }

  /**
   * loadData
   *
   * This method acts similarly to the loadData one.
   * The difference is that it takes the extra time to click on each flight's
   * information.
   * This gives us details that would otherwise not be known, like the layover
   * flights.
   *
   */
  public void loadAllData() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-asrj15")));

    JavascriptExecutor jE = (JavascriptExecutor) driver;

    while (true) {

      ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

      if (driver.findElements(By.cssSelector(".css-1affg1x")).size() != 0) {
        break;
      }

    }

    /*
     * This is the part that adds the extra infomation.
     * After all flights are loaded it scrolls back to the top of the page,
     * moves to each element one by one and clicks on it to give us access to the
     * information inside
     */

    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");

    List<WebElement> flights = driver.findElements(By.className("e1ygvaur0"));

    for (WebElement flight : flights) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", flight);
      flight.click();
    }

    for (WebElement flight : flights) {
      // Airlines
      List<WebElement> airlines = flight.findElements(By.className("e1ngmrql4"));
      List<String> airlinesStrLst = new ArrayList();

      for (WebElement element2 : airlines) {

        airlinesStrLst.add(element2.getText());
      }

      // Times
      List<WebElement> times = flight.findElements(By.className("eovrwni2"));
      String originDepartureTime = times.get(0).getText();
      String originArrivalTime = times.get(1).getText();
      String destinationDepartureTime = times.get(2).getText();
      String destinationArrivalTime = times.get(3).getText();

      // Durations
      List<WebElement> durations = flight.findElements(By.className("e1n8f0fp0"));
      String originDuration = durations.get(0).getText();
      String destinationDuration = durations.get(1).getText();

      // Locations
      List<WebElement> locations = flight.findElements(By.className("eovrwni1"));
      String originDepartureLocation = locations.get(0).getText();
      String originArrivalLocation = locations.get(1).getText();
      String destinationDepartureLocation = locations.get(2).getText();
      String destinationArrivalLocation = locations.get(3).getText();

      // Prices
      List<WebElement> prices = flight.findElements(By.className("ed2an4d3"));
      String priceStandard = prices.get(0).getText();
      String priceFlexible = "";
      if (prices.size() > 1) {
        priceFlexible = prices.get(1).getText();
      }
      // Stops
      List<WebElement> stops = flight.findElements(By.className("exysb4p0"));
      int maxStops = 0;

      for (WebElement stop : stops) {
        String stopTxt = stop.getText().replaceAll("\\D*", "");

        if (Integer.parseInt(stopTxt) > maxStops) {
          maxStops = Integer.parseInt(stopTxt);
        }
      }

      FlightObject fO = new FlightObject(airlinesStrLst, originDepartureTime, originArrivalTime,
          destinationDepartureTime, destinationArrivalTime, originDuration, destinationDuration,
          originDepartureLocation, originArrivalLocation, destinationDepartureLocation, destinationArrivalLocation,
          priceStandard, priceFlexible, maxStops);
      flighttList.add(fO);

    }

  }

  /**
   * stopsDirect
   *
   * This test clicks on the direct flights button and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the flights loaded follow the limitation asked by the test, that they are
   * without stops.
   *
   */
  @Test
  public void stopsDirect() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1sfdkpa:nth-of-type(1)")));
    element.click();

    loadData();

    // Number of flights shown at the top left of the screen
    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    for (FlightObject flight : flighttList) {
      assertEquals(flight.getMaxStops(), 0);
    }
    driver.close();
  }

  /**
   * stopsOneStop
   *
   * This test clicks on the direct flights button and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the flights loaded follow the limitation asked by the test, that they do
   * not have more than one stop.
   *
   */
  @Test
  public void stopsOneStop() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1sfdkpa:nth-of-type(2)")));
    element.click();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    for (FlightObject flight : flighttList) {
      assertEquals(flight.getMaxStops(), 1);
    }
    driver.close();
  }

  /**
   * priceUpperLimit
   *
   * This test drags the upper price slider and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the price of the flights loaded are within the limited price range caused
   * by the slider filtering the results
   *
   */
  @Test
  public void priceUpperLimit() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[1]/section[2]/div/div/div/div/div[2]/div[2]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, -490, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();
    assertEquals(numberOfFlights, flighttList.size());

    String upperPrice = driver
        .findElement(By
            .xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[1]/section[2]/div/div/div/div/div[5]"))
        .getText();
    double upperPriceNumber;
    // this part formats the String of the price so that it can be parsed correctly
    // as a number for the comparisons below
    upperPrice = upperPrice.replace(",", "");
    upperPriceNumber = Double.parseDouble((String) upperPrice.subSequence(4, upperPrice.length() - 1));

    for (FlightObject flight : flighttList) {

      String flightPrice = flight.getPriceStandard().replace(",", "");

      double flightPriceNumber = Double.parseDouble((String) flightPrice.subSequence(4, flightPrice.length() - 1));

      assertTrue(flightPriceNumber <= upperPriceNumber);

    }
    driver.close();
  }

  /**
   * priceLowerLimit
   *
   * This test drags the lower price slider and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the price of the flights loaded are within the limited price range caused
   * by the slider filtering the results
   * 
   */
  @Test
  public void priceLowerLimit() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[1]/section[2]/div/div/div/div/div[2]/div[1]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, 490, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();
    assertEquals(numberOfFlights, flighttList.size());

    String lowerPrice = driver
        .findElement(By
            .xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[1]/section[2]/div/div/div/div/div[4]"))
        .getText();
    double lowerPriceNumber;

    lowerPrice = lowerPrice.replace(",", "");
    lowerPriceNumber = Double.parseDouble((String) lowerPrice.subSequence(4, lowerPrice.length() - 1));

    for (FlightObject flight : flighttList) {

      String flightPrice = flight.getPriceStandard().replace(",", "");

      double flightPriceNumber = Double.parseDouble((String) flightPrice.subSequence(4, flightPrice.length() - 1));

      assertTrue(flightPriceNumber >= lowerPriceNumber);

    }
    driver.close();
  }

  /**
   * travelTime
   *
   * This test drags the travel time slider and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the duration of the flights loaded is within the limits caused by the
   * slider filtering the results
   * In a return flight the website uses the highest of the two and the test
   * follows that principle.
   *
   */
  @Test
  public void travelTime() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[3]/div/div/div/div/div[2]/div")));
    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[3]/div/div/div/div/div[2]/div"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, -300, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    /*
     * This part formats the test in the format required by Java's Duration which in
     * our case is PT + digit H for the hours + digit M for the minutes
     */
    String lowerTravel = "PT" + driver
        .findElement(By
            .xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[3]/div/div/div/div/div[4]"))
        .getText();
    lowerTravel = lowerTravel.substring(0, lowerTravel.length() - 3).toUpperCase().replaceAll("\\s+", "")
        .replaceAll("\\P{Print}", "");

    Duration dur = Duration.parse(lowerTravel);
    Long milliDur = dur.toMillis();

    for (FlightObject flight : flighttList) {

      String originDuration = "PT" + flight.getOriginDuration().substring(0, flight.getOriginDuration().length() - 3)
          .toUpperCase().replaceAll("\\s+", "").replaceAll("\\P{Print}", "");
      String destinationDuration = "PT"
          + flight.getDestinationDuration().substring(0, flight.getDestinationDuration().length() - 3).toUpperCase()
              .replaceAll("\\s+", "").replaceAll("\\P{Print}", "");

      long dur1 = Duration.parse(originDuration).toMillis();
      long dur2 = Duration.parse(destinationDuration).toMillis();
      long longerTravelDuration;

      if (dur1 >= dur2) {
        longerTravelDuration = dur1;
      } else {
        longerTravelDuration = dur2;
      }

      assertTrue(milliDur >= longerTravelDuration);

    }
    driver.close();
  }

  /**
   * flightDepartureFromOrigin
   *
   * This test moves both the upper and the lower slider for the departure times
   * of the origin flight
   * Following that it asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the departure times of the flights loaded are within the limited time
   * range caused by the slider filtering the results
   * both when compared to the upper and when compared to the lower limit
   */
  @Test
  public void flightDepartureFromOrigin() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[1]")));
    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[1]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, 200, 0).perform();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[2]")));
    WebElement dragger2 = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[2]"));
    Actions act2 = new Actions(driver);
    act2.dragAndDropBy(dragger2, -200, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    String departureFromOrigin1 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[4]"))
        .getText();
    String departureFromOrigin2 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[5]"))
        .getText();

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Date d1 = new Date();
    try {
      d1 = dateFormat.parse(departureFromOrigin1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date d2 = new Date();
    try {
      d2 = dateFormat.parse(departureFromOrigin2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    for (FlightObject flight : flighttList) {

      String departure = flight.getOriginDepartureTime();
      Date d3 = new Date();
      try {
        d3 = dateFormat.parse(departure);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      assertTrue(d1.compareTo(d3) <= 0);
      assertTrue(d2.compareTo(d3) >= 0);

    }
    driver.close();
  }

  /**
   * flightDepartureFromDestination
   *
   * This test moves both the upper and the lower slider for the departure times
   * of the return flight
   * Following that it asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the departure times of the flights loaded are within the limited time
   * range caused by the slider filtering the results
   * both when compared to the upper and when compared to the lower limit
   */
  @Test
  public void flightDepartureFromDestination() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[1]")));
    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[1]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, 200, 0).perform();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[2]")));
    WebElement dragger2 = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[2]"));
    Actions act2 = new Actions(driver);
    act2.dragAndDropBy(dragger2, -200, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    String departureFromDestination1 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[4]"))

        .getText();
    String departureFromDestination2 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[5]"))
        .getText();

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Date d1 = new Date();
    try {
      d1 = dateFormat.parse(departureFromDestination1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date d2 = new Date();
    try {
      d2 = dateFormat.parse(departureFromDestination2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    for (FlightObject flight : flighttList) {

      String departure = flight.getDestinationDepartureTime();
      Date d3 = new Date();
      try {
        d3 = dateFormat.parse(departure);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      assertTrue(d1.compareTo(d3) <= 0);
      assertTrue(d2.compareTo(d3) >= 0);

    }
    driver.close();
  }

  /**
   * flightArrivalFromOrigin
   *
   * This test moves both the upper and the lower slider for the arrival times of
   * the origin flight
   * Following that it asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the arrival times of the flights loaded are within the limited time range
   * caused by the slider filtering the results
   * both when compared to the upper and when compared to the lower limit
   */
  @Test
  public void flightArrivalFromOrigin() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    element = wait.until(ExpectedConditions
        .elementToBeClickable(By.cssSelector(".css-1rmvpx7:nth-child(1) .lzk5ml1:nth-child(2) > .\\_1yoskx0")));
    element.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[1]")));
    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[1]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, 200, 0).perform();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[2]")));
    WebElement dragger2 = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[2]/div[2]"));
    Actions act2 = new Actions(driver);
    act2.dragAndDropBy(dragger2, -200, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    String arrivalFromOrigin1 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[4]"))
        .getText();
    String arrivalFromOrigin2 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[1]/div/div/div/div/div/div[5]"))
        .getText();

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Date d1 = new Date();
    try {
      d1 = dateFormat.parse(arrivalFromOrigin1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date d2 = new Date();
    try {
      d2 = dateFormat.parse(arrivalFromOrigin2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    for (FlightObject flight : flighttList) {

      String departure = flight.getOriginArrivalTime();
      Date d3 = new Date();
      try {
        d3 = dateFormat.parse(departure);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      assertTrue(d1.compareTo(d3) <= 0);
      assertTrue(d2.compareTo(d3) >= 0);

    }
    driver.close();
  }

  /**
   * flightArrivalFromDestination
   *
   * This test moves both the upper and the lower slider for the arrival times of
   * the return flight
   * Following that it asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. the arrival times of the flights loaded are within the limited time range
   * caused by the slider filtering the results
   * both when compared to the upper and when compared to the lower limit
   */
  @Test
  public void flightArrivalFromDestination() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    element = wait.until(ExpectedConditions
        .elementToBeClickable(By.cssSelector(".css-1rmvpx7:nth-child(2) .lzk5ml1:nth-child(2) > .\\_1yoskx0")));
    element.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[1]")));
    WebElement dragger = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[1]"));
    Actions act = new Actions(driver);
    act.dragAndDropBy(dragger, 200, 0).perform();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[2]")));
    WebElement dragger2 = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[2]/div[2]"));
    Actions act2 = new Actions(driver);
    act2.dragAndDropBy(dragger2, -200, 0).perform();

    loadData();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    String arrivalFromDestination1 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[4]"))
        .getText();
    String arrivalFromDestination2 = driver
        .findElement(By.xpath(
            "/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/div[2]/section[2]/div/div/div/div/div/div[5]"))
        .getText();

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Date d1 = new Date();
    try {
      d1 = dateFormat.parse(arrivalFromDestination1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date d2 = new Date();
    try {
      d2 = dateFormat.parse(arrivalFromDestination2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    for (FlightObject flight : flighttList) {

      String departure = flight.getDestinationArrivalTime();
      Date d3 = new Date();
      try {
        d3 = dateFormat.parse(departure);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      assertTrue(d1.compareTo(d3) <= 0);
      assertTrue(d2.compareTo(d3) >= 0);

    }
    driver.close();
  }

  /**
   * airlinePicker
   *
   * It removes an airline from the list of chosen ones and then asserts that
   * 1. the flights loaded are the same amount as the website shows us on the
   * results
   * 2. there are no flights in the list that have the removed one in their
   * details
   * 
   * When filtered like that the website checks all layover flights so the test
   * does as well
   * To do that it uses the the more detailed loadAllData method
   * 
   */
  @Test
  public void airlinePicker() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1ubn1th")));
    element.click();

    element = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/section/div/div/ul/li[1]/label")));
    element.click();

    loadAllData();

    String removedAirline = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[2]/div/div/section/div/div/ul/li[1]/label"))
        .getText();

    String flights = driver
        .findElement(By.xpath("/html/body/div[1]/div/div[2]/main/div[1]/div[1]/header/div[1]/span[2]")).getText();
    int numberOfFlights = new Scanner(flights).useDelimiter("\\D+").nextInt();

    assertEquals(numberOfFlights, flighttList.size());

    for (FlightObject flight : flighttList) {
      assertFalse(flight.getAirlines().contains(removedAirline));
    }
    driver.close();
  }

}
