package entities;



public class Mesto  {
	private String naziv;
	private String adresa;
	private String prostorija;
	
	public Mesto() {}
	
	public Mesto(String naziv, String adresa, String prostorija) {
		super();
		this.naziv = naziv;
		this.adresa = adresa;
		this.prostorija = prostorija;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getProstorija() {
		return prostorija;
	}

	public void setProstorija(String prostorija) {
		this.prostorija = prostorija;
	}
	
	
	}
	
	
	
	









