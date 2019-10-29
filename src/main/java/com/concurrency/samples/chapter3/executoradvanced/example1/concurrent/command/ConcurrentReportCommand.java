package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command;

import com.concurrency.samples.chapter3.executoradvanced.example1.wdi.data.WDIDAO;

import java.net.Socket;


/**
 * Class that implements the concurrent version of the Report command. 
 * Report: The format of this query is: r:codIndicator where codIndicator 
 * is the code of the indicator you want to report
 *
 */
public class ConcurrentReportCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentReportCommand (Socket socket, String [] command) {
		super(socket, command);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
	
		WDIDAO dao=WDIDAO.getDAO();
		return dao.report(command[3]);
	}

}
