package br.com.argos.service;

import br.com.argos.dao.AnimalDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Animal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a animals. */
public class AnimalService {

    private final AnimalDAO animalDAO;

    public AnimalService() {
        this(new AnimalDAO());
    }

    public AnimalService(AnimalDAO animalDAO){
        this.animalDAO = Objects.requireNonNull(animalDAO, "AnimalDAO cannot be null");
    }

    public void create(Animal animal){
        validateAnimal(animal, true);
        animalDAO.insert(animal);
    }

    public Animal findById(UUID id){
        if (id == null){
            throw new RequiredFieldException("id");
        }
        return animalDAO.findById(id);
    }

    public List<Animal> findAll(){
        return animalDAO.findAll();
    }

    public void update(Animal animal){
        if (animal == null){
            throw new ValidationException("Animal cannot be null");
        }
        if (animal.getIdAnimal() == null){
            throw new RequiredFieldException("id");
        }
        validateAnimal(animal, false);
        animalDAO.update(animal);
    }

    public void delete(UUID id){
        if (id == null){
            throw new RequiredFieldException("id");
        }
        animalDAO.delete(id);
    }

    public void deleteByEarTag(String earTag) {
        if (earTag == null || earTag.isBlank()) {
            throw new RequiredFieldException("earTag");
        }
        animalDAO.deleteByEarTag(earTag.trim());
    }

    private void validateAnimal(Animal animal, boolean newAnimal) {
        if (animal == null) {
            throw new ValidationException("Animal cannot be null");
        }

        if (animal.getEarTag() == null || animal.getEarTag().trim().isEmpty()) {
            throw new ValidationException("EarTag cannot be null or empty");
        }

        if (animal.getEarTag().trim().length() > 20) {
            throw new ValidationException("EarTag cannot exceed 20 characters");
        }

        if (animal.getWeight() == null) {
            throw new ValidationException("Weight cannot be null");
        }

        BigDecimal minLimit = BigDecimal.ZERO;
        BigDecimal maxLimit = new BigDecimal("99999.99");

        if (animal.getWeight().compareTo(minLimit) < 0 || animal.getWeight().compareTo(maxLimit) > 0) {
            throw new ValidationException("Weight must be between 0 and 99999.99");
        }

        if (animal.getBirthDate() != null && animal.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date cannot be in the future");
        }

        if (animal.getExceptionReason() != null && animal.getExceptionReason().length() > 255) {
            throw new ValidationException("Animal exception reason cannot exceed 255 characters");
        }

        if (animal.getExceptionStartDate() == null) {
            throw new ValidationException("Start date cannot be null");
        }

        if (animal.getExceptionStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Start date cannot be in the future");
        }

        if (animal.getExceptionEndDate() != null
                && animal.getExceptionEndDate().isBefore(animal.getExceptionStartDate())) {
            throw new ValidationException("End date cannot be before start date");
        }

        if (animal.getBatchId() == null) {
            throw new ValidationException("Batch ID cannot be null");
        }

        Animal existing = animalDAO.findByEarTag(animal.getEarTag().trim());
        if (existing != null){
            boolean sameAnimal = !newAnimal && animal.getIdAnimal().equals(existing.getIdAnimal());
            if (!sameAnimal){
                throw new ValidationException("Ear tag already registered");
            }
        }

    }

}
