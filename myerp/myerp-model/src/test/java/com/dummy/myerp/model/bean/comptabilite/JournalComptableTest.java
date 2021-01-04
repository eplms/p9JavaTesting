package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JournalComptableTest {

	@Test
	public void getByCode() {
		// Création de la liste de journaux comptables
		List<JournalComptable> listJournauxComptables=new ArrayList<JournalComptable>();
		
		// Création des journaux et ajouts des journaux dans la liste
		JournalComptable journalComptableAchat=new JournalComptable();
		journalComptableAchat.setCode("Achat");
		journalComptableAchat.setLibelle("Journal des achats");
		listJournauxComptables.add(journalComptableAchat);
		
		JournalComptable journalComptableVente=new JournalComptable();
		journalComptableVente.setCode("Vente");
		journalComptableVente.setLibelle("Journal des ventes");
		listJournauxComptables.add(journalComptableVente);
		
		JournalComptable journalComptableTresorerie= new JournalComptable();
		journalComptableTresorerie.setCode("Trésorerie");
		journalComptableTresorerie.setLibelle("Journal de trésorerie");
		listJournauxComptables.add(journalComptableTresorerie);
		
		JournalComptable journalComptableOperationsDiverses= new JournalComptable();
		journalComptableOperationsDiverses.setCode("OpérationsDiverses");
		journalComptableOperationsDiverses.setLibelle("Journal des Opérations Diverses");
		listJournauxComptables.add(journalComptableOperationsDiverses);
		//Assert
		
		assertEquals(journalComptableOperationsDiverses,JournalComptable.getByCode(listJournauxComptables, "OpérationsDiverses"));

		
		assertNotEquals(journalComptableTresorerie,JournalComptable.getByCode(listJournauxComptables,"Vente"));
	}

}
