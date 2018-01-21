package com.vladmihalcea.flexypool.metric.codahale;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Reservoir;
import com.vladmihalcea.flexypool.metric.Histogram;
import com.vladmihalcea.flexypool.metric.Metrics;
import com.vladmihalcea.flexypool.metric.Timer;
import com.vladmihalcea.flexypool.common.ConfigurationProperties;
import com.vladmihalcea.flexypool.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * CodahaleMetricsTest - CodahaleMetrics Test
 *
 * @author Vlad Mihalcea
 */
public class CodahaleMetricsTest {

    @Mock
    private ConfigurationProperties configurationProperties;

    @Mock
    private MetricRegistry metricRegistry;

    @Mock
    private ReservoirFactory reservoirFactory;

    @Mock
    private Reservoir reservoir;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMetricRegistry() {
        CodahaleMetrics codahaleMetrics = new CodahaleMetrics(configurationProperties, metricRegistry, reservoirFactory);
        assertSame(metricRegistry, ReflectionUtils.getFieldValue(codahaleMetrics, "metricRegistry"));
    }

    @Test
    public void testHistogram() {
        CodahaleMetrics codahaleMetrics = new CodahaleMetrics(configurationProperties, reservoirFactory);
        assertSame(configurationProperties, codahaleMetrics.getConfigurationProperties());
        when(reservoirFactory.newInstance(com.codahale.metrics.Histogram.class, "histo")).thenReturn(reservoir);
        Histogram histogram = codahaleMetrics.histogram("histo");
        verify(reservoirFactory, times(1)).newInstance(com.codahale.metrics.Histogram.class, "histo");
        assertNotNull(histogram);
    }

    @Test
    public void testTimer() {
        CodahaleMetrics codahaleMetrics = new CodahaleMetrics(configurationProperties, reservoirFactory);
        when(reservoirFactory.newInstance(com.codahale.metrics.Timer.class, "timer")).thenReturn(reservoir);
        Timer timer = codahaleMetrics.timer("timer");
        verify(reservoirFactory, times(1)).newInstance(com.codahale.metrics.Timer.class, "timer");
        assertNotNull(timer);
    }

    @Test
    public void testStartStopUsingDefaultConfiguration() {
        when(configurationProperties.isJmxEnabled()).thenReturn(true);
        when(configurationProperties.getMetricLogReporterMillis()).thenReturn(5L);
        testStartStop(configurationProperties);
    }

    @Test
    public void testStartStopUsingNoJmx() {
        when(configurationProperties.isJmxEnabled()).thenReturn(false);
        when(configurationProperties.getMetricLogReporterMillis()).thenReturn(5L);
        testStartStop(configurationProperties);
    }

    public void testStartStop(ConfigurationProperties currentConfiguration) {
        Metrics codahaleMetrics = CodahaleMetrics.FACTORY.newInstance(currentConfiguration);
        codahaleMetrics.histogram("histo");
        codahaleMetrics.timer("timer");
        codahaleMetrics.start();
        codahaleMetrics.stop();
    }
}
