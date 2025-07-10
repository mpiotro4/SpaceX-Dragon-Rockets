# SIX Coding Exercise - SpaceX Dragon Rockets Repository

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
* Maven 3.8+

### Build & Test
Run `mvn clean test` to compile the project and execute the unit tests.

## Project Structure

```
src/
 ├─ main/
 │   └─ java/
 │       ├─ model/          # Domain interfaces & enums
 │       ├─ repository/     # Generic and in-memory repos
 │       ├─ repository/impl # Implementations of repos
 │       ├─ service/        # Core interfaces
 │       └─ service/impl/   # Implementations (including StatusUpdater)
 └─ test/
     └─ java/
         ├─ domain/            # Domain-model unit tests
         ├─ repository/        # Repository contract tests
         └─ service/impl/      # StatusService & SpaceXService tests

```
## Assumptions & Extensibility

* Manual constructor-based dependency injection works for quick prototypes but can become unwieldy. I would recommend using a DI framework (e.g. Spring) for more efficient, scalable configuration, although constructor injection was chosen here for simplicity
* Add remove of missions and rockets - it wasn't specified as requirement, so I assumed it was out of scope.
* Introduce validation and custom exceptions - library can be easily broken by providing invalid data and doesn't give any meaningful feedback    
* Add logging
* Switch to maven
* Create separate repository eg MissionAssignmentRepository to store missionAssignments so its not coupled with busines logic

## Use of AI

Throughout the development of this project, I leveraged AI assistance to streamline design decisions and accelerate implementation:

* **Interactive Design Exploration**: I iteratively refined the architecture by discussing SOLID principles, repository patterns, factory patterns, and status-derivation logic with the AI. This dialogue helped me break down responsibilities and identify services to extract.
* **Code Generation & Refactoring**: Using AI prompts, I generated initial implementations for interfaces, enums, repositories, factories, and services. When I needed to split or reorganize code (e.g. extracting `StatusService`), I instructed the AI to update the canvas code directly.
* **Unit Test Coverage**: I asked the AI to propose and inject comprehensive unit tests for both domain logic and the `StatusService`, ensuring correct behavior before manual implementation.
* **Documentation Drafting**: The AI helped craft the README sections, from project overview to extensibility recommendations, and now this “Use of AI” section, maintaining a consistent tone and structure.
* **Iterative Feedback Loop**: My general workflow involved asking the AI for suggestions, reviewing generated code, running tests, and then requesting targeted fixes. This fast-feedback loop allowed me to focus on high-level design while the AI handled repetitive boilerplate and refactoring tasks.
