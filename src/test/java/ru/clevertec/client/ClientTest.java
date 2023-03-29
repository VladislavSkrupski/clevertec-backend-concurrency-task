package ru.clevertec.client;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.server.Server;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientTest {
    @Spy
    private Server server = new Server();

    @ParameterizedTest
    @MethodSource("initProvider")
    void sendRequestsShouldInvokeServerCorrectNumberOfTimes(int n) throws InterruptedException {
        Client client = new Client(n);

        client.sendRequests(server);

        verify(server, times(n)).processRequest(any());
    }

    private static Stream<Arguments> initProvider() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(5),
                Arguments.of(10),
                Arguments.of(100),
                Arguments.of(1000),
                Arguments.of(1234)
        );
    }
}