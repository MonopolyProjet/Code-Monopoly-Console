package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Joueur{
	// on declare tous les attributs de cette classe
	private String nom;
	private int argent;
	private String couleur;
	private ArrayList <CarteChance> listeCarteChance;
	private ArrayList <CarteCommunaute> listeCarteCommunaute;
	private ArrayList <Case> listePropriete;
	private Case caseActuelle;
	private int indiceCaseActuelle;
	private int nbGares;
	private boolean enPrison;	// pour voir si le joueur est en prison ou pas
	private int nbTourEnPrison; // pour savoir depuis combien de temps le joueur est en prison
	 
	
	// constructeur 
	public Joueur (String nom, String couleur) {
		this.nom = nom;
		this.couleur = couleur;
		this.argent = 1500000;
		this.listeCarteChance = new ArrayList <CarteChance> ();
		this.listeCarteCommunaute = new ArrayList <CarteCommunaute> ();
		this.listePropriete = new ArrayList <Case> ();
		this.caseActuelle = new Case("depart",0);
		this.indiceCaseActuelle = 0;
		this.nbGares = 0;
		this.enPrison = false; 			// par defaut on est pas en prison
		this.nbTourEnPrison = 0; 		// par defaut il na jamais ete en prison
	}
	
	// methode pour construire une partie pour un chargement (reprendre une partie en cours)
	Joueur (String nomPartieACharger, String fichierACharger, int num) throws FileNotFoundException, IOException {
		
		// on declare le fichier dans lequel on va lire
		File file = new File ("Sauv" +nomPartieACharger +File.separator +fichierACharger);
					
		// si le fichier existe on va faire les operations suivantes
		if (file.exists())
		{
			// on test si pas de probleme pour trouver le fichier
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
			
			String temp = "";	// pour le saut de ligne
			
			// on va recuperer tout les attributs un par un
			this.nom = sc.nextLine();
			this.argent = sc.nextInt();
			this.couleur = sc.nextLine();
			
			// on va recuperer les num�ros de carte chance et ajoute les cartes correspondante � la liste des cartes du joueur
			int indiceTempChance = 0;
			Plateau p = new Plateau ();
			while (sc.nextLine() != "/")
			{
				// on recupere le numero et on recheche la carte avant de l'ajouter
				indiceTempChance = sc.nextInt();
				// on recupere la carte issu du plateau
				this.listeCarteChance.add(p.getCarteChance(indiceTempChance));
			}
			temp = sc.nextLine();
			// on va recuperer les num�ros de carte chance et ajoute les cartes correspondantes � la liste des cartes du joueur
			int indiceTempCo = 0;
			while (sc.nextLine() != "/")
			{
				// on recupere le numero et on recheche la carte avant de l'ajouter
				indiceTempCo = sc.nextInt();
				// on recupere la carte issu du plateau
				this.listeCarteCommunaute.add(p.getCarteCommunaute(indiceTempCo));
			}
			temp = sc.nextLine();
			// on va recuperer toute les cases qu'il a a partir de leur nom, on recherche la case et on la retournera
			String nom = "";
			while (sc.nextLine() != "/")
			{
				// on recupere le nom de la case
				nom = sc.nextLine();
				// ajoute a la liste
				this.listePropriete.add(p.rechercherCase(nom));
			}
			
			this.caseActuelle = p.rechercherCase(sc.nextLine());
			this.indiceCaseActuelle = sc.nextInt();
			this.nbGares = sc.nextInt();
			if (sc.nextLine() == "true")
				this.enPrison = true;
			else
				this.enPrison = false;
			this.nbTourEnPrison = sc.nextInt();
		}
	} // fin du constructeur
	
	
	// methode pour sauvegarder le joueur
	public void sauvegarde(String nomPartie, String nomFichier) {
		try
		{
			// on y place le nouveau fichier texte
			File dossier = new File ("Sauv" +nomPartie +File.separator);
			if (dossier.isDirectory() == false)
					dossier.mkdir();
			
			// on cree le fichier dans le dossier de la sauvegarde
			File file = new File (dossier +File.separator +nomFichier);
						
				
			PrintWriter pw = new PrintWriter (file);
			
			pw.write(this.nom +"\n");
			pw.write(this.argent +"\n");
			pw.write(this.couleur +"\n");
			// on ecrit tout les numeros de carte chance qu'il a (apres un saut de ligne)
			for (int x=0; x<listeCarteChance.size(); x++)
				pw.write(this.listeCarteChance.get(x).getNum() +"\n");
			pw.write("\n");
			// on ecrit tout les numeros de carte comunaute qu'il a (apres un saut de ligne)
			for (int z=0; z<listeCarteCommunaute.size(); z++)
				pw.write(this.listeCarteCommunaute.get(z).getNum() +"\n");
			pw.write("\n");
			// on ecrit tout les noms de carte de propriete qu'il a (apres un saut de ligne)
				for (int w=0; w<listePropriete.size(); w++)
					pw.write(this.listePropriete.get(w).getNomCase() +"\n");
			pw.write("\n");
			// nom de la case actuelle
			pw.write(this.caseActuelle.getNomCase());
			pw.write(this.indiceCaseActuelle);
			pw.write(this.nbGares);
			// on gere si il est en prison
			if (this.enPrison == true)
				pw.write("true");
			else
				pw.write("false");
			pw.write(this.nbTourEnPrison);
			pw.close();
		}
		catch (IOException exception)
		{
			System.out.println("Impossible d'ecrire la sauvegarde de ce joueur " +exception.getMessage());
		}
	}
		
	// methode pour sortir un joueur de prison
	public void sortPrison () {
		this.enPrison = false;
		// on remet son nombre de tour en prison a zero
		this.nbTourEnPrison = 0;
	}
	
	// methode pour ajouter un tour dans la prison
	public void setNbTourPrison () {
		this.nbTourEnPrison++;
	}
	
	// methode pour recuperer le nombre de tour qu'il a passer en prison
	public int getNbTourEnPrison () {
		return this.nbTourEnPrison;
	}
	
	// methode pour mettre joueur en prison
	public void vaPrison () {
		this.caseActuelle = new Case("prison", 10);
		// on change aussi l'indice de la case à celui de la prison
		this.indiceCaseActuelle = 11;
		// on change le boolean pour savoir si le joueur est en prison
		this.enPrison = true;
	}
	
	// methode pour savoir si le joueur est en prison
	public boolean getPrison () {
		return this.enPrison;
	}
	
	//methode pour connaitre le nb de maisons qu'il possède en tout
	public int getNbMaisons(){
		int nbMaisons=0;
		for(int i=0; i<listePropriete.size();i++){
			nbMaisons+=listePropriete.get(i).getNbMaison();
		}
		return(nbMaisons);
	}
		
	//methode pour connaitre le nb d'hotel qu'il possède en tout
	public int getNbHotels(){
		int nbHotels=0;
		for(int i=0; i<listePropriete.size();i++){
			nbHotels+=listePropriete.get(i).getNbHotel();
		}
		return(nbHotels);
	}
	
	//methode pour faire reculer un joueur
	public void reculerJoueur(int nbCases){
		this.indiceCaseActuelle=this.getIndCaseActuelle()-nbCases;
	}
			
	// methode a lancer quand on passe la case depart
	public void passeCaseDepart () {
		gagneArgent(200000);
	}
	
	// methode pour ajouter carte de communaute
	public void ajouterCarteCommunaute (CarteCommunaute carte){
		listeCarteCommunaute.add(carte);
	}
	
	//methode pour ajouter une carte chance
	public void ajouterCarteChance(CarteChance e){
		this.listeCarteChance.add(e);
	}
		
	//methode pour avancer le joueur
	public void avancerJoueur (Case c, int nbCases) {
		this.caseActuelle = c;
		this.indiceCaseActuelle += nbCases;
	}
	
	// methode pour changer la case actuelle depuis une case donnée
	public void setIndCaseActuelle(String nomCase, int numCase){
		this.caseActuelle = new Case(nomCase,numCase);
	}
	
	// methode pour recupere l'indice de la case actuelle
	public int getIndCaseActuelle() {
		return indiceCaseActuelle;
	}
	
	//methode pour recuperer le nom du joueur
	public String getNom(){
		return(this.nom);
	}
	
	// methode pour recuperer la case actuelle
	public Case getCaseActuelle() {
		return caseActuelle;
	}
	// methode pour recuperer l'argent
	public int getArgent () {
		return this.argent;
	}
	
	// methode pour demander si le joueur veut acheter la propriété
	public boolean veutAcheter () {
		boolean veut = false;
		// on affiche le nom pour savoir a qui on parle
		int rep = 0;
		Scanner sc = new Scanner(System.in); // pour le type int
		while (rep != 1 && rep != 2)
		{
			System.out.println(nom);
			System.out.println("Voulez vous acheter la propriete ? (entrer le numero de votre reponse)");
			System.out.println("1) Oui");
			System.out.println("2) Non");
			rep = sc.nextInt();
		}	
					
		if (rep == 1)
			veut = true;
		
		return veut;
	}
	
	// methode pour acheter une propriete
	public void acheterCase (Case c) {
		//on va verifier qu'il peut l'acheter
		if (argent >= c.getPrix())
		{
			this.listePropriete.add(c);	// on ajoute la propriete a sa liste de propriete
			this.argent -= c.getPrix();	// on retire l'argent
			// on l'informe sur son argent
			System.out.println("Il vous reste desormais " +argent +" mille(s)");
		}
		else
			System.out.println("Le joueur n'a pas assez d'argent");
	}

	// methode pour acheter une propriete
	public void acheterCaseEnchere (Case c, int somme) {
		//on va verifier qu'il peut l'acheter
		if (argent >= somme)
		{
			this.listePropriete.add(c);	// on ajoute la propriete a sa liste de propriete
			this.argent -= somme;	// on retire l'argent
			// on l'informe sur son argent
			System.out.println("Il vous reste desormais " +argent +" mille(s)");
		}
		else
			System.out.println("Le joueur n'a pas assez d'argent");
	}
		
	public void payerTaxe(int montantTaxe){
		this.argent-=montantTaxe;
	}
	
	public void gagneArgent (int somme) {
		this.argent += somme;
	}
	
	public void retirerArgent (int somme) {
		// on va regader que le joueur a assez d'argent sinon on passe par l'hypotheque
		while (argent - somme < 0)
		{	
			System.out.println("Vous n'avez pas asser d'argent, il vous manque: " +(somme-argent));
			hypotheque();
		}
		
		this.argent -= somme;
	}
	
	// methode pour gerer l'hypotheque
	public void hypotheque() {
		int indice = 0;
		Scanner sc = new Scanner(System.in); // pour la reponse
		
		// on va afficher tout les noms de ses proprietes et demander le numero de celle qu'il veut bloquer
		System.out.println("Vous allez entrer le numero de la case à hypothequer");
		for (int i=0; i<listePropriete.size(); i++)
		{
			// on va verifie qu'elle n'est pas deja hypotheque
			if (!listePropriete.get(i).estHypotheque())
				System.out.println(i +") " +listePropriete.get(i).getNomCase());
		}
		
		System.out.print("Case à hypothéquer : ");
		indice = sc.nextInt();
		
		// on recupere la veleur d'hypotheque et on l'ajoute a l'argent du joueur
		// et on change le statue de la case
		gagneArgent(listePropriete.get(indice).getHypo());
		listePropriete.get(indice).hypothequer();
		//on lui dit combien il a recupere
		System.out.println("Vous avez recuperer " +listePropriete.get(indice).getHypo() +" milles");
	}


	// methode pour savoir si le joueur a la case
	public boolean aCase (Case c) {
		boolean trouve = false;
		int cpt = 0;
		// on parcourt la liste de ces proprietes
		while (!trouve)
		{
			if (listePropriete.get(cpt) == c)
				trouve = true;
			else
				cpt ++;
		}
		return trouve;
	}
	
	// methode pour afficher toutes les proprietes d'un joueur sous forme de liste
	private void afficherPropri() {
		System.out.println("Le joueur possède toute les propriétés suivantes : " +"\n");
		// on parcourt toutes ses proprietes et on les affiche
		for (int i=0; i<listePropriete.size(); i++)
		{
			System.out.println(listePropriete.get(i) +"\n");
		}
	}
	
	// methode toString du joueur
	public String toString() {
		String s = "";
		
		s = s +"Le joueur s'appel : " +nom +", il a la couleur " +couleur +" et a : " +argent +" milles";
		return s;
	}
	
	// methode pour renvnoyer le nombre de propriete que le joueur a en sa possesion 
	public int getNbProp () {
		return listePropriete.size();
	}
	
	// methode pour retourner une propriete du joueur
	public Case getProprieteJoueur (int ind) {
		return listePropriete.get(ind);
	}
	
	//Methode pour donner le nombre de gares d'un joueur (utilise pour les cartes)
	public int getNbGares(){
		return(this.nbGares);
	}
	
	// methode pour voir si leu joueur peut acheter de l'immobiller donc si il a toute les proprietes d'une meme famille
	public boolean peutAcheterImmo () {
		boolean peutAcheter = false;
		// on va stocker le nombre de case d'une meme couleur
		int nbCarteMarron = 0;
		int nbCarteBleuClair = 0;
		int nbCarteViolet = 0;
		int nbCarteOrange = 0;
		int nbCarteRouge = 0;
		int nbCarteJaune = 0;
		int nbCarteVert = 0;
		int nbCarteBleuFonce = 0;
		String tempCouleur;
		
		//on va parcourir toutes ses cartes
		for (int i=0; i<listePropriete.size(); i++)
		{
			// on va recuperer la couleur de la case a chaque fois et remettre les compteurs a jour
			tempCouleur = listePropriete.get(i).getCouleur();
			
			// on s'occuper des compteurs
			if (tempCouleur == "marron")
				nbCarteMarron++;
			else if (tempCouleur == "bleu clair")
				nbCarteBleuClair++;
			else if (tempCouleur == "violet")
				nbCarteViolet++;
			else if (tempCouleur == "orange")
				nbCarteOrange++;
			else if (tempCouleur == "rouge")
				nbCarteRouge++;
			else if (tempCouleur == "jaune")
				nbCarteJaune++;
			else if (tempCouleur == "vert")
				nbCarteVert++;
			else if (tempCouleur == "bleu fonce")
				nbCarteBleuFonce++;
			
			// on s'occupe du bouleen
			if (nbCarteBleuClair == 3 || nbCarteViolet == 3 || nbCarteOrange == 3 || nbCarteRouge == 3 || nbCarteJaune == 3 || nbCarteVert == 3)
				peutAcheter = true;
			else if (nbCarteMarron == 2 || nbCarteBleuFonce == 2)
				peutAcheter = true;
		}
		return peutAcheter;
	} // fin de la methode peut acheter
	
	// methode pour voir si un joueur a toutes les case d'une meme couleur
	public boolean aTouteCaseCouleur (String couleurRef) {
		// on parcourt toutes ces cartes et on renvoi vrai si il a toute les case de la meme couleur
		int cpt = 0;
		boolean aTouteCase = false;
		for (int i=0; i<listePropriete.size(); i++)
		{
			if (listePropriete.get(i).getCouleur() == couleurRef)
				cpt++;
		}
		// on gere le boolean car toute les couleurs n'ont pas le meme nombre de case
		if (couleurRef == "marron" || couleurRef == "bleu fonce")
		{
			if (cpt == 2)
				aTouteCase = true;
		}
		else
		{
			if (cpt == 3)
				aTouteCase = true;
		}
		return aTouteCase;
	}
	
	// methode pour voir si un joueur est ruine
	public boolean ruine () {
		boolean ruine = false;
		
		// on va regarder si toute ces proprietes sont hypothequees
		boolean touteHypo = false;
		int cptHypo = 0;
		for (int i=0; i<listePropriete.size(); i++)
		{
			if (listePropriete.get(i).estHypotheque())
				cptHypo++;
		}
		if (cptHypo == listePropriete.size())
			touteHypo = true;
		
		if (touteHypo && argent == 0)
			ruine = true;
		
		return ruine;
	}
	
	// methode pour afficher tout les noms de ces proprietes
	public void afficheListeProp() {
		for (int i=0; i<this.listePropriete.size(); i++)
			System.out.println(this.listePropriete.get(i).getNomCase());
	}
	
	//////////////////////////////////////////////////
	/////////// FONCTION MAIN ////////////////////////
	//////////////////////////////////////////////////
	public static void main (String [] args) {
		Joueur j = new Joueur("Bob","Jaune");
		// j.avancerJoueur(6);
		//on affiche le nom du joueur
		System.out.println("Le nom du joueur est : " +j.getNom());
		// on affiche la case actuelle du joueur
		System.out.println("La case actuelle du joueur est : " +"\n" +j.getCaseActuelle());
		// on affiche l'argent qu'il a 
		System.out.println("Le joueur a : " +j.getArgent() +" mille(s)");
		// on lui ajoute 10 mille et on lui retirera mille
		j.gagneArgent(10000);
		System.out.println("Le joueur a : " +j.getArgent() +" mille(s) apres l'ajout");
		j.retirerArgent(1000);
		System.out.println("Le joueur a : " +j.getArgent() +" mille(s) apres le retrait");
		
		//on va lui faire acheter une propriete
		Case c = new Case("champs", 0);
		
		j.acheterCase(c);
		// on va verifier qu'il est la case
		if (j.aCase (c))
			System.out.println("Le joueur a bien la case : " +c);
		else
			System.out.println("La transaction n'a pas fonctionné");
		
		// on va afficher toute les proprietes du joueur
		Case c2 = new Case("pigalle", 1);
		j.acheterCase(c2);
		j.afficherPropri();
		
		// on affiche le joueur directement
		System.out.println(j);
		
	} // fin main
		
} // Fin de la classe joueur
