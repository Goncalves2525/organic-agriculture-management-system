# Organic Agriculture Management System

This project consists of an integrated system to support the management of an agricultural facility in organic production mode (OPM). The system allows managing plots, crops, agricultural operations, irrigation, sensors, product marketing, and other essential components for organic agriculture.

## Overview

The system is composed of different interconnected modules:

1. **Agricultural Operations Management** - Registration and consultation of operations such as sowing, weeding, pruning, harvesting, and application of production factors
2. **Irrigation and Fertigation System** - Control and management of irrigation and fertilizer application
3. **Sensor Management** - Monitoring of meteorological and soil data
4. **Distribution Management** - Routes and logistics for distribution of agricultural baskets
5. **Warehouse Management** - Control of temperature and storage conditions

## System Components

The system is developed with a modular architecture:

### 1. Sensors Management
Implemented in C and Assembly for acquisition and processing of meteorological and soil sensor data.

### 2. Farm Operations Management
Main component based on Oracle PL/SQL for managing all agricultural operations.

### 3. Basket Management
Java module for managing baskets and distribution routes.

### 4. Farm Coordination
Main Java application that integrates all components and provides user interface.

## Technologies Used

- **Java** - Main application and user interface
- **Oracle PL/SQL** - Database and procedures
- **C/Assembly** - Sensor and hardware management
- **Advanced Data Structures** - Graphs and algorithms for route optimization

## Main Features

### Field Notebook
- Registration of agricultural operations
- Monitoring of production factor applications
- Registration of harvests and productivity
- Operation planning

### Irrigation System
- Control of irrigation sectors
- Programming of irrigation cycles
- Fertigation application
- Water consumption monitoring

### Sensors
- Reading of temperature, humidity, wind speed
- Analysis of meteorological data
- Monitoring of soil humidity and pH
- Alerts for extreme conditions

### Basket Management
- Determination of optimized routes
- Identification of ideal hubs
- Transport capacity calculation
- Delivery scheduling

## Installation and Configuration

### Prerequisites
- Java JDK 11 or higher
- Oracle Database 19c or higher
- Maven 3.6 or higher
- Development kit for Raspberry Pi Pico W
- C development environment (GCC, Make)
- IDE (recommended: IntelliJ IDEA or NetBeans)

### Step-by-Step Installation Guide

#### 1. Java Environment Configuration

##### Linux (Ubuntu/Debian)
```bash
# Install Java JDK 11
sudo apt install openjdk-11-jdk

# Verify installation
java -version
javac -version

# Install Maven
sudo apt install maven

# Verify Maven installation
mvn -version
```

##### macOS
```bash
# Using Homebrew to install Java JDK 11
brew tap adoptopenjdk/openjdk
brew install --cask adoptopenjdk11

# Verify installation
java -version
javac -version

# Install Maven
brew install maven

# Verify Maven installation
mvn -version
```

##### Windows
1. Download Java JDK 11 from: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
2. Run the installer and follow the instructions
3. Configure environment variables:
    - Add `JAVA_HOME` to system environment variables: `C:\Program Files\Java\jdk-11.0.X`
    - Add `%JAVA_HOME%\bin` to PATH variable

4. Download Maven from: https://maven.apache.org/download.cgi
5. Extract to a directory, such as `C:\Program Files\Apache\maven`
6. Configure environment variables:
    - Add `M2_HOME` to system environment variables: `C:\Program Files\Apache\maven`
    - Add `%M2_HOME%\bin` to PATH variable

7. Verify installation:
```cmd
java -version
javac -version
mvn -version
```

#### 2. Oracle Database Configuration

##### Linux (Ubuntu/Debian)
1. Download Oracle Database Express Edition from: https://www.oracle.com/database/technologies/xe-downloads.html
```bash
# Install necessary dependencies
sudo apt-get install alien libaio1

# Convert RPM to DEB and install (assuming you downloaded the RPM version)
sudo alien -i oracle-database-xe-21c-1.0-1.x86_64.rpm

# Configure the database
sudo /etc/init.d/oracle-xe-21c configure
```

##### macOS
Oracle Database is not officially supported on macOS. Alternative options:
1. Use Docker:
```bash
# Download and run the Oracle Database Docker image
docker pull container-registry.oracle.com/database/express:latest
docker run -d -p 1521:1521 -p 5500:5500 -e ORACLE_PWD=password container-registry.oracle.com/database/express:latest
```

2. Use a virtual machine with Linux or Windows to run Oracle Database

##### Windows
1. Download Oracle Database Express Edition from: https://www.oracle.com/database/technologies/xe-downloads.html
2. Run the installer and follow the instructions
3. During installation, set a password for the SYSTEM user

For all operating systems, after installation, create a user for the application:
```sql
-- Connect as SYSTEM or SYS
sqlplus SYSTEM/your_password@localhost:1521/XE

-- Create a new user
CREATE USER farm_manager IDENTIFIED BY password;
GRANT CONNECT, RESOURCE, DBA TO farm_manager;
```

Execute the database creation scripts:

##### Linux/macOS
```bash
cd database/scripts
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_schema.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_tables.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_procedures.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_triggers.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @insert_initial_data.sql
```

##### Windows
```cmd
cd database\scripts
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_schema.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_tables.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_procedures.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_triggers.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @insert_initial_data.sql
```

#### 3. Sensors Component Configuration

##### Linux (Ubuntu/Debian)
```bash
# Install development tools
sudo apt install build-essential cmake gcc-arm-none-eabi libnewlib-arm-none-eabi

# Install Raspberry Pi Pico SDK
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
export PICO_SDK_PATH=$(pwd)
```

##### macOS
```bash
# Using Homebrew
brew install cmake
brew install --cask gcc-arm-embedded

# Install Raspberry Pi Pico SDK
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
export PICO_SDK_PATH=$(pwd)
```

##### Windows
1. Install CMake: https://cmake.org/download/
2. Install ARM GCC Toolchain: https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-rm/downloads
3. Install Visual Studio Build Tools (or full Visual Studio)
4. Clone the Raspberry Pi Pico SDK repository:
```cmd
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
set PICO_SDK_PATH=%cd%
```

For all operating systems, compile the code for the microcontroller:

##### Linux/macOS
```bash
cd SensorsManagement/ColetorDeDados
mkdir build && cd build
cmake ..
make
```

##### Windows
```cmd
cd SensorsManagement\ColetorDeDados
mkdir build
cd build
cmake -G "NMake Makefiles" ..
nmake
```

Upload the firmware to the Raspberry Pi Pico W:
1. Hold the BOOTSEL button while connecting the Pico W to your computer
2. Copy the generated .uf2 file to the drive that appears

Compile the data processing components:

##### Linux/macOS
```bash
cd SensorsManagement/ProcessadorDeDados
make
   
cd SensorsManagement/SaidaDeDados
make
```

##### Windows
```cmd
cd SensorsManagement\ProcessadorDeDados
nmake -f Makefile.win
   
cd SensorsManagement\SaidaDeDados
nmake -f Makefile.win
```

#### 4. Main Project Configuration

##### Linux/macOS/Windows
1. Clone the repository:
```bash
git clone https://github.com/your-username/farm-management.git
cd farm-management
```

2. Configure the properties file:
```bash
# Linux/macOS
cp config/application.properties.template config/application.properties

# Windows
copy config\application.properties.template config\application.properties
```

3. Edit the `config/application.properties` file with database connection information:
```properties
# Database Connection
database.inet=localhost
database.port=1521
database.sid=XEPDB1
database.user=farm_manager
database.password=password
   
# Sensors Configuration
sensors.input.directory=/path/to/sensor/data
sensors.output.directory=/path/to/processed/data
```

4. Compile the project with Maven:
```bash
# Linux/macOS/Windows
mvn clean install
```

#### 5. System Execution

##### Linux
```bash
# Start the Oracle database
sudo systemctl start oracle-xe

# Start the sensors component
cd SensorsManagement/ProcessadorDeDados
./processadordedados /dev/ttyUSB0 config output 50

# Start the data output component
cd SensorsManagement/SaidaDeDados
./saidadedados output farm_data 60000

# Run the main application
cd <project-directory>
java -jar target/farm-management.jar
```

##### macOS
```bash
# Start the Oracle database (if using Docker)
docker start <container-id>

# Start the sensors component
cd SensorsManagement/ProcessadorDeDados
./processadordedados /dev/tty.usbserial config output 50

# Start the data output component
cd SensorsManagement/SaidaDeDados
./saidadedados output farm_data 60000

# Run the main application
cd <project-directory>
java -jar target/farm-management.jar
```

##### Windows
```cmd
# Start the Oracle database
# Use Oracle Database Configuration Assistant or Windows services

# Start the sensors component
cd SensorsManagement\ProcessadorDeDados
processadordedados.exe COM3 config output 50

# Start the data output component
cd SensorsManagement\SaidaDeDados
saidadedados.exe output farm_data 60000

# Run the main application
cd <project-directory>
java -jar target\farm-management.jar
```

#### 6. Installation Verification

1. Test the database connection through the "Database Connection Test" option in the main menu
2. Verify sensor data reception through the "Sensor Data" option in the main menu
3. Import sample data through the "Import legacy data" option if needed

#### 7. Common Troubleshooting

- **Database connection error**: Check the settings in the application.properties file and verify that the Oracle service is running
- **Sensors not detected**:
    - **Linux/macOS**: Check serial port permissions (`sudo chmod 666 /dev/ttyUSB0` or equivalent)
    - **Windows**: Check the correct COM port number in Device Manager
- **Compilation errors**: Check the JDK version and if all dependencies are installed correctly
- **Oracle issues on macOS**: Consider using a virtual machine or Docker as an alternative

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── repository/       # Data access
│   │   │   ├── tables/           # Entity classes
│   │   │   ├── ui/               # User interface
│   │   │   ├── utils/            # Utilities
│   │   │   └── App.java          # Entry point
│   │   └── resources/
│   │       └── config/           # Configuration files
│   └── test/                     # Unit tests
├── SensorsManagement/            # Sensors component (C/Assembly)
│   ├── ColetorDeDados/
│   ├── ProcessadorDeDados/
│   └── SaidaDeDados/
└── database/                     # Database scripts and models
    └── scripts/
```

## Main Application Menu

The main menu provides access to the following features:
- Database Connection Test
- Legacy Data Import
- Sowing Management
- Weeding Management
- Production Factor Applications
- Harvest Registration
- Irrigation and Fertigation Registration
- Operations Log Consultation
- Distribution Management (GFH Manager)
- Sensor Data Visualization
- Operation Cancellation

## Contribution

The project follows an agile methodology based on SCRUM, with four-week sprints. Development is done according to a Test-Driven Development (TDD) approach.

## License

This project was developed as part of the Informatics Engineering Degree (LEI) at the Instituto Superior de Engenharia do Porto (ISEP).
