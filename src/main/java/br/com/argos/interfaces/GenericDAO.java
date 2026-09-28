package br.com.argos.interfaces;

import java.sql.SQLException;
import java.util.List;


public interface GenericDAO <T, ID> {

//    MÉTODO INSERT
     void insert(T entity) throws SQLException;

    //    MÉTODO UPDATE
    void update(T entity) throws SQLException;

    //    MÉTODO DELETE
    void delete(ID id) throws SQLException;

    //    MÉTODO FINDALL (procurar todos)
    List<T> findAll() throws SQLException;

    //    MÉTODO FINDBYID  (procurar por ID)
    T findById(ID id) throws SQLException;



}
