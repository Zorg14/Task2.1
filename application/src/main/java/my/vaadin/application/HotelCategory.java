/*package my.vaadin.application;

public enum HotelCategory {
    Hotel, Hostel, GuestHouse, Appartments
}*/

package my.vaadin.application;

public class HotelCategory 
{
	//private Set<String> hotelCategory = new HashSet<String>();
	private Long ID;
	private String hotelCategory = "";
	
	
    //Hotel, Hostel, GuestHouse, Appartments	
	public HotelCategory(Long ID, String hotelCategory)
	{
		super();
		this.ID = ID;
		this.hotelCategory = hotelCategory;
	}
	
	public HotelCategory()
	{
		
	}	
	
	public void setID(Long id) 
	{
		this.ID = ID;
	}
	
	public Long getID()
	{
		return ID;
	}	

	public void setHotelCategory(String hotelCategory) 
	{
		this.hotelCategory = hotelCategory;
	}
	
	public String getHotelCategory() 
	{
		return hotelCategory;
	}
	
	@Override
	public String toString() 
	{
		return "Category is: "+ hotelCategory;
	}

	@Override
	protected HotelCategory clone() throws CloneNotSupportedException
	{
		return (HotelCategory) super.clone();
	}

	@Override
	public boolean equals(Object obj) 
	{
		return super.equals(obj);
	}

	@Override
	public int hashCode()
		{
		return super.hashCode();
	}	
	
	public boolean isPersisted() {
		return ID != null;
	}
}




