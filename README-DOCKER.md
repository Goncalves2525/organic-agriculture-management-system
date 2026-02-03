# Docker Setup for Organic Agriculture Management System

This guide explains how to run the Organic Agriculture Management System using Docker and Docker Compose, making it much easier to set up and run on macOS, Linux, and Windows.

## Why Docker?

Running this application with Docker solves several challenges:

- **No Oracle Database installation on macOS**: Oracle DB is not officially supported on macOS, but Docker makes it work seamlessly
- **Simplified dependencies**: No need to manually install Java, Maven, GCC, or other tools
- **Consistent environment**: Same setup works on Mac, Linux, and Windows
- **One-command startup**: Start all services with a single command
- **Isolated environment**: Doesn't interfere with other software on your machine

## Prerequisites

Before you begin, install:

1. **Docker Desktop** (includes Docker Compose)
   - macOS: Download from [docker.com](https://www.docker.com/products/docker-desktop)
   - Windows: Download from [docker.com](https://www.docker.com/products/docker-desktop)
   - Linux: Follow instructions at [docs.docker.com](https://docs.docker.com/engine/install/)

2. **Git** (to clone the repository)

### System Requirements

- **RAM**: At least 8GB (Oracle Database requires significant memory)
- **Disk Space**: At least 10GB free space
- **Docker Desktop Settings**: Allocate at least 4GB RAM to Docker

## Quick Start

### 1. Clone the Repository (if not already done)

```bash
git clone <repository-url>
cd organic-agriculture-management-system
```

### 2. Configure Environment Variables

Copy the example environment file and adjust if needed:

```bash
cp .env.example .env
```

Edit `.env` to customize database credentials (optional):

```bash
# Default values are already set in .env.example
ORACLE_PASSWORD=OraclePassword123
DATABASE_USER=farm_manager
DATABASE_PASSWORD=password
```

### 3. Configure Application Properties for Docker

For Docker, use the Docker-specific configuration:

```bash
# Backup the original config
cp src/main/resources/config/application.properties src/main/resources/config/application.properties.backup

# Use the Docker configuration
cp src/main/resources/config/application.properties.docker src/main/resources/config/application.properties
```

### 4. Start the Oracle Database

First, start just the database container:

```bash
docker-compose up -d oracle-db
```

This will:
- Pull the Oracle Database XE 21c image (first time only, ~1-2GB download)
- Create and start the database container
- Automatically create the `farm_manager` user with the configured password

**Note**: We use the `gvenzl/oracle-xe` image, which is a community-maintained Oracle XE image that doesn't require Oracle Container Registry authentication and is optimized for development.

Wait for the database to be ready (this can take 1-2 minutes on first startup):

```bash
# Check database status
docker-compose logs -f oracle-db

# Look for: "DATABASE IS READY TO USE!"
```

### 5. Initialize the Database Schema

Once the database is ready, run the SQL initialization script:

```bash
./docker/scripts/run-all-sql.sh
```

This script will:
- Create all tables and schema objects
- Create stored procedures and functions
- Insert initial sample data

### 6. Start the Application

Now start the remaining services:

```bash
# Option A: Start in background (recommended for normal use)
docker-compose up -d

# Option B: Start with logs visible (useful for debugging)
docker-compose up
```

**Note**: Since the database is already running, you can also start just the app:

```bash
docker-compose up -d farm-app
```

### 7. Access the Application

The application runs in interactive terminal mode:

```bash
docker attach farm-management-app
```

You can now interact with the application menu!

## Docker Services Overview

The Docker setup includes these services:

### 1. `oracle-db` - Oracle Database Express Edition
- **Ports**: 1521 (database), 5500 (web console)
- **Purpose**: Stores all farm management data
- **Data**: Persisted in Docker volume `oracle-data`

### 2. `farm-app` - Java Application
- **Purpose**: Main farm management application
- **Depends on**: oracle-db
- **Interactive**: Yes (terminal-based UI)

### 3. `sensors-processor` - Sensor Data Processor (Optional)
- **Purpose**: Processes data from agricultural sensors
- **Note**: Configured but not active by default (for testing without hardware)

### 4. `sensors-output` - Sensor Data Output (Optional)
- **Purpose**: Outputs processed sensor data
- **Note**: Configured but not active by default

## Common Commands

### Starting Services

```bash
# Start all services
docker-compose up -d

# Start only specific services
docker-compose up -d oracle-db farm-app

# Start with logs visible
docker-compose up
```

### Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: deletes all data)
docker-compose down -v
```

### Viewing Logs

```bash
# View logs for all services
docker-compose logs -f

# View logs for specific service
docker-compose logs -f farm-app
docker-compose logs -f oracle-db
```

### Accessing Containers

```bash
# Access the Java application (interactive mode)
docker attach farm-management-app

# Execute commands in the database container
docker exec -it farm-oracle-db bash

# Access SQLPlus in the database
docker exec -it farm-oracle-db sqlplus farm_manager/password@//localhost:1521/XEPDB1
```

### Rebuilding After Code Changes

```bash
# Rebuild the Java application
docker-compose build farm-app

# Restart with the new build
docker-compose up -d farm-app
```

## Database Management

### Accessing Oracle Database

#### Option 1: SQLPlus from Container

```bash
docker exec -it farm-oracle-db sqlplus farm_manager/password@//localhost:1521/XEPDB1
```

#### Option 2: Oracle SQL Developer (GUI)

Connect with these settings:
- **Hostname**: localhost
- **Port**: 1521
- **Service Name**: XEPDB1
- **Username**: farm_manager
- **Password**: password (or your custom password from .env)

#### Option 3: Web Console

Access the Oracle web console at: [https://localhost:5500/em](https://localhost:5500/em)

### Running SQL Scripts Manually

```bash
# Execute a specific SQL script
docker exec -it farm-oracle-db sqlplus farm_manager/password@//localhost:1521/XEPDB1 @/docker-entrypoint-initdb.d/sql_files/CREATE_sprint1.sql
```

### Backing Up Database

```bash
# Export the Oracle data volume
docker run --rm -v organic-agriculture-management-system_oracle-data:/data -v $(pwd):/backup alpine tar czf /backup/oracle-backup.tar.gz -C /data .
```

### Restoring Database

```bash
# Stop services
docker-compose down

# Remove old volume
docker volume rm organic-agriculture-management-system_oracle-data

# Recreate and restore
docker volume create organic-agriculture-management-system_oracle-data
docker run --rm -v organic-agriculture-management-system_oracle-data:/data -v $(pwd):/backup alpine tar xzf /backup/oracle-backup.tar.gz -C /data

# Start services
docker-compose up -d
```

## Troubleshooting

### Database Container Won't Start

**Problem**: Database container crashes or won't start

**Solutions**:
1. Check Docker has enough memory allocated (at least 4GB)
   - Docker Desktop → Settings → Resources → Memory
2. Check logs: `docker-compose logs oracle-db`
3. Remove old container and try again:
   ```bash
   docker-compose down -v
   docker-compose up -d oracle-db
   ```

### Application Can't Connect to Database

**Problem**: Application shows "Database Server not reachable!"

**Solutions**:
1. Ensure database is healthy: `docker-compose ps`
2. Check the database logs: `docker-compose logs oracle-db`
3. Verify connection settings in `application.properties`
4. Wait longer - Oracle DB can take 2-3 minutes to initialize

### Port Already in Use

**Problem**: Error "port is already allocated"

**Solutions**:
1. Check if Oracle or another database is running on port 1521:
   ```bash
   # macOS/Linux
   lsof -i :1521

   # Windows
   netstat -ano | findstr :1521
   ```
2. Stop the conflicting service or change ports in `docker-compose.yml`

### Out of Disk Space

**Problem**: "no space left on device"

**Solutions**:
1. Clean up unused Docker resources:
   ```bash
   docker system prune -a --volumes
   ```
2. Free up disk space on your machine

### Sensor Components Not Working

**Problem**: Sensors not processing data

**Note**: The sensor components are configured but disabled by default. They require:
- Physical sensor hardware (Raspberry Pi Pico W)
- USB serial connection to the host machine
- Device mapping in docker-compose.yml

To enable sensors, modify `docker-compose.yml`:
```yaml
sensors-processor:
  devices:
    - /dev/ttyUSB0:/dev/ttyUSB0  # Linux
    # - /dev/tty.usbserial:/dev/ttyUSB0  # macOS
  command: ["processadorDados", "/dev/ttyUSB0", "/app/config/config.txt", "/app/data/output", "50"]
```

## Performance Tips

### Improve Startup Time

1. **Use Docker BuildKit** (faster builds):
   ```bash
   export DOCKER_BUILDKIT=1
   docker-compose build
   ```

2. **Don't rebuild unnecessarily**: Images are cached after first build

### Reduce Memory Usage

1. **Run only needed services**:
   ```bash
   # Just database and app (skip sensor components)
   docker-compose up -d oracle-db farm-app
   ```

2. **Adjust Java heap size** in `.env`:
   ```bash
   JAVA_OPTS=-Xmx256m -Xms128m
   ```

## Development Workflow

### Making Code Changes

1. Edit your Java source files
2. Rebuild the container:
   ```bash
   docker-compose build farm-app
   ```
3. Restart the service:
   ```bash
   docker-compose up -d farm-app
   ```

### Running Tests

```bash
# Run Maven tests in a container
docker-compose run --rm farm-app mvn test
```

### Debugging

Enable debug mode by modifying `docker-compose.yml`:

```yaml
farm-app:
  environment:
    - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Xmx512m -Xms256m
  ports:
    - "5005:5005"
```

Then connect your IDE debugger to `localhost:5005`.

## Differences from Manual Setup

| Aspect | Manual Setup | Docker Setup |
|--------|-------------|--------------|
| Java Installation | Required | Not required (in container) |
| Maven Installation | Required | Not required (in container) |
| Oracle on macOS | Not supported | Works via Docker |
| C Compiler | Manual install | Not required (in container) |
| Startup | Multiple commands | One command |
| Isolation | Uses system | Isolated containers |
| Cleanup | Manual | `docker-compose down` |

## Switching Back to Manual Setup

To restore the original configuration:

```bash
# Restore original application.properties
cp src/main/resources/config/application.properties.backup src/main/resources/config/application.properties
```

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Oracle Database Express Edition](https://www.oracle.com/database/technologies/appdev/xe.html)
- [Project Main README](README.md)

## Getting Help

If you encounter issues:

1. Check the logs: `docker-compose logs -f`
2. Verify Docker is running: `docker ps`
3. Ensure ports are available: `lsof -i :1521` (macOS/Linux)
4. Review this troubleshooting section
5. Check Docker Desktop settings (memory, disk space)

## Summary

Docker makes running this application much simpler, especially on macOS where Oracle Database isn't natively supported. With just a few commands, you have a fully functional farm management system running!

```bash
# The complete workflow:
cp .env.example .env
cp src/main/resources/config/application.properties.docker src/main/resources/config/application.properties
docker-compose up -d oracle-db
# Wait for database...
./docker/scripts/run-all-sql.sh
docker-compose up -d
docker attach farm-management-app
```

Happy farming! 🌱
