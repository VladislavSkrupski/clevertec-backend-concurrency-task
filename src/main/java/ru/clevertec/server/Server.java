package ru.clevertec.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.clevertec.model.Report;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor
public class Server {
    @Getter
    private List<Integer> clientDataList = new CopyOnWriteArrayList<>();
    @Getter
    private int receivedRequestsCount = 0;
    private Lock lock = new ReentrantLock();

    public Report processRequest(Report report) throws InterruptedException {
        Thread.sleep(new Random().nextInt(900) + 100);
        lock.lock();
        try {
            receivedRequestsCount++;
            clientDataList.add(report.getValue());
            return new Report(clientDataList.size());
        } finally {
            lock.unlock();
        }
    }
}
