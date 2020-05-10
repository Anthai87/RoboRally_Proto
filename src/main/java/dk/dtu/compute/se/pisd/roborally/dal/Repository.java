/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.dal;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Command.values;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @Co-Authors S195383, S176481, S195827, S176485
 */
class Repository implements IRepository {

    private static final String GAME_GAMEID = "gameID";

    private static final String GAME_NAME = "name";

    private static final String GAME_CURRENTPLAYER = "currentPlayer";

    private static final String GAME_PHASE = "phase";

    private static final String GAME_STEP = "step";

    private static final String PLAYER_PLAYERID = "playerID";

    private static final String PLAYER_NAME = "name";

    private static final String PLAYER_COLOUR = "colour";

    private static final String PLAYER_GAMEID = "gameID";

    private static final String PLAYER_POSITION_X = "positionX";

    private static final String PLAYER_POSITION_Y = "positionY";

    private static final String PLAYER_HEADING = "heading";

    private static final String FIELD_GAMEID = "gameID";

    private static final String FIELD_PLAYERID = "playerID";

    private static final String FIELD_TYPE = "type";

    private static final int FIELD_TYPE_REGISTER = 1;

    private static final int FIELD_TYPE_HAND = 0;

    private static final String FIELD_POS = "position";

    private static final String FIELD_VISIBLE = "visible";

    private static final String FIELD_COMMAND = "command";

    private static final String ACCOUNT_GAMEID = "gameID";

    private static final String ACCOUNT_PLAYERID = "playerID";

    private static final String ACCOUNT_FIRSTCHECKPOINT = "firstCheckpoint";

    private static final String ACCOUNT_SECONDCHECKPOINT = "secondCheckpoint";

    private static final String ACCOUNT_THIRDCHECKPOINT = "thirdCheckpoint";


    private Connector connector;

    Repository(Connector connector) {
        this.connector = connector;
    }

    @Override
    public boolean createGameInDB(Board game) {
        if (game.getGameId() == null) {
            Connection connection = connector.getConnection();
            try {
                connection.setAutoCommit(false);

                PreparedStatement ps = getInsertGameStatementRGK();

                ps.setString(1, "Date: " + new Date());
                ps.setNull(2, Types.TINYINT);
                ps.setInt(3, game.getPhase().ordinal());
                ps.setInt(4, game.getStep());

                int affectedRows = ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (affectedRows == 1 && generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                }
                generatedKeys.close();

                createPlayersInDB(game);

                createCardFieldsInDB(game);

                createAccountsInDB(game);

                ps = getSelectGameStatementU();
                ps.setInt(1, game.getGameId());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                    rs.updateRow();
                }
                rs.close();

                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Some DB error");

                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            System.err.println("Game cannot be created in DB, since it has a game id already!");
        }
        return false;
    }


    @Override
    public boolean updateGameInDB(Board game) {
        assert game.getGameId() != null;

        Connection connection = connector.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, game.getGameId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
                rs.updateInt(GAME_STEP, game.getStep());
                rs.updateRow();
            }
            rs.close();

            updatePlayersInDB(game);

            updateCardFieldsInDB(game);

            updateAccountsInDB(game);

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Some DB error");

            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return false;
    }


    @Override
    public Board loadGameFromDB(int id) {
        Board game;
        try {
            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            int playerNo = -1;
            if (rs.next()) {
                game = LoadBoard.loadBoard(null);
                if (game == null) {
                    return null;
                }
                playerNo = rs.getInt(GAME_CURRENTPLAYER);
                game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
                game.setStep(rs.getInt(GAME_STEP));
            } else {
                return null;
            }
            rs.close();

            game.setGameId(id);
            loadPlayersFromDB(game);
            loadCardFieldsFromDB(game);
            loadAccountsFromDB(game);

            if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
                game.setCurrentPlayer(game.getPlayer(playerNo));
            } else {
                return null;
            }


            return game;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Some DB error");
        }
        return null;
    }

    @Override
    public List<GameInDB> getGames() {
        List<GameInDB> result = new ArrayList<>();
        try {
            PreparedStatement ps = getSelectGameIdsStatement();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(GAME_GAMEID);
                String name = rs.getString(GAME_NAME);
                result.add(new GameInDB(id, name));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void createCardFieldsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectCardFieldStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        for (int i = 0; i < game.getPlayersNumber(); i++) {
            rs.moveToInsertRow();
            Player player = game.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                rs.updateInt(FIELD_GAMEID, game.getGameId());
                rs.updateInt(FIELD_PLAYERID, i);
                rs.updateInt(FIELD_TYPE, FIELD_TYPE_REGISTER);
                rs.updateInt(FIELD_POS, j);
                rs.updateBoolean(FIELD_VISIBLE, player.getProgramField(j).isVisible());
                if (player.getProgramField(j).getCard() != null) {
                    rs.updateInt(FIELD_COMMAND, player.getProgramField(j).getCard().command.ordinal());
                }
                rs.insertRow();
            }
            for (int k = 0; k < Player.NO_CARDS; k++) {
                rs.updateInt(FIELD_GAMEID, game.getGameId());
                rs.updateInt(FIELD_PLAYERID, i);
                rs.updateInt(FIELD_TYPE, FIELD_TYPE_HAND);
                rs.updateInt(FIELD_POS, k);
                rs.updateBoolean(FIELD_VISIBLE, player.getCardField(k).isVisible());
                if (player.getCardField(k).getCard() != null) {
                    rs.updateInt(FIELD_COMMAND, player.getCardField(k).getCard().getCommand().ordinal());
                }
                rs.insertRow();

            }

        }
        rs.close();
    }

    private void createAccountsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectAccountStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        for (int i = 0; i < game.getPlayersNumber(); i++) {
            Player player = game.getPlayer(i);
            rs.moveToInsertRow();
            rs.updateInt(ACCOUNT_GAMEID, game.getGameId());
            rs.updateInt(ACCOUNT_PLAYERID, i);
            rs.updateBoolean(ACCOUNT_FIRSTCHECKPOINT, player.getAccount().isFirstCheckPoint());
            rs.updateBoolean(ACCOUNT_SECONDCHECKPOINT, player.getAccount().isSecondCheckPoint());
            rs.updateBoolean(ACCOUNT_THIRDCHECKPOINT, player.getAccount().isThirdCheckPoint());
            rs.insertRow();
        }
        rs.close();
    }


    private void createPlayersInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        for (int i = 0; i < game.getPlayersNumber(); i++) {
            Player player = game.getPlayer(i);
            rs.moveToInsertRow();
            rs.updateInt(PLAYER_GAMEID, game.getGameId());
            rs.updateInt(PLAYER_PLAYERID, i);
            rs.updateString(PLAYER_NAME, player.getName());
            rs.updateString(PLAYER_COLOUR, player.getColor());
            rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
            rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
            rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
            rs.insertRow();
        }

        rs.close();
    }


    private void loadPlayersFromDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersASCStatement();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            if (i++ == playerId) {
                String name = rs.getString(PLAYER_NAME);
                String colour = rs.getString(PLAYER_COLOUR);
                Player player = new Player(game, colour, name, playerId, new Account());
                game.addPlayer(player);

                int x = rs.getInt(PLAYER_POSITION_X);
                int y = rs.getInt(PLAYER_POSITION_Y);
                player.setSpace(game.getSpace(x, y));
                int heading = rs.getInt(PLAYER_HEADING);
                player.setHeading(Heading.values()[heading]);
            } else {
                System.err.println("Game in DB does not have a player with id " + i + "!");
            }
        }
        rs.close();
    }

    private void loadAccountsFromDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectAccountStatement();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(ACCOUNT_PLAYERID);
            Player player = game.getPlayer(playerId);
            boolean firstcheckpoint = rs.getBoolean(ACCOUNT_FIRSTCHECKPOINT);
            boolean secondcheckpoint = rs.getBoolean(ACCOUNT_SECONDCHECKPOINT);
            boolean thirdcheckpoint = rs.getBoolean(ACCOUNT_THIRDCHECKPOINT);
            if (firstcheckpoint == false) {
                player.getAccount().setFirstCheckPoint(0);
            } else if (firstcheckpoint == true) {
                player.getAccount().setFirstCheckPoint(1);
            }
            if (secondcheckpoint == false) {
                player.getAccount().setSecondCheckPoint(0);
            } else if (secondcheckpoint) {
                player.getAccount().setSecondCheckPoint(1);
            }
            if (thirdcheckpoint == false) {
                player.getAccount().setThirdCheckPoint(0);
            } else if (thirdcheckpoint == true) {
                player.getAccount().setThirdCheckPoint(1);
            }
        }
        rs.close();
    }

    private void loadCardFieldsFromDB(Board game) throws SQLException {

        PreparedStatement ps = getSelectCardFieldStatement();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(FIELD_PLAYERID);
            Player player = game.getPlayer(playerId);
            int type = rs.getInt(FIELD_TYPE);
            int pos = rs.getInt(FIELD_POS);
            CommandCardField field;
            if (type == FIELD_TYPE_REGISTER) {
                field = player.getProgramField(pos);
            } else if (type == FIELD_TYPE_HAND) {
                field = player.getCardField(pos);
            } else {
                field = null;
            }
            if (field != null) {
                field.setVisible(rs.getBoolean(FIELD_VISIBLE));
                Object c = rs.getObject(FIELD_COMMAND);
                if (c != null) {
                    Command card = values()[rs.getInt(FIELD_COMMAND)];
                    field.setCard(new CommandCard(card));
                } else {
                    field.setCard(null);
                }

            }
        }
        rs.close();
    }

    private void updateCardFieldsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectCardFieldStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(FIELD_PLAYERID);
            Player player = game.getPlayer(playerId);
            int cardType = rs.getInt(FIELD_TYPE);
            int pos = rs.getInt(FIELD_POS);
            CommandCardField field;

            if (cardType == FIELD_TYPE_HAND) {
                field = player.getCardField(pos);
            } else if (cardType == FIELD_TYPE_REGISTER) {
                field = player.getProgramField(pos);
            } else {
                field = null;
            }
            if (field != null) {
                rs.updateBoolean(FIELD_VISIBLE, field.isVisible());
                if (field.getCard() == null) {
                    rs.updateNull(FIELD_COMMAND);
                } else {
                    rs.updateInt(FIELD_COMMAND, field.getCard().getCommand().ordinal());
                }
            }
            rs.updateRow();
        }
        rs.close();
    }

    private void updateAccountsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectAccountStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            Player player = game.getPlayer(playerId);
            rs.updateBoolean(ACCOUNT_FIRSTCHECKPOINT, player.getAccount().isFirstCheckPoint());
            rs.updateBoolean(ACCOUNT_SECONDCHECKPOINT, player.getAccount().isSecondCheckPoint());
            rs.updateBoolean(ACCOUNT_THIRDCHECKPOINT, player.getAccount().isThirdCheckPoint());
            rs.updateRow();
        }
        rs.close();
    }


    private void updatePlayersInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            Player player = game.getPlayer(playerId);
            rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
            rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
            rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
            rs.updateRow();
        }
        rs.close();
    }

    private static final String SQL_INSERT_GAME =
            "INSERT INTO Game(name, currentPlayer, phase, step) VALUES (?, ?, ?, ?)";

    private PreparedStatement insert_game_stmt = null;

    private PreparedStatement getInsertGameStatementRGK() {
        if (insert_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                insert_game_stmt = connection.prepareStatement(
                        SQL_INSERT_GAME,
                        Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return insert_game_stmt;
    }

    private static final String SQL_SELECT_GAME =
            "SELECT * FROM Game WHERE gameID = ?";

    private PreparedStatement select_game_stmt = null;

    private PreparedStatement getSelectGameStatementU() {
        if (select_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_game_stmt = connection.prepareStatement(
                        SQL_SELECT_GAME,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_game_stmt;
    }

    private static final String SQL_SELECT_PLAYERS =
            "SELECT * FROM Player WHERE gameID = ?";

    private PreparedStatement select_players_stmt = null;

    private PreparedStatement getSelectPlayersStatementU() {
        if (select_players_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_players_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_players_stmt;
    }

    private static final String SQL_SELECT_ACCOUNTS = "SELECT * FROM Account WHERE gameID = ?";

    private PreparedStatement select_accounts_stmt = null;

    private PreparedStatement getSelectAccountStatement() {
        if (select_accounts_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_accounts_stmt = connection.prepareStatement(SQL_SELECT_ACCOUNTS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_accounts_stmt;
    }

    private PreparedStatement select_accounts_stmt_u = null;

    private PreparedStatement getSelectAccountStatementU() {
        if (select_accounts_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_accounts_stmt_u = connection.prepareStatement(
                        SQL_SELECT_ACCOUNTS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_accounts_stmt_u;
    }

    private static final String SQL_SELECT_CARD_FIELDS = "SELECT * FROM CardField WHERE gameID = ?";

    private PreparedStatement select_card_field_stmt = null;

    private PreparedStatement getSelectCardFieldStatement() {
        if (select_card_field_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_card_field_stmt = connection.prepareStatement(SQL_SELECT_CARD_FIELDS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_card_field_stmt;
    }

    private PreparedStatement select_card_field_stmt_u = null;

    private PreparedStatement getSelectCardFieldStatementU() {
        if (select_card_field_stmt_u == null) {
            Connection connection = connector.getConnection();
            try {
                select_card_field_stmt_u = connection.prepareStatement(
                        SQL_SELECT_CARD_FIELDS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_card_field_stmt_u;
    }


    private static final String SQL_SELECT_PLAYERS_ASC =
            "SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";

    private PreparedStatement select_players_asc_stmt = null;

    private PreparedStatement getSelectPlayersASCStatement() {
        if (select_players_asc_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_players_asc_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS_ASC);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_players_asc_stmt;
    }

    private static final String SQL_SELECT_GAMES =
            "SELECT gameID, name FROM Game";

    private PreparedStatement select_games_stmt = null;

    private PreparedStatement getSelectGameIdsStatement() {
        if (select_games_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_games_stmt = connection.prepareStatement(
                        SQL_SELECT_GAMES);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_games_stmt;
    }
}
