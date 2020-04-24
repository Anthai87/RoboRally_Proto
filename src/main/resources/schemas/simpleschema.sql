CREATE TABLE IF NOT EXISTS Game (
    gameID int NOT NULL UNIQUE AUTO_INCREMENT,

  name varchar(255),

  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,
  
  PRIMARY KEY (gameID),

);
  
CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  name varchar(255),
  colour varchar(31),
  
  positionX int,
  positionY int,
  heading tinyint,
  
  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID)
);

CREATE TABLE IF NOT EXISTS CardField (
    gameID int NOT NULL,
    playerID tinyint NOT NULL,
    type tinyint NOT NULL,
    position  tinyint NOT NULL,

    visible BIT NOT NULL,
    command tinyint,

    PRIMARY KEY (gameID, playerID, type, position ),
    FOREIGN KEY (gameID) REFERENCES Game(gameID),
    FOREIGN KEY (gameID, playerID) REFERENCES Player(gameID, playerID)
);