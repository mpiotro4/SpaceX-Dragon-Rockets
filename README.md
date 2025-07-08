# SpaceX In-Memory Service

This project simulates core SpaceX operations (rockets and missions) entirely in memory. It demonstrates a clean, SOLID-aligned architecture with:

* **Domain Models**: `Rocket`, `Mission`, `MissionSummary`
* **Factories**: `RocketFactory`, `MissionFactory` to encapsulate object creation
* **Repository Layer**: Generic `CrudRepository<T,ID>` with in-memory implementations
* **Service Layer**:
  * `SpaceXService` – orchestration of business operations
  * `StatusService` – encapsulates status-derivation logic for rockets & missions
  * `SummaryService` – builds mission summaries

## Features

1. **Add Rocket**: automatically generates ID, sets status ON_GROUND
2. **Add Mission**: use factory, default status SCHEDULED
3. **Assign Rockets**: single or batch assignments, triggers status updates
4. **Status Transitions**:
   * **Mission**:
     * `PENDING` if any assigned rocket is `IN_REPAIR`
     * `IN_PROGRESS` if a rocket is `IN_SPACE` and none in repair
     * `ENDED` if mission was in progress and all rockets return `ON_GROUND`
     * `SCHEDULED` if all assigned rockets remain `ON_GROUND`
   * **Rocket**:
     * `IN_REPAIR` if any associated mission is `PENDING`
     * `IN_SPACE` if associated mission is `IN_PROGRESS` and none pending
     * `ON_GROUND` otherwise
5. **In-Memory Persistence**: swap to a real database via `CrudRepository`
6. **Unit Tests**: comprehensive coverage for domain logic and status updates

## Getting Started

### Prerequisites
* Java 11+
* IntelliJ IDEA (built-in build and test runner)

### Build & Test
Open the project in IntelliJ and use **Build → Build Project**. Run all tests via **Run → Run 'All Tests'** or using the IDE's test icons in the editor gutter.

## Project Structure

```
src/
 ├─ main/
 │   ├─ java/
 │   │   ├─ model/          # Domain interfaces & enums
 │   │   ├─ repository/     # Generic and in-memory repos
 │   │   ├─ repository/impl # Implementations of repos  
 │   │   ├─ service/        # Core interfaces
 │   │   └─ service/impl/   # Implementations (including StatusUpdater)
 └─ test/
     ├─ domain/            # Domain-model unit tests
     ├─ repository/        # Repository contract tests
     └─ service/impl/      # StatusService & SpaceXService tests
```

## Extensibility

* Swap `InMemoryRocketRepository` with a JPA- or DB-backed implementation
* Add new rocket or mission types by providing custom `RocketFactory`/`MissionFactory`
* Extend status derivation logic in enums without modifying service code
* Introduce custom exceptions (e.g. `RocketNotFoundException`, `InvalidStatusTransitionException`) and validation at service boundaries
* Integrate a DI framework (e.g. Spring, Guice) for streamlined configuration
* Add logging and metrics (e.g. SLF4J, Micrometer) for observability
* Expose a RESTful API layer with DTOs and mappers
* Centralize validation rules using Bean Validation (JSR 380)
* Emit domain events for mission lifecycle changes to decouple side-effects

## Use of AI

Throughout the development of this project, I leveraged AI assistance to streamline design decisions and accelerate implementation:

* **Interactive Design Exploration**: I iteratively refined the architecture by discussing SOLID principles, repository patterns, factory patterns, and status-derivation logic with the AI. This dialogue helped me break down responsibilities and identify services to extract.
* **Code Generation & Refactoring**: Using AI prompts, I generated initial implementations for interfaces, enums, repositories, factories, and services. When I needed to split or reorganize code (e.g. extracting `StatusService`), I instructed the AI to update the canvas code directly.
* **Unit Test Coverage**: I asked the AI to propose and inject comprehensive unit tests for both domain logic and the `StatusService`, ensuring correct behavior before manual implementation.
* **Documentation Drafting**: The AI helped craft the README sections, from project overview to extensibility recommendations, and now this “Use of AI” section, maintaining a consistent tone and structure.
* **Iterative Feedback Loop**: My general workflow involved asking the AI for suggestions, reviewing generated code, running tests, and then requesting targeted fixes. This fast-feedback loop allowed me to focus on high-level design while the AI handled repetitive boilerplate and refactoring tasks.
