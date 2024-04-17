package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "playedGames", target = "playedGames")
    @Mapping(source = "wonGames", target = "wonGames")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creation_date", target = "creation_date")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "gameCode", target = "gameCode")
    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "playerCount", target = "playerCount")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    @Mapping(source = "roundCount", target = "roundCount")
    @Mapping(source = "gameType", target = "gameType")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "cities", target = "cities")
    GameInfoDTO convertEntityToGameInfoDTO(Game game);

}
