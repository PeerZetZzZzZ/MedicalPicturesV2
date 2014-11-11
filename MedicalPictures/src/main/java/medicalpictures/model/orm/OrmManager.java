/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import medicalpictures.model.exception.AddUserFailed;

/**
 * Responisble for creating objects in database.
 *
 * @author PeerZet
 */
@Stateful
public class OrmManager {

    private final static Logger LOGGER = Logger.getLogger(OrmManager.class.getName());
    private EntityManager em;
    private EntityTransaction tx;

    @PostConstruct
    private void createConnection() {
        LOGGER.info("Entity Manager started - connected to the MedicalPictures.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MedicalPictures");
        em = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public EntityTransaction getEntityTransaction() {
        return tx;
    }

    public void persistObject(Object object) throws AddUserFailed {
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(object);
            tx.commit();
        } catch (Exception ex) {
            throw new AddUserFailed("User add failed");
        }
    }

    @PreDestroy
    private void closeConnections() {
        em.close();
    }
}
