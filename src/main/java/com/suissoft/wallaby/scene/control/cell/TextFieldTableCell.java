package com.suissoft.wallaby.scene.control.cell;

import java.lang.reflect.Field;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * A class containing a {@link TableCell} implementation that draws a
 * {@link TextField} node inside the cell.
 * 
 * <p>
 * By default, the TextFieldTableCell is rendered as a {@link Label} when not
 * being edited, and as a TextField when in editing mode. The TextField will, by
 * default, stretch to fill the entire table cell.
 * 
 * @param <S>
 *            The type of the TableView generic type (i.e. S ==
 *            TableView&lt;S&gt;)
 * @param <T>
 *            The type of the elements contained within the TableColumn.
 * @since JavaFX 2.2
 */
public class TextFieldTableCell<S, T> extends
		javafx.scene.control.cell.TextFieldTableCell<S, T> {

	/**
	 * Creates a default TextFieldTableCell with a null converter. Without a
	 * {@link StringConverter} specified, this cell will not be able to accept
	 * input from the TextField (as it will not know how to convert this back to
	 * the domain object). It is therefore strongly encouraged to not use this
	 * constructor unless you intend to set the converter separately.
	 */
	public TextFieldTableCell() {
		super();
	}

	/**
	 * Creates a TextFieldTableCell that provides a {@link TextField} when put
	 * into editing mode that allows editing of the cell content. This method
	 * will work on any TableColumn instance, regardless of its generic type.
	 * However, to enable this, a {@link StringConverter} must be provided that
	 * will convert the given String (from what the user typed in) into an
	 * instance of type T. This item will then be passed along to the
	 * {@link TableColumn#onEditCommitProperty()} callback.
	 * 
	 * @param converter
	 *            A {@link StringConverter converter} that can convert the given
	 *            String (from what the user typed in) into an instance of type
	 *            T.
	 */
	public TextFieldTableCell(StringConverter<T> converter) {
		super(converter);
	}

	/**
	 * Provides a {@link TextField} that allows editing of the cell content when
	 * the cell is double-clicked, or when
	 * {@link TableView#edit(int, javafx.scene.control.TableColumn)} is called.
	 * This method will only work on {@link TableColumn} instances which are of
	 * type String.
	 * 
	 * @param <S>
	 *            The type of the TableView generic type (i.e. S ==
	 *            TableView&lt;S&gt;)
	 * @return A {@link Callback} that can be inserted into the
	 *         {@link TableColumn#cellFactoryProperty() cell factory property}
	 *         of a TableColumn, that enables textual editing of the content.
	 */
	public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}

	/**
	 * Provides a {@link TextField} that allows editing of the cell content when
	 * the cell is double-clicked, or when
	 * {@link TableView#edit(int, javafx.scene.control.TableColumn) } is called.
	 * This method will work on any {@link TableColumn} instance, regardless of
	 * its generic type. However, to enable this, a {@link StringConverter} must
	 * be provided that will convert the given String (from what the user typed
	 * in) into an instance of type T. This item will then be passed along to
	 * the {@link TableColumn#onEditCommitProperty()} callback.
	 * 
	 * @param converter
	 *            A {@link StringConverter} that can convert the given String
	 *            (from what the user typed in) into an instance of type T.
	 * @param <S>
	 *            The type of the TableView generic type (i.e. S ==
	 *            TableView&lt;S&gt;)
	 * @param <T>
	 *            The type of the content in all cells in this TableColumn.
	 * @return A {@link Callback} that can be inserted into the
	 *         {@link TableColumn#cellFactoryProperty() cell factory property}
	 *         of a TableColumn, that enables textual editing of the content.
	 */
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final StringConverter<T> converter) {
		return list -> new TextFieldTableCell<S, T>(converter);
	}

	private TextField textField;
	
	private String getEditedValue() {
		return textField != null ? textField.getText() : null;
	}
	
	@SuppressWarnings("unchecked")
	private TablePosition<S, T> getEditingCell() {
		return (TablePosition<S, T>) getUserData();
	}

	private void updateEditingCell() {
		final int row = getIndex();
		final TableColumn<S, T> tableColumn = getTableColumn();
		if (row >= 0 && tableColumn != null && tableColumn.getTableView() != null) {
			setUserData(new TablePosition<>(tableColumn.getTableView(), row, tableColumn));
		} else {
			setUserData(null);
		}
		System.out.println("updateEditingCell: userData=" + getUserData());
	}
	private void clearEditingCell() {
		setUserData(null);
		System.out.println("clearEditingCell: userData=" + getUserData());
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (textField == null) {
			initTextField();
		}
		updateEditingCell();
	}
	
	@Override
	public void cancelEdit() {
		System.out.println("cancelEdit: editedValue=" + getEditedValue());
		super.cancelEdit();
		updateEditingCell();
	}
	
	@Override
	public void commitEdit(T newValue) {
		System.out.println("commitEdit: editedValue=" + getEditedValue() + ", newValue=" + newValue);
		if (isEditing()) {
			super.commitEdit(newValue);
		} else {
	        final TableView<S> table = getTableView();
	        final Object userData = getUserData();
			System.out.println("commitEdit (not editing): table=" + table + ", userData=" + userData);
	        if (table != null && userData instanceof TablePosition) {
				final TablePosition<S, T> editingCell = getEditingCell();
	            // Inform the TableView of the edit being ready to be committed.
	            final CellEditEvent<S, T> editEvent = new CellEditEvent<S, T>(
	                table,
	                editingCell,
	                TableColumn.editCommitEvent(),
	                newValue
	            );

	    		System.out.println("fireEvent: " + editEvent);
	            Event.fireEvent(editEvent.getTableColumn(), editEvent);

		        // update the item within this cell, so that it represents the new value
		        updateItem(newValue, false);

	            // reset the editing cell on the TableView
	            table.edit(-1, null);

	            // request focus back onto the table, only if the current focus
	            // owner has the table as a parent (otherwise the user might have
	            // clicked out of the table entirely and given focus to something else.
	            // It would be rude of us to request it back again.
	            requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
	        }
		}
		updateEditingCell();
    }
	
    public void commitEdit() {
		System.out.println("commitEdit: editedValue=" + getEditedValue());
        if (textField != null) {
			final T converted = getConverter().fromString(textField.getText());
			commitEdit(converted);
        }
    }
    

	private void initTextField() {
		try {
			final Field field = javafx.scene.control.cell.TextFieldTableCell.class.getDeclaredField("textField");
			field.setAccessible(true);
			final TextField textField = (TextField) field.get(this);
			if (textField != null) {
				textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
					final TablePosition<S, T> editingCell = getEditingCell();
					System.out.println("focused: " + oldValue + "-->" + newValue + ", editedValue=" + getEditedValue() + ", editingCell=" + editingCell);
					if (!newValue && editingCell != null) {
						commitEdit();
					}
				});
				textField.setOnKeyPressed((event) -> {
					System.out.println("keyPressed: " + event + ", editedValue=" + getEditedValue());
					if (event.getCode() == KeyCode.ESCAPE) {
						clearEditingCell();
					} else if (event.getCode() == KeyCode.TAB) {
						final TablePosition<S, ?> pos = getTableView().getEditingCell();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								editNextColumn(event.isShiftDown(), pos);
							}
						});
					}
				});
				System.out.println("text field initialised");
				this.textField = textField;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void editNextColumn(boolean right, TablePosition<S, ?> pos) {
		System.out.println("editNextColumn: right=" + right  + ", pos=" + pos);
		if (pos != null) {
			final TableView<S> table = getTableView();
			final int col = pos.getColumn();
			final TableColumn<S, ?> tableColumn; 
			if (right) {
				tableColumn = col > 0 ? table.getColumns().get(col - 1) : null;
			} else {
				tableColumn = (col+1) < table.getColumns().size() ? table.getColumns().get(col + 1) : null;
			}
			if (tableColumn != null) {
				table.edit(pos.getRow(), tableColumn);
				System.out.println("table.edit: row=" + pos.getRow() + ", col=" + table.getColumns().indexOf(tableColumn) + ", tableColumn=" + tableColumn);
			}
		}
	}

	//from ControlUtils
    private static void requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(Control c) {
        Scene scene = c.getScene();
        final Node focusOwner = scene == null ? null : scene.getFocusOwner();
        if (focusOwner == null) {
            c.requestFocus();
        } else if (! c.equals(focusOwner)) {
            Parent p = focusOwner.getParent();
            while (p != null) {
                if (c.equals(p)) {
                    c.requestFocus();
                    break;
                }
                p = p.getParent();
            }
        }
    }
}
