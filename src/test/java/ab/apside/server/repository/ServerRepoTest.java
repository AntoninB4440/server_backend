package ab.apside.server.repository;
import ab.apside.server.model.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static ab.apside.server.enumeration.Status.SERVER_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServerRepoTest {

    @Autowired
    private ServerRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindServerByIpAddress() {
        //given
        String ipAddress = "192.168.1.161";
        Server server = new Server(
                null,
                ipAddress,
                "Ubuntu Linux",
                "16 GB",
                "Personal PC",
                "http://localhost:8080/server/image/server1.png",
                SERVER_UP
        );
        underTest.save(server);
        //when
        Server serverFound = underTest.findByIpAddress(ipAddress);
        //then
        assertThat(serverFound.getIpAddress()).isEqualTo("192.168.1.161");
    }

    @Test
    void itShouldNotFindServerByIpAddress() {
        //given
        String ipAddress = "192.168.1.161";

        //when
        Server serverFound = underTest.findByIpAddress(ipAddress);
        //then
        assertThat(serverFound).isNull();
    }
}