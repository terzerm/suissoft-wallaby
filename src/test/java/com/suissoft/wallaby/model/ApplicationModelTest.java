package com.suissoft.wallaby.model;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import javafx.collections.ObservableList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.suissoft.model.dao.Dao;
import com.suissoft.model.dao.partner.JuristicPersonDao;
import com.suissoft.model.dao.partner.NaturalPersonDao;
import com.suissoft.model.entity.partner.JuristicPerson;
import com.suissoft.model.entity.partner.NaturalPerson;
import com.suissoft.model.entity.workflow.Workflow;
import com.suissoft.wallaby.inject.guice.FxmlModule;

/**
 * Unit test for {@link ApplicationModel}
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationModelTest {
	
	@Mock
	private NaturalPersonDao naturalPersonDao;
	@Mock
	private JuristicPersonDao juristicPersonDao;
	@Mock
	private Dao<Workflow> workflowDao;
	
	//under test
	private ApplicationModel applicationModel;
	
	private class Module extends AbstractModule {
		@Override
		protected void configure() {
			bind(NaturalPersonDao.class).toInstance(naturalPersonDao);
			bind(JuristicPersonDao.class).toInstance(juristicPersonDao);
			bind(new TypeLiteral<Dao<Workflow>>() {}).toInstance(workflowDao);
		}
	}
	
	@Before
	public void beforeEach() {
		final FxmlModule fxmlModule = new FxmlModule(this);
		final Injector injector = Guice.createInjector(fxmlModule, new Module());
		applicationModel = injector.getInstance(ApplicationModel.class);
		fxmlModule.injectFxmlMembers(injector);
		
	}
	@Test
	public void shouldLoadDefaultData() {
		verify(naturalPersonDao, atLeast(1)).insertOrUpdate(any(NaturalPerson.class));
		verify(juristicPersonDao, atLeast(1)).insertOrUpdate(any(JuristicPerson.class));
		verify(workflowDao, atLeast(1)).insertOrUpdate(any(Workflow.class));
	}
	@Test
	public void shouldListInstances() {
		//given
		final Class<?>[] entityClasses = {NaturalPerson.class, JuristicPerson.class, Workflow.class};
		
		for (Class<?> entityClass : entityClasses) {
			//given
			assertNull(entityClass.getName(), applicationModel.getList(entityClass));
			addMockObservableListFor(entityClass);
			
			//when
			final ObservableList<?> list1 = applicationModel.getList(entityClass);
			final ObservableList<?> list2 = applicationModel.getList(entityClass);
		
			//then
			assertNotNull(entityClass.getName(), list1);
			assertNotNull(entityClass.getName(), list2);
			assertSame(entityClass.getName(), list1, list2);		
		}
	}

	private <E> void addMockObservableListFor(Class<E> entityClass) {
		applicationModel.addList(entityClass, mockObservableList(entityClass));
	}
	@SuppressWarnings("unchecked") //generic mock
	private static <E> ObservableList<E> mockObservableList(Class<E> entityClass) {
		return mock(ObservableList.class);
	}
}
