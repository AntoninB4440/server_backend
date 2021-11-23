package ab.apside.server.service;

import ab.apside.server.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    /**
     * Save a server in the database
     * @param server which need to be saved
     * @return server saved
     */
    Server create(Server server);

    /**
     * Ping a server to see if he is up or down
     * @param ipAddress IpAddress where is hosted the server to ping
     * @return the server pinged
     * @throws IOException
     */
    Server ping(String ipAddress) throws IOException;

    /**
     * Find and return all the server with a limit to show (ex : show only 10 servers)
     * @param limit limit of server to show off
     * @return the limited list of server found
     */
    Collection<Server> list(int limit);

    /**
     * Find by id a server and return it
     * @param id id of the server to find
     * @return the server found
     */
    Server get(Long id);

    /**
     * Update and save a server
     * @param server who need to be updated
     * @return the server updated
     */
    Server update(Server server);

    /**
     * Find and Delete a server by is id
     * @param id server's id to delete
     * @return true if the server has been deleted
     */
    Boolean delete(Long id);

}
