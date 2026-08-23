package korisnici;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import entities.Dogadjaj;
import app.Platforma;

public abstract class Korisnik {
	protected String korisnickoIme;
	protected String lozinka;
	
	public Korisnik(String korisnickoIme, String lozinka) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	
	public Korisnik() {
	
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	//Metoda se nalazi u korisniku jer vazi za sve korisnike
	public void pretragaDogadjaja(ArrayList<Dogadjaj> dogadjaji) {
		int odabir;
		while(true) {
		System.out.println("Po cemu zelite da pretrazite aktivne dogadjaje:");
		System.out.println("1 - Po mestu");
		System.out.println("2 - Po periodu");
		System.out.println("3 - Po delimicnom nazivu");
		
		try {
			odabir=Platforma.SC.nextInt();
		}catch(InputMismatchException e) {
			odabir=0;
		}
		Platforma.SC.nextLine();
		
		if(odabir>=1&&odabir<=3) {
			break;
		}
		
		if(odabir<1||odabir>3) {
			System.out.println("Niste uneli validnu opciju.Pokusajte ponovo");
		}
		}
		
		if(odabir==1) {//PRETRAGA PO MESTU
			System.out.println("Unesite tacan naziv mesta:");
			String mesto=Platforma.SC.nextLine();
			System.out.println("Trenutni aktivni dogadjaji:");
			Dogadjaj.pronadjiDogadjajPoMestu(mesto,dogadjaji);
		}
		
		if(odabir==2) {//PRETRAGA PO PERIODU
			LocalDate periodOd;
			LocalDate periodDo;
			
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy");
			
			while(true) {
			try {
			System.out.println("Unesite pocetni datum (u formatu: 01.01.2026):");
			periodOd=LocalDate.parse(Platforma.SC.nextLine(), formatter);
			System.out.println("Unesite krajnji datum (u formatu: 01.01.2026):");
			periodDo=LocalDate.parse(Platforma.SC.nextLine(), formatter);
			
			if (periodDo.isBefore(periodOd)) {
				System.out.println("Krajnji datum ne moze biti pre pocetnog.");
				continue;
			}
			break;
			}catch(DateTimeParseException e) {
				System.out.println("Uneli ste pogresan format datuma. Pokusajte ponovo");
			}
		}
			System.out.println("Trenutni aktivni dogadjaji:");
			Dogadjaj.pronadjiDogadjajPoPeriodu(dogadjaji,periodOd,periodDo);
		}
		
		if(odabir==3) {//PRETRAGA PO DELIMICNOM NAZIVU
			System.out.println("Unesite naziv dogadjaja koji zelite da pretrazite");
			String naziv=Platforma.SC.nextLine();
			System.out.println("Trenutni aktivni dogadjaji:");
			Dogadjaj.pronadjiDogadjajPoDelimicnomNazivu(naziv,dogadjaji);
		}
	}
}
	
	

