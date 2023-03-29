package ru.clevertec.client;

import lombok.Getter;
import ru.clevertec.server.Server;
import ru.clevertec.model.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Client {
    @Getter
    private List<Integer> dataList = new CopyOnWriteArrayList<>();
    @Getter
    private AtomicInteger accumulator = new AtomicInteger(0);

    private Lock lock = new ReentrantLock();

    public Client(int n) {
        dataList.addAll(IntStream.rangeClosed(1, n).boxed().toList());
    }

    public void sendRequests(Server server) throws InterruptedException {
        List<Callable<Report>> callables = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(dataList.size());

        Callable<Report> callable = () -> {
            lock.lock();
            int index = new Random().nextInt(dataList.size());
            int value;
            try {
                value = dataList.remove(index);
            } finally {
                lock.unlock();
            }
            return server.processRequest(new Report(value));
        };

        for (int i = 0; i < dataList.size(); i++) {
            callables.add(callable);
        }

        receiveResponses(executorService.invokeAll(callables));

        executorService.shutdown();
    }

    private void receiveResponses(List<Future<Report>> futures) {
        accumulator.set(0);
        futures.parallelStream().forEach(future -> {
            try {
                accumulator.accumulateAndGet(future.get().getValue(), Integer::sum);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
