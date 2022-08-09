package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Arrays.asList;

public class ParamsTest {

    @ValueSource (strings = {"JUnit 5", "TestNG"})
    @ParameterizedTest (name = "При поиске в яндексе по запросу {0} в результатах отобржается текст {0}")
    void yaTestCommon(String testData){
        Selenide.open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item").find(text(testData)).shouldBe(visible);

    }

    // @CsvFileSource (resources = "test_data/1.csv")
    @CsvSource(value = {
            "JUnit 5, statement on the war in Ukraine",
            "TestNG, is a testing framework inspired from JUnit and NUnit"
    })
    @ParameterizedTest (name = "При поиске в яндексе по запросу {0} в результатах отобржается текст {1}")
    void yaTestComplex(String searchData, String expectedResult){
        Selenide.open("https://ya.ru");
        $("#text").setValue(searchData);
        $("button[type='submit']").click();
        $$("span.ExtendedText-Full").find(text(expectedResult)).shouldBe(visible);

    }

    static Stream<Arguments> yaTestVeryComplexDataProvider() {
        return Stream.of(
                Arguments.of("JUnit 5", asList("JUnit 5", "framework")),
                Arguments.of("TestNG", asList("TestNG", "framework"))
        );

    }

    @MethodSource (value = "yaTestVeryComplexDataProvider")
    @ParameterizedTest (name = "При поиске в яндексе по запросу {0} в результатах отобржается текст {1}")
    void yaTestVeryComplex(String searchData, List<String> expectedResult){
        Selenide.open("https://ya.ru");
        $("#text").setValue(searchData);
        $("button[type='submit']").click();
        $$("span.ExtendedText-Full").shouldHave(CollectionCondition.texts(expectedResult));
   }
}
