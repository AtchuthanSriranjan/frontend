CREATE TABLE IF NOT EXISTS leaderboard (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  time_seconds INTEGER NOT NULL,
  size INTEGER NOT NULL,
  board_type TEXT NOT NULL CHECK (board_type IN ('fixed','random')),
  solved_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
