package com.concurrency.samples.chapter3.executoradvanced.example1.serial.client;

import com.concurrency.samples.chapter3.executoradvanced.example1.common.Constants;
import com.concurrency.samples.chapter3.executoradvanced.example1.wdi.data.WDI;
import com.concurrency.samples.chapter3.executoradvanced.example1.wdi.data.WDIDAO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Class that implements a client of the serial server. It implements the Runnable interface.
 * It will be executed as a Thread
 * @author author
 *
 */
public class SerialClient implements Runnable{

	public void run() {

		WDIDAO dao = WDIDAO.getDAO();
		List<WDI> data = dao.getData();
		Date globalStart, globalEnd;
		Random randomGenerator = new Random();

		globalStart = new Date();

		for (int i = 0; i < 10; i++) {

			for (int j = 0; j < 9; j++) {
				try (Socket echoSocket = new Socket("localhost",
						Constants.SERIAL_PORT);
					 PrintWriter out = new PrintWriter(
								echoSocket.getOutputStream(), true);
					 BufferedReader in = new BufferedReader(
								new InputStreamReader(
										echoSocket.getInputStream()));
					 BufferedReader stdIn = new BufferedReader(
								new InputStreamReader(System.in))) {
					WDI wdi = data.get(randomGenerator.nextInt(data.size()));

					StringWriter writer = new StringWriter();
					writer.write("q");
					writer.write(";");
					writer.write(wdi.getCountryCode());
					writer.write(";");
					writer.write(wdi.getIndicatorCode());

					String command = writer.toString();
					out.println(command);
					String output = in.readLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try (Socket echoSocket = new Socket("localhost",
					Constants.SERIAL_PORT);
					PrintWriter out = new PrintWriter(
							echoSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(echoSocket.getInputStream()));
					BufferedReader stdIn = new BufferedReader(
							new InputStreamReader(System.in))) {
				WDI wdi = data.get(randomGenerator.nextInt(data.size()));

				StringWriter writer = new StringWriter();
				writer.write("r");
				writer.write(";");
				writer.write(wdi.getIndicatorCode());

				String command = writer.toString();
				out.println(command);
				String output = in.readLine();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		globalEnd = new Date();
		System.out.println("Total Time: "
				+ ((globalEnd.getTime() - globalStart.getTime()) / 1000)
				+ " seconds.");

	}

}
