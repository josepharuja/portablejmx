# Dev Easy — Cross Cutting

Framework‑agnostic, vendor‑neutral observability and cross‑cutting library for Java. 
It provides per‑instance atomic metrics (ready for aggregation in Prometheus/Grafana), 
JMX exposure, runtime feature switches, and a secure‑by‑default posture. Requires Java 17+.

[![CI](https://github.com/<your-org>/<your-repo>/actions/workflows/ci.yml/badge.svg)](https://github.com/<your-org>/<your-repo>/actions/workflows/ci.yml)

## Modules
- dev-easy-cross-cutting-core (jar): Core API + default in‑memory registry, feature gating (static via system props/env; runtime via JMX flags).
- dev-easy-cross-cutting-exporter-jmx (jar): JMX utilities and `FeatureFlags` MBean for runtime toggles.
- dev-easy-cross-cutting-web-demo (war): Minimal demo app (placeholder for endpoints; to be expanded).

## Quickstart
Add dependencies (Maven):

```xml
<dependency>
  <groupId>org.deveasy</groupId>
  <artifactId>dev-easy-cross-cutting-core</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>org.deveasy</groupId>
  <artifactId>dev-easy-cross-cutting-exporter-jmx</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Enable metrics with system properties (recommended minimal):

```
-Dobs.enabled=true -Dobs.metrics.enabled=true
```

Use the API:

```java
import org.leedsmet.observe.Observability;
import org.leedsmet.observe.meter.Counter;
import org.leedsmet.observe.meter.Timer;

public class Example {
  public void handle() {
    Counter c = Observability.globalRegistry().counter("http.server.requests");
    c.increment();

    Timer t = Observability.globalRegistry().timer("db.query");
    try (Timer.Sample s = t.start()) {
      // do work
      s.stop();
    }
  }
}
```

## Feature switches
Static (startup flags):
- `obs.enabled=true|false` (master)
- `obs.metrics.enabled=true|false`
- `obs.trace.enabled=true|false` (placeholder)

Dynamic (runtime via JMX):
- MBean: `org.deveasy.obs:type=Features`
  - `setMetricsEnabled(boolean)` swaps between default and no‑op registry safely.

## Security by default
- No network ports are opened by default.
- JMX integration uses the in‑process Platform MBeanServer only (does not enable remote JMX).
- Planned: cardinality caps and tag/attribute sanitization to prevent data leaks and DoS via high cardinality.

## Build
- Java 17+
- Maven: `mvn -B -DskipTests=false verify`

## Status & Roadmap
- See `docs/STATUS.md` for current progress.
- See `docs/DESIGN.md` and `docs/ROADMAP.md` for architecture and milestones.

## Why per‑instance atomic metrics?
Counters/timers use `LongAdder/DoubleAdder` to remain thread‑safe and low‑contention. 
Aggregate across instances in Prometheus/Grafana using standard labels (e.g., instance, service).

## Next milestones (short)
- JMX meter exposure (read‑only), then Prometheus exporter module, tracing facade.
- Security hardening: tag filters, cardinality limits.

## License
Apache-2.0 (TBD — confirm before release)
