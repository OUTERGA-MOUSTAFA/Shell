package ma.youcode.lineperm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

//  T = type de l'entité (User, FichierProtege, AccessLog).
    T save(T t);
    Optional<T> findById(int id);// f class Optional<User> user = userDao.findById(10); optional hit i9der user makaynesh ola logd
    List<T> findAll();// reje3 liya List<usres> 
    boolean delete(int id);// boolean deleted = userDao.delete(10);
    
}
