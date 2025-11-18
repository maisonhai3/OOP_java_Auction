# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

# Going Going Gone - Auction System

## Project Overview

An auction management system built with Java and Swing, following Clean Architecture principles (adapted for
simplicity - no interfaces).

## Architecture

### Clean Architecture (KISS Variant)

```
src/auction/
├── domain/              - Business entities (User, Lot, Bid, AuctionSession, etc.)
├── usecases/           - Application business logic (Services)
├── infrastructure/     - Data persistence (future: SQLite repositories)
└── presentation/       - UI layer (Swing applications)
```

**Key Principle:** We skip interfaces to keep the project small and fast to develop.

### Dependency Flow

```
Presentation → Use Cases → Domain
                ↓
         Infrastructure
```

---

## Design Patterns Used

### 1. Singleton Pattern

**Where:** All service classes in `/usecases`
**Why:** Single instance of each service, shared across the application

**Services using Singleton:**

- `LotService`
- `UserService`
- `BiddingService`
- `AuctionSessionService`
- `NotificationService`
- `LobbyServices`
- `StaffLobbyServices`

**Usage:**

```java
LotService lotService = LotService.getInstance();
```

### 2. Builder Pattern

**Where:** `Lot` domain entity
**Why:** Flexible object construction with required/optional fields

**Implementation:**

- `Lot` has private constructor
- `Lot.LotBuilder` nested class for construction
- Required field: `name`
- Optional fields: `estimatePrice`, `reservePrice`

**Usage:**

```java
Lot lot = new Lot.LotBuilder("Vintage Watch")
        .estimatePrice(1000f)
        .reservePrice(800f)
        .build();
```

**Responsibility Separation:**

- **UI**: Collects raw string data
- **Service**: Validates, parses, uses Builder
- **Domain**: Knows how to construct itself

---

## Key Services

### LotService (Singleton)

**Purpose:** Manages lot creation and storage

**Methods:**

- `createLot(String name, String estimatePriceStr, String reservePriceStr)` - Creates lot from raw strings (Builder
  Pattern)
- `getAllLots()` - Returns all lots
- `getLot(int index)` - Gets lot by index
- `getLotCount()` - Returns number of lots

**Pattern:** Uses Builder Pattern internally, presentation layer doesn't need to know about it.

### StaffLobbyServices (Singleton)

**Purpose:** Manages auction sessions

**Methods:**

- `createAuctionSession(String title, int lotID)` - Creates auction session
- `getAllAuctionSessions()` - Returns all auction sessions
- `getAvailableAuctionSession()` - Returns only SCHEDULED and STARTED auctions

---

## Applications

### 1. EndUserApp

**Purpose:** End-user interface for bidding
**Features:**

- User login/registration
- Auction lobby
- Bidding interface

### 2. StaffApp

**Purpose:** Staff interface for managing lots and auctions
**Features:**

- **Tab 1: Lot Management**
    - Create lots with Builder Pattern
    - View all created lots
    - Input: name (required), estimate price (optional), reserve price (optional)

- **Tab 2: Auction Session Management**
    - Create auction sessions
    - Select lot from dropdown (no manual ID entry)
    - View all auction sessions

**How to run:**

```bash
java auction.presentation.StaffApp
```

---

## Recent Changes (2025-11-18)

### 1. Fixed Java Compatibility

- **File:** `Main.java`
- **Change:** Added proper class declaration for Java 21 compatibility
- **Before:** Implicitly declared class
- **After:** `public class Main` with `public static void main(String[] args)`

### 2. Implemented Builder Pattern for Lot

- **File:** `Lot.java`
- **Changes:**
    - Made constructor private
    - Added `LotBuilder` nested class
    - Added getters for all fields
    - Sets default values (status = UNSOLD, noReserve = false)

### 3. Updated LotService

- **File:** `LotService.java`
- **Changes:**
    - Added `createLot()` method that accepts raw strings
    - Service validates and parses input
    - Service uses Builder Pattern internally
    - Made `addLot(Lot)` private (internal use only)

### 4. Applied Singleton to All Services

- **Files:** All services in `usecases/`
- **Pattern:**
  ```java
  private static final ServiceName INSTANCE = new ServiceName();
  private ServiceName() { }
  public static ServiceName getInstance() { return INSTANCE; }
  ```

### 5. Created StaffApp

- **File:** `StaffApp.java`
- **Features:**
    - Tabbed interface (Lot Management + Auction Session Management)
    - Lot creation form with Builder Pattern integration
    - Lot dropdown selector (no manual ID input)
    - Auto-refresh on creation
    - Professional UI matching EndUserApp styling

### 6. Enhanced StaffLobbyServices

- **File:** `StaffLobbyServices.java`
- **Changes:**
    - Initialize `auctionList` in constructor
    - `createAuctionSession()` now adds to list
    - Added `getAllAuctionSessions()` method

---

## Future Plans

### SQLite Integration (See `/docs/sqlite-integration-plan.md`)

**Status:** Planned, not yet implemented

**What will change:**

1. Add SQLite JDBC dependency
2. Create `DatabaseConnection.java` in infrastructure
3. Create repository classes (UserRepository, LotRepository, etc.)
4. Update services to use repositories
5. Database file: `auction.db` (auto-created)

**Architecture after SQLite:**

```
Presentation → Services → Repositories → Database
                  ↓
               Domain
```

---

## Code Style Guidelines

### 1. Keep It Simple (KISS)

- No interfaces unless absolutely necessary
- Direct concrete class usage
- Prefer readability over clever abstractions

### 2. Builder Pattern for Complex Objects

- Use when objects have optional fields
- Service layer owns Builder usage, not UI

### 3. Singleton for Services

- One instance per service class
- Consistent pattern across all services

### 4. Separation of Concerns

- **Domain**: Pure business entities, no dependencies
- **Use Cases**: Business logic, orchestrates domain
- **Presentation**: UI only, no business logic
- **Infrastructure**: Data access (future)

---

## Development Notes

### Compiling

```bash
javac src/**/*.java
```

### Running

```bash
# End User App
java -cp src auction.presentation.EndUserApp

# Staff App
java -cp src auction.presentation.StaffApp

# Main (launches EndUserApp)
java -cp src Main
```

### Java Version

- **Target:** Java 21+
- **Features used:** Switch expressions, records (future)

---

## Documentation

- **SQLite Integration Plan:** `/docs/sqlite-integration-plan.md`
- **Builder Pattern Guide:** `/docs/Adapt_Builder_pattern.md` (Vietnamese)

---

## Contributors

- Developer: [Your Name]
- AI Assistant: Claude (Anthropic)

Last Updated: 2025-11-18
