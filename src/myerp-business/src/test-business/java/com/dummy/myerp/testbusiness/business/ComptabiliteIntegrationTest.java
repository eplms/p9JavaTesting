package com.dummy.myerp.testbusiness.business;

import org.junit.FixMethodOrder;
import org.junit.Test;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteIntegrationTest extends BusinessTestCase {

	@Test
	public void test1() {
		assertNotNull(getBusinessProxy().getComptabiliteManager().getListCompteComptable());
	}
	
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
	
	@Test
	public void addReferenceShouldUpDateSequenceEcritureComptable() {
		// ARRANGE : Initialisation d'une écriture comptable sans référence
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setReference(null);
		vEcritureComptable.setLibelle("Presta 2");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

		// ACT : L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// ASSERT : Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertEquals("00002",vEcritureComptable.getReference().substring(8));
	}

}
