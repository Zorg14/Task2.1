package my.vaadin.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelService {

	private static HotelService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private final HashMap<Long, Hotel> hotels = new HashMap<>();
	private long nextId = 0;
	
	private final HashMap<Long,HotelCategory> categorys = new HashMap<>();
	private long nextCatID = 4;
	
	public static HotelService getInstance() {
		if (instance == null) {
			instance = new HotelService();
			instance.ensureTestData();
		}
		return instance;
	}
	
	public synchronized HashMap<Long,HotelCategory> getCategories() {
		return categorys;
	}
	
	public void setNextID(long nextId)
	{
		this.nextId = nextId;		
	}
	
	public long getNextID()
	{
		nextId++;
		return nextId;
	}

	public synchronized List<Hotel> findAll() {
		return findAll(null);
	}

	public synchronized List<Hotel> findAll(String stringFilter) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel hotel : hotels.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| hotel.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized List<Hotel> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel contact : hotels.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}

	public synchronized long count() {
		return hotels.size();
	}

	public synchronized void delete(Hotel value) {
		hotels.remove(value.getId());
	}

	public synchronized void save(Hotel entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Hotel) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotels.put(entry.getId(), entry);
	}
	
	public synchronized void saveCategory(String entry) 
	{		 
		Collection<Long> collection= categorys.keySet();
		for (Long key : collection) 
		{
			HotelCategory HotCat = categorys.get(key);
		    if (key != null) 
		    {
		        if (entry.equals(HotCat.getHotelCategory()))
		        {
		        	return;
		        }
		    }
		}		
		categorys.put(new Long(nextCatID++), new HotelCategory(nextCatID,entry));
	}
	
	public synchronized void editCategory(String editableName,String newName) 
	{		 
		Collection<Long> collection= categorys.keySet();		
		for (Long key : collection) 
		{
			HotelCategory HotCat = categorys.get(key);
		    if (key != null) 
		    {
		        if (editableName.equals(HotCat.getHotelCategory()))
		        {
		        	categorys.put(key, new HotelCategory(key,newName));
		        	return;
		        }
		    }
		} 
		for (Hotel hotel: hotels.values())
		{
		   if(hotel.getCategory().equals(editableName))
		   {
			   hotel.setCategory(newName);
			   hotels.put(hotel.getId(), hotel);
		   }
		}	
	}
	
	public synchronized void deleteCategory(Set<String> items) 
	{		 
		Collection<Long> collection= categorys.keySet();
		ArrayList<Long> findKey = new ArrayList<>();
		for (Long key : collection) 
		{
			for(String name : items)
			{
				HotelCategory HotCat = categorys.get(key);
				if (key != null) 
				{
					if (name.equals(HotCat.getHotelCategory())) 
					{
						findKey.add(key);
					}
	        	}
	    	}
		}
		for(Long key: findKey)
		{
			categorys.remove(key);
		}
		for (String category: items)
		{
			for (Hotel hotel: hotels.values())
			{
			   if(hotel.getCategory().equals(category))
			   {
				   hotel.setCategory("Not defined");
				   hotels.put(hotel.getId(), hotel);
			   }
			}
		}
	}

	public void ensureTestData() {
		if (findAll().isEmpty()) {
			
			categorys.put(new Long(0),new HotelCategory(new Long(0),"Undefined"));
			categorys.put(new Long(1),new HotelCategory(new Long(1),"Hotel"));
			categorys.put(new Long(2),new HotelCategory(new Long(2),"Hostel"));
			categorys.put(new Long(3),new HotelCategory(new Long(3),"GuestHouse"));
			categorys.put(new Long(4),new HotelCategory(new Long(4),"Appartments"));
			
			final String[] hotelData = new String[] {
					
					
										
					"3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;",
					"Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos",
					"Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos",
					"Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos",
					"Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos",
					"Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos",
					"Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos",
					"Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos",
					"Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos",
					"Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos",
					"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos",
					"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos",
					"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos",
					"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos",
					"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos",
					"Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos",
					"Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos",
					"Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos",
					"Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos",
					"Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos",
					"Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos",
					"Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos",
					"Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos" };

			Random r = new Random(0);
			String category;
			for (String hotel : hotelData) {
				String[] split = hotel.split(";");
				Hotel h = new Hotel();
				h.setName(split[0]);
				h.setRating(Integer.parseInt(split[1]));
				h.setUrl(split[2]);
				h.setAddress(split[3]);
				category = categorys.get(new Long(r.nextInt(4))).getHotelCategory();
				h.setCategory(category); 
				long daysOld = r.nextInt(365 * 30);
				h.setOperatesFrom(daysOld);
				save(h);											
			}
		}		
	}

}
