package com.suissoft.wallaby.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.inject.Inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.suissoft.model.inject.guice.EntityManagerModule;
import com.suissoft.model.util.PersistenceUnit;
import com.suissoft.wallaby.controller.action.QuitAction;
import com.suissoft.wallaby.controller.action.SelectViewAction;
import com.suissoft.wallaby.inject.guice.FxmlModule;

public class WallabyApplication extends Application {
	
	public static final String ROOT_NODE = "rootNode";
	
	private Stage stage;
	private Scene scene;
	private Parent rootNode;
	
	@Inject
	private QuitAction quitAction;
	@Inject
	private SelectViewAction.NaturalPersonView viewAction;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/Application.fxml"));
		rootNode = fxmlLoader.load();
		scene = new Scene(rootNode);
		stage.setTitle("Suissoft Wallaby");
		stage.setScene(scene);
		
		injectDependencies(fxmlLoader.getController());
		initializeWindow();

		stage.show();
	}
	
	private void initializeWindow() {
		stage.onHiddenProperty().set(evt -> {quitAction.handle(null);});
		stage.onShowingProperty().set(evt -> {viewAction.handle(null);});
	}
	
	private void injectDependencies(Object controller) {
		final EntityManagerModule entityManagerModule = new EntityManagerModule(PersistenceUnit.H2_MEMORY);
		final FxmlModule fxmlModule = new FxmlModule(controller);
		final ApplicationModule applicationModule = new ApplicationModule();
		final Injector injector = Guice.createInjector(entityManagerModule, fxmlModule, applicationModule);
		injector.injectMembers(this);
		fxmlModule.injectFxmlMembers(injector);
	}
	private class ApplicationModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(WallabyApplication.class).toInstance(WallabyApplication.this);
			bind(Application.class).toInstance(WallabyApplication.this);
			bind(Stage.class).toInstance(stage);
			bind(Window.class).toInstance(stage);
			bind(Scene.class).toInstance(scene);
		}
	}
	public Parent getRootNode() {
		return rootNode;
	};
}
