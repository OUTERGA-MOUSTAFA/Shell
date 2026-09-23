package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ma.youcode.lineperm.db.DBConnection;

abstract class AbstractDao <T> implements Dao<T> {
    
    protected  Connection getConnection(){
        return DBConnection.getInstance().getConnection();
    }


    // la fermeture dyale preparedStetment
    protected void close(PreparedStatement ps) {
        if (ps != null) {
            try { ps.close(); } catch (SQLException ignored) { }
        }
    }
}
