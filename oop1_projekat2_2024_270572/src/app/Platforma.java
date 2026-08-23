package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import entities.Dogadjaj;
import entities.Karta;
import korisnici.Clan;
import korisnici.Korisnik;
import korisnici.Organizator;
import korisnici.Validator;

public class Platforma {
	private ArrayList<Korisnik> korisnici;
	private ArrayList<Dogadjaj> dogadjaji;
	private String naziv;
	
	public static final Scanner SC = new Scanner(System.in);//konstanta za sve klase gde se koristi
	
	Platforma(){
		this.korisnici=new ArrayList<>();
		this.dogadjaji=new ArrayList<>();
	}
	
	
	public Platforma(ArrayList<Korisnik> korisnici,
            ArrayList<Dogadjaj> dogadjaji,
            String naziv) {
		this.korisnici = (korisnici != null) ? new ArrayList<>(korisnici) : new ArrayList<>();
		this.dogadjaji = (dogadjaji != null) ? new ArrayList<>(dogadjaji) : new ArrayList<>();
		this.naziv = naziv;
}

	
	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(ArrayList<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public ArrayList<Dogadjaj> getDogadjaji() {
		return dogadjaji;
	}

	public void setDogadjaji(ArrayList<Dogadjaj> dogadjaji) {
		this.dogadjaji = dogadjaji;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public static ArrayList<Korisnik>  ucitajKorisnike(){//metoda za ucitavanje svih korisnika iz fajla
		ArrayList<Korisnik> korisnici=new ArrayList<Korisnik>();
		File f=new File("data/korisnici.csv");
		if(!f.exists()) {//ako korisnici.csv ne postoje vraca se prazna lista
			return korisnici;
		}
		try (BufferedReader br=new BufferedReader(new FileReader(f))){
			String linija;
			br.readLine();
			while((linija=br.readLine())!=null) {
				String[] kolone=linija.split("\\|");
				if (kolone[0].equals("Clan")) {//provera tipa korisnika
					Clan clan=new Clan();//pravljenje objekta tog tipa
					try {
						clan.fromCSV(linija);//dodavanje informacija
						korisnici.add(clan);//dodavanje korinika u listu korisnici
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (kolone[0].equals("Validator")) {
					Validator validator=new Validator();
					try {
						validator.fromCSV(linija);
						korisnici.add(validator);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (kolone[0].equals("Organizator")) {
					Organizator organizator=new Organizator();
					try {
						organizator.fromCSV(linija);
						korisnici.add(organizator);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
			}
			br.close();
		}catch(IOException e) {
			System.out.println("Nije moguce otvoriti fajl korisnici.csv");
		}
		
		return korisnici;
	}
	
	
	public static ArrayList<Dogadjaj> ucitavanjeDogadjaja(){
		//ucitavanje svih dogadjaja u listu iz dogadjaji.csv
		ArrayList<Dogadjaj> dogadjaji=new ArrayList<Dogadjaj>();
		File f=new File("data/dogadjaji.csv");
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			String linija;
			br.readLine();
			while ((linija=br.readLine())!=null) {
				Dogadjaj dogadjaj=new Dogadjaj();
				try {
					dogadjaj.fromCSV(linija);
					dogadjaji.add(dogadjaj);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			return dogadjaji;//Ako fajl ne postoji vraca se prazna lista
		} catch (IOException e) {
			System.out.println("Nije moguce otvoriti fajl dogadjaji.csv");
			e.printStackTrace();
		}
		return dogadjaji;
	}
	
	public static void azurirajKorisnike(ArrayList<Korisnik> korisnici) {//Azuriranje podataka u korisnici.csv
		File f=new File("data/korisnici.csv");
		String linija="";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			bw.write("tip|korisnickoIme|lozinka|brojTelefona|novcanaSredstva|kupljeneKarte|dogadjaji");
	        bw.newLine();
		    for (Korisnik korisnik : korisnici) {
		    	if (korisnik instanceof Clan) {
		    	Clan k=(Clan) korisnik;
		    	 linija=k.toCSV(k);
		    	}else if(korisnik instanceof Validator) {
		    		Validator k=(Validator) korisnik;
		    		 linija=k.toCSV(k);
		    	}else if(korisnik instanceof Organizator) {
		    		Organizator k=(Organizator) korisnik;
		    		 linija=k.toCSV(k);
		    	}
		        bw.write(linija);
		        bw.newLine();
		    }
		    bw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static void azurirajDogadjaje(ArrayList<Dogadjaj> dogadjaji) {
		//Azuriranje podataka u dogadjaji.csv
		File f=new File("data/dogadjaji.csv");
		String linija="";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			bw.write("identifikator|naziv|datumPocetka|datumKraja|sadrzaj|mesto");
	        bw.newLine();
		    for (Dogadjaj dogadjaj : dogadjaji) {
		    	linija=dogadjaj.toCSV(dogadjaj);
		        bw.write(linija);
		        bw.newLine();
		    }
		    bw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static Clan registracijaKorisnika(ArrayList<Korisnik> korisnici) {
		Clan clan=new Clan();
		//unos podataka
		while(true) {
		System.out.print("Unesite zeljeno korisnicko ime:");
		clan.setKorisnickoIme(SC.nextLine());
		if(clan.proveraKorisnickogImena(korisnici)) {
		break;
		}else {
			System.out.println("Uneto korisnicko ime vec postoji. Pokusajte ponovo");
			continue;
		}
	}
		
		System.out.print("Unesite zeljenu lozinku:");
		clan.setLozinka(SC.nextLine());
		
		System.out.print("Unesite Vas broj telefona:");
		clan.setBrojTelefona(SC.nextLine());
		
		clan.setNovcanaSredstva(0.0);
		
		clan.setKupljeneKarteIDs(null);
		//pozivanje metode za upis clanova u CSV fajl
		return clan;
		}
	
	
	public static Korisnik prijavaKorisnika(ArrayList<Korisnik> korisnici) {
		while (true) {
		System.out.print("Unesite Vase korisnicko ime");
		String korisnickoIme=SC.nextLine();
		System.out.print("Unesite Vasu lozinku:");
		String lozinka=SC.nextLine();
		
		for(Korisnik korisnik:korisnici) {
			if(korisnik.getKorisnickoIme().equals(korisnickoIme)&&korisnik.getLozinka().equals(lozinka)) {
				System.out.println("Uspesno ste se prijavili kao "+korisnik.getKorisnickoIme());
				return korisnik;
			}
		}
		System.out.println("Uneti podaci se ne poklapaju. Pokusajte ponnovo.");
		}	
	}
	
	public static void proveraAktivnihDogadjaja(ArrayList<Dogadjaj> dogadjaji) {
		//proverava koji dogadjaji su aktivni
		System.out.println("Trenutni aktivni dogadjaji su:");
		for(Dogadjaj dogadjaj:dogadjaji) {
			if(dogadjaj.aktivniDogadjaj()) {
				System.out.println(dogadjaj.toString());//Ako je dogadjaj aktivan, ispisuje se
			}
		}
	}
	

	public static void main(String[] args) {
		
		Korisnik korisnik=null;
		Clan clan=null;
		Validator validator=null;
		Organizator organizator=null;
		int opcija1;
		int opcijaOrganizator=0;
		int opcijaClan=0;
		int opcijaValidator=0;
		
		Platforma platforma=new Platforma();
		platforma.korisnici=ucitajKorisnike();
		platforma.naziv="Event RS";
		platforma.dogadjaji=ucitavanjeDogadjaja();
		
		boolean radiAplikacija=true;
		
		System.out.println("Dobrodosli u "+platforma.naziv+"!");
		while(radiAplikacija) {//Petlja koja kontrolise rad same aplikacije
		
		while(true) {
		System.out.println("Zelite li da se:");
		System.out.println("1 - Prijavite se");
		System.out.println("2 - Registrujte se");
		System.out.println("3 - Izlaz iz aplikacije");
		System.out.print("Odaberite opciju:");
		
		try{
			//ako korisnik unese karakter koji nije broj hvata se izuzetak
			opcija1=SC.nextInt();
		}catch(InputMismatchException e) {
			System.out.println("Uneli ste nevalidnu opciju. Pokusajte ponovo.");
			opcija1=0;//vraca se nula kako bi se while petlja nastavila i omogucila ponovni unos podataka
		}
		SC.nextLine();
		
		if (opcija1 >= 1 && opcija1 <= 3) {
			break;
		}
		if (opcija1<1&&opcija1>3) {
			System.out.println("Niste uneli validnu opciju. Pokusajte ponovo.");
			
		}
		}
		
		if(opcija1==3) {//IZLAZ IZ APLIKACIJE
			System.out.println("Hvala na koriscenju aplikacije. Dovidjenja!");
	        radiAplikacija = false;//petlja se prekida,samim tim i rad aplikacije
		}
		if (opcija1==2) {//REGISTRACIJA KORISNIKA
			Clan noviClan=registracijaKorisnika(platforma.korisnici);
			platforma.korisnici.add(noviClan);//novi clan se dodaje na listu svih korisnika
			System.out.println("Uspesno ste se registrovali!");
			azurirajKorisnike(platforma.korisnici);//fajl se potom azurira
			continue;//rad aplikacije se nastavlja
		}
		
		if(opcija1==1) {//PRIJAVA KORISNIKA
			korisnik=prijavaKorisnika(platforma.korisnici);
		}
		
		if(korisnik instanceof Organizator) {;//OPCIJE ZA ORGANIZATORA
		organizator=(Organizator) korisnik;
		while(true) {//petlja odgovorna za rad organizatora
			while(true) {
			System.out.println("Odaberite jednu od sledecih opcija:");
			System.out.println("1 - Kreiranje dogadjaja");
			System.out.println("2 - Pretraga dogadjaja");
			System.out.println("3 - Prikaz kupljenih karata");
			System.out.println("4 - Prikaz aktivnih karata");
			System.out.println("5 - Odjava");
			
			try {
				opcijaOrganizator=SC.nextInt();
				SC.nextLine();
			}catch(InputMismatchException e) {
				opcijaOrganizator=0;
				SC.nextLine();//ponisti pogresan unos u skeneru
			}
			if(opcijaOrganizator>=1&&opcijaOrganizator<=5) {
				break;
			}
			if(opcijaOrganizator<1||opcijaOrganizator>5) {
				System.out.println("Niste uneli validnu opciju.Pokusajte ponovo");
			}
			}
			
			if(opcijaOrganizator==1) {//KREIRANJE DOGADJAJA
				Dogadjaj dogadjaj=organizator.kreirajDogadjaj(platforma.dogadjaji);//organizator kreira dogadjaj
				platforma.dogadjaji.add(dogadjaj);//taj dogadjaj se dodaje u listu dogadjaja
				azurirajDogadjaje(platforma.dogadjaji);//azuriranje datoteke dogadjaja
				organizator.kreiranjeKarataZaDogadjaj(dogadjaj);
				azurirajKorisnike(platforma.korisnici);//azuriranje datoteke korisnika
			}
			
			if(opcijaOrganizator==2) {//PRETRAGA DOGADJAJA
				organizator.pretragaDogadjaja(platforma.dogadjaji);
			}
			
			if(opcijaOrganizator==3) {//PRIKAZ KUPLJENIH KARATA
				organizator.prikazKupljenihKarata();
			}
			
			if(opcijaOrganizator==4) {//PRIKAZ AKTIVNIH KARATA
				Organizator.prikazAktiviranihKarata();
			}
			
			if(opcijaOrganizator==5) {//ODJAVA
				organizator=null;//organizator se postavi na null
				korisnik=null;//korisnik se postavi na null
				System.out.println("Odjavljeni ste.");
				break;//prekida se while petlja odgovorna za rad organizatora
			}
		}	
		}
		
		
		if(korisnik instanceof Clan) {;//OPCIJE ZA CLANA
		clan=(Clan) korisnik;
		while(true) {
		while(true) {
		System.out.println("Odaberite jednu od sledecih opcija:");
		System.out.println("1 - Kupovina karata");
		System.out.println("2 - Pretraga dogadjaja");
		System.out.println("3 - Prikaz kupljenih karata");
		System.out.println("4 - Odjava");
		
		try {
			opcijaClan=SC.nextInt();
		}catch(InputMismatchException e) {
			opcijaClan=0;
		}
		SC.nextLine();
		
		if(opcijaClan>=1&&opcijaClan<=4) {
			break;
		}
		
		if(opcijaClan<1||opcijaClan>4) {
			System.out.println("Niste uneli validnu opciju.Pokusajte ponovo");
		}
		}
		
		if(opcijaClan==1) {//KUPOVINA KARATA
			proveraAktivnihDogadjaja(platforma.dogadjaji);
			ArrayList<Karta> karte=Karta.ucitajKarte();
			clan.kupovinaKarata(karte);
			azurirajKorisnike(platforma.korisnici);
			Karta.azurirajKarte(karte);
			azurirajDogadjaje(platforma.dogadjaji);
		}
		
		if(opcijaClan==2) {//PRETRAGA DOGADJAJA
			clan.pretragaDogadjaja(platforma.dogadjaji);
		}
		
		if (opcijaClan==3) {//PRIKAZ KUPLJENIH KARATA za clana
			System.out.println("Vase kupljene karte:");
			clan.prikazKupljenihKarata();
		}
		
		if(opcijaClan==4) {//ODJAVA
			clan=null;
			korisnik=null;
			System.out.println("Odjavljeni ste.");
			break;
			
		}	
	}
	}
		
		if(korisnik instanceof Validator) {;//OPCIJE ZA VALDATORA
		validator=(Validator) korisnik;
		while(true) {
		while(true) {
		System.out.println("Odaberite jednu od sledecih opcija:");
		System.out.println("1 - Validacija karte");
		System.out.println("2 - Aktivacija karte");
		System.out.println("3 - Pretraga aktivnih dogadjaja");
		System.out.println("4 - Prikaz aktivnih karata");
		System.out.println("5 - Odjava");
		
		try {
			opcijaValidator=SC.nextInt();
		}catch(InputMismatchException e) {
			opcijaValidator=0;
		}
		SC.nextLine();
		
		if(opcijaValidator>=1&&opcijaValidator<=5) {
			break;
		}
		
		if(opcijaValidator<1||opcijaValidator>5) {
			System.out.println("Niste uneli validnu opciju.Pokusajte ponovo");
		}
		}
		
		if(opcijaValidator==1){  //VALIDACIJA KARTE
			System.out.println("Unesite ID karte koju zelite da validirate");
			String kartaID=SC.nextLine();
			try{
				validator.validacijaKarte(kartaID); 
			}catch(NullPointerException e) {
				System.out.println("Ne postoji karta sa unetim ID-jem.");
			}
			
		}
		
		if(opcijaValidator==2) {//AKTIVACIJA KARTE
			System.out.println("Unesite ID karte koju zelite da aktiviratte");
			String kartaID=SC.nextLine();
			try {
			validator.aktiviranjeKarte(kartaID);
			}catch(NullPointerException e) {
				System.out.println("Trazena karta nije pronadjena");
			}
		}
		
		if(opcijaValidator==3) {//PRETRAGA AKTIVNIH DOGADJAJA
			validator.pretragaDogadjaja(platforma.dogadjaji);
		}
		
		if(opcijaValidator==4) {//PRIKAZ AKTIVNIH KARATA
			Organizator.prikazAktiviranihKarata();
		}
		
		if(opcijaValidator==5) {//ODJAVA
			validator=null;
			korisnik=null;
			System.out.println("Odjavljeni ste.");
			break;
			
		}
		
	}
}	
}
}
}



















