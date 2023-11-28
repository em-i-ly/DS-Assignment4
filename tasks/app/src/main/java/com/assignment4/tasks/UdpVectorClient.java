package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;


public class UdpVectorClient {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Enter your id (1 to 3): ");
        Scanner id_input = new Scanner(System.in);
        int id = id_input.nextInt();

        // prepare the client socket
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // initialize the buffers
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        int port = 4040;
        List<String> logs;

        int startTime = 0;
        VectorClock vcl = new VectorClock(4);
        vcl.setVectorClock(id, startTime);

        //ask for user input aka message to the server
        System.out.println(id+": Enter any message:");
        Scanner input = new Scanner(System.in);

        while(true) {
            String messageBody = input.nextLine();
            // increment clock
            if (!messageBody.isEmpty()){
                vcl.tick(id);
            }
            HashMap<Integer, Integer> messageTime = new HashMap<>();
            messageTime.put(id,vcl.getCurrentTimestamp(id));
            Message msg = new Message(messageBody, messageTime);
            vcl.tick(id);
            String responseMessage = msg.content + ':' + msg.messageTime;

            // check if the user wants to quit
            if(messageBody.equals("quit")){
                clientSocket.close();
                System.exit(1);
            }

            // send the message to the server
            sendData = responseMessage.getBytes();
            DatagramPacket messageToSend = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(messageToSend);

            // check if the user wants to see the history
            if(messageBody.equals("history")) {
                System.out.println("Receiving the chat history...");
                logs = new ArrayList<>();
                clientSocket.setSoTimeout(3000);

                // use a while loop to receive all messages being sent
                while (true) {
                    try {
                        DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivedPacket);
                        String receivedData = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                        logs.add(receivedData); // add the messages received to arraylist to process later
                    } catch (IOException e){
                        break; // break the loop when an error is encountered, ie no more messages to receive
                    }
                }

                UdpVectorClient uc = new UdpVectorClient();
                uc.showHistory(logs); // gives out all the unsorted logs stored at the server
                uc.showSortedHistory(logs); // shows sorted logs
            }
            else
            {
                VectorClientThread client;
                client = new VectorClientThread(clientSocket, vcl, id);
                Thread receiverThread = new Thread(client);
                receiverThread.start();
            }
        }
    }
    public void showHistory(List<String> logs){

        // prints the unsorted logs (history) coming form the server
        for (String message : logs) {
            System.out.println(message);
        }
    }
    public void showSortedHistory(List<String> logs){

        // prints sorted logs (history) received
        System.out.println("Print sorted conversation using attached vector clocks");
        Map<int[], String> logMap = new HashMap<>();
        // iterate through logs
        for (String log:logs) {
            String[] responseMessageArray = log.split(":");
            // filter message
            String[] receivedValues = responseMessageArray[1].replaceAll("\\p{Punct}", " ").trim().split("\\s+");
            // make copy int array for string array
            int [] values = new int [receivedValues.length];
            // iterate through values to arse them individually
            for (int i = 0; i < receivedValues.length; i++) {
                values[i] = Integer.parseInt(receivedValues[i]);
            }
            // put parsed values and corresponding message in map
            logMap.put(values, responseMessageArray[0]);
        }
        // custom comparator that sorts based on first element in map
        Comparator<int[]> customComparator = Comparator.comparingInt(o -> o[0]);

        // sort the logs based on the vector clocks using the custom comparator
        List<Map.Entry<int[], String>> sortedLogs = logMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(customComparator))
                .collect(Collectors.toList());

        // create a LinkedHashMap to store the sorted logs
        LinkedHashMap<int[], String> sortedLogMap = new LinkedHashMap<>();
        sortedLogs.forEach(entry -> sortedLogMap.put(entry.getKey(), entry.getValue()));
        sortedLogMap.forEach((clockArray, log) -> System.out.println(Arrays.toString(clockArray) + " " + log));

    }
}
