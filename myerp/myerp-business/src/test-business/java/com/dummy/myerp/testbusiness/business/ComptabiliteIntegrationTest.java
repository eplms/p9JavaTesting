package com.dummy.myerp.testbusiness.business;

import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComptabiliteIntegrationTest extends BusinessTestCase {
	
	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

	
	@Test
	public void test1() {
		assertNotNull(manager.getListCompteComptable());
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
		manager.addReference(vEcritureComptable);
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
		manager.addReference(vEcritureComptable);
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
		manager.addReference(vEcritureComptable);
		// ASSERT : Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertEquals("VE-2021/00001",vEcritureComptable.getReference());
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
		//Si référence null alors NullPointerException
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
		//ARRANGE : Initialisation d'une écriture ne comportant pas de référence existante en base
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
		vEcritureComptable.setReference("VE-2021/00001");
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
	@Test(expected=FunctionalException.class)
	public void checkEcritureComptableShouldThrowExceptionWhenReferenceEcritureComptableIsNull() throws FunctionalException{
		//ARRANGE : Initialisation d'une écriture ne comportant pas de référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		// Si reference null alors NullPointerException
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
	
	
	//Test de insertEcritureComptable avec une écriture comptable correcte
	@Test
	public void insertEcritureComptableShouldWriteEcritureComptable() throws FunctionalException{
		//ARRANGE : Initilisation d'une écriture comptable
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		manager.addReference(vEcritureComptable);
		vEcritureComptable.setLibelle("Achat de prestation");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(512), null, new BigDecimal(102), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(411), null, null, new BigDecimal(102)));
		
		
		//ACT : Utilisation de insertEcritureComptable
		manager.insertEcritureComptable(vEcritureComptable);
		
		//ASSERT : verification de l'écriture comptable en base de données
		/* On ne peut pas comparer les objets car l'objet avant l'écriture en base n'a pas d'id alors que celui 
		 * qui est récupéré en base pour vérification en un qui lui a été attribué autamatiquement par la base.
		 * Ce qui identifie l'écriture comptable est alors la référence
		 * */
		Boolean vEcritureComptablePresenteEnBase=false;
		List<EcritureComptable> listEcritureComptable=manager.getListEcritureComptable();
		for(EcritureComptable ecritureComptable: listEcritureComptable) {
			if(ecritureComptable.getReference().equals(vEcritureComptable.getReference())) {
				vEcritureComptablePresenteEnBase=true;
			}
		}
		assertTrue(vEcritureComptablePresenteEnBase);

		
		//ASSERT INADAPTES : Avant l'insert, l'écriture comptable n'a pas d'Id
		//assertTrue(manager.getListEcritureComptable().contains(vEcritureComptable));
		//assertThat(manager.getListEcritureComptable(),hasItem(vEcritureComptable));
		
	}
	
	
	//Test de insertEcritureComptable avec une écriture comptable incorrecte
		@Test(expected=FunctionalException.class)
		public void insertEcritureComptableShouldThrowNullPointerExceptionWhenEcritureComptableIsIncorrect() throws FunctionalException{
			//ARRANGE : Initilisation d'une écriture comptable n'ayant pas de libellé
			EcritureComptable vEcritureComptable = new EcritureComptable();
			vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
			vEcritureComptable.setDate(new Date());
			manager.addReference(vEcritureComptable);
			//vEcritureComptable.setLibelle("Achat de prestation");
			vEcritureComptable.getListLigneEcriture()
					.add(new LigneEcritureComptable(new CompteComptable(512), null, new BigDecimal(102), null));
			vEcritureComptable.getListLigneEcriture()
					.add(new LigneEcritureComptable(new CompteComptable(411), null, null, new BigDecimal(102)));
			
			
			//ACT : Utilisation de insertEcritureComptable
			
			manager.insertEcritureComptable(vEcritureComptable);

		}
	
	//Test de upDateEcritureComptable avec écriture comptable correcte
	@Test
	public void updateEcritureComptableShouldUpdateEcritureComptableWhenEcritureComptableIsCorrect() throws FunctionalException {
		//ARRANGE : Initilisation d'une écriture comptable modifiée à partir d'une écriture comptable existante
		// Le libellé de la facture a été modifié
			EcritureComptable vEcritureComptable = new EcritureComptable();
			vEcritureComptable.setId(-5);
			vEcritureComptable.setJournal(new JournalComptable("BQ","Banque"));
			vEcritureComptable.setReference("BQ-2016/00005");
			Calendar cal = Calendar.getInstance();
			cal.set(2016,11,27);
			Date date = cal.getTime();
			vEcritureComptable.setDate(date);
			vEcritureComptable.setLibelle("Paiement Facture Emmanuel");
			vEcritureComptable.getListLigneEcriture()
					.add(new LigneEcritureComptable(new CompteComptable(512), null, new BigDecimal(3000), null));
			vEcritureComptable.getListLigneEcriture()
					.add(new LigneEcritureComptable(new CompteComptable(411), null, null, new BigDecimal(3000)));
			
		//ACT
			manager.updateEcritureComptable(vEcritureComptable);
			
		//ASSERT
			EcritureComptable vEcritureComptablePresenteEnBase = null;
			List<EcritureComptable> listEcritureComptable=manager.getListEcritureComptable();
			for(EcritureComptable ecritureComptable: listEcritureComptable) {
				if(ecritureComptable.getId().equals(vEcritureComptable.getId())) {
					vEcritureComptablePresenteEnBase=ecritureComptable;
				}
			}
			assertEquals("Paiement Facture Emmanuel",vEcritureComptablePresenteEnBase.getLibelle());	
	}
	
	//Test de upDateEcritureComptable avec écriture comptable incorrecte
		@Test(expected=FunctionalException.class)
		public void updateEcritureComptableShouldUpdateEcritureComptableWhenEcritureComptableIsNotCorrect() throws FunctionalException {
			//ARRANGE : Initilisation d'une écriture comptable modifiée à partir d'une écriture comptable existante
			// La référence de l'écriture a une chaine nulle
				EcritureComptable vEcritureComptable = new EcritureComptable();
				vEcritureComptable.setId(-5);
				vEcritureComptable.setJournal(new JournalComptable("BQ","Banque"));
				Calendar cal = Calendar.getInstance();
				cal.set(2016,11,27);
				Date date = cal.getTime();
				//vEcritureComptable.setReference("BQ-2016/00005");
				//Si référence null alors NullPointerException
				vEcritureComptable.setReference("");
				vEcritureComptable.setLibelle("Paiement facture Emmanuel");
				vEcritureComptable.setDate(date);
				vEcritureComptable.getListLigneEcriture()
						.add(new LigneEcritureComptable(new CompteComptable(512), null, new BigDecimal(3000), null));
				vEcritureComptable.getListLigneEcriture()
						.add(new LigneEcritureComptable(new CompteComptable(411), null, null, new BigDecimal(3000)));
				
			//ACT
				manager.updateEcritureComptable(vEcritureComptable);
				
			//ASSERT
				EcritureComptable vEcritureComptablePresenteEnBase = null;
				List<EcritureComptable> listEcritureComptable=manager.getListEcritureComptable();
				for(EcritureComptable ecritureComptable: listEcritureComptable) {
					if(ecritureComptable.getId().equals(vEcritureComptable.getId())) {
						vEcritureComptablePresenteEnBase=ecritureComptable;
					}
				}
				assertEquals("Paiement Facture Emmanuel",vEcritureComptablePresenteEnBase.getLibelle());	
		}
	
	
	//Test de DeleteEcritureComptable
	@Test
	public void deleteEcritureComptableShouldDeleteEcritureComptable() {			
			//ACT : suppression de l'écriture comptable en base ayant l'id -5
			manager.deleteEcritureComptable(-3);
		
			//ASSERT : vérification de la suppression
			Boolean vEcritureComptablePresenteEnBase=false;
			List<EcritureComptable> listEcritureComptable=manager.getListEcritureComptable();
			for(EcritureComptable ecritureComptable: listEcritureComptable) {
				if(ecritureComptable.getId().equals(-3)) {
					vEcritureComptablePresenteEnBase=true;
				}
			}
			assertFalse(vEcritureComptablePresenteEnBase);	
	}
	
	
}
