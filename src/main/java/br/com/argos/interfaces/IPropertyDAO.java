package br.com.argos.interfaces;

import br.com.argos.model.Property;

import java.util.List;
import java.util.UUID;

public interface IPropertyDAO {
    List<Property> findByName(String name);
    List<Property> findByUser(UUID userId);
    int deleteByUser(UUID userId);
}
