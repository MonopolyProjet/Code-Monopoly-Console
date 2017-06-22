package src;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SingleSelectionModel;

public class Jeu {
	// les attributs
	String nom;
	static int nbJoueur;
	static ArrayList <Joueur> lesJoueurs;
	private static int ordre;
	
	
	//constructeur pour nouvelle partie
	Jeu (String nom) {
		this.nom = nom;
		lesJoueurs = new ArrayList <Joueur> ();
		ordre = 0;
	}
	
	// methode pour construire une partie pour un chargement (reprendre une partie en cours)
	Jeu (String nomPartieACharger, int num) throws FileNotFoundException, IOException {
		// on declare le fichier dans lequel on va lire
		File file = new File ("Sauv" +nomPartieACharger +File.separator +nomPartieACharger +".txt");
				
		// si le fichier existe on va faire les operation suivante
		if (file.exists())
		{
			// on test si pas de probleme pour trouver le fichier a lire
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
			// on declare l'ecouteur
			Scanner sc = new Scanner (fis);
			String temp = "";
			
			this.nom = sc.next();
			this.ordre = sc.nextInt();
			this.nbJoueur = sc.nextInt();
			
			// on ajoute les joueurs a la liste de joueur en les construissant en meme temps
			while (sc.nextLine() != "/")
			{
				this.lesJoueurs.add(new Joueur (this.nom, sc.nextLine()));
			}
			temp = sc.nextLine();
			// on lance la creation du plateau
			Plateau p = new Plateau (this.nom, sc.nextLine());
		}	
		
		catch(FileNotFoundException e){
			System.out.println("Pas de fichier de ce nom trouv� : "+e.getMessage());
		}
		
	} // fin du constructeur
	
	
	// methode pour sauvegarder le jeu
	public void sauvegarde(Plateau p) {
		try
		{
			// on y place le nouveau fichier texte
			File dossier = new File ("Sauv" +this.nom +File.separator);
			if (dossier.isDirectory() == false)
					dossier.mkdir();
			
			// on crée le fichier dans le dossier de la sauvegarde
			File file = new File (dossier +File.separator +this.nom + ".txt");
						
				
			PrintWriter pw = new PrintWriter (file);
			pw.write(nom +"\n");
			pw.write(ordre);
			pw.write(lesJoueurs.size() +"\n");
			pw.write("/" +"\n");  		// pour symbolyser le saut de ligne
			// on va donner le nom des fichiers a ecrire pour les joueurs
			for (int i=0; i<this.nbJoueur; i++)
				pw.write("joueur" +i +".txt" +"\n");	// on met le numero pour pouvoir le reconstruire a partir de ces fichiers
			pw.write("/" +"\n");  		// pour symbolyse le saut de ligne
			pw.write("plateau.txt");
			pw.close();
			
			// on lance la sauvegarde de tous les joueurs
			for (int i=0; i<lesJoueurs.size(); i++)
				lesJoueurs.get(i).sauvegarde(this.nom, (lesJoueurs.get(i).getNom() +".txt") );
			// on lance la sauvegarde du plateau
			p.sauvegarde(this.nom, "plateau.txt");	
			
		}
		catch (IOException exception)
		{
			System.out.println("Impossible d'ecrire la sauvegarde " +exception.getMessage());
		}
		
			
		
	}
	
	// fonction pour creer les joueurs
	public void creerJoueur () {
		
		int i = 0; // compteur pour la declaration des joueurs
		int tempCouleur;
		String tempNom;
		// ArrayList avec toutes les couleurs de pion
		ArrayList arrayPions = new ArrayList <String>();
		arrayPions.add("Rouge");
		arrayPions.add("Bleu");
		arrayPions.add("Noir");
		arrayPions.add("Rose");
		arrayPions.add("Marron");
		arrayPions.add("Mauve");
		arrayPions.add("Cyan");
		arrayPions.add("Vert");
		Scanner scInt = new Scanner(System.in); // pour recuperer les entiers tapes
		Scanner scString = new Scanner(System.in); // pour recup les string tapes
		
		// on va creer les joueurs mais on doit savoir combien il y en a
		System.out.print("\n" +"Entrez mainenant le nombre de joueur de cette partie (entre 2 et 8): ");
		this.nbJoueur = scInt.nextInt();	//on donne une valeur a la variable
		
		// on verifie que le nombre de joueur
		while (nbJoueur < 2 ||  nbJoueur > 8 )
		{
			System.out.print("\n" +"Nombre de joueur incorrect : Entrez maintenant le nombre de joueur de cette partie (entre 2 et 8) : ");
			nbJoueur = scInt.nextInt();
		}
		
		// on donne l'info sur le nombre de joueur 
		System.out.println("Cette partie va donc se jouer avec " +this.nbJoueur +" joueurs");
					
		System.out.println("\n" +"Vous aller entrer les noms de tous les joueurs avant de commencer la partie" +"\n");
		// on fait une boucle pour creer les joueurs un par un
		while (i<nbJoueur)
		{
			boolean valide = false;
			boolean trouve = false;
			String tempNom1 = "";
			while (valide == false)
			{
				System.out.print("\n" +"Entrez le nom d'un des joueurs: ");
				tempNom1 = scString.nextLine();
								
				// on va verifier que le nom n'est pas deja pris par un autre joueur
				for (int j=0; j<this.lesJoueurs.size(); j++)
				{
					if (tempNom1 == lesJoueurs.get(j).getNom())
						trouve = true;
				}
				if (trouve)
				{
					System.out.println("Ce nom est deja pris par un autre joueur, recommencez");
					valide = false;
				}
				else
					//sinon on passe au deuxieme joueur
					valide = true;
			}
				
			// on va afficher le tableau des pions avec leurs couleurs
			for (int j=0; j<8-i; j++)
				System.out.println( j+1 + ": " + arrayPions.get(j));
						
			// on recupere la reponse
			System.out.print("Entrez le chiffre correspondant a la couleur du pion que le joueur veut : ");
			tempCouleur = scInt.nextInt ();
			// on verifie que la donnees est valide
			if(tempCouleur > 8 - i) // -i car une couleur est enlever à chaque fois
			{
				System.out.print("Nombre invalide veuillez recommencer ... Saisisez votre reponse : ");
				// on recupere la reponse
				tempCouleur = scInt.nextInt ();
			}
			else
			{
				// ajoute a la liste de joueur et declaration des objets joueur
				lesJoueurs.add(new Joueur (tempNom1, (String)arrayPions.get(tempCouleur-1)));	//-1 pour retomber sur les indices du tableau
			}
			
			arrayPions.remove(tempCouleur-1); 	//on enlève la couleur pour indiquer que la couleur n'est plus disponible
			i++; // on augmente i pour passer à la construction du joueur suivant
		
		
			// on avertie que le joueur a ete cree
			System.out.println("Joueur " +tempNom1 +" cree");
			
		}		
	} // fin methode pour creer les joueurs
	
	// fonction pour voir si une propriete n'est pas deja a quelqu'un
	boolean appartientDeja (Case c) {
		boolean appartient = false;
		
		for (int i=0; i<nbJoueur; i++)
		{
			if (lesJoueurs.get(i).aCase(c))
				appartient = true;
		}
		return appartient;
	}

	// methode pour lancer les des
	private int lanceDes () {
		int nbAlea = 0;
		
		
		Random r = new Random(); //Generation du nombre aleatoire
		nbAlea = 2 + r.nextInt(12 - 2); //On fait en sorte que le nombre soit compris entre 2 et 12
		
		return nbAlea;
	}
	
	// methode pour tirer un nombre au hasard (pour les cartes)
	private static int nombreAlea () {
		int nbAlea = 0;
		
		Random r = new Random(); //Generation du nombre aléatoire
		nbAlea = 2 + r.nextInt(16); //On fait en sorte que le nombre soit compris entre 2 et 12
		
		return nbAlea;
	}
		
	// methode pour les encheres
	private void mettreAuxEncheres (int ordre) {
		// on parcourt tout la liste de joueur pour qu'il rencherissent chacun leur tour jusqu'a ce qu'ils ne veulent plus
		// on recuperer la valeur de base de cette propriete
		int valeurProp = lesJoueurs.get(ordre).getCaseActuelle().getPrix();
		// on cree un liste avec les joueurs participants a l'enchere
		ArrayList <Joueur> joueursEnCourse = new ArrayList <Joueur> (); 
		
		// on va creer une ArrayListe de joueur avec tout les joueurs
		for (int i=0; i<nbJoueur; i++)
		{
			if (i != ordre) // on ajoute pas le joueur qui a refuse la propriete (celui qui joue en ce moment)
				joueursEnCourse.add(lesJoueurs.get(i));
		}
		
		// on va demander a chaque joueur de la liste cree si ils veulent acheter la propriete
		int j = 0;
		while (joueursEnCourse.size() != 1)		// tant qu'il ne reste pas un seul joueur en course on recommence
		{
			// on affiche la valeur actuelle au fur et a mesure
			System.out.println("Valeur actuelle de la propriete : " +valeurProp);
			if (joueursEnCourse.get(j).veutAcheter())
			{
				valeurProp += 20000; 	// le prix de la propriete augmente petit a petit
			}
			else
			{
				// on supprime le joueur de la liste des joueurs interresses
				joueursEnCourse.remove(j);
			}
			
			// on va passer au joueur suivant
			j++;	
		}
		
		// le joueur qui a encherie en dernier va acheter la propriete
		joueursEnCourse.get(0).acheterCaseEnchere(lesJoueurs.get(ordre).getCaseActuelle(), valeurProp);
		
	}	// fin de la methode mettreAuxEnchere
	
	// methode pour tirer une carte chance et l'ensemble de l'execution qui en suit
	private void tirerCarteChance(Plateau p, int ordre) {
		// on tire la carte chance qui est au debut de la pile et on la remet à la fin apres
		CarteChance chance = p.getCarteChance(0);
		// on va l'afficher
		System.out.println(chance);
		
		// on va s'occuper de chacunes des cartes
		// on recuperer l'indice de la carte
		int indCarte = chance.getNum();
		switch (indCarte)
		{
		case 1:
			//"Aller tout droit en prison"
			lesJoueurs.get(ordre).setIndCaseActuelle("prison", 10);
			lesJoueurs.get(ordre).vaPrison();
		break;
		case 2:
			//"Aller à la gare la plus proche. Si vous passez par la case départ recevez 200
			
			if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 7)
			{
				lesJoueurs.get(ordre).setIndCaseActuelle("lyon", 15);
				if(lesJoueurs.get(ordre).getCaseActuelle().appartientA())
				{
					if(lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==1) 
					{
						lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(25000);
						lesJoueurs.get(ordre).retirerArgent(25000);
						}
						else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==2)
						{
							lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(50000);;
							lesJoueurs.get(ordre).retirerArgent(50000);
						}
						else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==3)
						{
							lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(100000);
							lesJoueurs.get(ordre).retirerArgent(100000);
						}
						else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==4)
						{
							lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(200000);
							lesJoueurs.get(ordre).retirerArgent(200000);
						}
					}
				}
				else if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase()==22)
				{
					lesJoueurs.get(ordre).setIndCaseActuelle("nord", 15);
						if(lesJoueurs.get(ordre).getCaseActuelle().appartientA())
						{
							if(lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==1)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(25000);
								lesJoueurs.get(ordre).retirerArgent(25000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==2)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(50000);;
								lesJoueurs.get(ordre).retirerArgent(50000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==3)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(100000);
								lesJoueurs.get(ordre).retirerArgent(100000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==4)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(200000);
								lesJoueurs.get(ordre).retirerArgent(200000);
							}
						}
					}
					else if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase()==36)
					{
						lesJoueurs.get(ordre).setIndCaseActuelle("montparnasse", 5);
						lesJoueurs.get(ordre).gagneArgent(200000);
						if(lesJoueurs.get(ordre).getCaseActuelle().appartientA())
						{
							if(lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==1) 
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(25000);
								lesJoueurs.get(ordre).retirerArgent(25000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==2)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(50000);
								lesJoueurs.get(ordre).retirerArgent(50000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==3)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(100000);
								lesJoueurs.get(ordre).retirerArgent(100000);
							}
							else if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().getNbGares()==4)
							{
								lesJoueurs.get(ordre).getCaseActuelle().getProprietaire().gagneArgent(200000);;
								lesJoueurs.get(ordre).retirerArgent(200000);
							}
						}
					}

		break;
		case 3:
			//"Rdv case Henri-Martin. Si vous passez par la case départ recevez 200 000"
			lesJoueurs.get(ordre).setIndCaseActuelle("henri-martin", 24);
			if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase()>24){
				lesJoueurs.get(ordre).gagneArgent(200000);
			}
		break;
		case 4:
			//"Avancez jusqu'aux Champs Elysées."
			lesJoueurs.get(ordre).setIndCaseActuelle("champs", 37);
		break;
		case 5:
			//"Votre club de danse orientale dans le centre de Paris vous rapporte M1.5 mille."
			lesJoueurs.get(ordre).gagneArgent(1500);
		break;
		case 6:
			//﻿"Payer M15 mille pour nettoyer les 32 fenêtres du Louvres."
			lesJoueurs.get(ordre).payerTaxe(15000);
			p.ajouterArgentPlateau(15000);
		case 7:
			//"Payer M50 milles à chaque joueur en échange de nourriture provenant du monde entier." 
			for(int i=0; i<nbJoueur; i++){
				if(ordre!=i) {
					lesJoueurs.get(ordre).payerTaxe(50000);
					lesJoueurs.get(i).gagneArgent(50000);
				}
			}
		break;
		case 8:
		break;
		case 9:
			//"Reculez de trois cases."
			lesJoueurs.get(ordre).reculerJoueur(3);
		break;
		case 10:
			//"Sortie de prison, vous pouvez garder cette carte pour sortir de prison"
			lesJoueurs.get(ordre).ajouterCarteChance(new CarteChance(10));
		break;
		case 11:
		break;
		case 12:
			//"Avancez jusqu'à la case départ, recevez M200 milles."
			lesJoueurs.get(ordre).setIndCaseActuelle("depart", 0);
			lesJoueurs.get(ordre).passeCaseDepart();
		break;
		case 13:
			// "Avancez jusqu'aux boulevard de la villette. Si vous passez par la case départ, recevez M200 milles."
			lesJoueurs.get(ordre).setIndCaseActuelle("villette", 11);
			if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase()>11){
				lesJoueurs.get(ordre).gagneArgent(200000);
			}
		break;
		case 14:
		break;
		case 15:
			//"Votre entreprise de réparation de bicyclette vous rapporte 50000"
			lesJoueurs.get(ordre).gagneArgent(50000);
		break;
		case 16:
			//"Vous faites redécorer vos propriétés par un designer de renommée internationale. 
			//Pour chaque maison payer M25 milles, pour chaque hôtel payer M100 Milles."
			lesJoueurs.get(ordre).payerTaxe(lesJoueurs.get(ordre).getNbMaisons()*25000);
			lesJoueurs.get(ordre).payerTaxe(lesJoueurs.get(ordre).getNbHotels()*100000);
		break;
		} // fin du switch
		
		// on supprime la carte au debut de la liste pour la mettre a la fin
		p.mettreAjourListeCarteChance();
	} // fin de la méthode
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// methode pour tirer une carte caisse de communaute et l'ensemble de l'execution qui en suit
	private void tirerCarteCaisseCommunaute (Plateau p, int ordre) {
		// on tire une carte au hasard dans la liste des cartes
		CarteCommunaute co = p.getCarteCommunaute(0);
		// on va l'afficher
		System.out.println(co);
		
		// on recuperer l'indice de la carte
		int indCarte = co.getNum();
		switch (indCarte)
		{
		case 1:
			// "La taxe d'habitation augmente. Payer M40 milles par maison. Payer M115 milles par hôtel."
			int prixMaison = 40000, prixHotel = 115000;
			// on compte cb d'hotel et cb de maison
			int nbMaison = 0, nbHotel = 0;
			// on parcourt tout ses propriété et on compte le nombre de maison et d'hotem
			// on recupere le nombre de propriété
			int nbProp = lesJoueurs.get(ordre).getNbProp();
			// boucle pour avoir compter
			for (int i=0; i<nbProp; i++)
			{
				// compteur prend le nombre de maison sur la propriete du joueur
				nbMaison += lesJoueurs.get(ordre).getProprieteJoueur(i).getNbMaison();
				nbHotel += lesJoueurs.get(ordre).getProprieteJoueur(i).getNbHotel();
			}
			// on fait le total de ce qu'il va devoir depenser
			int retrait = nbMaison * prixMaison + nbHotel * prixHotel;
			// on le retire au joueur
			lesJoueurs.get(ordre).retirerArgent(retrait);
			p.ajouterArgentPlateau(retrait);
		break;
		case 2:
			// "Vous organisez une fête privée sur les bords de Seine. Payer M50 milles."
			lesJoueurs.get(ordre).retirerArgent(50000);
			p.ajouterArgentPlateau(50000);
		break;
		case 3:
			// "Votre compagnie ferroviaire dégage de gros profits. Recevez M200 milles."
			lesJoueurs.get(ordre).gagneArgent(200000);
		break;
		case 4:
			// "Le fisc effectue un contrôle de vos impôts. Allez en prion. Avancez tout droit en prison. 
			//Ne passez pas par la case départ. Ne toucher pas M200 milles."
			Case cPrison = p.getCase(11);
			lesJoueurs.get(ordre).avancerJoueur(cPrison, 11);
			System.out.println("Vous etes maintenant en prison ...");
		break;
		case 5:
			// "Vous bénéficiez d'une remise d'impôt. Recevez M50 milles."
			lesJoueurs.get(ordre).gagneArgent(50000);
		break;
		case 6:
			// "L'organisation de votre propre festival de musique vous rapporte M10 milles."
			lesJoueurs.get(ordre).gagneArgent(10000);
			break;
		case 7:
			// "Tout le monde se cotise pour participer à un séjour à la campagne. Chacun vous verse M10 milles."
			int somme = (nbJoueur - 1) * 10000;
			lesJoueurs.get(ordre).gagneArgent(somme);
			// on enleve l'argent a tout les autres joueurs
			for (int i=0; i<ordre; i++)
				lesJoueurs.get(i).retirerArgent(10000);
			for (int j=ordre+1; j<nbJoueur+1; j++)
				lesJoueurs.get(j).retirerArgent(10000);
		break;
		case 8:
			// "Votre boutique parisienne vous rapport M25 milles."
			lesJoueurs.get(ordre).gagneArgent(25000);
		break;
		case 9:
			// "Vous louez votre musée d'art pour une exposition internationale. Recevez M100 milles."
			lesJoueurs.get(ordre).gagneArgent(100000);
		break;
		case 10:
			// "Vous organisez le festival de la Saint-Patrick à Pigale.Payer M100 milles."
			lesJoueurs.get(ordre).retirerArgent(100000);
			p.ajouterArgentPlateau(100000);
		break;
		case 11:
			// "Vous revendez votre costume du carnaval. Recevez M20 milles."
			lesJoueurs.get(ordre).gagneArgent(20000);
		break;
		case 12:
			// "Vous dépensez M50 milles au marché de noël des Champs Elysées."
			lesJoueurs.get(ordre).retirerArgent(50000);
			p.ajouterArgentPlateau(50000);
		break;
		case 13:
			// "Avancez jusqu'à la case départ. Touchez M200 milles."
			Case cDepart = p.getCase(1);
			lesJoueurs.get(ordre).avancerJoueur(cDepart, 1);
			System.out.println("Vous etes maintenant à la case depart.");
			lesJoueurs.get(ordre).passeCaseDepart();
		break;
		case 14:
			// "VOUS ETES LIBERE DE PRISON. Cette carte peut être conservée jusqu'à ce qu'elle soit utilisée ou vendue."
			lesJoueurs.get(ordre).ajouterCarteCommunaute(co);
		break;
		case 15:
			// "Les entrées de votre soirée spéciale Karaoké vous rapportent M10 milles."
			lesJoueurs.get(ordre).gagneArgent(10000);
		break;
		case 16:
			// "Vous louez votre batiment destiné aux sciences à une équipe de chercheurs étrangers. Recevez M100 milles."
			lesJoueurs.get(ordre).gagneArgent(100000);
		break;
		}// fin du sswitch
		
		// on supprime la carte au debut de la liste pour la mettre a la fin
		p.mettreAjourListeCarteCo();
				
	} // fin de la méthode pour tirer une carte caisse de communaute
	
	// methode pour tirer une carte caisse de communaute et l'ensemble de l'execution qui en suit
		private void tomberCasePiege (Plateau p,int ordre) {
			// on va recuperer le nom de la case
			String nomCase = lesJoueurs.get(ordre).getCaseActuelle().getNomCase();
			
			// selon le nom de la case
			if (nomCase == "depart")
			{
				lesJoueurs.get(ordre).passeCaseDepart();
			}
			else if (nomCase == "prison")
			{
				// on regarde si le joueur est juste en visite simple ou dans la prison
				//si il est en prison, on va voir depuis combien de temps il y est, si c'est 3, on le fait sorti
				if (lesJoueurs.get(ordre).getPrison())
				{
					 // on regarde si il a fait ses 3 tours de prison
					if (lesJoueurs.get(ordre).getNbTourEnPrison() == 3)
					{
						// on le fait sortir de prison
						lesJoueurs.get(ordre).sortPrison();
					}
					else // sinon il a le droit de tirer les des et si il fait un double il peut sortir
					{
						if(lanceDes() == 2 || lanceDes() == 4 || lanceDes() == 6 || lanceDes() == 8 || lanceDes() == 10 || lanceDes() == 12)
							lesJoueurs.get(ordre).sortPrison();
						else
							lesJoueurs.get(ordre).IncNbTourPrison();
					}
				}
			}
			else if (nomCase == "aller_prison")
			{
				// le joueur va en prison
				lesJoueurs.get(ordre).vaPrison();
			}
			else if (nomCase == "impots_sur_le_revenu")
			{
				// on retire 200 000 au joueur et on les places sur le plateau
				lesJoueurs.get(ordre).retirerArgent(200000);
				p.ajouterArgentPlateau(200000);
			}
			else if (nomCase == "parc_gratuit")
			{
				// le joueur recupere l'argent du plateau
				p.recupererArgent(lesJoueurs.get(ordre));
			}
			else
			{
				// "taxe_de_luxe" on fait perdre 100 000 au joueur pour les mettre sur le plateau
				lesJoueurs.get(ordre).retirerArgent(100000);
				p.ajouterArgentPlateau(100000);
			}
	} // fin de la procedure
	
	// methode pour gerer l'achat de l'immobilier
	public void acheterImmo(int ordre, Plateau p) {
		int tempRepImmo = 0;
		int reponse = 0;
		int nbMaisonAAjouter = 0;
		Scanner scImmo = new Scanner(System.in);
		
		System.out.println("Voulez vous acheter une maison ou un hotel ?   (selectionnez le numero correspondant)");
		System.out.println("1) Oui");
		System.out.println("2) Non");
		reponse = scImmo.nextInt();
		
		if (reponse == 1)	// si il veut acheter
		{
			System.out.println("Quel type d'immobilier voulez vous acheter ?   (selectionnez le numero correspondant)");
			System.out.println("1) Maison");
			System.out.println("2) Hotel");
			tempRepImmo = scImmo.nextInt();
			
			// on va lancer la fonction correspondante
			// tant qu'il n'a pas fait d'erreur
			boolean erreur = true;
			while (erreur)
			{
				if (tempRepImmo == 1)
				{
					System.out.println("Combien de maison voulez vous ajouter ?   (entrer le nombre)");
					nbMaisonAAjouter = scImmo.nextInt();
					System.out.println("Sur quelle case voulez vous ajouter l'immobilier ?");
					String caseChoisie = scImmo.nextLine();
					// on stock la case dans une variable temporraire
					Case tempCase = p.rechercherCase(caseChoisie);
					
					// on regarde que la case n'est pas hypotheque
					if (!tempCase.estHypotheque())
					{
						// on regarde qu'il n'y a deja pas 4 maisons sur la propriete
						if (tempCase.getNbMaison() + nbMaisonAAjouter <= 4)
						{
							// on ajoute la maison
							tempCase.ajouteMaison(nbMaisonAAjouter, lesJoueurs.get(ordre));
							erreur = false;
						}
						else
							System.out.println("Vous ne pouvez pas ajouter autant de maison, il y a deja� " +lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() +" maisons sur cette case");
					}
					else
						System.out.println("Cette propriete est hypothequee, vous ne pouvez pas ajouter d'immobilier sur cette case");
				}
				
				
				else if (tempRepImmo == 2) // si il veut acheter un hotel
				{
					// tant qu'il a choisi la mauvaise case on recommence
					boolean choisi = false;
					while (!choisi)
					{
						System.out.println("Sur quelle case voulez vous ajouter l'immobilier ?");
						String caseChoisie = scImmo.nextLine();
						// on stock la case dans une variable temporraire
						Case tempCase = p.rechercherCase(caseChoisie);
						
						// on verifie que la case n'est pas hypotheque
						if (!tempCase.estHypotheque())
						{
							tempCase.ajouteHotel(lesJoueurs.get(ordre));
							choisi = true;
						}
						else
							System.out.println("Cette case est hypothequee, vous ne pouvez pas ajoute d'immobilier sur cette case");
				
					}
				}
			}
		}
		
		
		else // si il ne veut pas acheter d'immobilier
			System.out.println("Tres bien, ce sera pour une prochaine fois !");
		
	} // fin de la méthode de la gestion des achats de l'immobilier
	
	// methode pour payer le loyer a un autre joueur lorsque l'on tombe sur sa case
	public void payerLoyer(int ordre){
		// on recupere le joueur qui possede la case
		Joueur j2 = lesJoueurs.get(ordre).getCaseActuelle().getProprietaire();
		
		// on va dabord regarder si la case n'est pas hypothequer
		if (!lesJoueurs.get(ordre).getCaseActuelle().estHypotheque())
		{
			// on regarde si la case a des maisons ou un hotel
			boolean vide = true; 	// pour dire de base que la case n'a rien dessus
			if (lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() != 0 || lesJoueurs.get(ordre).getCaseActuelle().getNbHotel() !=0)
				vide = false;
			
			int somme = 0;
			// si la case est vide, on regarde si il a les trois terrains
			if (vide)
			{
				//on va regarder si il a toute les proprietes de la meme couleur pour multiplié le prix du loyer
				// on va pacourir toute les cartes du joueur qui va recuperer l'argent pour voir si il les a toute
				j2.aTouteCaseCouleur(lesJoueurs.get(ordre).getCaseActuelle().getCouleur());
				// on retire l'argent au joueur est on le donne a l'autre
				somme = (2 * (lesJoueurs.get(ordre).getCaseActuelle().getLoyer()));
			}
			else	// si il y a de l'immobilier sur la case
			{
				
				// on va regarder le nombre de maison qu'il y a sur la propriete
				if (lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() == 1)
					somme = lesJoueurs.get(ordre).getCaseActuelle().getLoyer1Maison();
				else if (lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() == 2)
					somme = lesJoueurs.get(ordre).getCaseActuelle().getLoyer2Maison();
				else if (lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() == 3)
					somme = lesJoueurs.get(ordre).getCaseActuelle().getLoyer3Maison();
				else if (lesJoueurs.get(ordre).getCaseActuelle().getNbMaison() == 4)
					somme = lesJoueurs.get(ordre).getCaseActuelle().getLoyer4Maison();
				else
					somme = lesJoueurs.get(ordre).getCaseActuelle().getLoyerHotel();
			}
			
			lesJoueurs.get(ordre).retirerArgent(somme);
			j2.gagneArgent(somme);
		}
		else
			System.out.println("Cette case est hypotheque, vous ne payer rien !");
		
	} // fin de la methode des loyers
	
	/////////////////////////////////////////////////////
	//////////////// FONCTION MAIN //////////////////////
	/////////////////////////////////////////////////////
	public static void main (String [] args) throws FileNotFoundException, IOException {
		
		Jeu jeu = null;
		Plateau p = null;
		
		
		
		// on va fabriquer une fenetre avec une fenetre affichant le plateau pour mieux repere (aucune action ne sera faites sur ce dernier)
		// on construit la fenetre
		JFrame fenetrePlateau = new JFrame ("Affichage du plateau");
		// on donne toute les propriete a la fenetre
		fenetrePlateau.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetrePlateau.setSize(620, 630);
		
		// on gere le contenu		
		JPanel contenu = new JPanel ();
		// on lui donne son layout
		contenu.setLayout(new GridLayout(1,1));
		// on cree l'image
		JLabel fondMonopoly = new JLabel(new ImageIcon("src\\plateauMonopoly.png"));
		//panelGauche.setLayout(new BorderLayout());
		contenu.add(fondMonopoly);
		// on l'ajoute a la fenetre
		fenetrePlateau.add(contenu);
		
		// on affiche la fenetre
		fenetrePlateau.setVisible(true);
		
		
		// on va demander si on veut charger une partie ou en creer une
		Scanner scInt = new Scanner(System.in); // pour recuperer ce qui est taper au clavier (entre dans le terminal)
		Scanner scString = new Scanner(System.in); // pour recup les string
		int reponseSauv = 0;
		
		// on va voir comment le joueur veut commencer a jouer
		while (reponseSauv != 1 && reponseSauv != 2)
		{
			
			System.out.println("1) Nouvelle partie");
			System.out.println("2) Charger partie");
			System.out.print("Que voulez vous faire ? 	(Entrez le numero correspondant) : ");
			reponseSauv = scInt.nextInt();
		}
		if (reponseSauv == 1)
		{
			// On creer le jeu
			System.out.print ("Veuillez entrer le nom de la partie: ");
			String nom = scString.nextLine();
			
			// si le nom saisie n'est pas vide on lance la creation
			if (nom != "")
			{
				// on r�ecrit le nom taper
				System.out.println("Votre partie s'appellera : " +nom);
				// on cree le jeu et le plateau
				jeu = new Jeu (nom);
				p = new Plateau();
							
				// on appel la methode pour creer les joueur
				jeu.creerJoueur();
				
				// on dit la somme de depart a tout les joueur au debut du jeu
				System.out.println("\n" +"//////////////////////////////////////");
				System.out.println("Pour toutes les saisies a faire, ne taper pas les accens, remplacez les par un caractere simple");
				System.out.println("Tout les joueurs ont recu 1 500 000 au debut de la partie");
				System.out.println("//////////////////////////////////////" +"\n");
			}
		}
		else if(reponseSauv == 2)
		{
			System.out.print ("Veuillez entrer le nom de la partie a charger (faire attention aux majuscules ...): ");
			String nom = scString.nextLine();
			// cr�ation du jeu a partir d'une sauvegarde
			try{	
			jeu = new Jeu (nom, 1);
			}
			catch(FileNotFoundException e){
				System.out.println("Nom de partie introuvable : "+e.getMessage());
			}
		}
			
		
		
		
		// on va faire marcher les joueurs
		boolean fini = false; 	// lorsque la partie doit d'arreter (si tout les joueurs sont ruin�)
		int nbPlusDansLeJeu = 0;
		
		// on va verifier qu'un joueur n'est pas ruine sinon on le supprime de la liste des joueurs
		for (int j=0; j<lesJoueurs.size(); j++)
		{
			//si il est ruine on le retire de la liste des joueurs et on fait un compteur pour donner le nombre de joueur encore en course
			if (lesJoueurs.get(j).ruine())
			{
				lesJoueurs.remove(j);
				nbPlusDansLeJeu++;
			}
			
			// on verifie que tout les joueurs ne sont pas tous ruin�
			if(nbPlusDansLeJeu == nbJoueur)
			{
				fini = true;
			}
		}
		
		while (!fini) // tant que tout les joueurs ne sont pas ruin� ou que l'on ne veut pas quitter on continu de faire tourner le jeu
		{
			///////////////////////////////////////////////////////////
			/////////////////   deplacement du joueur /////////////////
			
			// on affiche la case actuelle du joueur pour qu'il sache ou il est sur le plateau
			System.out.println("\n");
			System.out.println("\n");
			
			// affiche infos
			/// Bloc pour avertir le joueur sur sa situation
			// nom joueur -- nom case actuelle -- argent 
			// nombre de propriete
			// liste des nom de propriete
			System.out.println("\n" +"////////////////////////////////////////////////////////");
			System.out.println("joueur : " +lesJoueurs.get(ordre).getNom() +"\t" +"//" +"\t" +"Case actuelle : " +lesJoueurs.get(ordre).getCaseActuelle().getNomCase() +"\t" +"//" +"\t"+"Argent : " +lesJoueurs.get(ordre).getArgent());
			System.out.println("Vous avez : " +lesJoueurs.get(ordre).getNbProp() +" propriete(s)");
			lesJoueurs.get(ordre).afficheListeProp();
			System.out.println("////////////////////////////////////////////////////////" +"\n");
			
			// on va savoir de combien de case il va avancer en lancant les des
			int nbCasesAvance = jeu.lanceDes();
			// si il y a double 6 il ramasse l'argent sur la plateau
			if (nbCasesAvance == 12)
			{
				// il recuperer l'argent du plateau
				lesJoueurs.get(ordre).gagneArgent(p.getArgentPlateau());
				p.retirerArgentPlateau();	// on remet l'argent du plateau a 0
			}
			
			// on gere le deplacement du joueur desormais
			int ind = lesJoueurs.get(ordre).getIndCaseActuelle() + nbCasesAvance % 40;	// on recupere l'indice de la nouvelle case
			lesJoueurs.get(ordre).avancerJoueur(p.getCase(ind), nbCasesAvance);		// on dit au joueur d'avancer jusqu'a la nouvelle case
			
			// phrase pour dire de combien de case le joueur a avancer et on affiche sa case actuelle
			System.out.println("\n" +lesJoueurs.get(ordre).getNom() + " avance de " + nbCasesAvance + " cases.");
			System.out.println(lesJoueurs.get(ordre).getNom() + " est maintenant a la case : " + lesJoueurs.get(ordre).getCaseActuelle() +"\n");
			
			/////////////////////////////////////////////////////////////////////////////
			/////// ON VA VOIR L'ACTION A REALISER EN FONCTION DU TYPE DE LA CASE ///////
			
			// si c'est une case de type chance
			if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 7 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 22 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 36)	
			{
				// on lance la methode qui s'occupe des actions des cartes chance
				jeu.tirerCarteChance(p, ordre);		// on envoi le plateau et l'indice du joueur	
			}
			
			// si c'est une case de type caisse de communaute
			else if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 2 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 17 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 33)	
			{
				// on lance la methode qui s'occupe des actions des cartes chance
				jeu.tirerCarteCaisseCommunaute(p, ordre);		// on envoi le plateau et l'indice du joueur				
			}
			
			// si c'est une case piege (taxes, prison ... )
			else if (lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 0 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 4 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 10 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 20 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 30 || lesJoueurs.get(ordre).getCaseActuelle().getNumCase() == 38)
			{
				// on lance la methode pour s'occuper des cases pieges
				jeu.tomberCasePiege(p, ordre);
			}
			
			// si c'est une carte de propriete
			else	
			{
				// on va voir si la propriete n'appartient deja pas a quelqu'un si c'est non lui demande si il veut acheter
				if (lesJoueurs.get(ordre).getCaseActuelle().getProprietaire() == null)
				{
					// on va voir si le joueur veut acheter la propriete
					if (lesJoueurs.get(ordre).veutAcheter())	// si le joueur veut acheter
					{
						//il l'acheter et on donne une phrase de reponse
						lesJoueurs.get(ordre).acheterCase(lesJoueurs.get(ordre).getCaseActuelle());
						System.out.println("Vous avez bien acheter cette propriete");
					}
					else	// si il la veut pas, on va la mettre aux encheres
						jeu.mettreAuxEncheres(ordre);
				}
				else // si elles est a quelqu'un, il va payer le taxe
					jeu.payerLoyer(ordre);
			}
			
			// on va voir si le joueur a la possibilite d'acheter de l'immobilier, si c'est possible on lui demande ce qu'il veut acheter
			if (lesJoueurs.get(ordre).peutAcheterImmo ())	// on verifie qu'il a trois cartes de la même couleur
			{
				// on va lancer la fonction pour acheter de l'immobilier
				jeu.acheterImmo(ordre, p);
			}
			
			// afficher les infos
			System.out.println("\n" +"////////////////////////////////////////////////////////");
			System.out.println("joueur : " +lesJoueurs.get(ordre).getNom() +"\t" +"//" +"\t" +"Case actuelle : " +lesJoueurs.get(ordre).getCaseActuelle().getNomCase() +"\t" +"//" +"\t"+"Argent : " +lesJoueurs.get(ordre).getArgent());
			System.out.println("Vous avez : " +lesJoueurs.get(ordre).getNbProp() +" propriete(s)");
			lesJoueurs.get(ordre).afficheListeProp();
			System.out.println("////////////////////////////////////////////////////////" +"\n");
			
			
			
			int repQuitter = 0;
			while (repQuitter != 1 && repQuitter != 2)
			{
				// on va voir si les joueurs veulent arreter de jouer
				System.out.println("Voulez vous quitter le jeu ?");
				System.out.println("1) Oui");
				System.out.println("2) Non");
				repQuitter = scInt.nextInt();
				
				if (repQuitter == 1)
				{
					int repSauv = 0;
					while (repSauv != 1 && repSauv !=2)
					{
						// on va demander si les joueurs veulent sauvegarder
						System.out.println("Voulez vous sauvegarder la partie ?	(taper le numero correspondant)");
						System.out.println("1) Oui");
						System.out.println("2) Non");
						repSauv = scInt.nextInt();
						if (repSauv == 1)
						{
							// on sauvegarde et on informe que �a c'est bien passer
							jeu.sauvegarde(p);
							System.out.println("Sauvegarde correctement effectuee");
						}
						else if (repSauv == 2)
							System.exit(0);
					}
					// on dit de finir
					fini = true;
				}
				else if (repQuitter == 2)
				{
					// on reparamettre la joueur qui va jouer
					ordre = (ordre + 1) % lesJoueurs.size();	// pour passer au joueur suivant
				}
				
			} // fin de la demande de quitter le jeu ou pas
			
			System.out.println("On va passer au joueur suivant");	
		
		} // fin du while pour la fin du jeu
		
	} // Fin de la fonction main
	
} 	// Fin de la classe Jeu

/** Tests realises :
 * Creer une nouvelle partie : exception apr�s le nom de la partie mais le jeu continu
 * Charger une partie en metant un nom faux : exception FileNotFound : ajout d'un catch dans le constructeur pour charger les parties
 * Indiquer un nombre de joueur correct : Ok
 * Indiquer une couleur : exception FileNotFound 
 * 						+ exception FileInputStream lors de la demande du nom des joueurs d'apr�s
 * Bug : caract�ristique des cases (prix, nom, couleurs, etc) = null
 * R�pondre non a la demande d'achat : exception IndexOutOfBoundsException --> arrete l'appli
 * Acheter une propriete : Ok
 * Continuer la partie apr�s un tour : Ok 
 * Sauvegarder : NullPointerException --> arrete l'appli 
 */
