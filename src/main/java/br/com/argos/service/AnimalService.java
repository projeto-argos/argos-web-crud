package br.com.argos.service;

import br.com.argos.dao.AnimalDAO;
import br.com.argos.dao.LotDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Animal;
import br.com.argos.util.FilterUtil;
import br.com.argos.util.Normalizer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a animals. */
public class AnimalService {

    private final AnimalDAO animalDAO;
    private final LotDAO lotDAO;

    public AnimalService() {
        this(new AnimalDAO(), new LotDAO());
    }

    public AnimalService(AnimalDAO animalDAO){
        this(animalDAO, new LotDAO());
    }

    public AnimalService(AnimalDAO animalDAO, LotDAO lotDAO){
        this.animalDAO = Objects.requireNonNull(animalDAO, "AnimalDAO cannot be null");
        this.lotDAO = Objects.requireNonNull(lotDAO, "LotDAO cannot be null");
    }

    public void create(Animal animal){
        Animal normalized = withClearedForSlaughter(normalizeAnimal(animal), false);
        validateAnimal(normalized, true);
        animalDAO.insert(normalized);
    }

    public Animal findById(UUID id){
        if (id == null){
            throw new RequiredFieldException("ID");
        }
        return animalDAO.findById(id);
    }

    public List<Animal> filter(String weight, String earTag, String notes, String exceptionReason, LocalDate exceptionStartDate, LocalDate exceptionEndDate, LocalDate birthDate, Boolean clearedForSlaughter) {
        return findAll().stream()
                .filter(animal -> FilterUtil.contains(animal.getWeight() != null ? animal.getWeight().toPlainString() : null, weight))
                .filter(animal -> FilterUtil.contains(animal.getEarTag(), earTag))
                .filter(animal -> FilterUtil.contains(animal.getNotes(), notes))
                .filter(animal -> FilterUtil.contains(animal.getExceptionReason(), exceptionReason))
                .filter(animal -> exceptionStartDate == null || Objects.equals(animal.getExceptionStartDate(), exceptionStartDate))
                .filter(animal -> exceptionEndDate == null || Objects.equals(animal.getExceptionEndDate(), exceptionEndDate))
                .filter(animal -> birthDate == null || Objects.equals(animal.getBirthDate(), birthDate))
                .filter(animal -> clearedForSlaughter == null || animal.isClearedForSlaughter() == clearedForSlaughter)
                .toList();
    }


    public List<Animal> findAll(){
        return animalDAO.findAll();
    }

    public Animal findByEarTag(String earTag){
        if (earTag == null || earTag.isBlank()){
            throw new RequiredFieldException("brinco");
        }
        return animalDAO.findByEarTag(Normalizer.text(earTag));
    }

    public List<Animal> findByBatch(UUID batchId){
        if (batchId == null) {
            throw new RequiredFieldException("lote");
        }
        return animalDAO.findByBatch(batchId);
    }

    public void update(Animal animal){
        if (animal == null){
            throw new ValidationException("O animal não pode ser nulo.");
        }
        if (animal.getIdAnimal() == null){
            throw new RequiredFieldException("ID");
        }
        Animal current = animalDAO.findById(animal.getIdAnimal());
        if (current == null){
            throw new ValidationException("Animal não encontrado.");
        }
        Animal normalized = withClearedForSlaughter(normalizeAnimal(animal), current.isClearedForSlaughter());
        validateAnimal(normalized, false);
        animalDAO.update(normalized);
    }

    public void delete(UUID id){
        if (id == null){
            throw new RequiredFieldException("ID");
        }
        animalDAO.delete(id);
    }

    public void deleteByEarTag(String earTag) {
        if (earTag == null || earTag.isBlank()) {
            throw new RequiredFieldException("brinco");
        }
        animalDAO.deleteByEarTag(earTag.trim());
    }

    private void validateAnimal(Animal animal, boolean newAnimal) {
        if (animal == null) {
            throw new ValidationException("O animal não pode ser nulo.");
        }

        if (animal.getEarTag() == null || animal.getEarTag().trim().isEmpty()) {
            throw new ValidationException("O brinco não pode ser nulo ou vazio.");
        }

        if (animal.getEarTag().trim().length() > 20) {
            throw new ValidationException("O brinco não pode exceder 20 caracteres.");
        }

        if (animal.getWeight() == null) {
            throw new ValidationException("O peso não pode ser nulo.");
        }

        BigDecimal minLimit = BigDecimal.ZERO;
        BigDecimal maxLimit = new BigDecimal("99999.99");

        if (animal.getWeight().compareTo(minLimit) <= 0 || animal.getWeight().compareTo(maxLimit) > 0) {
            throw new ValidationException("O peso deve ser maior que 0 e no máximo 99999.99.");
        }

        if (animal.getBirthDate() != null && animal.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("A data de nascimento não pode estar no futuro.");
        }

        if (animal.getExceptionReason() != null && animal.getExceptionReason().length() > 255) {
            throw new ValidationException("O motivo da exceção do animal não pode exceder 255 caracteres.");
        }

        if (animal.getExceptionStartDate() == null) {
            throw new ValidationException("A data de início não pode ser nula.");
        }

        if (animal.getExceptionStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("A data de início não pode estar no futuro.");
        }

        if (animal.getExceptionEndDate() != null
                && animal.getExceptionEndDate().isBefore(animal.getExceptionStartDate())) {
            throw new ValidationException("A data de término não pode ser anterior à data de início.");
        }

        if (animal.getLotId() == null) {
            throw new ValidationException("O ID do lote não pode ser nulo.");
        }

        if (lotDAO.findById(animal.getLotId()) == null) {
            throw new ValidationException("O lote informado não existe ou foi excluído.");
        }

        Animal existing = animalDAO.findAnyByEarTagAndBatch(animal.getEarTag(), animal.getLotId());
        if (existing != null){
            boolean sameAnimal = !newAnimal && animal.getIdAnimal().equals(existing.getIdAnimal());
            if (!sameAnimal){
                throw new ValidationException("O brinco já está cadastrado.");
            }
        }

    }

    /** Copia o animal trocando só o campo "liberado para abate". */
    private Animal withClearedForSlaughter(Animal animal, boolean cleared) {
        return new Animal(animal.getIdAnimal(), animal.getLotId(), animal.getOriginLotId(),
                animal.getWeight(), animal.getEarTag(), animal.getNotes(),
                animal.getExceptionReason(), animal.getExceptionStartDate(),
                animal.getExceptionEndDate(), animal.getBirthDate(), animal.getUpdatedAt(),
                animal.isActive(), cleared);
    }

    private Animal normalizeAnimal(Animal animal) {
        if (animal == null) {
            throw new ValidationException("O animal não pode ser nulo.");
        }
        return new Animal(animal.getIdAnimal(), animal.getLotId(), animal.getOriginLotId(),
                animal.getWeight(), Normalizer.text(animal.getEarTag()), Normalizer.text(animal.getNotes()),
                Normalizer.text(animal.getExceptionReason()), animal.getExceptionStartDate(),
                animal.getExceptionEndDate(), animal.getBirthDate(), animal.getUpdatedAt(),
                animal.isActive(), animal.isClearedForSlaughter());
    }

}
