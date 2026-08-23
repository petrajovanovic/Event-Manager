package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import enums.VrstaKarte;
import interfejsi.Serializable;
import interfejsi.Validirajuce;
import korisnici.Clan;

public class Karta implements Validirajuce, Serializable<Karta> {
	private String identifikator;
	private Clan kupac;
	private double cena;
	private VrstaKarte vrstaKarte;
	private Dogadjaj dogadjaj;
	private boolean iskoriscena;
	;
	
	public Karta() {}

	public Karta(String identifikator, Clan kupac, double cena, VrstaKarte vrstaKarte, Dogadjaj dogadjaj,
			boolean iskoriscena) {
		super();
		this.identifikator = identifikator;
		this.kupac = kupac;
		this.cena = cena;
		this.vrstaKarte = vrstaKarte;
		this.dogadjaj = dogadjaj;
		this.iskoriscena = iskoriscena;
	}
	
	public Karta(Dogadjaj dogadjaj,int brojac, double cena, VrstaKarte vk) {
		  String id=dogadjaj.getIdentifikator()+"-"+brojac;
			this.setIdentifikator(id);
			this.setKupac(null);
			this.setCena(cena);
			this.setVrstaKarte(vk);
			this.setDogadjaj(dogadjaj);
			this.setIskoriscena(false);
	}

	public String getIdentifikator() {
		return identifikator;
	}

	public void setIdentifikator(String identifikator) {
		this.identifikator = identifikator;
	}

	public Clan getKupac() {
		return kupac;
	}

	public void setKupac(Clan kupac) {
		this.kupac = kupac;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public VrstaKarte getVrstaKarte() {
		return vrstaKarte;
	}

	public void setVrstaKarte(VrstaKarte vrstaKarte) {
		this.vrstaKarte = vrstaKarte;
	}

	public Dogadjaj getDogadjaj() {
		return dogadjaj;
	}

	public void setDogadjaj(Dogadjaj dogadjaj) {
		this.dogadjaj = dogadjaj;
	}

	public boolean isIskoriscena() {
		return iskoriscena;
	}

	public void setIskoriscena(boolean iskoriscena) {
		this.iskoriscena = iskoriscena;
		
	}
	
	@Override
	public String toString() {//Ispis metode je skracen, odnosno prilagodjen za korisnike
	    return identifikator + " | " +
	           (dogadjaj != null ? dogadjaj.getNaziv() : "dogadjaj?") + " | " +
	           cena + " RSD | " +
	           vrstaKarte + " | " +
	           (iskoriscena ? "ISKORIŠĆENA" : "AKTIVNA");
	}
	
	@Override
	public String toCSV(Karta object) {
		// Ako nema kupca, čuva se prazan string
		String kupacIme = (object.kupac != null) ? object.kupac.getKorisnickoIme() : "";
		
		// Ako nema identifiktora, cuva se prazan string
		String dogadjajID = (object.dogadjaj != null) ? object.dogadjaj.getIdentifikator() : "";
		
		return object.identifikator+"|"+kupacIme+"|"+object.cena+
				"|"+object.vrstaKarte+"|"+dogadjajID+"|"+object.iskoriscena;
	}
	
	@Override
	public Karta fromCSV(String csv) throws ParseException {
		//delimo csv liniju u niz
		String[] kolone=csv.split("\\|");
		//provera da li je moguce podeliti fajl
		if(kolone.length!=6) {
			throw new ParseException("Nije moguce parsirati csv datoteku",0);
		}
		
		try {
			this.identifikator=kolone[0];
			
			// kupac (može biti prazan)
		    if (!kolone[1].isEmpty()) {
		    	this.kupac = new Clan();
		        this.kupac.setKorisnickoIme(kolone[1]);
		    } else {
		        this.kupac = new Clan();
		        this.kupac.setKorisnickoIme("");
		    }
			
			this.cena=Double.parseDouble(kolone[2]);
			
			try {
				this.vrstaKarte=VrstaKarte.valueOf(kolone[3]);
			}catch(IllegalArgumentException e) {
				throw new ParseException("Nepoznata vrsta karte:"+kolone[3],0);
			}
			
			this.dogadjaj=new Dogadjaj();
			this.dogadjaj=Dogadjaj.pronadjiDogadjajPoID(kolone[4]);
			this.iskoriscena=Boolean.parseBoolean(kolone[5]);
			}catch(Exception e) {
				
				throw new ParseException("Greska pri parsiranju:"+e.getMessage(),0);
			}
		return this;
			}
	
	@Override
    public boolean validiraj() {
		if (kupac == null) return false;

	    // ucitamo pravi dogadjaj iz fajla
	    Dogadjaj punDogadjaj = Dogadjaj.pronadjiDogadjajPoID(
	            this.dogadjaj.getIdentifikator()
	    );

	    if (punDogadjaj == null) return false;

	    LocalDate danas = LocalDate.now();

	    return !danas.isBefore(punDogadjaj.getDatumPocetka())
	        && !danas.isAfter(punDogadjaj.getDatumKraja());
	}
    
    
  @Override
    public boolean aktiviraj() {
	  ArrayList<Karta> karte=ucitajKarte();
	  for(Karta k:karte) {
		  if(k.identifikator.equals(this.identifikator)) {//trazenje karte u listi prema IDju
			  if(k.validiraj()) {//ako karta u listi moze da se validira
				  k.iskoriscena=true;//postaje iskoriscena
				  azurirajKarte(karte);//azuriranje fajla karte.csv da mi se upisalo da je karta iskoriscena
				  return true;
			  }
		  }
	  }
	  return false;
	  }
  
  public void upisKarte() {
		String csv = this.toCSV(this);
		
		File f=new File("data/karte.csv");
		
		FileWriter fw=null;
		BufferedWriter bw=null;
		
		try {
			//provera da li fajl postoji
			if(!f.exists()) {
				f.createNewFile();
			}
			
			//upis u fajl
			fw= new FileWriter(f,true);//true da bi se dodalo na kraj
			bw=new BufferedWriter(fw);
			
			//upis jednog reda+novi red
			bw.write(csv);
			bw.newLine();
		}catch(IOException e) {
			System.out.println("greska pri upisu karte u fajl");
			e.printStackTrace();
		}finally {
		    try {
		        if (bw != null) bw.close();
		        if (fw != null) fw.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
  
  public static Karta pronadjiKartuPoID(String kartaID) {
		ArrayList <Karta> karte=ucitajKarte();
			for(Karta karta:karte) {
				if(karta.identifikator.equals(kartaID)) {
					return karta;
					}else {
			}
			}return null;
  }
  
  public static ArrayList<Karta> ucitajKarte(){
	  ArrayList<Karta> karte=new ArrayList<Karta>();
		File f=new File("data/karte.csv");
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			String linija;
			br.readLine();
			while ((linija=br.readLine())!=null) {
				Karta karta=new Karta();
				try {
					karta.fromCSV(linija);
					karte.add(karta);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			br.close();
		} catch (FileNotFoundException e) {
			return karte;//Ako fajl ne postoji vraca se prazna lista
		} catch (IOException e) {
			System.out.println("Nije moguce otvoriti fajl dogadjaji.csv");
			e.printStackTrace();
		}
		return karte;
  }
  
  public static void azurirajKarte(ArrayList<Karta> karte) {//Azuriranje podataka u karte.csv
		File f=new File("data/karte.csv");
		String linija="";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			bw.write("identifikator|kupac|cena|vrstaKarte|dogadjaj|iskoriscena");
	        bw.newLine();
		    for (Karta karta : karte) {
		    	linija=karta.toCSV(karta);
		        bw.write(linija);
		        bw.newLine();
		    }
		    bw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
 }
  
  
	
	


