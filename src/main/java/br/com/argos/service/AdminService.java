package br.com.argos.service;

import br.com.argos.dao.AdminDAO;
import br.com.argos.model.Admin;
import br.com.argos.util.Validador;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.exceptions.RequiredFieldException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a admins. */
public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService() {
        this(new AdminDAO());
    }

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = Objects.requireNonNull(adminDAO, "adminDAO cannot be null");
    }

    public void create(Admin admin) {
        validarAdmin(admin, true);
        String hash = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        Admin comHash = new Admin(admin.getIdAdmin(), admin.getFullName(), admin.getCpf(), admin.getEmail(),
                admin.getPhone(), hash, null, true);
        adminDAO.insert(comHash);
    }

    public Admin findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
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
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        adminDAO.delete(id);
    }

    public Admin authenticate(String email, String password) {
        if (email == null || email.isBlank()){
            throw new RequiredFieldException("email");
        }
        if (password == null  || password.isBlank()){
            throw new RequiredFieldException("password");
        }

        Admin admin = adminDAO.findByEmail(email);

        if (admin == null || !admin.isActive() || !BCrypt.checkpw(password, admin.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        return admin;
    }

    private void validarAdmin(Admin admin, boolean newAdmin) {

        if (admin == null) {
            throw new ValidationException("Fill in the required fields.");
        }

        if (newAdmin){
            String password = admin.getPassword();
            if (password == null || password.isBlank()){throw new RequiredFieldException("password");}
            if (!Validador.senhaValida(password)) {throw new ValidationException("Password must be 8 to 64 characters");}
        }

        if (admin.getFullName() == null || admin.getFullName().isBlank()){
            throw new RequiredFieldException("full_name");
        }
        if (admin.getFullName().length() > 120){
            throw new ValidationException("The name cannot exceed 120 characters.");
        }

        if (admin.getCpf() == null || admin.getCpf().isBlank()){
            throw new RequiredFieldException("cpf");
        }
        if (!Validador.cpfValido(admin.getCpf())){
            throw new ValidationException("Invalid CPF.");
        }

        if (admin.getEmail() == null || admin.getEmail().isBlank()) {
            throw new RequiredFieldException("email");
        }

        if (!Validador.emailValido(admin.getEmail())) {
            throw new ValidationException("Invalid email.");
        }

        if (admin.getPhone() != null && !admin.getPhone().isBlank() && !Validador.telefoneValido(admin.getPhone())){
            throw new ValidationException("Invalid phone");
        }
    }
}
