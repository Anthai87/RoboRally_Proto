-- Need to switch of FK check for MySQL since there are crosswise FK references
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,
  name varchar(255),
  name varchar(255),
  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,
  
  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID)
);;


  
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
);;

CREATE TABLE IF NOT EXISTS CARD(
gameID int NOT NULL,
  playerID tinyint NOT NULL,
CARDS1   ENUM('FORWARD','RIGHT','LEFT','FAST_FORWARD','OPTION_LEFT_RIGHT'),
CARDS2   ENUM('FORWARD','RIGHT','LEFT','FAST_FORWARD','OPTION_LEFT_RIGHT'),
CARDS3   ENUM('FORWARD','RIGHT','LEFT','FAST_FORWARD','OPTION_LEFT_RIGHT'),
CARDS4   ENUM('FORWARD','RIGHT','LEFT','FAST_FORWARD','OPTION_LEFT_RIGHT'),
CARDS5   ENUM('FORWARD','RIGHT','LEFT','FAST_FORWARD','OPTION_LEFT_RIGHT'),

 PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID),
  FOREIGN KEY (playerID) REFERENCES PLAYER(playerID)
);;


SET FOREIGN_KEY_CHECKS = 1






