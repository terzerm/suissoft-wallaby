package com.suissoft.wallaby.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Unit test for {@link ApplicationController}
 */
public class ApplicationControllerTest {

	@Test
	public void shouldInjectParentControllerOfChildControllers() throws Exception {
		//given
		final ApplicationController controller = new ApplicationController();
		final MenuBarController menuBarController = new MenuBarController();
		final ToolBarController toolBarController = new ToolBarController();
		
		//given: this is normally done by FXML
		Field f;
		f = ApplicationController.class.getDeclaredField("menuBarController");
		f.setAccessible(true);
		f.set(controller, menuBarController);
		f = ApplicationController.class.getDeclaredField("toolBarController");
		f.setAccessible(true);
		f.set(controller, toolBarController);
		assertNull(menuBarController.getParentController());
		assertNull(toolBarController.getParentController());
		
		//when
		controller.initialize();
		
		//then
		assertSame(controller, menuBarController.getParentController());
		assertSame(controller, toolBarController.getParentController());
		assertSame(controller, menuBarController.getApplicationController());
		assertSame(controller, toolBarController.getApplicationController());
		assertSame(controller, controller.getApplicationController());
	}
}
