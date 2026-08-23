package korisnici;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import app.Platforma;
import entities.Karta;
import interfejsi.Serializable;

public class Clan extends Korisnik  implements Serializable<Clan> {
	private String brojTelefona;
	private double novcanaSredstva;
	private ArrayList<String> kupljeneKarteIDs;
	
	
	public Clan(){
		super();
		this.kupljeneKarteIDs = new ArrayList<>();
		this.novcanaSredstva = 0.0;
	}
	
	public Clan(String korisnickoIme, String lozinka, String brojTelefona, double novcanaSredstva,
			ArrayList<String> kupljeneKarteIDs) {
		super(korisnickoIme, lozinka);
		this.brojTelefona = brojTelefona;
		this.novcanaSredstva = novcanaSredstva;
		this.kupljeneKarteIDs = new ArrayList<>();  
	}
	
	public Clan(String korisnickoIme, String lozinka, String brojTelefona) {
		super(korisnickoIme, lozinka);
		this.brojTelefona = brojTelefona;
		this.novcanaSredstva = 0.0;
		this.kupljeneKarteIDs = new ArrayList<>();
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public double getNovcanaSredstva() {
		return novcanaSredstva;
	}

	public void setNovcanaSredstva(double novcanaSredstva) {
		this.novcanaSredstva = novcanaSredstva;
	}


	
	public ArrayList<String> getKupljeneKarteIDs() {
		return kupljeneKarteIDs;
	}

	public void setKupljeneKarteIDs(ArrayList<String> kupljeneKarteIDs) {
		this.kupljeneKarteIDs = kupljeneKarteIDs;
	}
	
	
	
	@Override
	public String toCSV(Clan object) {
		
		String karteIDs="";//pravimo prazan string
		//proverava da li ima podataka u atributu kupljeneKarteIDs
		if(object.kupljeneKarteIDs !=null&& !object.kupljeneKarteIDs.isEmpty()) {
		//ukoliko ima, podaci se konvertuju u string i odvajaju sa ";" radi bolje reprezentacije u CSV fajlu
			karteIDs=String.join(";", object.kupljeneKarteIDs);
		}else{
		//ako nema podataka, u sCSV fajl se upisuje prazan string
			karteIDs=" ";
		}
		return "Clan"+"|"+object.korisnickoIme+"|"+object.getLozinka()+"|"+object.brojTelefona+"|"+object.novcanaSredstva
				+"|"+karteIDs+"|"+" ";
		}
	
	@Override
	public Clan fromCSV(String csv) throws ParseException {
		//pravljenje kopije instance sa trenutnim vrednostima
		Clan kopijaClan=new Clan();
		kopijaClan.setKorisnickoIme(this.korisnickoIme);
		kopijaClan.setLozinka(this.getLozinka());
		kopijaClan.setBrojTelefona(this.brojTelefona);
		kopijaClan.setKupljeneKarteIDs(this.kupljeneKarteIDs);
		kopijaClan.setNovcanaSredstva(this.novcanaSredstva);
		
		//podatke iz datoteke razdvojiti u listu stringova
		String[] kolone=csv.split("\\|");
		if (kolone.length!=7) {
			//ukoliko dodje do neuspesnog parsiranja baca izuzetak
			throw new ParseException("Nije moguce parsirati csv reprezentaciju clana",0);
		}
		
		//ucitane podatke postaviti za trenutne vrednosti
		try {
			this.korisnickoIme=kolone[1];
			this.lozinka=kolone[2];
			this.brojTelefona=kolone[3];
			this.novcanaSredstva=Double.parseDouble(kolone[4]);
			
			this.kupljeneKarteIDs=new ArrayList<>(); //pravi se nova lista
			
			//ubacivanje podataka u kupljeneKarteIDs:
			if (!kolone[5].isEmpty()) {//provera da li su karte u korisnici.csv prazne
				String[] karteIDs=kolone[5].split(";");//pravi se lista stringovs od karata iz fajla
				for (String kartaID:karteIDs) {//iteracija po tim stringovima
					this.kupljeneKarteIDs.add(kartaID.trim());//svaki string posebno se ubacuje u kupljeneKarteIds
			}
			}
			
			
		}catch (Exception e) {
			// Vraca se kopiju ako dođe do greške
			this.korisnickoIme = kopijaClan.getKorisnickoIme();
			this.lozinka = kopijaClan.getLozinka();
			this.brojTelefona = kopijaClan.getBrojTelefona();
			this.kupljeneKarteIDs = kopijaClan.getKupljeneKarteIDs();
			this.novcanaSredstva = kopijaClan.getNovcanaSredstva();
			throw new ParseException("Greska pri parsiranju clana", 0);
		}
		
		
		return this;	
	}
	

		
	public boolean proveraKorisnickogImena(ArrayList<Korisnik> korisnici) {//PROVERAVA JEDINSTVENOST KORISNICKOG IMENA
		for (Korisnik korisnik:korisnici) {
			if(this.korisnickoIme.equals(korisnik.korisnickoIme)) {
				return false;
			}
		}
		return true;
	}
	
	public void kupovinaKarata(ArrayList<Karta> karte) {
		System.out.println("Napisite tacan ID dogadjaja za koji zelite da kupite kartu:");
		String dogadjajID=Platforma.SC.nextLine();//odabir dogadjaja
		int odabir;
		while(true) {
		System.out.println("Odaberite vrstu karte koju zelite:");
		System.out.println("1 - individualna");
		System.out.println("2 - grupna");
		System.out.println("3 - porodicna");
		
		try {
			odabir=Platforma.SC.nextInt();//odabir karte
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
		String vrstaKarte="";
		if(odabir==1) {
			vrstaKarte="INDIVIDUALNA";
		}
		if(odabir==2) {
			vrstaKarte="GRUPNA";
		}
		if(odabir==3) {
			vrstaKarte="PORODICNA";
		}
		
		boolean kupljeno=false;//oznacava da ni jedna karta jos nije kupljena
		for(Karta karta:karte) {
			//provera IDja dogadjaja, vrste karte i da li je polje kupac prazno
				if(!kupljeno &&karta.getDogadjaj().getIdentifikator().equals(dogadjajID)
						&&karta.getVrstaKarte().toString().equals(vrstaKarte)&&karta.getKupac().
						getKorisnickoIme().equals("")){
					karta.setKupac(this);//karti se dodeljuje kupac
					kupljeno=true;
					this.kupljeneKarteIDs.add(karta.getIdentifikator());//kupcu se dodeljuje karta
					System.out.println("Uspesno ste kupili kartu!");
				}
			}
		
			if(!kupljeno) {//ako kupljeno posle petlje ostane false, znaci da ni jedna karta nije kupljena
			System.out.println("Nema slobodnih karata za izabrani dogadjaj i vrstu");
			}
	}
	
	public void prikazKupljenihKarata() {
		ArrayList<Karta> karte=Karta.ucitajKarte();//ucitavanje svih karata iz datoteke
		for(Karta k:karte) {
			//ukoliko se kupac karte poklapa sa korisnickim imenom clana
			if(k.getKupac().getKorisnickoIme().equals(this.korisnickoIme)&&
					k.isIskoriscena()==false) {//i ako karta nije iskorisccena
				System.out.println(k);//na konzolu se ispisuje ta karta
			}
		}
	}
}

		
	
	
	

	
	



