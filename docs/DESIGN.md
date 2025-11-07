### Portable Observe — High‑Level Design

#### Vision
Provide a tiny, framework‑agnostic observability toolkit for Java applications that delivers metrics, health, and optional tracing with:
- zero/low‑config bootstrap,
- first‑class JMX integration (existing strength),
- Prometheus and OTLP (OpenTelemetry) export options,
- strict cardinality guardrails,
- and easy integration into any runtime (plain Java, Spring, Jakarta EE, Micronaut, Quarkus).

#### Design Principles
- Minimal friction: zero‑config by default; one‑liner customization.
- Interoperability over lock‑in: bridges for JMX, Prometheus, OTLP. Optional Micrometer adapter.
- Cheap when disabled: no‑op facades, low allocation on hot paths.
- Progressive layering: standalone core → exporters → framework integrations.
- Safe defaults: bounded cardinality, sensible timeouts, graceful failure.

#### Module Architecture
- portable-observe-core (jar)
  - API: `Observability`, `MeterRegistry`, `Counter`, `Timer`, `DistributionSummary`, `GaugeSupplier`, `Tags`, `HealthRegistry`.
  - SDK: default in‑memory registry, percentiles, SLA buckets, exemplar support, JVM metrics collectors.
  - Config: `ObsConfig` (system props/env/app props), common tags, service/resource attributes.
  - Bootstrap: `ServiceLoader` discovery for exporters and auto‑instrumentations.
  - No required external dependencies; logging via slf4j‑api (future change).

- portable-observe-exporter-jmx (jar)
  - Bridge meters/health to JMX using existing PortableJMX classes.
  - Stable ObjectName strategy: `obs:service=<name>,type=<meterType>,name=<meterName>`.
  - Registration behavior reuse (replace/ignore/fail) from existing code.

- portable-observe-exporter-prometheus (jar) [planned]
  - Prometheus text exposition (0.0.4).
  - Embedded HTTP endpoint and servlet filter adapter.

- portable-observe-exporter-otlp (jar) [planned]
  - Bridge to OpenTelemetry SDK (if present) or lightweight OTLP client.

- portable-observe-autoconfigure-spring (jar) [planned]
  - Spring Boot auto‑configuration and conditional beans.

- portable-observe-instrumentations (jar) [planned]
  - JVM metrics, HTTP (Servlet/JAX‑RS), JDBC, executors, caches.

- portable-observe-web-demo (war)
  - Minimal demo app: `/metrics` (Prometheus), `/health`, JMX exposure.

#### Configuration Model
Priority: System properties > Env vars > application properties/YAML > defaults.
- `obs.enabled=true|false`
- `obs.service.name=<string>`
- `obs.metrics.exporters=prometheus,jmx,otlp`
- `obs.prometheus.port=9464`, `obs.prometheus.path=/metrics`
- `obs.jmx.domain=obs`
- `obs.otlp.endpoint=http://otel-collector:4318`
- `obs.tags.common=env:prod,region:eu-west-1`
- `obs.cardinality.max-series=10000`

#### Public API Sketch
```java
MeterRegistry registry = Observability.globalRegistry();
Counter requests = registry.counter("http.server.requests", Tags.of("method","GET"));
requests.increment();

Timer t = registry.timer("db.query");
try (Timer.Sample s = t.start()) {
  dao.call();
  s.stop();
}

HealthRegistry health = Observability.health();
health.register("db", () -> db.ping() ? UP : DOWN);
```

#### JMX Mapping
- Registry exposed as a management MBean (high‑level stats).
- Each meter category has a collection MBean with attributes/operations for querying.
- ObjectName conflicts resolved per registration behavior (fail/ignore/replace).

#### Cardinality Controls
- Global cap per registry; per‑meter caps; blacklisting of high‑cardinality tags.
- Hash‑bucket fallback when caps exceeded (keeps bounded series).

#### Thread‑Safety and Performance
- Lock‑free counters with LongAdder/Striped adders.
- Timer/summary with ring buffers + fixed bins for SLA/Pxx approximations.
- No synchronization on the hot path; background maintenance for percentiles.

#### Interop Strategy
- JMX: native via exporter.
- Prometheus: pull model; servlet filter and embedded HTTP.
- OTLP: bridge to OTel SDK when present; export via OTLP otherwise.
- Micrometer: optional adapter to reuse existing Micrometer code.

#### Failure Modes
- Exporters are optional; if one fails to bind (e.g., port busy), it degrades gracefully and logs.
- Registry always available; disabled mode returns no‑op meters.


#### JMX Feature Flags and Usage (update)
- Feature flags are exposed via a dedicated MBean: `org.deveasy.obs:type=Features` (read/write for booleans).
- The core `Observability.startWithFeatures()` attempts to auto‑register this MBean using reflection when the JMX exporter module is on the classpath. This keeps `core` free of a hard dependency on the exporter.
- Current toggles:
  - `isMetricsEnabled()` / `setMetricsEnabled(boolean)` — swaps the global registry between Default (atomic per‑instance metrics) and No‑Op safely.
  - `isObsEnabled()` / `setObsEnabled(boolean)` — master gate; currently delegates to metrics state when turning off.
  - `isTraceEnabled()` / `setTraceEnabled(boolean)` — reserved for future tracing implementation.
- Security posture:
  - Registration uses the in‑process Platform `MBeanServer`; the library does not enable remote JMX. If remote JMX is enabled by the JVM, secure it with authz/authn + TLS.
  - ObjectName domain: `org.deveasy.obs`. Replace/ignore/fail registration behavior is supported by `JmxPlatform` utilities.
- Operational guidance:
  - Prefer static flags for defaults (system properties/env). Use the JMX FeatureFlags MBean for incident response flips without restart.
  - All API calls remain safe while flipping features; registries are swapped atomically.
