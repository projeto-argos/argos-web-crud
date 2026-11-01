package br.com.argos.service;

import br.com.argos.dao.UserDAO;
import br.com.argos.model.User;
import br.com.argos.util.FilterUtil;
import br.com.argos.util.Normalizer;
import br.com.argos.util.Validator;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.onlyDigits;

/** Regras de negócio e validações relacionadas a usuários. */
public class UserService {

    // Hash falso, só para igualar o tempo de resposta do login
    private static final String DUMMY_HASH = BCrypt.hashpw("senha-falsa-argos", BCrypt.gensalt());

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAO());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = Objects.requireNonNull(userDAO, "userDAO cannot be null");
    }

    /**
     - padroniza os dados, valida e grava a senha já em hash
     */
    public void create(User user) {
        User limpo = normalizeUser(user);
        validateUser(limpo, true);

        // A senha nunca vai em texto puro para o banco
        String hash = BCrypt.hashpw(limpo.getPassword(), BCrypt.gensalt());
        User comHash = new User(limpo.getIdUser(), limpo.getFullName(), limpo.getCpf(), limpo.getEmail(),
                limpo.getPhone(), limpo.getRole(), hash, limpo.getBirthDate(), null, limpo.isActive());
        userDAO.insert(comHash);
    }

    public User findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }
        return userDAO.findById(id);
    }

    public List<User> filter(String fullName, String cpf, String email, String phone, String role) {
        return findAll().stream()
                .filter(user -> FilterUtil.contains(user.getFullName(), fullName))
                .filter(user -> FilterUtil.contains(user.getCpf(), FilterUtil.digits(cpf)))
                .filter(user -> FilterUtil.contains(user.getEmail(), email))
                .filter(user -> FilterUtil.contains(user.getPhone(), FilterUtil.digits(phone)))
                .filter(user -> FilterUtil.contains(user.getRole(), role))
                .toList();
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findByEmail(String email){
        if (email == null || email.isBlank()) {
            throw new RequiredFieldException("e-mail");
        }
        return userDAO.findByEmail(Normalizer.email(email));
    }

    /** Atualiza os dados cadastrais */
    public void update(User user) {
        User limpo = normalizeUser(user);
        validateUser(limpo, false);
        if (limpo.getIdUser() == null) {
            throw new RequiredFieldException("ID");
        }
        userDAO.update(limpo);
    }

    /** Desativa o usuário (soft delete) */
    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }
        userDAO.delete(id);
    }

    public void deleteByCpf(String cpf) {
        String digits = onlyDigits(cpf);
        if (digits == null) {
            throw new RequiredFieldException("CPF");
        }
        if (!Validator.isValidCpf(digits)) {
            throw new ValidationException("CPF inválido");
        }
        userDAO.deleteByCpf(digits);
    }

    /** Troca a senha */
    public void changePassword(UUID id, String newPassword) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }
        validatePassword(newPassword);
        userDAO.updatePassword(id, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    /**
     - Confere e-mail e senha. Devolve o usuário autenticado ou lança ValidationException.
     - mensagem sempre a mesma.

     */
    public User authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new RequiredFieldException("e-mail");
        }
        if (password == null || password.isBlank()) {
            throw new RequiredFieldException("senha");
        }

        User user = userDAO.findByEmailForAuthentication(Normalizer.email(email));

        if (user == null || !user.isActive()) {
            BCrypt.checkpw(password, DUMMY_HASH);
            throw new ValidationException("Credenciais inválidas");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new ValidationException("Credenciais inválidas");
        }

        return user;
    }

    /** Padroniza os dados de entrada */
    private User normalizeUser(User user) {
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

    /** Regras da senha em texto puro  */
    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new RequiredFieldException("senha");
        }

        int tamanhoEmBytes = password.getBytes(StandardCharsets.UTF_8).length;

        if (tamanhoEmBytes > 72){
            throw new ValidationException("A senha excede o limite de 72 bytes do BCrypt");
        }

        if (!Validator.isValidPassword(password)) {
            throw new ValidationException("A senha deve ter entre 8 e 64 caracteres");
        }
    }

    /** Valida os campos do usuário. Senha apenas exigida no cadastro */
    private void validateUser(User user, boolean newUser) {

//        OBRIGATÓRIOS
        if (user == null) {
            throw new ValidationException("Preencha os campos obrigatórios.");
        }

        if (newUser) {
            validatePassword(user.getPassword());
        }

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RequiredFieldException("nome completo");
        }
        if (user.getFullName().length() > 120) {
            throw new ValidationException("O nome não pode exceder 120 caracteres");
        }

        if (user.getCpf() == null || user.getCpf().isBlank()) {
            throw new RequiredFieldException("CPF");
        }
        if (!Validator.isValidCpf(user.getCpf())) {
            throw new ValidationException("CPF inválido");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RequiredFieldException("e-mail");
        }
        if (!Validator.isValidEmail(user.getEmail())) {
            throw new ValidationException("E-mail inválido");
        }
        if (user.getEmail().length() > 120) {
            throw new ValidationException("O e-mail não pode exceder 120 caracteres");
        }

        // OPCIONAIS
        if (user.getPhone() != null && !user.getPhone().isBlank()
                && !Validator.isValidPhone(user.getPhone())) {
            throw new ValidationException("Número de telefone inválido");
        }

        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("A data de nascimento não pode ser no futuro");
        }

        if (user.getRole() != null && user.getRole().length() > 50) {
            throw new ValidationException("O cargo não pode exceder 50 caracteres");
        }
    }
}