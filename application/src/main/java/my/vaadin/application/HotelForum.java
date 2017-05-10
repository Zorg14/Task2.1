package my.vaadin.application;

import java.util.HashSet;
import java.util.Iterator;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForum extends FormLayout 
{
	private TextField name = new TextField("Hotel name");	
	private TextField address = new TextField("Hotel address"); 	
	private TextField rating = new TextField("Hotel rating");
	private DateField operatesFrom = new DateField("Operates from");
	private NativeSelect<String> category = new NativeSelect<>("Hotel Category");
	private TextField url = new TextField("booking.com link");
	private TextArea feedback = new TextArea("Customer feedback");
	
	private Button saveHotel = new Button("Save"); 
	private Button deleteHotel = new Button("Delete");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	public HotelForum(MyUI myUI) 
	{
		this.myUI = myUI;
		
		name.setDescription("Name input field");
		address.setDescription("Address input field");
		rating.setDescription("Set rating to the hotel");
		operatesFrom.setDescription("Amount of days that hotel operates");
		category.setDescription("Set hotel's category");
		url.setDescription("Hotel's web-sete");
		feedback.setDescription("Feedback field");
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveHotel, deleteHotel);
		addComponents(name, address, rating, operatesFrom, category, url, feedback, buttons);	
		
		SetCategorys();
		
		saveHotel.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveHotel.setClickShortcut(KeyCode.ENTER);
		
		bindField();
		
		saveHotel.addClickListener(e -> save());
		deleteHotel.addClickListener(e -> delete());
	}
	
	private void SetCategorys() 
	{
		HashSet<String> settedCategory = new HashSet<>();
        for(Iterator iter = service.getCategories().values().iterator(); iter.hasNext();)
        {
        	HotelCategory HotCat = (HotelCategory)iter.next();
        	settedCategory.add(HotCat.getHotelCategory());
        }
        category.setItems(settedCategory);		
	}

	private void bindField()
	{
		binder.forField(rating).withConverter(new StringToIntegerConverter(0, "Only integers"))
		.withValidator(v -> (v<6 && v>0), "Value must be in between of 1 and 5")
		.bind(Hotel::getRating, Hotel::setRating);
		binder.forField(name).bind(Hotel::getName,Hotel::setName);
		binder.forField(address).bind(Hotel::getAddress,Hotel::setAddress);
		
		binder.forField(operatesFrom)
		.withConverter(new DateConverter())
		.bind(Hotel::getOperatesFrom,Hotel::setOperatesFrom);
		
		binder.forField(category).bind(Hotel::getCategory,Hotel::setCategory);
		binder.forField(url).bind(Hotel::getUrl,Hotel::setUrl);
		binder.forField(feedback).bind(Hotel::getFeedback,Hotel::setFeedback);
		//binder.bindInstanceFields(this);
	}
	
	public void setHotel(Hotel hotel)
	{
		this.hotel = hotel;
		binder.setBean(hotel);
		
		deleteHotel.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
	}
	
	private void delete()
	{
		service.delete(hotel);
		myUI.updateList();
		setVisible(false);
	}
	
	private void save()
	{
		service.save(hotel);
		myUI.updateList();
		setVisible(false);
	}
}
