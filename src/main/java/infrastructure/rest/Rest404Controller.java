package infrastructure.rest;

import infrastructure.anotation.rest.RestDelete;
import infrastructure.anotation.rest.RestEndpoint;
import infrastructure.anotation.rest.RestGetAll;
import infrastructure.anotation.rest.RestGetById;
import infrastructure.anotation.rest.RestPut;
import infrastructure.anotation.rest.RestUpdate;

@RestEndpoint(resource = "/page404")
public class Rest404Controller {

    @RestGetAll
    public String getAll() {
        return "404 endpoint not found";
    }

}
