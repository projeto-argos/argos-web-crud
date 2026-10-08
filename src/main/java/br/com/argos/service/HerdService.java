package br.com.argos.service;

import br.com.argos.dao.HerdDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Herd;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.onlyDigits;
import static br.com.argos.util.Normalizer.text;

public class HerdService {

    private final HerdDAO herdDAO;

    public HerdService(){
        this(new HerdDAO());
    }

    public HerdService(HerdDAO herdDAO){
        this.herdDAO = Objects.requireNonNull(herdDAO, "HerdDAO cannot be null");
    }

    public void create(Herd herd){
        Herd normalized = normalizedHerd(herd);
        validateHerd(normalized);
        herdDAO.insert(normalized);
    }

    public Herd findById(UUID id){
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return herdDAO.findById(id);
    }

    public List<Herd> findAll(){
        return herdDAO.findAll();
    }

    public List<Herd> findByName(String name){
        if (name == null || name.isEmpty()) {
            throw new RequiredFieldException("name");
        }
        return herdDAO.findByName(name.strip());
    }

    public void update(Herd herd){
        Herd normalized = normalizedHerd(herd);
        if (normalized.getIdHerd() == null){
            throw new RequiredFieldException("id");
        }
        validateHerd(normalized);
        herdDAO.update(normalized);
    }

    public void delete(UUID id){
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        herdDAO.delete(id);
    }

    public int deleteByProperty(UUID propertyId){
        if (propertyId == null) {
            throw new RequiredFieldException("propertyId");
        }
        return herdDAO.deleteByProperty(propertyId);
    }

    private Herd normalizedHerd(Herd herd){
        if (herd == null) {
            throw new RequiredFieldException("herd");
        }

        return new Herd(
                herd.getIdHerd(),
                herd.getPropertyId(),
                text(herd.getName()),
                text(herd.getBreed()),
                text(herd.getPurpose()),
                onlyDigits(herd.getHeadCount()),
                herd.isActive()
        );
    }
    private void validateHerd(Herd herd, boolean newHerd){

//        OBRIGATÓRIOS
        if (herd == null){
            throw new RequiredFieldException("herd");
        }

        if (herd.getName() == null || herd.getName().isBlank()){
            throw new RequiredFieldException("name");
        }

        if (herd.getBreed().length() > 100){
            throw new ValidationException("Name cannot exceed 100 characters");
        }

        if (herd.getPropertyId() == null){
            throw new RequiredFieldException("propertyId");
        }

        if (herd.getHeadCount < 0){
            throw new RequiredFieldException("Head count cannot be negative");
        }

//        OPCIONAIS
        if (herd.getBreed() != null || !herd.getBreed().isBlank()){
            throw new ValidationException("breed");
        }


    }
}
