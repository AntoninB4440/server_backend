package ab.apside.server.resource;

import ab.apside.server.model.Response;
import ab.apside.server.model.Server;
import ab.apside.server.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static ab.apside.server.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImpl serverService;

    /**
     * Retrieve the server list find in the database with a limit of 30 server when hitting the url "/server/list"
     * @return A response including the list of server with a limited size (30)
     */
    @GetMapping("/list")
    public ResponseEntity<Response> getServers() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("servers", serverService.list(10)))
                        .message("Servers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Ping a server by his ipAddress and retrieve it when hitting the url "/server/ping/$ipAddress"
     * @param ipAddress Server's Ip address which need to be pinged
     * @return A response including the server pinged
     * @throws IOException
     */
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", server))
                        .message(server.getStatus() == SERVER_UP ? "Ping success" : "Ping failed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Find and return in a response the server find by id in the database when hitting the url "/server/get/$id"
     * @param id which identify the server in the database
     * @return the server find from the id
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        Server server = serverService.get(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", server))
                        .message("Server retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Create and save a server from the request body when hitting the url "/server/save"
     * @param server server we want to save in database
     * @return the server created
     */
    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", serverService.create(server)))
                        .message("Server created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", serverService.update(server)))
                        .message("Server updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Find and delete the server find by id in the database when hitting the url "/server/delete/$id"
     * @param id which identify the server in the database
     * @return true or false depend on the request result
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deleted", serverService.delete(id)))
                        .message("Server deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    /**
     * Find and return the file from the server/images folder on D disk when hitting the url "server/iamges/$filename"
     * @param fileName Name of the file to retrieve
     * @return byte of the image found
     * @throws IOException
     */
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get("D:/images/server/"+ fileName));
    }
}
