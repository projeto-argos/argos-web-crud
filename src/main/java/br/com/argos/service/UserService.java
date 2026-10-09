package br.com.argos.service;

import br.com.argos.dao.UserDAO;
import br.com.argos.model.User;
import br.com.argos.util.Normalizer;
import br.com.argos.util.Validador;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a usuários. */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAO());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = Objects.requireNonNull(userDAO, "userDAO cannot be null");
    }

    /**
     * Cadastra um usuário: padroniza os dados, valida e grava a senha já em hash.
     */
    public void create(User user) {
        User limpo = normalizarUsuario(user);
        validarUsuario(limpo, true);

        // A senha nunca vai em texto puro para o banco
        String hash = BCrypt.hashpw(limpo.getPassword(), BCrypt.gensalt());
        User comHash = new User(limpo.getIdUser(), limpo.getFullName(), limpo.getCpf(), limpo.getEmail(),
                limpo.getPhone(), limpo.getRole(), hash, limpo.getBirthDate(), null, true);
        userDAO.insert(comHash);
    }

    public User findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return userDAO.findById(id);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    /** Atualiza os dados cadastrais */
    public void update(User user) {
        User limpo = normalizarUsuario(user);
        validarUsuario(limpo, false);
        if (limpo.getIdUser() == null) {
            throw new RequiredFieldException("id");
        }
        userDAO.update(limpo);
    }

    /** Desativa o usuário (soft delete) */
    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        userDAO.delete(id);
    }

    /** Troca a senha */
    public void changePassword(UUID id, String newPassword) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        validarSenha(newPassword);
        userDAO.updatePassword(id, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    /**
     * Confere e-mail e senha. Devolve o usuário autenticado ou lança ValidationException.
     * mensagem sempre a mesma, para não revelar se o e-mail existe.
     */
    public User authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new RequiredFieldException("email");
        }
        if (password == null || password.isBlank()) {
            throw new RequiredFieldException("password");
        }

        User user = userDAO.findByEmail(Normalizer.email(email));

        if (user == null || !user.isActive() || !BCrypt.checkpw(password, user.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        return user;
    }

    /** Padroniza os dados de entrada */
    private User normalizarUsuario(User user) {
        if (user == null) {
            return null;
        }
        // A senha não é normalizada
        return new User(user.getIdUser(),
                Normalizer.text(user.getFullName()),
                Normalizer.onlyDigits(user.getCpf()),
                Normalizer.email(user.getEmail()),
                Normalizer.onlyDigits(user.getPhone()),
                Normalizer.text(user.getRole()),
                user.getPassword(), user.getBirthDate(), user.getUpdatedAt(), user.isActive());
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

    /** Valida os campos do usuário. Senha apenas exigida no cadastro */
    private void validarUsuario(User user, boolean newUser) {

//        OBRIGATÓRIOS
        if (user == null) {
            throw new ValidationException("Fill in the required fields.");
        }

        if (newUser) {
            validarSenha(user.getPassword());
        }

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RequiredFieldException("full_name");
        }
        if (user.getFullName().length() > 120) {
            throw new ValidationException("The name cannot exceed 120 characters");
        }

        if (user.getCpf() == null || user.getCpf().isBlank()) {
            throw new RequiredFieldException("cpf");
        }
        if (!Validador.cpfValido(user.getCpf())) {
            throw new ValidationException("Invalid CPF");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RequiredFieldException("email");
        }
        if (!Validador.emailValido(user.getEmail())) {
            throw new ValidationException("Invalid email");
        }
        if (user.getEmail().length() > 120) {
            throw new ValidationException("The email cannot exceed 120 characters");
        }

        // OPCIONAIS
        if (user.getPhone() != null && !user.getPhone().isBlank()
                && !Validador.telefoneValido(user.getPhone())) {
            throw new ValidationException("Invalid phone number");
        }

        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date cannot be in the future");
        }

        if (user.getRole() != null && user.getRole().length() > 50) {
            throw new ValidationException("The role cannot exceed 50 characters");
        }
    }
}