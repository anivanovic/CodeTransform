package hr.github.anivanovic.codetransform;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.DeferredCommand;
import com.sencha.gxt.widget.core.client.button.TextButton;

class TypeMapping {

	private TypeMapping() {}

	private static Map<String, Class<?>> classMapping = new HashMap<>();

	static {
		classMapping.put(ContentPanel.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.ContentPanel.class);
		classMapping.put(TextArea.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.form.TextArea.class);
		classMapping.put(ListStore.class.getCanonicalName(),
				com.sencha.gxt.data.shared.ListStore.class);
		classMapping.put(Button.class.getCanonicalName(), TextButton.class);
		classMapping.put(ButtonBar.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.button.ButtonBar.class);
		classMapping.put(TreeItem.class.getCanonicalName(),
				com.google.gwt.user.client.ui.TreeItem.class);
		classMapping.put(Tree.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.tree.Tree.class);
		classMapping.put(DeferredCommand.class.getCanonicalName(), Scheduler.class);
		classMapping.put(Grid.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.grid.Grid.class);
		classMapping.put(ColumnConfig.class.getCanonicalName(),
				com.sencha.gxt.widget.core.client.grid.ColumnConfig.class);
		classMapping.put(GridCellRenderer.class.getCanonicalName(), AbstractCell.class);
	}

	public static Optional<Class<?>> getSubstituteClass(String oldClass) {
		Class<?> subClass = classMapping.get(oldClass);
		if (subClass == null) {
			System.out.println("No substitute class found for: " + oldClass);
		}

		return Optional.ofNullable(subClass);
	}

}
