package com.vladmihalcea.flexypool.metric.codahale;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.vladmihalcea.flexypool.common.ConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * <code>Slf4jMetricReporter</code> - Slf4j Metric Reporter
 *
 * @author Vlad Mihalcea
 * @since 1.0
 */
public class Slf4jMetricReporter implements MetricsLifeCycleCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodahaleMetrics.class);

    private Slf4jReporter slf4jReporter;

    private long metricLogReporterMillis;

    /**
     * Create a Log Reporter and activate it if the metricLogReporterMillis property is greater than zero.
     *
     * @param configurationProperties configuration properties
     * @param metricRegistry          metric registry
     * @return {@link Slf4jMetricReporter}
     */
    @Override
    public Slf4jMetricReporter init(ConfigurationProperties configurationProperties, MetricRegistry metricRegistry) {
        metricLogReporterMillis = configurationProperties.getMetricLogReporterMillis();
        if (metricLogReporterMillis > 0) {
            this.slf4jReporter = Slf4jReporter
                    .forRegistry(metricRegistry)
                    .outputTo(LOGGER)
                    .build();
        }
        return this;
    }

    /**
     * Start Log Reporter
     */
    @Override
    public void start() {
        if (slf4jReporter != null) {
            slf4jReporter.start(metricLogReporterMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stop Log Reporter
     */
    @Override
    public void stop() {
        if (slf4jReporter != null) {
            slf4jReporter.stop();
        }
    }
}
