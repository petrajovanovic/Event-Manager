package entities;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import app.Platforma;
import interfejsi.Serializable;


public class Dogadjaj implements Serializable<Dogadjaj>{
	private String identifikator;
	private String naziv;
	private LocalDate datumPocetka;
	private LocalDate datumKraja;
	private LinkedHashMap<String, String> sadrzaj;//Linked HaspMap cuva redosled kljuceva
	private Mesto mesto;
	
	public Dogadjaj(){
		this.sadrzaj = new LinkedHashMap<>();
	}

	public Dogadjaj(String identifikator, String naziv, LocalDate datumPocetka, LocalDate datumKraja,
			LinkedHashMap<String, String> sadrzaj, Mesto mesto) {
		super();
		this.identifikator = identifikator;
		this.naziv = naziv;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.sadrzaj = sadrzaj;
		this.mesto = mesto;
	}

	public String getIdentifikator() {
		return identifikator;
	}

	public void setIdentifikator(String identifikator) {
		this.identifikator = identifikator;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public LocalDate getDatumPocetka() {
		return datumPocetka;
	}

	public void setDatumPocetka(LocalDate datumPocetka) {
		this.datumPocetka = datumPocetka;
	}

	public LocalDate getDatumKraja() {
		return datumKraja;
	}

	public void setDatumKraja(LocalDate datumKraja) {
		this.datumKraja = datumKraja;
	}

	public HashMap<String, String> getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(LinkedHashMap<String, String> sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public Mesto getMesto() {
		return mesto;
	}

	public void setMesto(Mesto mesto) {
		this.mesto = mesto;
	}
	
	@Override 
	public String toCSV(Dogadjaj object) {
		
		// Formatiranje datuma
		String pocetakStr = object.datumPocetka.toString();
		String krajStr = object.datumKraja.toString();
		
		// HashMap formatiranje: dan1:sadrzaj1;dan2:sadrzaj2
		StringBuilder sadrzajStr = new StringBuilder();
		if (object.sadrzaj != null && !object.sadrzaj.isEmpty()) {
			for (HashMap.Entry<String, String> entry : object.sadrzaj.entrySet()) {
				if (sadrzajStr.length() > 0) {
					sadrzajStr.append(";");
				}
				sadrzajStr.append(entry.getKey()).append(":").append(entry.getValue());
			}
		}
		return object.identifikator+"|"+object.naziv+"|"+pocetakStr+"|"+krajStr+
				"|"+sadrzajStr.toString()+"|"+object.mesto.getNaziv();
	}
	
	@Override
	public Dogadjaj fromCSV(String csv) throws ParseException{
		
		//Delimo csv string na kolone
		String[] kolone=csv.split("\\|");
		
		//provera da li moze da se parsira
		if(kolone.length!=6) {
			throw new ParseException("Nije moguce parsirati csv dokument",0);
			}
		
		//pravljenje kopije objekta
		Dogadjaj dogadjajKopija=new Dogadjaj();
		dogadjajKopija.identifikator=this.identifikator;
		dogadjajKopija.datumKraja=this.datumKraja;
		dogadjajKopija.datumPocetka=this.datumPocetka;
		dogadjajKopija.mesto=this.mesto;
		dogadjajKopija.naziv=this.naziv;
		dogadjajKopija.sadrzaj=this.sadrzaj;
		
		try {
			this.identifikator=kolone[0];
			this.naziv=kolone[1];
			this.datumPocetka=LocalDate.parse(kolone[2]);
			this.datumKraja=LocalDate.parse(kolone[3]);
			
			//Parsiranje hash map-a iz stringa
			this.sadrzaj=new LinkedHashMap<>();
			if(!kolone[4].trim().isEmpty()) {
				String[] parovi=kolone[4].split(";");
				for (String par:parovi) {
					String[] danSadrzaj=par.split(":");
					if(danSadrzaj.length==2) {
						this.sadrzaj.put(danSadrzaj[0].trim(), danSadrzaj[1].trim());
					}
				}
			}
			
			this.mesto = new Mesto();
			this.mesto.setNaziv(kolone[5]);
			}catch (Exception e) {
				//ako dodje do greske vracamo kopiju
				this.identifikator=dogadjajKopija.identifikator;
				this.datumKraja=dogadjajKopija.datumKraja;
				this.datumPocetka=dogadjajKopija.datumPocetka;
				this.mesto=dogadjajKopija.mesto;
				this.naziv=dogadjajKopija.naziv;
				this.sadrzaj=dogadjajKopija.sadrzaj;
				throw new ParseException("Greska pri parsiranju dogadjaja:"+e.getMessage(),0);
			}
		return this;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("Identifikator: ").append(identifikator != null ? identifikator : "/").append("\n");
	    sb.append("Naziv: ").append(naziv != null ? naziv : "/").append("\n");
	    sb.append("Datum pocetka: ").append(datumPocetka != null ? datumPocetka : "/").append("\n");
	    sb.append("Datum kraja: ").append(datumKraja != null ? datumKraja : "/").append("\n");
	    sb.append("Mesto: ").append(mesto != null ? mesto.getNaziv() : "/").append("\n");

	    sb.append("Sadrzaj po danima:\n");
	    if (sadrzaj != null && !sadrzaj.isEmpty()) {
	        for (var entry : sadrzaj.entrySet()) {
	            sb.append("  ").append(entry.getKey())
	              .append(" -> ").append(entry.getValue()).append("\n");
	        }
	    } else {
	        sb.append("  /nema sadrzaja/\n");
	    }

	    return sb.toString();
	}
	
	public static Dogadjaj pronadjiDogadjajPoID(String dogadjajID) {
		ArrayList<Dogadjaj> dogadjaji=Platforma.ucitavanjeDogadjaja();
		for(Dogadjaj d:dogadjaji) {
			if(d.identifikator.equals(dogadjajID)) {
				return d;
			}
		}
		return null;
	}
	
	public boolean aktivniDogadjaj() {//proverava da li je dogadjaj aktivan
		LocalDate danas=LocalDate.now();
		if(danas.isBefore(this.datumKraja)||danas.isEqual(this.datumKraja)) {
			return true;//dogadjaj je aktivan 
		}
		return false;//nije aktivan
	}

	public boolean proveraID(ArrayList<Dogadjaj> dogadjaji) {
		for(Dogadjaj dogadjaj:dogadjaji) {
			if(this.identifikator.equals(dogadjaj.getIdentifikator())) {
				return false;//dogadjaj sa ovim IDjem vec postoji
			}
		}
		return true;
	}
	
	public static void pronadjiDogadjajPoMestu(String mesto,ArrayList<Dogadjaj> dogadjaji) {
		for(Dogadjaj d:dogadjaji) {
				if(d.mesto.getNaziv().equals(mesto)&&d.aktivniDogadjaj()) {
					System.out.println(d);
				}
			}
			
		}
	
	public static void pronadjiDogadjajPoPeriodu(ArrayList<Dogadjaj> dogadjaji,
												LocalDate periodOd,LocalDate periodDo) {
		for(Dogadjaj d:dogadjaji) {
			if(d.aktivniDogadjaj()&&
			//ako je datum pocetka isti ili posle unetog, a datum kraja pre ili isti kao uneti datum
			//stampamo dogadjaj
			(d.getDatumPocetka().isAfter(periodOd)||d.getDatumPocetka().isEqual(periodOd))
			&&(d.getDatumKraja().isBefore(periodDo)||d.getDatumKraja().isEqual(periodDo))){
				System.out.println(d);
			}
		}
		
	}
	
	public static void pronadjiDogadjajPoDelimicnomNazivu(String naziv,ArrayList<Dogadjaj> dogadjaji) {
		for(Dogadjaj d:dogadjaji) {
			if(d.naziv.toLowerCase().contains(naziv.toLowerCase())&&d.aktivniDogadjaj()) {
				//ako naziv dogadjaja sadrzi uneti naziv, dogadjaj se stampa
				//+zanemaruje se velicina slova
				System.out.println(d);
			}
		}
	}
}














