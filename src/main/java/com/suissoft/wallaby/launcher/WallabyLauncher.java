package com.suissoft.wallaby.launcher;

import javafx.application.Application;

import com.suissoft.wallaby.application.WallabyApplication;

public class WallabyLauncher {
	public static void main(String[] args) {
		try {
			Application.launch(WallabyApplication.class, args);
		} catch (Exception e) {
			System.err.println("Uncaught exception: e=" + e);
			e.printStackTrace();
		}
	}
}
