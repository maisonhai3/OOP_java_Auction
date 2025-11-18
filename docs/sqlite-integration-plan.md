# SQLite Integration Plan (KISS)

## What Goes Where

**Put in `infrastructure/`:**

- `DatabaseConnection.java` - Manages SQLite connection
- Repository classes (e.g., `UserRepository.java`, `BidRepository.java`) - Handle database CRUD

**Modify in `usecases/`:**

- Services get repositories via constructor
- Use repositories to save/load instead of just creating objects

**Initialize in `presentation/`:**

- Create database connection at startup
- Create repositories
- Pass repositories to services

---

## What You Need

1. **SQLite JDBC driver** - Add to classpath
2. **Database file** - `auction.db` (auto-created)

---

## Example: UserService Changes

**Before:**

```java
public User createUser(String username) {
    return new User(username);
}
```

**After:**

```java
private UserRepository userRepo;

public UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
}

public User createUser(String username) {
    User user = new User(username);
    userRepo.save(user);
    return user;
}
```

---

## Implementation Order

1. Add SQLite dependency
2. Create `DatabaseConnection`
3. Create one repository (start with User)
4. Update one service to use it
5. Repeat for other entities as needed