# SocialSim - Java Social Network Simulation

A Java-based social network simulation project.

## Features

- **Users** - Create and manage user profiles with bio and join date
- **Posts** - Three types of posts:
  - Regular text posts
  - Event posts (with date and location)
  - Poll posts (with multiple choice options)
- **Comments** - Add comments to posts
- **Likes** - Like posts (with duplicate-like prevention)
- **Notifications** - Automatic notifications for likes and comments
- **Exception Handling** - Custom exceptions for invalid operations

## Project Structure

```
src/
├── App.java                  # Main entry point
├── SocialNetwork.java        # Core network management
├── User.java                 # User model
├── Post.java                 # Base post class
├── EventPost.java            # Event post (extends Post)
├── PollPost.java             # Poll post (extends Post)
├── Comment.java              # Comment model
├── Notification.java         # Notification model
├── ApplicationException.java # Base exception
├── EmptyContentException.java
├── AlreadyLikedException.java
├── InvalidOptionException.java
└── UserNotFoundException.java
```

## How to Run

Open the project in IntelliJ IDEA and run `App.java`.

## Author

Avishag Yonatan
