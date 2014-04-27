package com.suissoft.wallaby.controller.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import com.suissoft.model.dao.Dao;
import com.suissoft.model.entity.partner.NaturalPerson;
import com.suissoft.wallaby.controller.ViewController;

public class NaturalPersonController extends ViewController<NaturalPerson> {
	@FXML
	private TableView<NaturalPerson> tableView;

	@Inject
	private Dao<NaturalPerson> personDao;

	@Override
	protected Class<NaturalPerson> elementType() {
		return NaturalPerson.class;
	}
	@Override
	protected ObservableList<NaturalPerson> load() {
		final List<NaturalPerson> persons = personDao.findAll();
		persons.add(new NaturalPerson());
		persons.get(0).setFirstName("Larry");
		persons.get(0).setLastName("Page");
		persons.get(0).setBirthday(new LocalDate(1973, 1, 1));
		persons.add(new NaturalPerson());
		persons.get(1).setFirstName("Mary");
		persons.get(1).setLastName("Pierce");
		persons.get(1).setBirthday(new LocalDate(1977, 10, 22));
		return FXCollections.observableList(persons);
	}
	
	@Override
	protected void bind(ObservableList<NaturalPerson> list) {
		tableView.setItems(list);
	}
}
