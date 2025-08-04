package otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleGauge;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class Controller {

    private final LongCounter aCountr;

    private final LongCounter bCountr;
    private final DoubleHistogram hHistoGram;
    private final LongUpDownCounter upDownCounter;
    private final DoubleGauge guageA;

    @GetMapping("/a")
    public String a() {
        increase(5, 2.5, 1);
        return "pong";
    }

    private void increase(int value, double value1, int value2) {
        this.aCountr.add(value);
        this.aCountr.add(4, Attributes.of(AttributeKey.stringKey("xxxB"), "yyyC"));
        this.hHistoGram.record(value1,Attributes.of(AttributeKey.stringKey("xxx"), "yyy"));
        this.bCountr.add(value2);
        this.bCountr.add(4, Attributes.of(AttributeKey.stringKey("xxxN"), "yyyM"));

    }

    private void increase(int upDownCounterValue, double guageValue) {
        double random = Math.random()* 10; // Random double value between 0.0 and 10.0
        int multiplier = 1;// Random multiplier between 0 and 10
        if (random > 5) {
            multiplier = -1; // If random is greater than 5, set multiplier to 2
        }

        this.upDownCounter.add((long) (upDownCounterValue* multiplier));
        this.guageA.set(random*guageValue);
    }
    @GetMapping("/b")
    public String b() {
        increase(3, 1.5, 2);
        return "pong";
    }

    public Controller(OpenTelemetry openTelemetry){
        aCountr = openTelemetry.getMeter("dvd-metrics")
                .counterBuilder("a-counter")
                .setDescription("A custom counter for demonstration purposes")
                .setUnit("X")
                .build();

        bCountr = openTelemetry.getMeter("dvd-metrics")
                .counterBuilder("b-counter")
                .setDescription("A custom counter for demonstration purposes")
                .setUnit("X")
                .build();

        hHistoGram = openTelemetry.getMeter("dvd-metrics")
                .histogramBuilder("h-histogram")
                .setDescription("A custom histogram for demonstration purposes")
                .setUnit("X")
                .build();
        hHistoGram.record(0.5, Attributes.of(AttributeKey.stringKey("xxx"), "yyy"));
        upDownCounter = openTelemetry.getMeter("dvd-metrics")
                .upDownCounterBuilder("a-up-down-counter")
                .setDescription("A custom histogram for demonstration purposes")
                .setUnit("X")
                .build();
        guageA = openTelemetry.getMeter("dvd-metrics")
                .gaugeBuilder("a-guage")
                .setDescription("A custom histogram for demonstration purposes")
                .setUnit("X")
                .build();
        guageA.set(0.0);
        new Thread(() -> {
            while (true) {
                try {
                    Random Random = new Random();

                    long sleepTime = Random.nextInt(1000) + 500; // Sleep between 500ms and 1500ms
                    Thread.sleep(sleepTime);
                    int value1 = Random.nextInt(10) + 1; // Random value between 1 and 10
                    double value2 = Random.nextDouble() * 10; // Random double value between 0.0 and 10.0
                    int value3 = Random.nextInt(5) + 1; // Random value between 1 and 5
                    increase(value1, value2, value3);
                    increase(10,10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}