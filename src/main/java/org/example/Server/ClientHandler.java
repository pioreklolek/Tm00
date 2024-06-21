package org.example.Server;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.BitmapEncoder;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private Connection connection;

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
        String url = "jdbc:sqlite:eegdata.db";

        try{
            connection = DriverManager.getConnection(url);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        try(BufferedReader  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out =  new PrintWriter(clientSocket.getOutputStream(), true)){

            String username = in.readLine();
            String line;
            int electrodeNumber = 1;
            while((line = in.readLine()) != null){
                if(line.equals("BYE")){
                    break;
                }
                List<Double>eeData = parseCSVLine(line);
                String base64Graph = createGraph(eeData);

                saveDataToBase(username, electrodeNumber, base64Graph);
                electrodeNumber++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(connection != null){
                    connection.close();
                }
                clientSocket.close();
            }catch (IOException | SQLException e){
                e.printStackTrace();
            }
        }
    }
    private List<Double> parseCSVLine(String line) {
        return List.of(line.split(",")).stream()
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
    private String createGraph(List<Double> data) {
        XYChart chart = new XYChartBuilder().width(600).height(400).build();
        chart.addSeries("EEG Data", data).setMarker(SeriesMarkers.NONE);
        try{
            byte[] imageBytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
            return Base64.getEncoder().encodeToString(imageBytes);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    private void saveDataToBase(String username, int electrodeNumber, String base64Graph){
        String sql = "INSERT INTO eeg_data(username, electrode_number, base64_graph) VALUES(?,?,?)";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1,username);
            pstmt.setInt(2, electrodeNumber);
        pstmt.setString(3,base64Graph);
        pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
