package com.example.demoopentracing;

import com.uber.jaeger.Tracer.Builder;
import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.metrics.StatsFactoryImpl;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.senders.HttpSender;
import io.opentracing.propagation.Format.Builtin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.uber.jaeger.propagation.b3.B3TextMapCodec;

@SpringBootApplication
public class DemoOpentracingApplication {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Bean
  public io.opentracing.Tracer jaegerTracer() {
    Builder builder = new Builder("spring-boot",
        new RemoteReporter(new HttpSender("http://jaeger-collector.istio-system:14268/api/traces"), 10,
        65000, new Metrics(new StatsFactoryImpl(new NullStatsReporter()))),
        new ConstSampler(true))
        .registerInjector(Builtin.HTTP_HEADERS, new B3TextMapCodec())
        .registerExtractor(Builtin.HTTP_HEADERS, new B3TextMapCodec());
    return builder.build();
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoOpentracingApplication.class, args);
  }
}
