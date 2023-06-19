package testingFunctional.crud.rest;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.rest.*;
import testingFunctional.crud.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@NeedConfig
@RestEndpoint(resource = "/user/{user_id}/book/{bookId}")//in calling program need to be on start /rest todo make this prefix configurable
@Singleton
public class UserRestControllerWithTwoParameters {

    @InjectByType
    UserService userService;

    @RestGetAll
    public String doGet() {
        return "all books  for user call";
    }

    @RestGetById
    public String getBookForUserById(@RequestParam(paramName = "user_id") int userId,
                                     @RequestParam(paramName = "bookId") int bookId,
                                     HttpServletRequest request, HttpServletResponse response) {
        return "user id is " + userId + "bookId is " + bookId + "successful inserted request "
                + request.getRequestURI() + "and response " + response.getLocale().toString();
    }

    @RestPut
    public String doPut(@RequestParam(paramName = "user_id") int userId,
                        @RequestParam(paramName = "bookId") int bookId) {
        return "user id is " + userId + "bookId is " + bookId;

    }

    @RestUpdate
    public String doPost(@RequestParam(paramName = "user_id") int userId,
                         @RequestParam(paramName = "bookId") int bookId,
                         @RequestBody UserDto userDto) {
        return "user id is " + userId + "bookId is " + bookId + "successful inserted request body " + userDto;

    }

    @RestDelete
    public String doDelete(@RequestParam(paramName = "user_id") int userId,
                           @RequestParam(paramName = "bookId") int bookId) {
        return "user id is " + userId + "bookId is " + bookId;
    }
}
