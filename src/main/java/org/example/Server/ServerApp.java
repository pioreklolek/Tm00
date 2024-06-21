package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp{
    public static void main(String[] args){
        int port = 2345;
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server nasluchuje na porcie : " + port);
            while(true){
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
