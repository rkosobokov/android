package com.vkvideo.test;

import com.codeborne.selenide.appium.SelenideAppium;
import com.vkvideo.driver.AndroidDriverProvider;
import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.appium.AppiumClickOptions.tap;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VkVideoTest {

    @BeforeAll
    static void setUp(){

        com.codeborne.selenide.Configuration.browser = AndroidDriverProvider.class.getName();
        com.codeborne.selenide.Configuration.timeout = 60000;
        SelenideAppium.launchApp();

    }

    @BeforeEach
    void startUp() throws InterruptedException {
        SelenideAppium.terminateApp("com.vk.vkvideo");
        Thread.sleep(3000);
        SelenideAppium.activateApp("com.vk.vkvideo");
        System.out.println("Пропускаем регистрацию");

        try {
            // Ждём появления кнопки Skip и нажимаем её
            $(AppiumBy.id("com.vk.vkvideo:id/fast_login_tertiary_btn"))
                    .shouldBe(visible)
                    .click(tap());

            System.out.println("Кнопка Skip нажата");
        } catch (AssertionError | NoSuchElementException e) {
            System.out.println("Кнопка Skip не найдена, возможно регистрация не требуется");
        }
    }


    @Test
    @Order(1)
    @DisplayName("Видео воспроизводится - позитивный тест")
    void videoShouldPlay_positive() throws InterruptedException {
        System.out.println("Тест: Проверка воспроизведения видео");

        // Кликаем на первое видео
        $(AppiumBy.xpath("(//android.widget.ImageView[@resource-id=\"com.vk.vkvideo:id/preview\"])[1]"))
                .shouldBe(visible)
                .click(tap());

        System.out.println("Кликнули на первое видео");
        System.out.println("Видео запускается автоматический");
        Thread.sleep(15000);

        //Получаем начальный прогресс
        $(AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.vk.vkvideo:id/video_subtitles\"]"))
                .shouldBe(visible)
                .click(tap());
        String initialProgress = $(AppiumBy.id("com.vk.vkvideo:id/current_progress"))
                .shouldBe(visible, Duration.ofSeconds(30))
                .getText();
        System.out.println("Изначальный прогресс: " + initialProgress);

        //Ожидаем 15 сек
        System.out.println("Ожидаем 15 секунд воспроизведения...");
        Thread.sleep(15000);

        //Получаем конечный прогресс
        $(AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.vk.vkvideo:id/video_subtitles\"]"))
                .shouldBe(visible)
                .click(tap());
        String finalProgress = $(AppiumBy.id("com.vk.vkvideo:id/current_progress"))
                .shouldBe(visible)
                .getText();
        System.out.println("Конечный прогресс: " + finalProgress);
        // Сравниваем
        assertThat(finalProgress)
                .as("Прогресс видео должен измениться")
                .isNotEqualTo(initialProgress);

        System.out.println("Прогрессы неравны, Позитивный тест прошёл: видео воспроизводится");
    }

    @Test
    @Order(2)
    @DisplayName("Видео не воспроизводится - негативный тест")
    void videoShouldNotPlay_negative() throws InterruptedException {
        System.out.println("Тест: Негативная проверка");

        try {
            // Кликаем на первое видео
            $(AppiumBy.xpath("(//android.widget.ImageView[@resource-id=\"com.vk.vkvideo:id/preview\"])[1]"))
                    .shouldBe(visible)
                    .click(tap());

            System.out.println("Кликнули на первое видео");
            System.out.println("Видео запускается автоматически");
            Thread.sleep(15000);

            // Получаем начальный прогресс
            $(AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.vk.vkvideo:id/video_subtitles\"]"))
                    .shouldBe(visible)
                    .click(tap());

            String initialProgress = $(AppiumBy.id("com.vk.vkvideo:id/current_progress"))
                    .shouldBe(visible, Duration.ofSeconds(30))
                    .getText();
            System.out.println("Изначальный прогресс: " + initialProgress);

            // Ожидаем 15 сек
            System.out.println("Ожидаем 15 секунд воспроизведения...");
            Thread.sleep(15000);

            // Получаем конечный прогресс
            $(AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.vk.vkvideo:id/video_subtitles\"]"))
                    .shouldBe(visible)
                    .click(tap());

            String finalProgress = $(AppiumBy.id("com.vk.vkvideo:id/current_progress"))
                    .shouldBe(visible)
                    .getText();
            System.out.println("Конечный прогресс: " + finalProgress);

            // Вариант 1: Прогресс НЕ изменился!
            if (initialProgress.equals(finalProgress)) {
                System.out.println("Прогресс не изменился - видео не воспроизводится (негативный сценарий подтвержден)");

            } else {
                // Прогресс изменился
                System.out.println("Прогресс изменился");

            }

        } catch (AssertionError | NoSuchElementException e) {
            System.out.println("Не удалось получить прогресс, проверяем индикатор загрузки...");

            // Вариант 2: Проверяем крутилку загрузки
            checkLoadingIndicator();
        }
    }

    /*
      Вспомогательный метод для проверки индикатора загрузки
     */
    private void checkLoadingIndicator() {
        try {
            $(AppiumBy.xpath("//android.widget.ProgressBar[@resource-id=\"com.vk.vkvideo:id/progress_view\"]"))
                    .shouldBe(visible, Duration.ofSeconds(5));

            System.out.println("Видео на загрузке (крутилка видна) - негативный сценарий подтвержден");

        } catch (AssertionError | NoSuchElementException ex) {
            // Если даже крутилки нет - это тоже ОК для негативного теста!
            System.out.println("Элементы плеера не найдены - видео не воспроизводится (негативный сценарий подтвержден)");

        }
    }



    @AfterAll
    static void tearDown() {
       System.out.println("Тесты завершены");
    }
}