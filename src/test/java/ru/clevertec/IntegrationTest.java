package ru.clevertec;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.client.Client;
import ru.clevertec.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    private Server server;

    @BeforeEach
    void initBefore() {
        server = new Server();
    }

    @ParameterizedTest
    @MethodSource("initProvider")
    void clientShouldHaveZeroSizeDataListAfterSendingRequests(int n) throws InterruptedException {
        Client client = new Client(n);

        int actualStartClientSideDataListSize = client.getDataList().size();

        client.sendRequests(server);

        int actualClientSideDataListSizeAfterSendingRequests = client.getDataList().size();

        assertAll(
                () -> assertThat(actualStartClientSideDataListSize).isEqualTo(n),
                () -> assertThat(actualClientSideDataListSizeAfterSendingRequests).isEqualTo(0)
        );
    }

    @ParameterizedTest
    @MethodSource("initProvider")
    void serverShouldHaveDataListWithClientDataAfterReceivingRequests(int n) throws InterruptedException {
        Client client = new Client(n);

        List<Integer> expectedStartClientSideDataList = new ArrayList<>(client.getDataList());

        client.sendRequests(server);

        List<Integer> actualServerSideDataList = server.getClientDataList();

        assertTrue(CollectionUtils.isEqualCollection(actualServerSideDataList, expectedStartClientSideDataList));
    }

    @ParameterizedTest
    @MethodSource("initProvider")
    void clientShouldHaveCorrectAccumulatorValueAfterSendingRequests(int n, int expectedAccumulatorValue) throws InterruptedException {
        Client client = new Client(n);
        client.sendRequests(server);

        int actualAccumulatorValue = client.getAccumulator().intValue();

        assertThat(actualAccumulatorValue).isEqualTo(expectedAccumulatorValue);
    }

    @RepeatedTest(10)
    void clientShouldHaveRepeatableAccumulatorValueAfterSendingRequests() throws InterruptedException {
        Client client = new Client(100);
        client.sendRequests(server);

        int actualAccumulatorValue = client.getAccumulator().intValue();

        assertThat(actualAccumulatorValue).isEqualTo(5050);
    }

    private static Stream<Arguments> initProvider() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(5, 15),
                Arguments.of(10, 55),
                Arguments.of(100, 5050),
                Arguments.of(1000, 500500),
                Arguments.of(1234, 761995)
        );
    }
}
