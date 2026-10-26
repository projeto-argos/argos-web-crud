package br.com.argos.service;

import br.com.argos.dao.LotDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Lot;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.text;

/**
 * Regras de negócio e validações relacionadas aos lotes.
 */
public class LotService {

    private final LotDAO lotDAO;

    public LotService() {
        this(new LotDAO());
    }

    public LotService(LotDAO lotDAO) {
        this.lotDAO = Objects.requireNonNull(
                lotDAO, "LotDAO cannot be null"
        );
    }

    public void create(Lot lot) {
        Lot normalized = normalizeLot(lot);
        validateLot(normalized);
        lotDAO.insert(normalized);
    }

    public Lot findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        return lotDAO.findById(id);
    }

    public List<Lot> findAll() {
        return lotDAO.findAll();
    }

    public Lot findByLotCodeAndHerd(String lotCode, UUID idHerd){
        if (idHerd == null) {
            throw new RequiredFieldException("idHerd");
        } if (lotCode == null || lotCode.isBlank()) {
            throw new RequiredFieldException("lotCode");
        }

        return lotDAO.findByLotCodeAndHerd(lotCode, idHerd);
    }

    public List<Lot> findByHerd(UUID idHerd) {
        if (idHerd == null) {
            throw new RequiredFieldException("idHerd");
        }

        return lotDAO.findByHerd(idHerd);
    }

    public List<Lot> findByCategory(String category) {
        String normalizedCategory = text(category);

        if (normalizedCategory == null) {
            throw new RequiredFieldException("category");
        }

        normalizedCategory = normalizedCategory.toUpperCase();
        validateCategory(normalizedCategory);

        return lotDAO.findByCategory(normalizedCategory);
    }

    public void update(Lot lot) {
        Lot normalized = normalizeLot(lot);

        if (normalized.getIdLot() == null) {
            throw new RequiredFieldException("idLot");
        }

        validateLot(normalized);
        lotDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        lotDAO.delete(id);
    }

    public void deleteByHerd(UUID idHerd) {
        if (idHerd == null) {
            throw new RequiredFieldException("idHerd");
        }

        lotDAO.deleteByHerd(idHerd);
    }

    private Lot normalizeLot(Lot lot) {
        if (lot == null) {
            throw new ValidationException("Lot cannot be null.");
        }

        String lotCode = text(lot.getLotCode());
        String category = text(lot.getCategory());

        return new Lot(
                lot.getIdLot(),
                lot.getHerdId(),
                lot.getHeadCount(),
                category != null ? category.toUpperCase() : null,
                lot.getLotCode(),
                lot.getOpenDate(),
                lot.getUpdatedAt(),
                lot.isActive()
        );
    }

    private void validateLot(Lot lot) {
        if (lot.getHerdId() == null) {
            throw new RequiredFieldException("idHerd");
        }

        if (lot.getLotCode() == null) {
            throw new RequiredFieldException("lotCode");
        }

        if (lot.getLotCode().length() > 20) {
            throw new ValidationException(
                    "Lot code cannot exceed 20 characters."
            );
        }

        if (lot.getCategory() == null) {
            throw new RequiredFieldException("category");
        }

        validateCategory(lot.getCategory());

        if (lot.getHeadCount() != null && lot.getHeadCount() < 0) {
            throw new ValidationException(
                    "Head count cannot be negative."
            );
        }
    }

    private void validateCategory(String category) {
        if (!category.equals("FATTENING")
                && !category.equals("BREEDING")
                && !category.equals("REARING")) {
            throw new ValidationException(
                    "Category must be FATTENING, BREEDING or REARING."
            );
        }
    }
}
