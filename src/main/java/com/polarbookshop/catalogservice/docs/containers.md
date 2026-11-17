## Docker Compose

Catalog Service successfully connected to PostgreSQL through
Docker Compose using the `polar-postgres` service name.

After running `docker compose down` and then `docker 
compose up -d` again, the previous database data was gone. 
This is because PostgreSQL is storing its data in the 
container's writable layer. When the container is removed,
that writable layer is deleted. Since no Docker volume
is configured, the database does not persist between
container recreations.