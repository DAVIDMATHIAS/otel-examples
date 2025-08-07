// Assuming OpenTelemetry is already set up and imported
const meter = opentelemetry.metrics.getMeter("devx-meter");

// Create instruments
const toolCallsCounter = meter.createCounter("toolCalls", {
  description: "A custom counter for demonstration purposes"
});
const modelCallsCounter = meter.createCounter("modelCalls", {
  description: "A custom counter for demonstration purposes"
});
const modelResponseTime = meter.createHistogram("modelResponseTime", {
  description: "Model Response time",
  unit: "milliseconds"
});
const toolResponseTime = meter.createHistogram("toolResponseTime", {
  description: "Model Response time",
  unit: "milliseconds"
});
const upDownCounter = meter.createUpDownCounter("a-up-down-counter", {
  description: "A custom histogram for demonstration purposes",
  unit: "X"
});
const gaugeA = meter.createObservableGauge("a-guage", {
  description: "A custom histogram for demonstration purposes",
  unit: "X"
});

// Gauge value holder
let gaugeValue = 0;
gaugeA.addCallback(observableResult => {
  observableResult.observe(gaugeValue, { XPort: "8080" });
});

// Function to push metrics
function pushMetrics() {
  const port = "8080";
  const value1 = Math.floor(Math.random() * 10) + 1;
  const value2 = Math.floor(Math.random() * 10) + 1;
  const value3 = Math.floor(Math.random() * 10) + 1;

  const random1 = Math.random() * 500;
  const random2 = Math.random() * 500;
  const random3 = Math.random() * 500;
  const random4 = Math.random() * 500;
  const random5 = Math.random() * 500;
  const random6 = Math.random() * 500;

  toolCallsCounter.add(value1, { XPort: port, toolName: "fetchOrchestraDocuments" });
  toolResponseTime.record(random1 + 1, { XPort: port, toolName: "fetchOrchestraDocuments" });

  toolCallsCounter.add(value2, { XPort: port, toolName: "fetchJiraIssues" });
  toolResponseTime.record(random2 + 1, { XPort: port, toolName: "fetchJiraIssues" });

  toolCallsCounter.add(value3, { XPort: port, toolName: "updateJiraIssues" });
  toolResponseTime.record(random3 + 1, { XPort: port, toolName: "updateJiraIssues" });

  modelCallsCounter.add(value1, { XPort: port, modelName: "GPT-4" });
  modelResponseTime.record(random4 + 1, { XPort: port, modelName: "GPT-4" });

  modelCallsCounter.add(value2, { XPort: port, modelName: "GPT-5" });
  modelResponseTime.record(random5 + 1, { XPort: port, modelName: "GPT-5" });

  modelCallsCounter.add(value3, { XPort: port, modelName: "GPT-4.1" });
  modelResponseTime.record(random6 + 1, { XPort: port, modelName: "GPT-4'1" });

  // UpDownCounter and Gauge
  const upDownValue = Math.random() > 0.5 ? 10 : -10;
  upDownCounter.add(upDownValue, { XPort: port });
  gaugeValue = Math.random() * 10 * 10;
}

// Push metrics every second
setInterval(pushMetrics, 1000);