package ru.clevertec;

import ru.clevertec.client.Client;
import ru.clevertec.server.Server;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        int n = 5284;
        Client client = new Client(n);
        Server server = new Server();

        System.out.println("исходный размер списка данных клиента: " + client.getDataList().size());
        client.sendRequests(server);
        System.out.println("размер списка данных клиента после отправки запросов: " + client.getDataList().size());
        System.out.println("количество полученных сервером запросов: " + server.getReceivedRequestsCount());
        System.out.println();
        System.out.println("расчётное значение аккумулятора: " + ((1 + n) * (n / 2)));
        System.out.println("итоговое значение аккумулятора: " + client.getAccumulator());
    }
}
