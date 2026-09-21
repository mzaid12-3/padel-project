# Repository Guidelines

## Project Structure & Module Organization

The repository is a multi-service Padel draft platform. The current implementation is a Spring Boot API in `backend/`; `frontend/` (Angular) and `optimizer/` (FastAPI) are reserved for later phases. Keep editor-specific metadata (such as `.idea/`) out of feature work unless it is intentionally shared configuration.

As the project grows, use a predictable layout:

- `backend/src/main/` for Spring Boot code, configuration, and Flyway migrations.
- `backend/src/test/` for Java tests and fixture data.
- `assets/` for static images, fonts, and other non-code resources.
- `docs/` for architecture notes and contributor-facing documentation.

Avoid placing new application files in the repository root. Document any new top-level directory in the README.

## Build, Test, and Development Commands

Start the local PostgreSQL dependency and use Maven from the backend directory:

```sh
docker compose up -d postgres  # start PostgreSQL
cd backend && mvn spring-boot:run  # run the API
cd backend && mvn test  # run backend tests
```

Do not add generated build output, dependency directories, or `.env` files to version control.

## Coding Style & Naming Conventions

Follow the formatter and linter configured for the chosen language; do not hand-format around those tools. In Java, use 4-space indentation, `PascalCase` types, `camelCase` members, and package-by-feature paths such as `com.padel.draft.player`. Use 2 spaces for YAML and JSON. Prefer clear names such as `PlayerCsvImportService` and `calculateCourtAvailability`.

## Testing Guidelines

Add tests with each behavior change. Name Java tests after the unit under test (for example, `PlayerCsvImportServiceTest`) and write test names that describe the expected outcome. Before opening a pull request, run the formatter, linter, and full test suite.

## Commit & Pull Request Guidelines

No Git history is available to establish an existing convention. Use concise, imperative commits such as `Add court availability service` or `Fix booking validation`. Keep commits scoped to one logical change. Pull requests should explain the change, link the relevant issue when applicable, list validation performed, and include screenshots for visible UI changes.

## Configuration & Secrets

Never commit credentials, API keys, or local environment files. Provide safe placeholders in an `.env.example` file and document required variables in the README.
