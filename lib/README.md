# Dependencies

## SQLite JDBC Driver

**Required for database functionality**

### Download

1. Go to: https://github.com/xerial/sqlite-jdbc/releases
2. Download the latest `sqlite-jdbc-X.X.X.X.jar` (e.g., `sqlite-jdbc-3.45.0.0.jar`)
3. Place it in this `/lib` directory

### Alternative: Direct Download Link

```bash
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
```

### Compile with JAR

```bash
javac -cp "lib/*:src" src/**/*.java
```

### Run with JAR

```bash
java -cp "lib/*:src" auction.presentation.StaffApp
java -cp "lib/*:src" auction.presentation.EndUserApp
```

### IDE Setup (IntelliJ IDEA)

1. Right-click project → Open Module Settings
2. Libraries → + → Java
3. Select the JAR file from `/lib`
4. Apply
