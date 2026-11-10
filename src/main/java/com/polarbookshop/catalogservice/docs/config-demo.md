
# Configuration Demo

The Catalog Service was built once using `.\gradlew.bat bootJar`.

## 1. Default Configuration
Command: `java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar`

Result: Welcome to the local book catalog!

## 2. Command-Line Argument
Command: `java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Welcome to the catalog from CLI"`

Result: Welcome to the catalog from CLI

## 3. JVM System Property
Command: `java '-Dpolar.greeting=Welcome to the catalog from JVM' -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar`

Result: Welcome to the catalog from JVM

## 4. Environment Variable
Command: `$env:POLAR_GREETING="Welcome to the catalog from ENV"; java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar`

Result: Welcome to the catalog from ENV

## 5. Configuration Precedence
Command: `java '-Dpolar.greeting=Welcome from JVM' -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Welcome from CLI"`

Result: Welcome from CLI

Command-line arguments have higher precedence than JVM system properties.
