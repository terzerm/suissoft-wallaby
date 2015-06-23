package com.suissoft.wallaby.controller.action;

import javafx.event.ActionEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.suissoft.model.entity.Entity;
import com.suissoft.model.entity.partner.JuristicPerson;
import com.suissoft.model.entity.partner.NaturalPerson;
import com.suissoft.wallaby.controller.TreeViewController;

public class SelectViewAction extends WallabyAction {
	
	private final Class<? extends Entity> entityClass;

	@Inject
	private TreeViewController treeViewController;
	
	@Singleton
	public static final class NaturalPersonView extends SelectViewAction {
		public NaturalPersonView() {super("Persons", NaturalPerson.class);}
	}
	@Singleton
	public static final class JuristicPersonView extends SelectViewAction {
		public JuristicPersonView() {super("Companies", JuristicPerson.class);}
	}
	
	public SelectViewAction(String text, Class<? extends Entity> entityClass) {
		super(text);
		this.entityClass = entityClass;
	}
	@Override
	public void handle(ActionEvent ae) {
		treeViewController.select(entityClass);
	}
}
