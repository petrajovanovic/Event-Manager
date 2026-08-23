package korisnici;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;

import app.Platforma;
import entities.Dogadjaj;
import entities.Karta;
import entities.Mesto;
import enums.VrstaKarte;
import interfejsi.Serializable;

public class Organizator extends Korisnik implements Serializable<Organizator>{
	private ArrayList<String> dogadjaji;
	
	
	public Organizator() {
        super();
        this.dogadjaji = new ArrayList<>();
    }
    
    public Organizator(String korisnickoIme, String lozinka) {
        super(korisnickoIme, lozinka);
        this.dogadjaji = new ArrayList<>();
    }
    
    public Organizator(String korisnickoIme, String lozinka, ArrayList<String> dogadjaji) {
        super(korisnickoIme, lozinka);
        this.dogadjaji = dogadjaji != null ? dogadjaji : new ArrayList<>();
    }
	

	public ArrayList<String> getDogadjaji() {
		return dogadjaji;
	}

	public void setDogadjaji(ArrayList<String> dogadjaji) {
		this.dogadjaji = dogadjaji;
	}
	

	
	@Override
	public String toCSV(Organizator object) {
		
		//pretvaranje liste identifikatora u string
		String dogadjajiIDs="";
		if(object.dogadjaji!=null&&!object.dogadjaji.isEmpty()) {
			dogadjajiIDs=String.join(";",object.dogadjaji);
		}else {
			dogadjajiIDs=" ";
		}
		
		return "Organizator|"+object.korisnickoIme+"|"+object.lozinka+"|"
		+" "+"|"+" "+"|"+" "+"|"+dogadjajiIDs;
	}
	
	@Override 
	public Organizator fromCSV(String csv) throws ParseException {
		//pravljenje kopije instance sa trenutnim vrednostima
		Organizator organizatorKopija=new Organizator();
		organizatorKopija.korisnickoIme=this.korisnickoIme;
		organizatorKopija.lozinka=this.lozinka;
		organizatorKopija.dogadjaji=this.dogadjaji;
		
		//podatke iz datoteke podeliti na listu stringova
		String [] kolone=csv.split("\\|");
		if (kolone.length!=7) {
			//ako dodje do neuspesnog parsiranja vracamo izuzetak
			throw new ParseException("Nije moguce parsirati podatke",0);
			}
		try {
			this.korisnickoIme=kolone[1];
			this.lozinka=kolone[2];
			//pravi se prazna lista za dogadjaje
			this.dogadjaji=new ArrayList<>();
			//provera da li ima dogadjaja u csv datoteci
			if (!kolone[6].trim().isEmpty()) {
				String [] dogadjajiIDs=kolone[6].split(";");//deljenje stringa na listu stringova
				for(String dogadjajID:dogadjajiIDs) {
					this.dogadjaji.add(dogadjajID.trim());//ubacuju se stringovi u praznu listu
				}
			}
		}catch(NumberFormatException e) {
			//vraca se kopija ako dodje do greske
			this.dogadjaji=organizatorKopija.dogadjaji;
			this.korisnickoIme=organizatorKopija.korisnickoIme;
			this.lozinka=organizatorKopija.lozinka;
			throw new ParseException("Grska pri parsiranju clanova",0);
		}
		return this;
	}
	
	public Dogadjaj kreirajDogadjaj(ArrayList<Dogadjaj> dogadjaji) {
		Dogadjaj dogadjaj=new Dogadjaj();
		LocalDate datumPocetka;
		LocalDate datumKraja;
		LinkedHashMap <String,String> sadrzaj=new LinkedHashMap<String,String>();
		Mesto mesto=new Mesto();
		System.out.println("Kreirajte zeljeni dogadjaj");
		
		while(true) {
		System.out.println("Unesite identifikator dogadjaja:");
		String id=Platforma.SC.nextLine();
		dogadjaj.setIdentifikator(id);
		if(dogadjaj.proveraID(dogadjaji)) {
			break;
		}else {
			System.out.println("Uneti identifikator vec postoji. Pokusajte ponovo.");
		}}
		
		
		System.out.println("Unesite naziv dogadjaja:");
		String naziv=Platforma.SC.nextLine();
		dogadjaj.setNaziv(naziv);
		
		while(true) {
			try {
			System.out.println("Unesite datum pocetka (u formatu: 12.07.2005)");
			String unos=Platforma.SC.nextLine();
			datumPocetka=LocalDate.parse(unos, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			dogadjaj.setDatumPocetka(datumPocetka);
			
			System.out.println("Unesite datum kraja (u formatu: 12.07.2005)");
			String unos1=Platforma.SC.nextLine();
			datumKraja=LocalDate.parse(unos1, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			dogadjaj.setDatumKraja(datumKraja);
			
			if(datumPocetka.isBefore(datumKraja)||datumPocetka.isEqual(datumKraja)) {
			break;
			}else {
				System.out.println("Datum pocetka mora da bude pre datuma kraja. pokusajte ponovo");
			}
			}catch (DateTimeParseException e) {
				System.out.println("Uneli ste pogresan format datuma. Pokusajte ponovo.");
			}}
			
			long brojDana=ChronoUnit.DAYS.between(datumPocetka, datumKraja)+1;
			System.out.println("Unesite sadrzaj dogadjaja:");
			for(long i=1;i<=brojDana;i++) {
				String dan=i+". dan";
				System.out.println("Unesite sadrzaj za "+i+".dan:");
				String s=Platforma.SC.nextLine();
				sadrzaj.put(dan, s);
			}
			dogadjaj.setSadrzaj(sadrzaj);
			
			System.out.println("Unesite mesto gde se doagadjaj odrzava:");
			String mestoUnos=Platforma.SC.nextLine();
			mesto=new Mesto();
			mesto.setNaziv(mestoUnos);
			dogadjaj.setMesto(mesto);
			
			this.dogadjaji.add(dogadjaj.getNaziv());
			
			
			return dogadjaj;
			
		}
	
	public void kreiranjeKarataZaDogadjaj(Dogadjaj dogadjaj) {
		//Kreiranje INDIVIDUALNIH karata
		int ind;
		double cena;
		while (true) {
		try {
		System.out.println("Koliko individualnih karata zelite da napravite?");
		ind=Platforma.SC.nextInt();
		Platforma.SC.nextLine();
		System.out.println("Kolika zelite da bude cena?");
		cena=Platforma.SC.nextDouble();
		Platforma.SC.nextLine();
		break;
		}catch(InputMismatchException e) {
			System.out.println("Uneli ste nevalidnu vrednost. Pokusajte ponovo");
			Platforma.SC.nextLine();//"Pojede" pogresan unos da bi se while petlja normalno nastavila
		}}
		int brojac=1;
		for(int i=0;i<ind;i++) {
			Karta karta=new Karta(dogadjaj, brojac, cena, VrstaKarte.INDIVIDUALNA);
			karta.upisKarte();
			brojac++;
			
		}
		
		//Kreiranje GRUPNIH karata
				while (true) {				
				try {
				System.out.println("Koliko grupnih karata zelite da napravite?");
				ind=Platforma.SC.nextInt();
				Platforma.SC.nextLine();
				System.out.println("Kolika zelite da bude cena?");
				cena=Platforma.SC.nextDouble();
				Platforma.SC.nextLine();
				break;
				}catch(InputMismatchException e) {
					System.out.println("Uneli ste nevalidnu vrednost. Pokusajte ponovo");
					Platforma.SC.nextLine();
				}}
				
				for(int i=0;i<ind;i++) {
					Karta karta=new Karta(dogadjaj, brojac, cena, VrstaKarte.GRUPNA);
					karta.upisKarte();
					brojac++;
					
				}
				
				//Kreiranje PORODICNIH karata
				while (true) {
				System.out.println("Koliko porodicnih karata zelite da napravite?");
				try {
				ind=Platforma.SC.nextInt();
				Platforma.SC.nextLine();
				System.out.println("Kolika zelite da bude cena?");
				cena=Platforma.SC.nextDouble();
				Platforma.SC.nextLine();
				break;
				}catch(InputMismatchException e) {
					System.out.println("Uneli ste nevalidnu vrednost. Pokusajte ponovo");
					Platforma.SC.nextLine();
				}}
				
				for(int i=0;i<ind;i++) {
					Karta karta=new Karta(dogadjaj, brojac, cena, VrstaKarte.PORODICNA);
					karta.upisKarte();
					brojac++;
					
				}
	}
	
	public void prikazKupljenihKarata() {
		//ukoliko korisnik unese ID dogadjaja koji ne postoji, baca se ovaj exception zbog problema oko
		//povezivanja tog dogadjaja sa kartom
		System.out.println("Unesite ID dogadjaja za koji zelite da vidite kupljene karte");
		String dogadjajID=Platforma.SC.nextLine();
		ArrayList<Karta> karte=Karta.ucitajKarte();//ucitavanje svih karata
		for(Karta k:karte) {
			//ako se prolsedjeni ID dogadjaj poklapa sa dogadjajem u karti
			if(k.getDogadjaj().getIdentifikator().equals(dogadjajID)&&
			//i ako je ime kupca razlicito od praznog stringa
			k.getKupac().getKorisnickoIme().isEmpty()){
				System.out.println(k);//karta se stampa
			}
		}
	}
	
	public static void prikazAktiviranihKarata(){//Metoda je staticka jer i validtor moze da je koristi
		System.out.println("Unesite ID dogadjaja za koji zelite da vidite kupljene karte");
		String dogadjajID=Platforma.SC.nextLine();
		
		ArrayList<Karta> karte=Karta.ucitajKarte();
		for(Karta k:karte) {
			if(k.isIskoriscena()==true&&//ako je karta iskoriscena i ako se prolsedjeni dogadjaj poklapa
				k.getDogadjaj().getIdentifikator().equals(dogadjajID)) {
				System.out.println(k);
			}
		}
	}
	}
	
	
	
	

