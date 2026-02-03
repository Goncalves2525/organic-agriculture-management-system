#!/bin/bash

# Script to execute all SQL files in the correct order
# This script should be run after the Oracle container is healthy

set -e

echo "========================================"
echo "Farm Management System - Database Setup"
echo "========================================"
echo ""

# Database connection details
DB_USER="${DATABASE_USER:-farm_manager}"
DB_PASSWORD="${DATABASE_PASSWORD:-password}"
DB_CONTAINER="farm-oracle-db"
DB_CONNECTION="$DB_USER/$DB_PASSWORD@//localhost:1521/XEPDB1"

# Check if container is running
if ! docker ps | grep -q "$DB_CONTAINER"; then
    echo "Error: Oracle database container '$DB_CONTAINER' is not running!"
    echo "Please start the container with: docker-compose up -d oracle-db"
    exit 1
fi

echo "Waiting for database to be ready..."
sleep 10

# Function to execute SQL file
execute_sql() {
    local sql_file=$1
    local description=$2

    echo ""
    echo "----------------------------------------"
    echo "Executing: $description"
    echo "File: $sql_file"
    echo "----------------------------------------"

    if [ -f "$sql_file" ]; then
        docker exec -i $DB_CONTAINER sqlplus -S $DB_CONNECTION @/container-entrypoint-initdb.d/sql_files/$(basename $sql_file) || {
            echo "Warning: Script $sql_file completed with errors (this may be normal for DROP statements)"
        }
        echo "✓ Completed: $description"
    else
        echo "⚠ Warning: File not found: $sql_file"
    fi
}

# Execute SQL scripts in order
echo ""
echo "Starting database initialization..."
echo ""

# Drop existing objects (optional - may produce errors if objects don't exist)
execute_sql "sql_files/DROP_sprint3.sql" "Drop Sprint 3 objects (optional)"
execute_sql "sql_files/DROP_sprint2.sql" "Drop Sprint 2 objects (optional)"

# Create schema
execute_sql "sql_files/CREATE_sprint1.sql" "Create Sprint 1 schema"
execute_sql "sql_files/CREATE_sprint2.sql" "Create Sprint 2 schema"
execute_sql "sql_files/CREATE_sprint3.sql" "Create Sprint 3 schema"

# Create functions and procedures
execute_sql "sql_files/COMPILED_functions_sprint2_sprint3.sql" "Create functions and procedures"

# Insert data
execute_sql "sql_files/INSERTS_v2a_aval_sprint1.sql" "Insert Sprint 1 data"
execute_sql "sql_files/GEN_INSERTS-UPDATES_DELETES_sprint2.sql" "Insert Sprint 2 data"
execute_sql "sql_files/GEN_INSERTS-UPDATES_DELETES_sprint3.sql" "Insert Sprint 3 data"

echo ""
echo "========================================"
echo "✓ Database setup completed!"
echo "========================================"
echo ""
echo "You can now start the application with:"
echo "  docker-compose up -d            # Start all services in background"
echo "  docker-compose up farm-app      # Start app with logs visible"
echo ""
echo "To access the interactive application:"
echo "  docker attach farm-management-app"
echo ""
