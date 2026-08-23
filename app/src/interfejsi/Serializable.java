package interfejsi;

import java.text.ParseException;

public interface Serializable<T> {
	//pretvara objekat u jedan red CSV fajla
	public String toCSV(T object);
	
	//popunjava objekat na osnovu jednog reda CSV fajla
	public T fromCSV(String csv) throws ParseException;

}
