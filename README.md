# Catalog Service

[![Commit Stage](https://github.com/p466-ethan-pautsch/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/p466-ethan-pautsch/catalog-service/actions/workflows/commit-stage.yml)


## Exercise E4 - DELETE on a Missing Book

Returning 204 No Content when deleting a nonexistent book is correct because DELETE is an idempotent HTTP method. Idempotency means that sending the same request multiple times has the same intended effect on the server as sending it once. Whether the book existed initially or was already deleted, the final result is the same: the book is no longer in the catalog.

## E1: Database Schema Evolution

The V2 migration adds a publisher column to the book table. The column is initially nullable to maintain backward compatibility with existing book records and application versions that do not provide publisher information.

To eventually make publisher mandatory, we would use a two-release approach.

Release 1:
- Add the nullable publisher column.
- Update the application to support reading and writing publisher information.
- Backfill publisher values for existing books.
- Ensure all new books include publisher information before enforcing the database constraint.

Release 2:
- After older application versions have been retired and all existing records have publisher values, introduce a new Flyway migration that adds a NOT NULL constraint.
- Update application validation to require publisher information.

This approach allows the database schema to evolve without breaking existing data or older application instances during deployment.