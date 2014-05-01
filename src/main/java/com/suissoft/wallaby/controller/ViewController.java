package com.suissoft.wallaby.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import javax.inject.Named;

import com.suissoft.model.dao.Dao;
import com.suissoft.model.entity.Entity;
import com.suissoft.wallaby.application.WallabyApplication;
import com.suissoft.wallaby.model.ApplicationModel;
import com.suissoft.wallaby.model.DaoManager;
import com.suissoft.wallaby.model.ModelBinder;
import com.suissoft.wallaby.model.ViewSpec;

public class ViewController {
	
	@FXML
	private TreeViewController treeViewController;
	
	@FXML
	@Named("listViewPane")
	private Pane listViewPane;

	@Inject
	private ApplicationModel model;
	
	@Inject
	private DaoManager daoManager;

	public <E extends Entity> void initModel(Control control, Class<E> entityClass) {
		ObservableList<E> list = model.getList(entityClass);
		if (list == null) {
			list = load(entityClass);
			model.addList(entityClass, list);
		}
		ModelBinder.bind(control, list);
	}
	
	public <E extends Entity> void replaceView(ViewSpec viewSpec) {
		TableView<?> viewControl = null;
		try {
			if (listViewPane != null) {
				final FXMLLoader loader = new FXMLLoader(WallabyApplication.class.getResource(viewSpec.getListView()));
				viewControl = loader.load();
				System.out.println("loaded: " + viewControl);
				listViewPane.getChildren().clear();
				listViewPane.getChildren().add(viewControl);
				System.out.println("placed: " + viewControl);
				initModel(viewControl, viewSpec.getEntityClass());
				System.out.println("bound: " + viewSpec.getEntityClassName());
			} else {
				System.out.println("not replacing list view");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("cannot load view: entity=" + viewSpec.getEntityClassName() + ", newListView=" + viewControl + ", e=" + e, e);
		}
	}
	
	private <E extends Entity> ObservableList<E> load(Class<E> entityClass) {
		final Dao<E> dao = daoManager.getDaoFor(entityClass);
		final List<E> entities = dao.findAll();
		return FXCollections.observableList(entities);
	}
}
