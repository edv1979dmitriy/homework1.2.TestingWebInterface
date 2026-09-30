import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class FormTest {

    private WebDriver driver; //Переменная для хранения WebSriver

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();//Настраиваем chromdriver через библиотеку
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);//Инициализируем driver перед каждым тестом
                                           // в режиме отключения графического интерфейса браузера

        driver.get("http://localhost:9999");//Открываем тестируемую страницу
    }

    @AfterEach
    public void tearDown() {
        driver.quit();//Закрываем браузер
        driver = null;//обнуляем драйвер после каждого теста
    }

    @Test
    public void shouldSendFormWithValidField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219876543");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    public void shouldSendFormNotValidFieldName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219876543");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    public void shouldSendFormNotValidFieldPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7921987654");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    public void shouldSendFormNotClickCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219876543");
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("rgba(255, 92, 92, 1)", result.getCssValue("color"));
    }
}
