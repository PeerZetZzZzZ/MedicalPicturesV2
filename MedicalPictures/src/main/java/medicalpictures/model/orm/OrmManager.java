/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PeerZet
 */
@Stateful
public class OrmManager {

    private final static Logger LOGGER = Logger.getLogger(OrmManager.class.getName());
    private EntityManager em;
    private EntityTransaction tx;

    @PostConstruct
    public void createConnection() {
        LOGGER.info("Entity Manager started - connected to the MedicalPictures.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MedicalPictures");
        em = emf.createEntityManager();
    }

    public void persistObject(Object object) {
        tx = em.getTransaction();
        tx.begin();
        em.persist(object);
        tx.commit();
    }
}
