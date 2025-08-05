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

    private final LongCounter toolCallsCounter;

    private final LongCounter modelCallsCounter;
    private final DoubleHistogram modelResponseTime;
    private final DoubleHistogram toolResponseTime;
    private final LongUpDownCounter upDownCounter;
    private final DoubleGauge guageA;

    String port = "8080";
    @GetMapping("/a")
    public String a() {
        increase(5, 2, 1);
        return "pong";
    }

    private void increase(int value1, int value2, int value3) {

        double random1 = Math.random() * 500; // Random double value between 0.0 and 10.0
        double random2 = Math.random() * 500;
        double random3 = Math.random() * 500;

        double random4 = Math.random() * 500;
        double random5 = Math.random() * 500;
        double random6 = Math.random() * 500;

        this.toolCallsCounter.add(value1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "fetchOrchestraDocuments"));
        this.toolResponseTime.record(random1+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "fetchOrchestraDocuments"));

        this.toolCallsCounter.add(value2, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "fetchJiraIssues"));
        this.toolResponseTime.record(random2+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "fetchJiraIssues"));

        this.toolCallsCounter.add(value3, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "updateJiraIssues"));
        this.toolResponseTime.record(random3+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("toolName"), "updateJiraIssues"));



        this.modelCallsCounter.add(value1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-4"));
        this.modelResponseTime.record(random4+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-4"));

        this.modelCallsCounter.add(value2, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-5"));
        this.modelResponseTime.record(random5+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-5"));

        this.modelCallsCounter.add(value3, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-4.1"));
        this.modelResponseTime.record(random6+1, Attributes.of(AttributeKey.stringKey("XPort"), port,AttributeKey.stringKey("modelName"), "GPT-4'1"));


    }

    private void increase(int upDownCounterValue, double guageValue) {
        double random = Math.random()* 10; // Random double value between 0.0 and 10.0
        int multiplier = 1;// Random multiplier between 0 and 10
        if (random > 5) {
            multiplier = -1; // If random is greater than 5, set multiplier to 2
        }

        this.upDownCounter.add((long) (upDownCounterValue* multiplier), Attributes.of(AttributeKey.stringKey("XPort"), port));
        this.guageA.set(random*guageValue, Attributes.of(AttributeKey.stringKey("XPort"), port));
    }
    @GetMapping("/b")
    public String b() {
        increase(3, 1, 2);
        return "pong";
    }

    public Controller(OpenTelemetry openTelemetry){
        port = System.getenv("SERVER_PORT");
        System.out.println("****************************************************");
        System.out.println("OpenTelemetry is running on port: " + port);
        System.out.println("****************************************************");

        toolCallsCounter = openTelemetry.getMeter("david-metrics")
                .counterBuilder("toolCalls")
                .setDescription("A custom counter for demonstration purposes")
                .build();

        modelCallsCounter = openTelemetry.getMeter("david-metrics")
                .counterBuilder("modelCalls")
                .setDescription("A custom counter for demonstration purposes")
                .build();

        modelResponseTime = openTelemetry.getMeter("david-metrics")
                .histogramBuilder("modelResponseTime")
                .setDescription("Model Response time")
                .setUnit("milliseconds")
                .build();
        toolResponseTime = openTelemetry.getMeter("david-metrics")
                .histogramBuilder("toolResponseTime")
                .setDescription("Model Response time")
                .setUnit("milliseconds")
                .build();
        upDownCounter = openTelemetry.getMeter("david-metrics")
                .upDownCounterBuilder("a-up-down-counter")
                .setDescription("A custom histogram for demonstration purposes")
                .setUnit("X")
                .build();
        guageA = openTelemetry.getMeter("david-metrics")
                .gaugeBuilder("a-guage")
                .setDescription("A custom histogram for demonstration purposes")
                .setUnit("X")
                .build();
        new Thread(() -> {
            while (true) {
                try {
                    Random Random = new Random();

                    long sleepTime = Random.nextInt(1000) + 500; // Sleep between 500ms and 1500ms
                    Thread.sleep(sleepTime);
                    int value1 = Random.nextInt(10) + 1; // Random value between 1 and 10
                    int value2 = Random.nextInt(10) +1; // Random double value between 0.0 and 10.0
                    int value3 = Random.nextInt(10) + 1; // Random value between 1 and 5
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