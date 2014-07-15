package com.suissoft.wallaby.controller.action;

import javafx.event.ActionEvent;
import javafx.print.PrinterJob;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.suissoft.wallaby.application.WallabyApplication;

@Singleton
@Named(PrintAction.NAME)
public class PrintAction extends WallabyAction {
	
	public static final String NAME = "print";
	
	@Inject
	private WallabyApplication application;
	
	public PrintAction() {
		super("Print");
	}
	@Override
	public void execute(ActionEvent ae) {
		final PrinterJob printerJob = PrinterJob.createPrinterJob();
		if (printerJob.showPrintDialog(null)) {
			if (printerJob.printPage(application.getRootNode())) {
				printerJob.endJob();
			}
		}
	}
}
