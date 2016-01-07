package dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("transactionManager")
public class daoImpl implements AccountDao {


	@PersistenceContext(unitName = "localDB")
	EntityManager entityManager;
}
