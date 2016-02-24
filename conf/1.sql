# --- !Ups
    CREATE TABLE Bookmarks (
      id INT NOT NULL AUTO_INCREMENT,
      title VARCHAR(100) NOT NULL,
      url VARCHAR(250) NOT NULL,
      PRIMARY KEY (id)
    );

# --- !Downs
    DROP TABLE Bookmarks;