package com.suissoft.wallaby.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;

public class ToolBarController {
	
	@FXML
	public void onPrint(ActionEvent evt) {
		final Node node = (Node)evt.getSource();
		final PrinterJob printerJob = PrinterJob.createPrinterJob();
		if (printerJob.showPrintDialog(null)) {
			if (printerJob.printPage(node.getScene().getRoot())) {
				printerJob.endJob();
			}
		}
	};
	
}