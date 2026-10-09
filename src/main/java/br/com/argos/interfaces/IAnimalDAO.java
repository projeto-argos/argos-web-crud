package br.com.argos.interfaces;

import br.com.argos.model.Animal;

import java.util.List;
import java.util.UUID;

public interface IAnimalDAO {
    public Animal findByEarTag(String earTag);
    public List<Animal> findByBatch(UUID idBatch);
    public void deleteByEarTag(String earTag);
}
