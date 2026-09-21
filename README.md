# Padel League Draft Optimizer

The first increment provides a Spring Boot API for leagues and player-pool CSV imports. It is the foundation for fixed-price snake drafts and future recommendations.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop (for PostgreSQL)

## Run locally

```sh
copy .env.example .env
docker compose up -d postgres
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`; OpenAPI documentation is available at `/swagger-ui.html`.

## First API flow

```sh
curl -X POST http://localhost:8080/api/leagues -H "Content-Type: application/json" -d "{\"name\":\"Thursday League\",\"salaryCap\":1000,\"rosterSize\":8}"
curl -X POST http://localhost:8080/api/leagues/{leagueId}/players/import -F "file=@players.csv"
```

CSV imports require the header: `name,rating,preferredSide,cost,matchesPlayed`. Valid sides are `LEFT`, `RIGHT`, and `BOTH`.

## Verify

```sh
cd backend
mvn test
```

See [the project specification](padel_league_draft_optimizer_project_spec.md) for the phased roadmap.
