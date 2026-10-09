package br.com.argos.interfaces;

import br.com.argos.model.Herd;

import java.util.List;
import java.util.UUID;

public interface IHerdDAO {
    List<Herd> findByName(String name);
    List<Herd> findByProperty(UUID propertyId);
    int deleteByProperty(UUID propertyId);
}