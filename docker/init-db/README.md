# Database Initialization Scripts

This directory contains scripts that are automatically executed when the Oracle database container is first created.

## Automatic User Creation

The `gvenzl/oracle-xe` image automatically creates the application user specified in the environment variables:
- User: `farm_manager` (from `APP_USER` env var)
- Password: `password` (from `APP_USER_PASSWORD` env var)

The `01-create-user.sql` script is kept as a reference but may not be needed with this image.

## Running Additional SQL Scripts

After the container is running, you can execute the project's SQL scripts manually:

### Option 1: Using Docker Exec

```bash
# Copy SQL files into the container (if not already mounted)
docker cp sql_files/CREATE_sprint1.sql farm-oracle-db:/tmp/

# Execute SQL script
docker exec -it farm-oracle-db sqlplus farm_manager/password@//localhost:1521/XEPDB1 @/tmp/CREATE_sprint1.sql
```

### Option 2: Using SQLPlus from Host (if sqlplus is installed)

```bash
sqlplus farm_manager/password@//localhost:1521/XEPDB1 @sql_files/CREATE_sprint1.sql
```

### Option 3: Execute All Scripts at Once

Use the provided helper script:

```bash
# From the project root
./docker/scripts/run-all-sql.sh
```

## SQL Scripts to Execute (in order)

Based on the project's sql_files directory:

1. `CREATE_sprint1.sql` - Create initial schema
2. `CREATE_sprint2.sql` - Create sprint 2 objects
3. `CREATE_sprint3.sql` - Create sprint 3 objects
4. `COMPILED_functions_sprint2_sprint3.sql` - Create functions
5. `INSERTS_v2a_aval_sprint1.sql` - Insert initial data
6. `GEN_INSERTS-UPDATES_DELETES_sprint2.sql` - Sprint 2 data
7. `GEN_INSERTS-UPDATES_DELETES_sprint3.sql` - Sprint 3 data

## Notes

- The `farm_manager` user is created with password `password` (configurable in .env)
- All scripts must be executed against the XEPDB1 pluggable database
- Make sure the database container is healthy before running scripts
