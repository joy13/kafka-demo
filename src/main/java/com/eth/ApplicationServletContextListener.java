package com.eth;

import com.eth.kafka.ConsumerTask;
import com.eth.kafka.ProducerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class ApplicationServletContextListener implements ServletContextListener {

    private ExecutorService executor;
    private ScheduledExecutorService scheduledExecutor;

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Initializing context");
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(new ProducerTask(), 0, 10, TimeUnit.SECONDS);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(new ConsumerTask()); // Task should implement Runnable.
    }

    public void contextDestroyed(ServletContextEvent event) {
        executor.shutdown();
        scheduledExecutor.shutdownNow();
    }
}
