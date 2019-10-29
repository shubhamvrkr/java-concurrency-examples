package com.concurrency.samples.chapter3.executoradvanced.example1.serial.command;


import com.concurrency.samples.chapter3.executoradvanced.example1.common.Command;
import com.concurrency.samples.chapter3.executoradvanced.example1.wdi.data.WDIDAO;

/**
 * Class that implements the serial version of the Report command. 
 * Report: The format of this query is: r:codIndicator where codIndicator 
 * is the code of the indicator you want to report
 * @author author
 *
 */
public class ReportCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ReportCommand (String [] command) {
		super(command);
	}
	
	@Override
	/**
	 * Method that executes the command
	 */
	public String execute() {
	
		WDIDAO dao= WDIDAO.getDAO();
		return dao.report(command[1]);
	}

}
