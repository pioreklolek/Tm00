package org.example.clientapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj nazwę użytkownika: ");
        String username = scanner.nextLine();

        System.out.print("Podaj ścieżkę do pliku CSV (np. tm00.csv): ");
        String filePath = scanner.nextLine();

        scanner.close();

        sendData(username, filePath);
    }

    public static void sendData(String username, String filePath) {
        try (Socket socket = new Socket("localhost", 12345);  // przykładowe IP i port
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader fileReader = new BufferedReader(new FileReader(new File(filePath)))) {

            out.println(username);  // wysyłamy nazwę użytkownika

            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);  // wysyłamy każdą linię pliku CSV
                Thread.sleep(2000);  // czekamy 2 sekundy
            }

            out.println("bye");  // informujemy o zakończeniu przesyłania

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
