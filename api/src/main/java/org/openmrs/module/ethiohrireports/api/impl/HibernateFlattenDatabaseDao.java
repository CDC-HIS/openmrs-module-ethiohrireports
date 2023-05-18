package org.openmrs.module.ethiohrireports.api.impl;

import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ethiohrireports.api.dao.FlattenDatabaseDao;

public class HibernateFlattenDatabaseDao implements FlattenDatabaseDao {
	
	private DbSessionFactory sessionFactory;
	
	@Override
	public void executeFlatteningScript() {
		
		sessionFactory.getCurrentSession().createSQLQuery("CALL sp_data_processing_etl()").executeUpdate();
	}
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
