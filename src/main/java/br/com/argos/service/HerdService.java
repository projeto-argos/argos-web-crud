package br.com.argos.service;

import br.com.argos.dao.HerdDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Herd;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.text;

/** Regras de negócio e validações relacionadas a rebanhos. */
public class HerdService {

    private static final String DEFAULT_PURPOSE = "Beef";

    private final HerdDAO herdDAO;

    public HerdService() {
        this(new HerdDAO());
    }

    public HerdService(HerdDAO herdDAO) {
        this.herdDAO = Objects.requireNonNull(herdDAO, "HerdDAO cannot be null");
    }

    public void create(Herd herd) {
        Herd normalized = normalizeHerd(herd);
        validateHerd(normalized);
        herdDAO.insert(normalized);
    }

    public Herd findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return herdDAO.findById(id);
    }

    public List<Herd> findAll() {
        return herdDAO.findAll();
    }

    public List<Herd> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new RequiredFieldException("name");
        }
        return herdDAO.findByName(name.strip());
    }

    public List<Herd> findByProperty(UUID propertyId) {
        if (propertyId == null) {
            throw new RequiredFieldException("propertyId");
        }
        return herdDAO.findByProperty(propertyId);
    }

    public void update(Herd herd) {
        Herd normalized = normalizeHerd(herd);
        if (normalized.getIdHerd() == null) {
            throw new RequiredFieldException("id");
        }
        validateHerd(normalized);
        herdDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        herdDAO.delete(id);
    }

    public int deleteByProperty(UUID propertyId) {
        if (propertyId == null) {
            throw new RequiredFieldException("propertyId");
        }
        return herdDAO.deleteByProperty(propertyId);
    }

    /** Limpa os dados: purpose vazio vira "Beef" e head_count nulo vira 0. */
    private Herd normalizeHerd(Herd herd) {
        if (herd == null) {
            throw new RequiredFieldException("herd");
        }

        String purpose = text(herd.getPurpose());
        Integer headCount = herd.getHeadCount();

        return new Herd(
                herd.getIdHerd(),
                herd.getPropertyId(),
                headCount != null ? headCount : 0,
                purpose != null ? purpose : DEFAULT_PURPOSE,
                text(herd.getName()),
                text(herd.getBreed()),
                herd.getUpdatedAt(),
                herd.isActive()
        );
    }

    private void validateHerd(Herd herd) {

        // OBRIGATÓRIOS
        if (herd.getName() == null) {
            throw new RequiredFieldException("name");
        }

        if (herd.getName().length() > 100) {
            throw new ValidationException("Name cannot exceed 100 characters");
        }

        if (herd.getPropertyId() == null) {
            throw new RequiredFieldException("propertyId");
        }

        if (herd.getHeadCount() < 0) {
            throw new ValidationException("Head count cannot be negative");
        }

        // OPCIONAIS
        if (herd.getBreed() != null && herd.getBreed().length() > 50) {
            throw new ValidationException("Breed cannot exceed 50 characters");
        }

        if (herd.getPurpose() != null && herd.getPurpose().length() > 50) {
            throw new ValidationException("Purpose cannot exceed 50 characters");
        }
    }
}