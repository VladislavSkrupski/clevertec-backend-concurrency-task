package ru.clevertec.server;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.model.Report;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ServerTest {
    private Server server = new Server();

    @ParameterizedTest
    @MethodSource("initProvider")
    void processRequestShouldReturnCorrectClientDataListSize(int value, int dataListSize) throws InterruptedException {
        int actual = 0;
        for (int i = 0; i < value; i++) {
            actual = server.processRequest(new Report(value)).getValue();
        }

        assertThat(actual).isEqualTo(dataListSize);
    }

    private static Stream<Arguments> initProvider() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(2, 2),
                Arguments.of(3, 3),
                Arguments.of(4, 4),
                Arguments.of(5, 5)
        );
    }
}