package korisnici;

import java.text.ParseException;

import entities.Karta;
import interfejsi.Serializable;

public class Validator extends Korisnik implements Serializable<Validator> {
	
	public Validator(){
		super();
	}

	public Validator(String korisnickoIme, String lozinka) {
		super(korisnickoIme, lozinka);
	}
	
	@Override
	public String toCSV(Validator object) {
		return "Validator|"+object.korisnickoIme+"|"+object.lozinka+"|"+" "+"|"+" "+"|"+" "+"|"+" ";
	}
	
	@Override 
	public Validator fromCSV(String csv) throws ParseException {
		//pravi se kopija objekta
		Validator validatorKopija=new Validator();
		validatorKopija.korisnickoIme=this.korisnickoIme;
		validatorKopija.lozinka=this.lozinka;
		
		//podatke iz datoteke podeliti na listru stringova
		String[] kolone=csv.split("\\|");
		if (kolone.length < 3) {
			//ako dodje do neuspesnog parsiranja vracamo izuzetak
			throw new ParseException("Nije moguce parsirati podatke",0);
			}
	
		
		//podesiti atribute na nove vrednosti
		try {
			this.korisnickoIme=kolone[1];
			this.lozinka=kolone[2];
		}catch(NumberFormatException e) {
			//ukoliko dodje do greske vraca se kopija
			this.korisnickoIme=validatorKopija.korisnickoIme;
			this.lozinka=validatorKopija.lozinka;
			throw new ParseException("Greska pri parsiranju clanova",0);
		}
		return this;
	}
	
	public void validacijaKarte(String kartaID) throws NullPointerException{
		//ako se unese nepostojaci ID baca se ovaj izuzetak
		Karta k=new Karta();
		k=Karta.pronadjiKartuPoID(kartaID);
		if(k.validiraj()) {
			System.out.println("Karta je validirana.");
		}else {
			System.out.println("Nije moguce validirati kartu.");
		}
		
	}
	
	public void aktiviranjeKarte(String kartaID) {
		Karta k=new Karta();
		k=Karta.pronadjiKartuPoID(kartaID);
		if (k.aktiviraj()) {
			System.out.println("Karta je uspesno aktivirana!");
			
		}
		else {
			System.out.println("Karta nije aktivirana");
		}
	}
	
	
	
	

}























