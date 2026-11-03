# Catalog Service

[![Commit Stage](https://github.com/p466-ethan-pautsch/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/p466-ethan-pautsch/catalog-service/actions/workflows/commit-stage.yml)


## Exercise E4 - DELETE on a Missing Book

Returning 204 No Content when deleting a nonexistent book is correct because DELETE is an idempotent HTTP method. Idempotency means that sending the same request multiple times has the same intended effect on the server as sending it once. Whether the book existed initially or was already deleted, the final result is the same: the book is no longer in the catalog.
