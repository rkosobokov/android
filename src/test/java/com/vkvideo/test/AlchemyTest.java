package com.vkvideo.test;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.SelenideAppium;
import com.vkvideo.driver.AlchemyDriverProvider;
import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.appium.AppiumClickOptions.tap;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlchemyTest {

    @BeforeAll
    static void setUp(){

        com.codeborne.selenide.Configuration.browser = AlchemyDriverProvider.class.getName();
        com.codeborne.selenide.Configuration.timeout = 60000;
        SelenideAppium.launchApp();

    }

    @BeforeEach
    void startUp() throws InterruptedException {
        SelenideAppium.terminateApp("com.ilyin.alchemy");
        Thread.sleep(3000);
        SelenideAppium.activateApp("com.ilyin.alchemy");
      //  System.out.println("Пропускаем регистрацию");


    }


    @Test
    @Order(1)
    @DisplayName("Alchemy")
    void videoShouldPlay_positive() throws InterruptedException {
        System.out.println("Тест: Alchemy");

        // Кликаем на первое видео
        $(AppiumBy.xpath("//x2.f1/android.view.View/android.view.View/android.view.View/android.view.View[5]/android.widget.Button"))
                .shouldBe(visible)
                .click(tap());
        $(AppiumBy.xpath("//x2.f1/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View[1]/android.view.View[2]"))
                .shouldBe(visible)
                .click(tap());
        String hints = $(AppiumBy.xpath("//x2.f1/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View//android.widget.TextView"))
                .shouldBe(visible, Duration.ofSeconds(30))
                .getText();
        System.out.println("Первое значение " + hints);
        $(AppiumBy.xpath("//android.widget.TextView[@text=\"Watch\"]"))
                .shouldBe(visible)
                .click(tap());
        Thread.sleep(20000);


        // Реклама 1: Найти элемент
        SelenideElement element = $(AppiumBy.xpath(
                "//android.widget.RelativeLayout[@content-desc=\"pageIndex: 1\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[2]"
        ));

// Проверить видимость
        element.shouldBe(visible);
        System.out.println("Элемент найден");

// Явно кликнуть
        element.click();
        System.out.println("Клик выполнен");
        Thread.sleep(20000);

        // Реклама 2: Найти элемент
        SelenideElement element2 = $(AppiumBy.xpath(
                "//android.widget.RelativeLayout[@content-desc=\"pageIndex: 2\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[2]"
        ));

//  Проверить видимость
        element2.shouldBe(visible);
        System.out.println("Элемент найден");

// Явно кликнуть
        element2.click();
        System.out.println("Клик выполнен");
        Thread.sleep(20000);


// Третий экран рекламы
        SelenideElement element3 = $(AppiumBy.xpath(
                "//android.widget.RelativeLayout[@content-desc=\"pageIndex: 3\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.ImageView"
        ));

//  Проверить видимость
        element3.shouldBe(visible);
        System.out.println("Элемент найден");

// Явно кликнуть
        element3.click();
        System.out.println("Клик выполнен");
        Thread.sleep(20000);

        String hints2 = $(AppiumBy.xpath("//x2.f1/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View//android.widget.TextView"))
                .shouldBe(visible, Duration.ofSeconds(30))
                .getText();
        System.out.println("Второе значение: " + hints2);

        // Конвертируем в числа и проверяем
        int firstValue = extractNumber(hints);
        int secondValue = extractNumber(hints2);

// Проверка, что второе значение = первое + 2
        assertThat(secondValue).isEqualTo(firstValue + 2);


    }

    private int extractNumber(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Текст не должен быть пустым");
        }

        // Убираем все нецифровые символы
        String numberOnly = text.replaceAll("[^0-9]", "");

        if (numberOnly.isEmpty()) {
            throw new NumberFormatException("В тексте нет чисел: " + text);
        }

        return Integer.parseInt(numberOnly);
    }


}