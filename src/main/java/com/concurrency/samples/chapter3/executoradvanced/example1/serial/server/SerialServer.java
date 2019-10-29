package com.concurrency.samples.chapter3.executoradvanced.example1.serial.server;

import com.concurrency.samples.chapter3.executoradvanced.example1.common.Command;
import com.concurrency.samples.chapter3.executoradvanced.example1.common.Constants;
import com.concurrency.samples.chapter3.executoradvanced.example1.serial.command.ErrorCommand;
import com.concurrency.samples.chapter3.executoradvanced.example1.serial.command.QueryCommand;
import com.concurrency.samples.chapter3.executoradvanced.example1.serial.command.ReportCommand;
import com.concurrency.samples.chapter3.executoradvanced.example1.serial.command.StopCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that implements the serial server.
 *
 * Few important methods one can override from executor:
 * 1 - beforeExecute()
 * 2 - afterExceute()
 * 3 - newTaskFor()
 *
 * changing initialization parameters
 * 1 - BlockingQueue<Runnable> - Storing pending tasks for executors, for changing order of execution of task
 * 2 - ThreadFactory - executor will use this factory to create threads that will execute the task.
 * 3 - RejectedExecutionHandler - after calling shutdown, all the threads sent to the executor will be rejected.
 * Override this to handle this situation of rejected task
 */
public class SerialServer {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        boolean stopServer = false;
        System.out.println("Initialization completed.");

        try {
            serverSocket = new ServerSocket(Constants.SERIAL_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        do {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(
                         clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));) {

                //read the unmarshalled command from the user
                String line = in.readLine();
                Command command;
                //marshall the command
                String[] commandData = line.split(";");
                System.out.println("Command: " + commandData[0]);
                if (commandData[0].equals("q")) {

                    System.out.println("Query");
                    command = new QueryCommand(commandData);
                } else if (commandData[0].equals("r")) {
                    System.out.println("Report");
                    command = new ReportCommand(commandData);
                } else if (commandData[0].equals("z")) {
                    System.out.println("Stop");
                    command = new StopCommand(commandData);
                    stopServer = true;
                } else {
                    System.out.println("Error");
                    command = new ErrorCommand(commandData);
                }
                //process the command
                String response = command.execute();
                System.out.println(response);
                //send response to the client
                out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!stopServer);

        /**
         * Bottlenecks
         * 1 - one thread can accept new connection and put in to pool
         * 2 - one thread can read the data on connection, marshall and send the data to custom executor framerwork to process
         */
    }

}
