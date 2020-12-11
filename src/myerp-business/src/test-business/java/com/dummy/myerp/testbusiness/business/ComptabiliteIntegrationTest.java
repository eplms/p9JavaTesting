package com.dummy.myerp.testbusiness.business;

import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ComptabiliteIntegrationTest extends BusinessTestCase {
	
	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

	
	@Test
	public void test1() {
		assertNotNull(getBusinessProxy().getComptabiliteManager().getListCompteComptable());
	}
	
	//Test avec une écriture comptable dans un journal pour une année qui n'existe pas en base
	@Test
	public void addReferenceShouldSetReferenceNotNullToEcritureComptable(){
		// ARRANGE : Initialisation d'une écriture comptable sans référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setReference(null);
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

		// ACT : L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// ASSERT : Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertNotNull(vEcritureComptable.getReference());
	}
	
	//Test avec une écriture comptable dans un journal pour lequel une année est déjà en cours et donc dont le numéro de séquence ne sera pas 00001
	@Test
	public void addReferenceShouldUpDateSequenceEcritureComptable() {
		// ARRANGE : Initialisation d'une écriture comptable sans référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		Calendar cal = Calendar.getInstance();
		cal.set(2016,11,20);
		Date date = cal.getTime();
		vEcritureComptable.setDate(date);
		vEcritureComptable.setReference(null);
		vEcritureComptable.setLibelle("Presta Extérieure");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

		// ACT : L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// ASSERT : Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertEquals("AC-2016/00041",vEcritureComptable.getReference());
	}
	
	//Test avec une écriture comptable dans un journal pour lequel une année n'est pas en cours et donc dont le numéro de séquence sera 0001
	@Test
	public void addReferenceShouldSetSequenceEcritureComptable() {
		// ARRANGE : Initialisation d'une écriture comptable sans référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setReference(null);
		vEcritureComptable.setLibelle("Presta 1");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

		// ACT : L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// ASSERT : Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertEquals("VE-2020/00001",vEcritureComptable.getReference());
	}
	
	// Test de checkEcritureComptable avec une écriture comptable comportant une référence existante sans id
	@Test (expected =FunctionalException.class)
	public void checkEcritureComptableShouldThrowExceptionWhenReferenceEcritureComptableAlreadyExistWhithoutId() throws Exception {
		//ARRANGE : Initialisation d'une écriture existante en base à la création de la base
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));		
		Calendar cal = Calendar.getInstance();
		cal.set(2016,11,20);
		Date date = cal.getTime();
		vEcritureComptable.setDate(date);
		vEcritureComptable.setReference("AC-2016/00001");
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
		
	}
	
	// Test de checkEcritureComptable avec une écriture comptable comportant une référence existante avec un id différent de l'écriture portant cette référence
	@Test (expected =FunctionalException.class)
	public void checkEcritureComptableShouldThrowExceptionWhenReferenceEcritureComptableAlreadyExistWithAnotherId() throws Exception {
		//ARRANGE : Initialisation d'une écriture existante en base à la création de la base
		EcritureComptable vEcritureComptable = new EcritureComptable();
		// L'Id de l'écriture en base avec cette référence est -1 et pas 1
		vEcritureComptable.setId(1);
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));		
		Calendar cal = Calendar.getInstance();
		cal.set(2016,11,20);
		Date date = cal.getTime();
		vEcritureComptable.setDate(date);
		vEcritureComptable.setReference("AC-2016/00001");
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
		
	}
	
	// Test de checkEcritureComptable avec une écriture comptable comportant une référence existante avec un id égal de l'écriture portant cette référence
	@Test
	public void checkEcritureComptableShouldNotThrowExceptionWhenReferenceEcritureComptableAlreadyExistWithSameId() throws Exception {
		//ARRANGE : Initialisation d'une écriture existante en base à la création de la base
		EcritureComptable vEcritureComptable = new EcritureComptable();
		// L'Id de l'écriture en base avec cette référence est bien -1
		vEcritureComptable.setId(-1);
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));		
		Calendar cal = Calendar.getInstance();
		cal.set(2016,11,20);
		Date date = cal.getTime();
		vEcritureComptable.setDate(date);
		vEcritureComptable.setReference("AC-2016/00001");
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
		
	}
	
	
	// Test de checkEcritureComptable avec une écriture comptable ne comportant pas de référence
	@Test(expected=FunctionalException.class)
	public void checkEcritureComptableShouldThrowExceptionWhenReferenceEcritureComptableIsEmpty() throws Exception{
		//ARRANGE : Initialisation d'une écriture ne comportant pas de référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setReference("");
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Achat de papeterie");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
	}
	
	
	// Test de checkEcritureComptable avec une écriture comptable ayant une référence qui n'existe pas encore en base
	@Test
	public void checkEcritureComptableShouldNotThrowExceptionWhenReferenceEcritureComptableIsDifferent() throws Exception{
		//ARRANGE : Initialisation d'une écriture ne comportant pas de référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
		vEcritureComptable.setReference("VE-2020/00001");
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Achat de papeterie");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
	}
	
	// Test de checkEcritureComptable avec une écriture comptable ne comportant pas de référence
	@Test(expected=NullPointerException.class)
	public void checkEcritureComptableShouldThrowExceptionWhenReferenceEcritureComptableIsNull() throws Exception{
		//ARRANGE : Initialisation d'une écriture ne comportant pas de référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setReference(null);
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Achat de papeterie");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
		//ACT 
		manager.checkEcritureComptable(vEcritureComptable);
	}
	
	
	
	
	
	
}
