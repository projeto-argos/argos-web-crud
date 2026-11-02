package br.com.argos.service;

import br.com.argos.dao.HerdDAO;
import br.com.argos.dao.LotDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Lot;
import br.com.argos.util.FilterUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.text;

/**
 * Regras de negócio e validações relacionadas aos lotes.
 */
public class LotService {

    private final LotDAO lotDAO;
    private final HerdDAO herdDAO;

    public LotService() {
        this(new LotDAO(), new HerdDAO());
    }

    public LotService(LotDAO lotDAO) {
        this(lotDAO, new HerdDAO());
    }

    public LotService(LotDAO lotDAO, HerdDAO herdDAO) {
        this.lotDAO = Objects.requireNonNull(
                lotDAO, "LotDAO cannot be null"
        );
        this.herdDAO = Objects.requireNonNull(
                herdDAO, "HerdDAO cannot be null"
        );
    }

    public void create(Lot lot) {
        Lot normalized = normalizeLot(lot);
        validateLot(normalized);
        lotDAO.insert(normalized);
    }

    public Lot findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }

        return lotDAO.findById(id);
    }

    public List<Lot> filter(String headCount, String category, String lotCode, LocalDate openDate) {
        return findAll().stream()
                .filter(lot -> headCount == null || headCount.isBlank() || String.valueOf(lot.getHeadCount()).equals(headCount.trim()))
                .filter(lot -> FilterUtil.contains(lot.getCategory(), category))
                .filter(lot -> FilterUtil.contains(lot.getLotCode(), lotCode))
                .filter(lot -> openDate == null || Objects.equals(lot.getOpenDate(), openDate))
                .toList();
    }

    public List<Lot> findAll() {
        return lotDAO.findAll();
    }

    public Lot findByLotCodeAndHerd(String lotCode, UUID idHerd){
        if (idHerd == null) {
            throw new RequiredFieldException("rebanho");
        } if (lotCode == null || lotCode.isBlank()) {
            throw new RequiredFieldException("código do lote");
        }

        return lotDAO.findByLotCodeAndHerd(lotCode, idHerd);
    }

    public List<Lot> findByHerd(UUID idHerd) {
        if (idHerd == null) {
            throw new RequiredFieldException("rebanho");
        }

        return lotDAO.findByHerd(idHerd);
    }

    public List<Lot> findByCategory(String category) {
        String normalizedCategory = text(category);

        if (normalizedCategory == null) {
            throw new RequiredFieldException("categoria");
        }

        normalizedCategory = normalizedCategory.toUpperCase();
        validateCategory(normalizedCategory);

        return lotDAO.findByCategory(normalizedCategory);
    }

    public void update(Lot lot) {
        Lot normalized = normalizeLot(lot);

        if (normalized.getIdLot() == null) {
            throw new RequiredFieldException("lote");
        }

        validateLot(normalized);
        lotDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }

        lotDAO.delete(id);
    }

    public void deleteByHerd(UUID idHerd) {
        if (idHerd == null) {
            throw new RequiredFieldException("rebanho");
        }

        lotDAO.deleteByHerd(idHerd);
    }

    private Lot normalizeLot(Lot lot) {
        if (lot == null) {
            throw new ValidationException("O lote não pode ser nulo.");
        }

        String lotCode = text(lot.getLotCode());
        String category = text(lot.getCategory());

        return new Lot(
                lot.getIdLot(),
                lot.getHerdId(),
                lot.getHeadCount(),
                category != null ? category.toUpperCase() : null,
                lotCode,
                lot.getOpenDate(),
                lot.getUpdatedAt(),
                lot.isActive()
        );
    }

    private void validateLot(Lot lot) {
        if (lot.getHerdId() == null) {
            throw new RequiredFieldException("rebanho");
        }

        if (herdDAO.findById(lot.getHerdId()) == null) {
            throw new ValidationException("O rebanho informado não existe ou foi excluído.");
        }

        if (lot.getLotCode() == null || lot.getLotCode().isBlank()) {
            throw new RequiredFieldException("código do lote");
        }

        if (lot.getLotCode().length() > 20) {
            throw new ValidationException("O código do lote não pode exceder 20 caracteres.");
        }

        if (lot.getCategory() == null) {
            throw new RequiredFieldException("categoria");
        }

        validateCategory(lot.getCategory());

        if (lot.getHeadCount() != null && lot.getHeadCount() < 0) {
            throw new ValidationException("A contagem de cabeças não pode ser negativa.");
        }

        if (lot.getOpenDate() != null && lot.getOpenDate().isAfter(LocalDate.now())) {
            throw new ValidationException("A data de abertura não pode estar no futuro.");
        }
    }

    private void validateCategory(String category) {
        if (!category.equals("ENGORDA")
                && !category.equals("CRIA")
                && !category.equals("RECRIA")) {
            throw new ValidationException("A categoria deve ser ENGORDA, RECRIA ou CRIA.");
        }
    }
}
