package com.suissoft.wallaby.model;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.LocalDate;

import com.suissoft.model.dao.Dao;
import com.suissoft.model.dao.partner.JuristicPersonDao;
import com.suissoft.model.dao.partner.NaturalPersonDao;
import com.suissoft.model.entity.partner.JuristicPerson;
import com.suissoft.model.entity.partner.NaturalPerson;
import com.suissoft.model.entity.workflow.Workflow;

/**
 * Stores model data.
 */
@Singleton
public class ApplicationModel {
	
	@Inject 
	private NaturalPersonDao naturalPersonDao;
	@Inject 
	private JuristicPersonDao juristicPersonDao;
	@Inject 
	private Dao<Workflow> workflowDao;

	private final Map<Class<?>, ObservableList<?>> listByElementType = new HashMap<>();
	
	@SuppressWarnings("unchecked")//safe cast, we know that the  types match in the map
	public <E> ObservableList<E> getList(Class<E> elementType) {
		return (ObservableList<E>)listByElementType.get(elementType);
	}
	public <E> void addList(Class<E> elementType, ObservableList<E> list) {
		final Object old = listByElementType.put(elementType, list);
		if (old != null) {
			throw new IllegalStateException("model already exists: " + old);
		}
	}
	
	@PostConstruct
	@Inject //workaround since guice does not support @PostConstruct yet
	private void loadDefaultData() {
		loadNaturalPersonData();
		loadJuristicPersonData();
		loadWorkflowData();
		System.out.println("default data loaded");
	}
	private void loadNaturalPersonData() {
		NaturalPerson person;
		person = new NaturalPerson();
		person.setFirstName("Larry");
		person.setLastName("Page");
		person.setBirthday(new LocalDate(1973, 1, 1));
		naturalPersonDao.insertOrUpdate(person);
		person = new NaturalPerson();
		person.setFirstName("Mary");
		person.setLastName("Pierce");
		person.setBirthday(new LocalDate(1977, 10, 22));
		naturalPersonDao.insertOrUpdate(person);
		person = new NaturalPerson();
		person.setFirstName("Henry");
		person.setLastName("Du Pont");
		person.setBirthday(new LocalDate(1962, 2, 28));
		naturalPersonDao.insertOrUpdate(person);
	}
	private void loadJuristicPersonData() {
		JuristicPerson person;
		person = new JuristicPerson();
		person.setName("Apple SA");
		juristicPersonDao.insertOrUpdate(person);
		person = new JuristicPerson();
		person.setName("Google Inc.");
		juristicPersonDao.insertOrUpdate(person);
	}
	private void loadWorkflowData() {
		Workflow workflow;
		workflow = new Workflow();
		workflow.setName("Online Orders");
		workflowDao.insertOrUpdate(workflow);
		workflow = new Workflow();
		workflow.setName("Online Shipment");
		workflowDao.insertOrUpdate(workflow);
	}
}
