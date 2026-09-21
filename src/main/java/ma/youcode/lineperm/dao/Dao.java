package ma.youcode.lineperm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

//  T = type de l'entité (User, FichierProtege, AccessLog).
    T save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    boolean delete(int id);
    
}
