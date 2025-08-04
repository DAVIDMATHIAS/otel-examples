export JAVA_TOOL_OPTIONS="-javaagent:/home/davidmathias/work/otel-example/opentelemetry-javaagent.jar" \
  OTEL_TRACES_EXPORTER=otlp \
  OTEL_METRICS_EXPORTER=otlp \
  OTEL_LOGS_EXPORTER=logging \
  OTEL_METRIC_EXPORT_INTERVAL=3000 \
  OTEL_SERVICE_NAME="my-custom-service2-java" \
  OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318 \
