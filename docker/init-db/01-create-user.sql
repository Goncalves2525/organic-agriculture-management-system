-- Database initialization script for Docker
-- This script creates the farm_manager user and grants necessary privileges

-- Connect to the PDB (Pluggable Database)
ALTER SESSION SET CONTAINER = XEPDB1;

-- Create user if not exists
BEGIN
    EXECUTE IMMEDIATE 'CREATE USER farm_manager IDENTIFIED BY password';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1920 THEN
            NULL; -- User already exists
        ELSE
            RAISE;
        END IF;
END;
/

-- Grant privileges
GRANT CONNECT, RESOURCE, DBA TO farm_manager;
GRANT CREATE SESSION TO farm_manager;
GRANT CREATE TABLE TO farm_manager;
GRANT CREATE VIEW TO farm_manager;
GRANT CREATE PROCEDURE TO farm_manager;
GRANT CREATE SEQUENCE TO farm_manager;
GRANT CREATE TRIGGER TO farm_manager;
GRANT UNLIMITED TABLESPACE TO farm_manager;

-- Commit changes
COMMIT;

-- Display success message
SELECT 'Database user farm_manager created successfully!' AS STATUS FROM DUAL;
