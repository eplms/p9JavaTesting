package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CompteComptableTest {

	@Test
	public void getByNumero() {
		//Arrange
		// Créer une liste de compte comptable
		List <CompteComptable> listCompteComptable= new ArrayList<CompteComptable>();
		CompteComptable banque=new CompteComptable();
		banque.setNumero(512);
		banque.setLibelle("Banque");
		listCompteComptable.add(banque);

		CompteComptable client=new CompteComptable();
		client.setNumero(411);
		client.setLibelle("Client");
		listCompteComptable.add(client);
		
		CompteComptable fournisseur=new CompteComptable();
		fournisseur.setNumero(401);
		fournisseur.setLibelle("Fournisseur");
		listCompteComptable.add(fournisseur);
		
		//Act
		// Chercher un compte avec un numéro arbitraire
		
		//Assert
		// Vérifier que le compte renvoyé est bien le compte attendu
		assertEquals(client, CompteComptable.getByNumero(listCompteComptable,411));
		
		listCompteComptable.clear();
		CompteComptable caisse=new CompteComptable();
		caisse.setNumero(530);
		caisse.setLibelle("Caisse");
		listCompteComptable.add(caisse);

		CompteComptable vente=new CompteComptable();
		vente.setNumero(701);
		vente.setLibelle("Ventes");
		listCompteComptable.add(vente);
		
		CompteComptable achat=new CompteComptable();
		achat.setNumero(601);
		achat.setLibelle("Achats");
		listCompteComptable.add(achat);
		
		assertNotEquals(achat, CompteComptable.getByNumero(listCompteComptable,701));

	}

}
