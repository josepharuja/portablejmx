### STATUS — Portable Observe

Updated: 2025-11-06

#### Summary
- Repository reshaped to a multi-module Maven build (parent + 3 modules scaffolded):
  - `portable-observe-core` (jar) — API/SDK (to be populated)
  - `portable-observe-exporter-jmx` (jar) — JMX bridge (to migrate existing code)
  - `portable-observe-web-demo` (war) — demo app (to move web resources)
- Design and roadmap docs added to guide incremental implementation.

#### Decisions
- Path A: multi-module layout first, then implement API.
- Java target remains 1.8 for now to keep legacy sources compiling; plan to upgrade to 11/17 later.
- Keep existing JMX utilities and reuse them in `exporter-jmx`.

#### In Progress
- Phase 1: API stubs and no-op implementations (not yet committed).
- Migration plan for existing sources into appropriate modules.

#### Next Actions
1) Create initial API interfaces in `portable-observe-core` (`Observability`, `ObsConfig`, `MeterRegistry`, `Counter`, `Timer`, `DistributionSummary`, `Tags`, `HealthRegistry`) and no-op impls.
2) Migrate JMX registration/assembler utilities into `portable-observe-exporter-jmx`.
3) Move web resources into `portable-observe-web-demo` (`src/main/webapp`).
4) Build `mvn -q -DskipTests=false verify` and fix any compilation/package issues.

#### Risks / Notes
- log4j 1.x is still present via dependency management; will migrate to slf4j/logback during modernization.
- Tests currently JUnit 3.x; migration to JUnit 5 is planned after API stubs are in place.


#### Update (2025-11-06)
- Build upgraded to Java 17 at the parent POM level; modules inherit source/target 17. ✓
- Initial JMX modernization utility added: `org.deveasy.jmx.support.JmxPlatform` (uses Platform MBeanServer, safe register/unregister with replace/ignore/fail). ✓
- Legacy directory `--username joseph.a.aruja` is no longer referenced by the build. Action item: remove the directory from VCS (safe to delete) to declutter the repo. *
