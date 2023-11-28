package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VectorClientThread implements Runnable {

    private final DatagramSocket clientSocket;
    VectorClock vcl;
    byte[] receiveData = new byte[1024];

    int id;
    public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {

        this.clientSocket = clientSocket;
        this.vcl = vcl;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // receive messages from server
            DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivedPacket);
            String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            String[] responseMessageArray = response.split(":");

            // filter received message timestamps
            String[] receivedValues = responseMessageArray[1].replaceAll("\\p{Punct}", " ").trim().split("\\s+");
            // parse the values
            int processId = Integer.parseInt(receivedValues[0]);
            int time = Integer.parseInt(receivedValues[1]);

            // update and increment local clock
            VectorClock vectorClock = new VectorClock(4);
            vectorClock.setVectorClock(processId, time);
            vcl.updateClock(vectorClock);

            // print message & vector timestamp
            System.out.println("Server:" + responseMessageArray[0] + " " + vcl.showClock());

        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
