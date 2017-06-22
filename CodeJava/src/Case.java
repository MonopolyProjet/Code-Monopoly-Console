package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Case {
	private int numCase;
	private String nom;
	private int nbMaison;
	private int nbHotel;
	private Joueur proprietaire;
	private int valeur;
	
	private int loyer;
	private String couleurCase;
	private int loyer1maison;
	private int loyer2maison;
	private int loyer3maison;
	private int loyer4maison;
	private int loyerHotel;
	private int hypo;
	private int prixMaison;
	private int prixHotel;
	
	private boolean hypotheque = false;
	
	// constructeur
	Case(String nom, int ind) { // est le nom de la carte qui doit etre construite
		this.numCase = ind;
		// on declare le nouveau fichier dans lequel on va lire
		File f = new File ("src/CartePropriete/" +nom +".txt");
		
		// si le fichier existe on va faire les operations suivantes
		if (f.exists())
		{
			// on test si pas de probleme
			try {
				f.createNewFile();
			}
			// si erreur
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} // fin du if
		
		// on va s'occuper de la lecture
		try (FileInputStream fis = new FileInputStream(f)) {
			// on cree un scanner
			Scanner sc = new Scanner (fis);
			// on attrape maintenant les informations ligne par ligne (tant qu'il y en a)
			// pour les cartes sans valeurs numérique de loyer dedans
			if (nom == "depart" || nom == "aller_prison" || nom == "impots_sur_le_revenu" || nom == "chance" || nom == "communaute" || nom == "parc_gratuit" || nom == "taxe_de_luxe" || nom == "prison")
				this.nom = sc.nextLine();
			// pour les gares
			else if (nom == "lyon" || nom == "nord" || nom == "montparnasse" || nom == "saint-lazare")
			{
				this.nom = sc.nextLine();
				this.loyer1maison = sc.nextInt();
				this.loyer2maison = sc.nextInt();
				this.loyer3maison = sc.nextInt();
				this.loyer4maison = sc.nextInt();
				this.hypo = sc.nextInt();
			}
			// pour les compagnies d'eau et electricite
			else if (nom == "eau" || nom == "electricite")
			{
				this.nom = sc.nextLine();
				this.loyer1maison = sc.nextInt();
				this.loyer2maison = sc.nextInt();
			}
			// pour les cartes de propriété
			else
			{
				this.nom = sc.nextLine();
				this.couleurCase = sc.nextLine();
				this.loyer = sc.nextInt();
				this.loyer1maison = sc.nextInt();
				this.loyer2maison = sc.nextInt();
				this.loyer3maison = sc.nextInt();
				this.loyer4maison = sc.nextInt();
				this.loyerHotel = sc.nextInt();
				this.hypo = sc.nextInt();
				this.prixMaison = sc.nextInt();
				this.prixHotel = sc.nextInt();
			}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// valeurs par defauts
		this.valeur = 2 * this.hypo;
		this.nbMaison = 0;
		this.nbHotel = 0;
		this.proprietaire = null;
		this.hypotheque = false;
		
	} // fin du constructeur
	
	
	// methode pour construire une partie pour un chargement (reprendre une partie en cours)
	Case (String nomPartieACharger, String fichierACharger, int num) throws FileNotFoundException, IOException {
		// on declare le fichier dans lequel on va lire
		File file = new File ("Sauv" +nomPartieACharger +File.separator +fichierACharger);
					
		// si le fichier existe on va faire les operations suivantes
		if (file.exists())
		{
			// on test si pas de probleme
			try 
			{
				file.createNewFile();
			}
			// si erreur
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} // fin du if
				
		// on va s'occuper de la lecture
		try (FileInputStream fis = new FileInputStream(file)) 
		{
			// on cree un scanner
			Scanner sc = new Scanner (fis);
			
			String temp = "";
			
			// on va recuperer tout les attributs un par un
			this.numCase = sc.nextInt();
			this.nom = sc.nextLine();
			this.nbMaison = sc.nextInt();
			this.nbHotel = sc.nextInt();
			// on recupere le joueur proprietaire
			this.proprietaire = new Joueur(nomPartieACharger, sc.nextLine()+".txt", 1);
			
			this.valeur = sc.nextInt();
			this.loyer = sc.nextInt();
			this.couleurCase = sc.nextLine();
			this.loyer1maison = sc.nextInt();
			this.loyer2maison = sc.nextInt();
			this.loyer3maison = sc.nextInt();
			this.loyer4maison = sc.nextInt();
			this.loyerHotel = sc.nextInt();
			this.hypo = sc.nextInt();
			this.prixMaison = sc.nextInt();
			this.prixHotel = sc.nextInt();
			if (sc.nextLine() == "true")
				this.hypotheque = true;
			else
				this.hypotheque = false;
				
		}
	} // fin du constructeur
	
		
	// methode pour sauvegarder la case
	public void sauvegarde(String nomPartie) {
		try
		{
			// on y place le nouveau fichier text
			File dossier = new File ("Sauv" +nomPartie +File.separator);
			if (dossier.isDirectory() == false)
				dossier.mkdir();
			
			// on creer le fichier dans le dossier de la sauvegarde
			File file = new File (dossier +File.separator +this.nom +".txt");
						
				
			PrintWriter pw = new PrintWriter (file);
			//on y place tout les attributs
			pw.write(this.numCase);
			pw.write(this.nom);
			pw.write(this.nbMaison);
			pw.write(this.nbHotel);
			// pour le proprietaire, on marque son nom
			pw.write(this.proprietaire.getNom());
			pw.write(this.valeur);
			pw.write(this.loyer);
			pw.write(this.couleurCase);
			pw.write(this.loyer1maison);
			pw.write(this.loyer2maison);
			pw.write(this.loyer3maison);
			pw.write(this.loyer4maison);
			pw.write(this.loyerHotel);
			pw.write(this.hypo);
			pw.write(this.prixMaison);
			pw.write(this.prixHotel);
			if (this.hypotheque)
				pw.write("true");
			else
				pw.write("false");
		}
		catch (IOException exception)
		{
			System.out.println("Impossible d'ecrire la sauvegarde de cette case" +exception.getMessage());
		}
	}
	
	
	
	
	
	
	
	//////////////////////////////////////////////////////////
	////// Ensemble des getteur pour les loyers //////////////
	public int getLoyer () {
		return this.loyer;
	}
	public int getLoyer1Maison () {
		return this.loyer1maison;
	}
	public int getLoyer2Maison () {
		return this.loyer2maison;
	}
	public int getLoyer3Maison () {
		return this.loyer3maison;
	}
	public int getLoyer4Maison () {
		return this.loyer4maison;
	}
	public int getLoyerHotel () {
		return this.loyerHotel;
	}
	public int getHypo () {
		return this.hypo;
	}
	
	
	// methode pour ajouter une maison
	void ajouteMaison (int nbMaison, Joueur j) {
		this.nbMaison += nbMaison;
		// on enleve l'argent au joueur
		j.retirerArgent(nbMaison * prixMaison);
	}
	
	// methode pour supprimer une maison 
	private void suppMaion (int nbSupp) {
		this.nbMaison -= nbSupp;
	}
	
	//methode pour recuperer le nombre de maison sur la case
	int getNbMaison() {
		return nbMaison;
	}
	
	// methode pour ajouter un hotel
	void ajouteHotel(Joueur j) { // il ne peut y avoir que une maison
		if (nbMaison == 4)
		{
			this.nbHotel = 1;
			this.nbMaison = 0;
			// on enleve l'argent au joueur
			j.retirerArgent(prixHotel);
		}
		else
			System.out.println("Il manque des maisons pour faire mettre un hotel ...");
	}
		
	// methode pour supprimer un hotel 
	private void suppHotel () {
		this.nbHotel--;
		this.nbMaison = 4;
	}
	
	// methode pour recuperer le nombre d'hotel
	int getNbHotel () {
		return nbHotel;
	}
	
	// methode pour retourner la couleur
	public String getCouleur() {
		return this.couleurCase;
	}
	
	// procedure return boolean true if it belongs to someone (si la case appartient a quelqu'un)
	public boolean appartientA(){
		if (this.proprietaire!=null) return(true);
		else return(false);
	}
		
	// methode pour recuperer le nom
	String getNomCase () {
		return this.nom;
	}
	
	// methode pour retourner le numero de la case
	public int getNumCase() {
		return (this.numCase%40);
	}
	
	// methode pour assigner un proprietaire
	void ajouterProprio (Joueur j) {
		this.proprietaire = j;
	}
	
	// methode pour recuperer le proprietaire
	Joueur getProprietaire () {
		return this.proprietaire;
	}
	
	//methode pour recuperer le prix
	int getPrix () {
		return this.valeur;
	}
	
	// methode toString
	public String toString() {
		String s = "";
		// pour les cases sans valeur de loyer
		if (numCase == 0 && numCase == 30 && numCase == 10 && numCase == 4 && numCase == 7 && numCase == 22 && numCase == 36 && numCase == 2 && numCase == 17 && numCase == 33 && numCase == 20 && numCase == 38)
			s = s +nom;
		// pour les compagnies
		else if (numCase == 12 && numCase == 28)
			s = s  +nom +"\n" +"Prix de la case : " +valeur +"\n" +"Multiplier par " +loyer1maison +" la somme des des si vous avez une des compagnies, sinon par " +loyer2maison;
		//pour les gares
		else if (numCase == 5 && numCase == 15 && numCase == 25 && numCase == 35)
			s = s +"gare " +nom +"\n" +"Prix de la case : " +valeur +"\n" +"Somme a multiplier par le nombre de gare : " +loyer1maison;
		// pour les cartes de propriete
		else
			s = s +nom +"\n" +"Valeur : " +this.valeur +"\n" +"Couleur : " +couleurCase +"\n" +"prix de la case : " +valeur +"\n" +"Loyer: " +loyer +"\n" +"1 maison: " +loyer1maison +"\n" +"2 maisons: " +loyer2maison +"\n" +"3 maisons: " +loyer3maison +"\n" +"4 maisons: " +loyer4maison +"\n" +"loyer hotel: " +loyerHotel +"\n" +"Prix immobilier: " +prixMaison;
		return(s);
	}
	
	
	
	/////////////////////////////////////////////////////
	//////////////// FONCTION MAIN //////////////////////
	/////////////////////////////////////////////////////
	
	public static void main (String [] args) {
	// on va cree 3 cartes (les trois premieres)
	Case c1 = new Case ("champs", 0);
	Case c2 = new Case ("fayette", 1);
	Case c3 = new Case ("foch", 2);
	Case c4 = new Case ("depart", 3);
	Case c5 = new Case ("chance", 4);
	
	// on va les afficher
	System.out.println(c1);
	System.out.println(c2);
	System.out.println(c3);
	System.out.println(c4);
	
	// on affiche le nom de la case
	c1.getNomCase();
	
	// on créer un propriétaire
	Joueur j1 = new Joueur ("Ludo", "bleu");
	
	// on ajoute un propriétaire 
	c1.ajouterProprio (j1);
	// on recupere le propriétaire et on l'affiche
	System.out.println(c1.getProprietaire ());
	
	// on ajoute a la case champs elysée 4 maisons
	// c1.ajouteMaison (4);
	// on ajoute un hotel
	// c1.ajouteHotel();
	// on affiche le nombre d'hotel sur la case
	System.out.println("Il y a : " +c1.getNbHotel() +" hotel sur cette case");
	System.out.println("Il y a : " +c1.getNbMaison() +" maisons sur cette case");
	// on supprime l'hotel et on réaffiche le nombre d'hotel
	c1.suppHotel();
	System.out.println("Il y a : " +c1.getNbHotel() +" hotel sur cette case");
	System.out.println("Il y a : " +c1.getNbMaison() +" maisons sur cette case");
	// puis on supprime une maison et on la réaffiche
	c1.suppMaion(1);
	System.out.println("Il y a : " +c1.getNbMaison() +" maisons sur cette case");
	
	// enfin on affiche la valeur de la case
	System.out.println("Le prix de cette propriete est de : " +c1.getPrix());
	}

	// methode pour marquer qu'une propriete est hypotheque
	public void hypothequer() {
		this.hypotheque = true;		
	}
	
	// methode pour qu'une case ne soit plus hypotheque
	public void dehypothequer() {
		this.hypotheque = false;
	}
	
	// methode pour vois si une case est hypotheque
	public boolean estHypotheque () {
		return this.hypotheque;
	}
	
}	// fin de la clase Case
