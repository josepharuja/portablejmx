### Roadmap — Portable Observe

This roadmap organizes delivery into small, verifiable increments. Each phase can ship independently.

#### Phase 0 — Reshape & Docs (current)
- Split into multi-module Maven build: parent POM + modules ✓
- Create initial design and status docs ✓
- Keep existing code compiling in old layout temporarily (to be migrated in Phase 1)

Deliverables:
- Parent POM with modules: `portable-observe-core`, `portable-observe-exporter-jmx`, `portable-observe-web-demo` ✓
- Docs: `docs/DESIGN.md`, `docs/STATUS.md`, `docs/ROADMAP.md`, updated `README.md` ✓

#### Phase 1 — Core API Stubs & Minimal Build Green
- In `portable-observe-core`: add public API interfaces (no heavy impl yet):
  - `Observability`, `ObsConfig`, `MeterRegistry`, `Counter`, `Timer`, `DistributionSummary`, `Tags`, `HealthRegistry`
  - Provide no-op default implementations so apps link and tests pass
- Create JVM metrics placeholders (interfaces only)
- Migrate existing JMX registration utilities into `exporter-jmx` module
- Add initial unit tests to validate API surface

Deliverables:
- Core JAR builds with interfaces and no-op implementations
- JMX exporter JAR includes existing registration code (compiles)

#### Phase 2 — Metrics MVP
- Implement in-memory `MeterRegistry`
- Implement `Counter`, `Timer` (wall time), `DistributionSummary`
- Add tag storage with guarded cardinality caps
- Expose JVM metrics (memory, GC, threads)
- Wire `exporter-jmx` to expose registry and meters as MBeans

Deliverables:
- Demo WAR exposes basic `/health` page and JMX visibility
- Benchmarks for counter/timer overhead (basic JMH)

#### Phase 3 — Prometheus Exporter
- New module `portable-observe-exporter-prometheus`
- Prometheus text exposition and embedded HTTP endpoint
- Servlet Filter for app servers; basic auth toggle

Deliverables:
- Demo shows `/metrics` with HTTP listener and servlet filter

#### Phase 4 — Interop Bridges
- OTLP exporter (bridge to OpenTelemetry SDK if present)
- Optional Micrometer adapter for drop-in compatibility

Deliverables:
- Send metrics to an OTLP collector; example config in README

#### Phase 5 — Health & Readiness
- `HealthRegistry` with component checks (UP/DOWN/DEGRADED)
- `/health` endpoint and MBeans

Deliverables:
- Demo shows health status; tests for failure modes

#### Phase 6 — Instrumentations (opt-in)
- HTTP (Servlet/JAX-RS), JDBC (DataSource proxy), executors, caches

Deliverables:
- Pluggable modules, examples in demo

#### Phase 7 — Hardening & Docs
- Performance tuning, memory caps, cardinality enforcement, backpressure
- CI (GitHub Actions), code quality (SpotBugs, Checkstyle), coverage (JaCoCo)
- Publication (Maven Central or internal)

Deliverables:
- 1.0.0-rc1 with full docs and samples
