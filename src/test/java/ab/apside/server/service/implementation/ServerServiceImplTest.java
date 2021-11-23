package ab.apside.server.service.implementation;

import ab.apside.server.model.Server;
import ab.apside.server.repository.ServerRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Collection;

import static ab.apside.server.enumeration.Status.SERVER_UP;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ServerServiceImplTest {

    @Mock
    private ServerRepo serverRepository;
    private ServerServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ServerServiceImpl(serverRepository);
    }


    @Test
    void canCreateAndSaveAServer() {
        //given
        Server server = new Server(null,"10.1.44.68","Ubuntu Linux","16 GB","Personal PC",
                "http://localhost:8080/server/image/server1.png",
                SERVER_UP
        );
        //when
        underTest.create(server);
        //then
        ArgumentCaptor<Server> serverArgumentCaptor = ArgumentCaptor.forClass(Server.class);

        verify(serverRepository).save(serverArgumentCaptor.capture());

        Server capturedSever = serverArgumentCaptor.getValue();
        assertThat(capturedSever).isEqualTo(server);
    }

    @Test
    void canPingServerFromIsIpAddress() throws IOException {
        //given
        Server server = new Server(null,"10.1.44.68","Ubuntu Linux","16 GB","Personal PC",
                "http://localhost:8080/server/image/server1.png",
                SERVER_UP
        );
        //when
        when(serverRepository.findByIpAddress(anyString())).thenReturn(server);
        //then
        Server expected = underTest.ping("10.1.44.68");
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(serverRepository).findByIpAddress(stringArgumentCaptor.capture());

        String capturedString = stringArgumentCaptor.getValue();

        assertThat(capturedString).isEqualTo(server.getIpAddress());
        assertThat(server.getIpAddress()).isEqualTo(expected.getIpAddress());

    }

    @Test
    void canListAllTheServers() {
        //When
        when(serverRepository.findAll(PageRequest.of(0,10))).thenReturn(Page.empty());
        Collection<Server> servers = underTest.list(10);
        //then
        assertThat(servers).isNotNull();
    }
}