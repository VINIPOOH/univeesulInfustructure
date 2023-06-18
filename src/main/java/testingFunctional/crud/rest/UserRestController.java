package testingFunctional.crud.rest;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.rest.*;
import testingFunctional.crud.model.UserModel;
import testingFunctional.crud.services.UserService;

import javax.servlet.http.HttpServletRequest;

@NeedConfig
@RestEndpoint(resource = "/user/{user_id}")
public class UserRestController {

    @InjectByType
    UserService userService;

    @RestGetAll
    public UserDto getAllUsers() {
        return new UserDto(userService.getAllUsers());
    }

    @RestGetById
    public UserDto getUserById(@RequestParam(paramName = "user_id") int userId){
        return new UserDto(userService.getUserById(userId));
    }

    @RestPut
    public UserDto doPut(@RequestParam(paramName = "user_id") int userId) {
        return new UserDto(userService.createUser(userId));
    }

    @RestUpdate
    public UserDto doPost(@RequestParam(paramName = "user_id") int userId, @RequestBody UserDto userDto) {
        return new UserDto(userService.updateUserName(userId, userDto.getName()));
    }

    @RestDelete
    public UserDto doDelete(@RequestParam(paramName = "user_id") int userId) {
        return new UserDto(userService.deleteUserById(userId));
    }
}
