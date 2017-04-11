package hr.github.anivanovic.codetransform.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

@SuppressWarnings("deprecation")
public class TypeMapping {

	private TypeMapping() {}

	private static Map<Class<?>, Class<?>> classMapping = new HashMap<>();

	static {
		classMapping.put(ContentPanel.class, com.sencha.gxt.widget.core.client.ContentPanel.class);
		classMapping.put(TextArea.class, com.sencha.gxt.widget.core.client.form.TextArea.class);
		classMapping.put(ListStore.class, com.sencha.gxt.data.shared.ListStore.class);
		classMapping.put(Button.class, TextButton.class);
		classMapping.put(ButtonBar.class, com.sencha.gxt.widget.core.client.button.ButtonBar.class);
		classMapping.put(TreeItem.class, com.google.gwt.user.client.ui.TreeItem.class);
		classMapping.put(Tree.class, com.sencha.gxt.widget.core.client.tree.Tree.class);
		classMapping.put(DeferredCommand.class, Scheduler.class);
		classMapping.put(Command.class, ScheduledCommand.class);
		classMapping.put(Grid.class, com.sencha.gxt.widget.core.client.grid.Grid.class);
		classMapping.put(ColumnConfig.class,
				com.sencha.gxt.widget.core.client.grid.ColumnConfig.class);
		classMapping.put(GridCellRenderer.class, AbstractCell.class);
		classMapping.put(ColumnConfig.class,
				com.sencha.gxt.widget.core.client.grid.ColumnConfig.class);
		classMapping.put(ColumnData.class, com.sencha.gxt.widget.core.client.grid.ColumnData.class);
		classMapping.put(CardLayout.class, CardLayoutContainer.class);
		classMapping.put(HorizontalPanel.class, HorizontalLayoutContainer.class);
		classMapping.put(Menu.class, com.sencha.gxt.widget.core.client.menu.Menu.class);
		classMapping.put(MenuItem.class, com.sencha.gxt.widget.core.client.menu.MenuItem.class);
		classMapping.put(TextToolItem.class, TextButton.class);
		classMapping.put(SelectionMode.class, com.sencha.gxt.core.client.Style.SelectionMode.class);
		classMapping.put(DateWrapper.class, com.sencha.gxt.core.client.util.DateWrapper.class);
	}

	public static Optional<Class<?>> getSubstituteClass(Class<?> oldClass) {
		Class<?> subClass = classMapping.get(oldClass);
		if (subClass == null) {
			System.out.println("No substitute class found for: " + oldClass.getName());
		}

		return Optional.ofNullable(subClass);
	}

}
