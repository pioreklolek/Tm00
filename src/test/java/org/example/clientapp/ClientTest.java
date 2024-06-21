package org.example.clientapp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClientTest {

    private static Stream<Arguments> provideTestData() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader("src/test/resources/testdata/test.csv"))) {
            List<String[]> lines = reader.readAll();
            return lines.stream().map(line -> Arguments.of(line[0], line[1], line[2]));
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testSendData(String username, String filepath, String expectedBase64Image) {
        // Mocking the server response
        ServerMock serverMock = mock(ServerMock.class);

        // Creating the client instance with mocked server
        Client client = new Client(serverMock);

        // Sending data
        client.sendData(username, filepath);

        // Verify that server received the expected base64 image
        verify(serverMock, times(1)).receiveData(eq(username), anyString(), eq(expectedBase64Image));
    }
}

interface ServerMock {
    void receiveData(String username, String electrodeLine, String base64Image);
}

class Client {
    private ServerMock server;

    public Client(ServerMock server) {
        this.server = server;
    }

    public void sendData(String name, String filepath) {
        // Implementation to read file and send data to server
        // This should include reading the file and sending data to the mocked server
    }
}
