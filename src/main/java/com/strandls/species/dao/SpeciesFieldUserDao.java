/**
 * 
 */
package com.strandls.species.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.species.pojo.SpeciesFieldUser;
import com.strandls.species.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class SpeciesFieldUserDao extends AbstractDAO<SpeciesFieldUser, Long> {

	private final Logger logger = LoggerFactory.getLogger(SpeciesFieldUserDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected SpeciesFieldUserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public SpeciesFieldUser findById(Long id) {
		SpeciesFieldUser result = null;
		Session session = sessionFactory.openSession();

		try {
			result = session.get(SpeciesFieldUser.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SpeciesFieldUser> findBySpeciesFieldId(Long speciesFieldId) {
		String qry = "from SpeciesFieldUser where speciesFieldId = :speciesFieldId";
		List<SpeciesFieldUser> result = null;

		Session session = sessionFactory.openSession();

		try {
			Query<SpeciesFieldUser> query = session.createQuery(qry);
			query.setParameter("speciesFieldId", speciesFieldId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}