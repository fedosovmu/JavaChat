package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger("com.company.Client");

    public static void main(String[] args)
    {
        new Client();
    }
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public Client() {

        Scanner scan = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 13);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Enter your name:");
            out.println(scan.nextLine());

            Resender resend = new Resender();
            resend.start();
            logger.info("Connected");

            String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
            logger.info("Disconnected");
        }
    }


    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Error threads were note closed!");
            logger.info("Error threads were note closed!");
        }
    }


    private class Resender extends Thread {

        private boolean stoped;

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.err.println("Error retrieving the message!");
                logger.info("Error retrieving the message!");
                e.printStackTrace();
            }
        }
    }
}
