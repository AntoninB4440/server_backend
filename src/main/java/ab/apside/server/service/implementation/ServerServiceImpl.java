package ab.apside.server.service.implementation;

import ab.apside.server.model.Server;
import ab.apside.server.repository.ServerRepo;
import ab.apside.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static ab.apside.server.enumeration.Status.SERVER_DOWN;
import static ab.apside.server.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.*;
import static org.springframework.data.domain.PageRequest.*;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepo serverRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public Server create(Server server) {
        log.info("Saving new server : {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging new server IP : {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all server");
        return serverRepo.findAll(of(0,limit)).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Server get(Long id) {
        log.info("Fetching server by id : {}", id);
        return serverRepo.findById(id).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Server update(Server server) {
        log.info("Updating new server : {}", server.getName());
        return serverRepo.save(server);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID : {}", id);
        serverRepo.deleteById(id);
        return TRUE;
    }

    /**
     * @return the URI of the image to set to the server
     */
    private String setServerImageUrl() {
        String[] imagesNames = {"server1.png","server2.png","server3.png","server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imagesNames[new Random().nextInt(4)]).toUriString();
    }
}
