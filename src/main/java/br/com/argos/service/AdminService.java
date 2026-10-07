package br.com.argos.service;

import br.com.argos.dao.AdminDAO;
import br.com.argos.model.Admin;
import br.com.argos.util.Normalizer;
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

    /**
     * Cadastra um admin: padroniza os dados, valida e grava a senha já em hash.
     * O admin nasce sempre ativo.
     */
    public void create(Admin admin) {
        Admin limpo = normalizarAdmin(admin);
        validarAdmin(limpo, true);

        String hash = BCrypt.hashpw(limpo.getPassword(), BCrypt.gensalt());
        Admin comHash = new Admin(limpo.getIdAdmin(), limpo.getFullName(), limpo.getCpf(),
                limpo.getEmail(), limpo.getPhone(), hash, null, true);
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

    /** Atualiza os dados cadastrais */
    public void update(Admin admin) {
        Admin limpo = normalizarAdmin(admin);
        validarAdmin(limpo, false);
        if (limpo.getIdAdmin() == null) {
            throw new RequiredFieldException("id");
        }
        adminDAO.update(limpo);
    }

    /** Desativa o admin (soft delete)*/
    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        adminDAO.delete(id);
    }

    /** Troca a senha: valida a nova senha, gera o hash e só então chama o DAO. */
    public void changePassword(UUID id, String newPassword) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        validarSenha(newPassword);
        adminDAO.updatePassword(id, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    /**
     * Confere e-mail e senha. Devolve o admin autenticado ou lança ValidationException.
     * A mensagem é sempre a mesma, para não revelar se o e-mail existe.
     */
    public Admin authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new RequiredFieldException("email");
        }
        if (password == null || password.isBlank()) {
            throw new RequiredFieldException("password");
        }

        Admin admin = adminDAO.findByEmail(Normalizer.email(email));

        // A ordem importa: o checkpw só roda se o admin existir e estiver ativo
        if (admin == null || !admin.isActive() || !BCrypt.checkpw(password, admin.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        return admin;
    }

    /** Padroniza os dados de entrada (CPF e telefone só com dígitos, e-mail em minúsculas). */
    private Admin normalizarAdmin(Admin admin) {
        if (admin == null) {
            return null;
        }

        return new Admin(admin.getIdAdmin(),
                Normalizer.text(admin.getFullName()),
                Normalizer.onlyDigits(admin.getCpf()),
                Normalizer.email(admin.getEmail()),
                Normalizer.onlyDigits(admin.getPhone()),
                admin.getPassword(), null, admin.isActive());
    }

    /** Regras da senha em texto puro (antes do hash). */
    private void validarSenha(String password) {
        if (password == null || password.isBlank()) {
            throw new RequiredFieldException("password");
        }
        if (!Validador.senhaValida(password)) {
            throw new ValidationException("Password must be 8 to 64 characters");
        }
    }

    /** Valida os campos do admin. A senha só é exigida no cadastro (newAdmin = true). */
    private void validarAdmin(Admin admin, boolean newAdmin) {

        if (admin == null) {
            throw new ValidationException("Fill in the required fields.");
        }

        if (newAdmin) {
            validarSenha(admin.getPassword());
        }

        if (admin.getFullName() == null || admin.getFullName().isBlank()) {
            throw new RequiredFieldException("full_name");
        }
        if (admin.getFullName().length() > 120) {
            throw new ValidationException("The name cannot exceed 120 characters.");
        }

        if (admin.getCpf() == null || admin.getCpf().isBlank()) {
            throw new RequiredFieldException("cpf");
        }
        if (!Validador.cpfValido(admin.getCpf())) {
            throw new ValidationException("Invalid CPF.");
        }

        if (admin.getEmail() == null || admin.getEmail().isBlank()) {
            throw new RequiredFieldException("email");
        }
        if (!Validador.emailValido(admin.getEmail())) {
            throw new ValidationException("Invalid email.");
        }
        if (admin.getEmail().length() > 120) {
            throw new ValidationException("The email cannot exceed 120 characters.");
        }

        // Telefone é opcional: só valida se foi preenchido
        if (admin.getPhone() != null && !admin.getPhone().isBlank()
                && !Validador.telefoneValido(admin.getPhone())) {
            throw new ValidationException("Invalid phone.");
        }
    }
}

