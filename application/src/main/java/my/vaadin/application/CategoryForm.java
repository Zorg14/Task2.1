package my.vaadin.application;

import java.util.HashSet;
import java.util.Iterator;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CategoryForm extends FormLayout
{		
	private TwinColSelect<String> selections = new TwinColSelect<>("Select category");		
	
	private Button saveCat = new Button("Save"); 
	private Button deleteCat = new Button("Delete");
	private Button editCat = new Button("Edit");
	
	private TextField editingField = new TextField();
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	
	public CategoryForm(MyUI myUI, HotelService service)
	{
		this.myUI = myUI;
		this.service = service;
		
		VerticalLayout layout = new VerticalLayout();
		
		HashSet<String> categorySet = new HashSet<>();
		for(Iterator iter = service.getCategories().values().iterator(); iter.hasNext();)
		{
			HotelCategory HotCat = (HotelCategory)iter.next();
			categorySet.add(HotCat.getHotelCategory());
		}
		
		selections.setItems(categorySet);					
		selections.setDescription("Set hotel's category");		
		selections.addSelectionListener(event -> {	     
			if(event.getAllSelectedItems().size() > 1)
			{
				editCat.setVisible(false);
			}else
			{
				editCat.setVisible(true);
			}
			layout.addComponent(new Label("Selected: " +
		            event.getNewSelection()));
		});
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout( saveCat, editCat, deleteCat);
		HorizontalLayout text = new HorizontalLayout(selections, editingField);
		addComponents(text, buttons);
		
		saveCat.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveCat.setClickShortcut(KeyCode.ENTER);
		
				
		saveCat.addClickListener(e -> save());
		deleteCat.addClickListener(e -> delete());
		editCat.addClickListener(e -> edit());
	}
	
	private void delete()
	{
		service.deleteCategory(selections.getSelectedItems());
		refreshSet();		
	}
	
	private void save()
	{
		service.saveCategory(editingField.getValue());
		refreshSet();	
	}
	
	private void edit()
	{
		String set = selections.getSelectedItems().iterator().next();		
		service.editCategory(set,editingField.getValue());
		refreshSet();
	}
	
	private void refreshSet()
	{
		HashSet<String> refreshedSet = new HashSet<>();
        for(Iterator iter = service.getCategories().values().iterator(); iter.hasNext();)
        {
        	HotelCategory HotCat = (HotelCategory)iter.next();
        	refreshedSet.add(HotCat.getHotelCategory());
        }
        selections.setItems(refreshedSet);
	}
	
}
