import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("deviceName","androidTestDevice");
        capabilities.setCapability("platformVersion","8.0");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","/Users/alex/Documents/JavaAppiumAutomation/appium_sandbox/JavaAppiumAutomation1/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void firstTest()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath( "//*[@class='android.view.ViewGroup']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching 'Java'",
                15
        );

    }

    @Test
    public void testCancelSearch()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        waitForElementAndClick(
                By.id( "org.wikipedia:id/search_container"),
                "Cannot find 'search_container' on main screen",
                5
        );

        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot clear search input",
                5
        );

        waitForElementAndClick(
                By.xpath( "//*[contains(@class, 'android.widget.ImageButton')]"),
                "Cannot find 'android.widget.ImageButton' back button to main",
                5
        );

        waitForElementNotPresent(
                 By.id("org.wikipedia:id/search_lang_button"),
                "'Search_lang_button' found",
                5
        );

    }

    @Test
    public void testCompareArticleTitle()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.xpath("//*[@class='android.view.ViewGroup']//*[@text='Java (programming language)']"),
                "Cannot find 'Java (programming language)'",
                5
        );

        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                 "We see unexpected title",
                "Java (programming language)",
                article_title
        );

    }

    @Test
    public void testCheckTextSearchFiled()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        assertElementHasText(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Search Wikipedia",
                "We see unexpected text in search filed",
                5
        );

    }

    @Test
    public void testCheckingSearch()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        waitForElementAndClick(
                By.id( "org.wikipedia:id/search_container"),
                "Cannot find 'search_container' on main screen",
                5
        );

        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );
        //Найдены статьи
        waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_description"),
                "Cannot find 'page_list_item_description'",
                15
        );

        waitForElementAndClick(
                By.id( "org.wikipedia:id/search_close_btn"),
                "Cannot find 'Close button'",
                5
        );

        WebElement empty_search = waitForElementPresent(
                By.id("org.wikipedia:id/search_empty_message"),
                "We see not empty search",
                5
        );


        String search_empty_image = empty_search.getAttribute("text");

        Assert.assertEquals(
                "We see not empty search",
                "Search Wikipedia in more languages",
                search_empty_image
        );



    }

    @Test
    public void testWordsInSearchResult()
    {
        //Фраза для поиска
        String search_value = "Java";
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'SKIP')]"),
                "Cannot find 'SKIP' on onboarding",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                search_value,
                "Cannot find search input",
                5
        );

        //Собираем в массив все элементы по id.
        List <WebElement> list_search_title = driver.findElements(By.id("org.wikipedia:id/page_list_item_title"));

        //Передаем размер массива в цикл
        for(int i = 0; i<list_search_title.size(); i++) {

            //Получаем из массива атрибут text
            String text_search_title = list_search_title.get(i).getAttribute("text");
            //Создаем паттерн по которому будем матчить полученный текст
            Pattern pattern = Pattern.compile(".*" + search_value + ".*");
            //Матчим полученный текста по паттерну
            Matcher matcher = pattern.matcher(text_search_title);
            //Если получили false передаем сообщение и прерываем цикл
            if(matcher.find() == false)
            {
                System.out.println("Not all search result contain " + search_value);
                break;
            }


        }
        //Если цикл завершился успешно выводим сообщение
        System.out.println("All search result contain " + search_value);




    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_message)
    {
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

   private WebElement assertElementHasText(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        String text = element.getAttribute("text");

        Assert.assertEquals(
                error_message,
                value,
                text
        );
        return element;
    }




}
