package medicalpictures.model.dao;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import medicalpictures.model.exception.AddToDbFailed;

/**
 * Responisble for creating objects in database.
 *
 * @author PeerZet
 */
@Stateful
public class ManagerDAO {

    @PersistenceContext(unitName = "MedicalPictures")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Persists object to database.
     *
     * @param object
     * @throws AddToDbFailed
     */
    public void persistObject(Object object) throws AddToDbFailed {
        try {
            em.persist(object);
        } catch (Exception ex) {
            throw new AddToDbFailed(ex.getMessage());
        }
    }
}
