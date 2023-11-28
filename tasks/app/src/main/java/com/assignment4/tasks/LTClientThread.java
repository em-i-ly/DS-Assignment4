package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class LTClientThread implements Runnable {

    private final DatagramSocket clientSocket;
    LamportTimestamp lc;

    byte[] receiveData = new byte[1024];

    public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
        this.clientSocket = clientSocket;
        this.lc = lc;
    }

    @Override
    public void run() {
        try {
            // receive response from server
            DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivedPacket);
            String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength()).trim();

            // parse received message
            String[] receivedMessage = response.split(":");
            String receivedMessageBody = receivedMessage[0];

            // print message and timestamp
            System.out.println("Server:" + receivedMessageBody + ":" + lc.getCurrentTimestamp());

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
