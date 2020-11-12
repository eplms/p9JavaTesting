package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

/**
 * {@link RowMapper} de {@link SequenceEcritureComptable}
 */
public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable>{

		
	@Override
	public SequenceEcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
		SequenceEcritureComptable vBean=new SequenceEcritureComptable();
		vBean.setAnnee(pRS.getInt("annee"));
		vBean.setCodeJournal(pRS.getString("journal_code"));
		vBean.setDerniereValeur(pRS.getInt("Derniere_Valeur"));
		
		return vBean;
	}

}
