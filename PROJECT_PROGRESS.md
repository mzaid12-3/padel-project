# Padel Draft Project Progress

This file tracks implementation progress and design decisions. The product requirements remain in [`padel_league_draft_optimizer_project_spec.md`](padel_league_draft_optimizer_project_spec.md); this file records how we are currently implementing them.

## Current product direction

This is a single-user Padel auction assistant.

The user:

1. Creates a league.
2. Sets a budget and squad size.
3. Uploads and reviews a player list.
4. Selects the player currently being auctioned.
5. Enters the current bid.
6. Receives an AI recommendation.
7. Buys or passes on the player.

The application tracks the squad, remaining budget, remaining slots, player availability, and future recommendations.

The first version does not include multiple owners, multiplayer bidding, manager invitations, snake drafts, or live multiplayer synchronization.

## Project setup

- Backend: Java with Spring Boot.
- Database: PostgreSQL running in Docker.
- Database viewer: pgAdmin running in Docker.
- Package organisation:

```text
com.padel.draft
├── controllers
├── services
├── repositories
└── domain
    ├── user
    ├── league
    ├── player
    └── draft
```

- Docker Compose is currently at `backend/docker-compose.yml`.
- PostgreSQL is exposed to the computer on port `5433`.
- pgAdmin is exposed at `http://localhost:5050`.

## Domain classes completed

### `User`

Location: `backend/src/main/java/com/padel/draft/domain/user/User.java`

Represents the person using the application. In the single-user version, this person is also the owner of the leagues they create.

Fields:

- auto-generated numeric `Long id`
- `name`
- unique `email`
- `password` (temporary plain field; must become a secure password hash before authentication)

### `League`

Location: `backend/src/main/java/com/padel/draft/domain/league/League.java`

Represents permanent league configuration.

Fields:

- auto-generated numeric `Long id`
- `owner`, a many-to-one relationship to `User`
- `name`
- `salaryCap`
- `squadSize`
- `createdAt`

`League` does not currently have a status. Auction lifecycle belongs to `DraftSession`.

### `Player`

Location: `backend/src/main/java/com/padel/draft/domain/player/Player.java`

Represents a player available for the auction.

Fields:

- auto-generated numeric `Long id`
- many-to-one `league`
- `name`
- `rating`
- `preferredSide` (currently a `String`; can become an enum later)
- `basePrice`
- `matchesPlayed`
- `status`

### `PlayerStatus`

Location: `backend/src/main/java/com/padel/draft/domain/player/PlayerStatus.java`

Allowed values:

```text
AVAILABLE
PURCHASED
REMOVED
```

New players start as `AVAILABLE`. Buying a player changes the status to `PURCHASED`; manually removed or unavailable players can be marked `REMOVED`.

## Design decisions

- Use normal auto-generated numeric IDs (`Long` with `GenerationType.IDENTITY`) rather than UUIDs for easier learning and readability.
- Use `BigDecimal` for money and rating values that may need exact decimal handling.
- Keep entities in `domain`; repositories will be responsible for database access, services for business rules, and controllers for HTTP endpoints.
- Use Lombok annotations to reduce repetitive getters/setters and JPA constructors. We are currently using `@Data`; revisit this later if entity relationship methods create recursion or unwanted mutability.
- No test-driven development for now. We are verifying progress with compilation and the generated Spring Boot context test. Add focused tests later when the auction rules become more complex.
- A league has one owner in the MVP. Multiple owners would require a many-to-many relationship and a bridge table, which is intentionally postponed.

## Next task

Create the draft domain package and then implement:

1. `DraftSession` entity containing the current auction state:
   - `id`
   - `league`
   - `startingBudget`
   - `remainingBudget`
   - `squadSize`
   - `remainingSlots`
   - `startedAt`
   - `completedAt`

The session will not have a status enum. Its lifecycle will be derived from timestamps:

```text
startedAt is null                  -> setup
startedAt has a value, completedAt is null -> active
completedAt has a value            -> completed
```

## Verification

From the backend directory:

```powershell
mvn compile
mvn test
```

The current implementation was committed and pushed as:

```text
7737d4b Reset project and add initial domain model
```

## Known cleanup before production

- Align the generated Maven project with the specification's Java 21 and Spring Boot 3 target. The generated `pom.xml` currently reports Java 17 and Spring Boot 4.1.1.
- Replace the temporary plain password field with Spring Security password hashing.
- Add Flyway migrations instead of relying on `ddl-auto=update`.
- Add request validation, repositories, services, controllers, and auction rules.
