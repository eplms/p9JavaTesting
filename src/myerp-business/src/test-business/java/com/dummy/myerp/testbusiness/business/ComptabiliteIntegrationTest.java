package com.dummy.myerp.testbusiness.business;

import org.junit.Test;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

public class ComptabiliteIntegrationTest extends BusinessTestCase {

	@Test
	public void test1() {
		assertNotNull(getBusinessProxy().getComptabiliteManager().getListCompteComptable());
	}
	
	
	@Test
	public void addReferenceTestShouldSetReferenceNotNullToEcritureComptable() {
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

		// ACT
		// L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// reference est "AC-2020/xxxxx"
		// ASSERT
		// Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertNotNull(vEcritureComptable.getReference());
	}
	@Test
	public void addReferenceTestShouldSetReferenceToEcritureComptable() {
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

		// ACT
		// L'écriture comptable est passée dans la méthode addReference
		getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
		// reference est "AC-2020/xxxxx"
		// ASSERT
		// Vérification que la référence a bien été ajoutée à l'écriture comptable
		assertEquals(vEcritureComptable.getReference(),"AC-2020/00002");
	}
	
	
	
}
