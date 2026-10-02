package br.com.argos.service;

import br.com.argos.dao.AdminDAO;
import br.com.argos.model.Admin;
import br.com.argos.util.Validador;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.exceptions.RequiredFieldException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a admins. */
public class AdminService {

    private final AdminDAO adminDAO = new AdminDAO();


    public void create(Admin admin) {
        validarAdmin(admin, true);
        adminDAO.insert(admin);
    }

    public Admin findById(UUID id) {
        Objects.requireNonNull(id, "O identificador do admin é obrigatório.");
        return adminDAO.findById(id);
    }

    public List<Admin> findAll() {
        return adminDAO.findAll();
    }

    public void update(Admin admin) {
        validarAdmin(admin, false);
        if (admin.getIdAdmin() == null) {
            throw new RequiredFieldException("id");
        }
        adminDAO.update(admin);
    }

    public void delete(UUID id) {
        Objects.requireNonNull(id, "O identificador do admin é obrigatório");
        adminDAO.delete(id);
    }

    private void validarAdmin(Admin admin, boolean novoAdmin) {

        if (admin.getFullName() == null || admin.getFullName().isBlank()){
            throw new RequiredFieldException("fullname");
        }
        if (admin.getFullName().length() > 120){
            throw new ValidationException("The name cannot exceed 120 characters.");
        }

        if (admin.getCpf() == null || !Validador.cpfValido(admin.getCpf())){
            throw new ValidationException("Invalid cpf.");
        }

        if (admin.getEmail() == null || !Validador.emailValido(admin.getEmail())){
            throw new ValidationException("Invalid email.");
        }

        if (admin.getPhone() != null && !admin.getPhone().isBlank() && !Validador.telefoneValido(admin.getPhone())){
            throw new ValidationException("Invalid phone");
        }

        if (novoAdmin && (admin.getPassword() == null || admin.getPassword().isBlank())){
            throw new RequiredFieldException("password");
        }
    }

}
