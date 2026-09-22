package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.youcode.lineperm.access.ControlerAcces;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import ma.youcode.lineperm.access.ControlerAcces;
// import ma.youcode.lineperm.model.FichierProtege;
// import ma.youcode.lineperm.model.User;

/**
 * FileService
 */
public class FileService {

    
    private final FichierDao fichierDao = new FichierDao();
    private final LogDao logDao = new LogDao();

    public List<FichierProtege> lister() {
        return fichierDao.findAll();
    }

    public Optional<FichierProtege> find(String nom) {
        return fichierDao.findByNom(nom);
    }
    
    public List<FichierProtege> lister() {
        return fichierDao.findAll();
    }

    public Optional<FichierProtege> find(String nom) {
        return fichierDao.findByNom(nom);
    }
}