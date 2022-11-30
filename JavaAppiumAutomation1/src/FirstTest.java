import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        capabilities.setCapability("platformVersion","9");
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

    @Test
    public void testSwipeArticle()
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
                "Appium",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannot find 'Appium' article in search",
                5
        );
        //driver.hideKeyboard();

        swipeUpToFindElement(
                By.xpath("//*[contains(@text, 'View article in browser')]"),
                "Cannot find the end of the article",
                20
        );



    }

    @Test
    public void saveFirstArticleToMyList()
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

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "Cannot find 'Java (programming language)' article in search",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_save'][@text='Save']"),
                "Cannot find 'Save input",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/snackbar_action'][@text='ADD TO LIST']"),
                "Cannot find 'Save input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                "Cannot find 'Save input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath( "//*[@resource-id='org.wikipedia:id/text_input']"),
                "Test_list",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='android:id/button1'][@text='OK']"),
                "Cannot find 'Save input",
                5
        );

        waitForElementAndClick(
                By.xpath( "//*[contains(@class, 'android.widget.ImageButton')]"),
                "Cannot find 'android.widget.ImageButton' back button to main",
                5
        );

        waitForElementAndClick(
                By.xpath( "//*[contains(@class, 'android.widget.ImageButton')]"),
                "Cannot find 'android.widget.ImageButton' back button to main",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/nav_tab_reading_lists']"),
                "Cannot find 'Save input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='Test_list']"),
                "Cannot find 'Save input",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find 'page_list_item_description'",
                15
        );

        swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article"
                );

        waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5
                );




    }

    @Test
    public void testAmountOfNotEmptySearch()
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

        String search_line = "Linkin Park Diskography";
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_title']";

        waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by the request " + search_line,
                15
        );
        int amount_of_search_results = getAmountOfElements(
                By.xpath(search_result_locator)
        );

        Assert.assertTrue(
                "We found too few results",
                amount_of_search_results > 0
        );
    }

    @Test
    public void testChangesOrientationOnSearchResults()
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
        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "Cannot find 'Java (programming language)' topic searching by "  + search_line,
                15
        );

        String title_before_rotation = waitForElementAndGetAttribute(
                By.xpath("//*[@class='android.view.ViewGroup']//*[@text='Object-oriented programming language']"),
                "text",
                "Cannot find title of article",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = waitForElementAndGetAttribute(
                By.xpath("//*[@class='android.view.ViewGroup']//*[@text='Object-oriented programming language']"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_rotation
        );

        driver.rotate(ScreenOrientation.PORTRAIT);

        String title_after_second_rotation = waitForElementAndGetAttribute(
                By.xpath("//*[@class='android.view.ViewGroup']//*[@text='Object-oriented programming language']"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_second_rotation
        );



    }

    @Test
    public void testSearchArticleInBackground()
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
        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "Cannot find 'Java (programming language)' topic searching by "  + search_line,
                15
        );

        driver.hideKeyboard();
        driver.runAppInBackground(10);

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "Cannot find articles after run background "  + search_line,
                15
        );
    };

     @Test
    public void testSearchAndSaveTwoArticles()
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

        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath( "//*[contains(@text, 'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "Cannot find 'Java (programming language)' article in search",
                5
        );

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_save'][@text='Save']"),
                "Cannot find button 'Save' on tabbar",
                5
        );
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/snackbar_action'][@text='ADD TO LIST']"),
                "Cannot find text 'ADD TO LIST",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                "Cannot find 'Save input",
                5
        );

        String name_list = "Java articles list";
        waitForElementAndSendKeys(
                By.xpath( "//*[@resource-id='org.wikipedia:id/text_input']"),
                name_list,
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='android:id/button1'][@text='OK']"),
                "Cannot find button with text 'OK",
                5
        );

        waitForElementAndClick(
                By.xpath( "//*[contains(@class, 'android.widget.ImageButton')]"),
                "Cannot find 'android.widget.ImageButton' back button to main",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java version history']"),
                "Cannot find 'Java version history' article in search",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_save'][@text='Save']"),
                "Cannot find button fot save article",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/snackbar_action'][@text='ADD TO LIST']"),
                "Cannot find text 'ADD TO LIST",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='"+ name_list +"']"),
                "Cannot find text 'ADD TO LIST",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/snackbar_action'][@text='VIEW LIST']"),
                "Cannot find text 'VIEW LIST'",
                5
        );

        swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article"
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java version history']"),
                "Cannot find 'Java version history' article",
                15
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java version history']"),
                "Cannot find articles after run background ",
                15
        );

        waitForElementPresent(
                By.xpath("//*[@class='android.view.ViewGroup']//*[@text='Java version history']"),
                "Cannot find title 'Java version history' in saved article",
                15
        );




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

    protected void swipeUp(int timeOfSwipe)
    {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release()
                .perform();
    }


    protected void swipeUpQuick()
    {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String error_message, int max_swipes)
    {
        int already_swipes = 0;
        while (driver.findElements(by).size() == 0){

            if(already_swipes > max_swipes){
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
                return;

            }
            swipeUpQuick();
            ++already_swipes;
        }
    }

    protected void swipeElementToLeft(By by, String error_message)
    {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                10);

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(300)
                .moveTo(left_x, middle_y)
                .release()
                .perform();
    }

    private int getAmountOfElements (By by)
    {
        List elements = driver.findElements(by);
        return elements.size();
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

}
