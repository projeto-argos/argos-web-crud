package br.com.argos.service;

import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Admin;
import br.com.argos.model.Animal;
import br.com.argos.model.Herd;
import br.com.argos.model.HomeSummary;
import br.com.argos.model.Property;
import br.com.argos.model.RecentRecord;
import br.com.argos.model.Supplier;
import br.com.argos.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Monta os dados da página inicial reaproveitando os Services que já existem.
 * Não tem DAO próprio: só junta o que os outros Services já devolvem.
 */
public class HomeService {

    private final UserService userService;
    private final PropertyService propertyService;
    private final AnimalService animalService;
    private final SupplierService supplierService;
    private final HerdService herdService;
    private final AdminService adminService;

    public HomeService() {
        this(new UserService(), new PropertyService(), new AnimalService(),
                new SupplierService(), new HerdService(), new AdminService());
    }

    public HomeService(UserService userService, PropertyService propertyService,
                       AnimalService animalService, SupplierService supplierService,
                       HerdService herdService, AdminService adminService) {
        this.userService = Objects.requireNonNull(userService, "userService cannot be null");
        this.propertyService = Objects.requireNonNull(propertyService, "propertyService cannot be null");
        this.animalService = Objects.requireNonNull(animalService, "animalService cannot be null");
        this.supplierService = Objects.requireNonNull(supplierService, "supplierService cannot be null");
        this.herdService = Objects.requireNonNull(herdService, "herdService cannot be null");
        this.adminService = Objects.requireNonNull(adminService, "adminService cannot be null");
    }

    /**
     * Busca cada tabela uma única vez, conta os registros ativos e separa os
     * {@code recentLimit} registros mais recentemente atualizados (updated_at).
     */
    public HomeSummary getSummary(int recentLimit) {
        if (recentLimit <= 0) {
            throw new ValidationException("The limit must be greater than zero.");
        }

        List<User> users = userService.findAll();
        List<Property> properties = propertyService.findAll();
        List<Animal> animals = animalService.findAll();
        List<Supplier> suppliers = supplierService.findAll();
        List<Herd> herds = herdService.findAll();
        List<Admin> admins = adminService.findAll();

        List<RecentRecord> all = new ArrayList<>();
        for (User u : users) {
            all.add(new RecentRecord(u.getFullName(), "Usuário", u.getUpdatedAt()));
        }
        for (Property p : properties) {
            all.add(new RecentRecord(p.getName(), "Propriedade", p.getUpdatedAt()));
        }
        for (Animal a : animals) {
            all.add(new RecentRecord(a.getEarTag(), "Animal", a.getUpdatedAt()));
        }
        for (Supplier s : suppliers) {
            all.add(new RecentRecord(s.getFullName(), "Fornecedor", s.getUpdatedAt()));
        }
        for (Herd h : herds) {
            all.add(new RecentRecord(h.getName(), "Rebanho", h.getUpdatedAt()));
        }
        for (Admin a : admins) {
            all.add(new RecentRecord(a.getFullName(), "Admin", a.getUpdatedAt()));
        }

        all.sort(Comparator.comparing(RecentRecord::getUpdatedAt,
                Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder())));

        List<RecentRecord> recent = all.subList(0, Math.min(recentLimit, all.size()));

        return new HomeSummary(users.size(), properties.size(), animals.size(),
                suppliers.size(), herds.size(), admins.size(), recent);
    }
}
