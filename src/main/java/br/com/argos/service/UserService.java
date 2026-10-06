package br.com.argos.service;

import br.com.argos.dao.UserDAO;
import br.com.argos.model.User;
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

    public void create(User user) {
        validarUsuario(user, true);
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        User comHash = new User(user.getIdUser(), user.getFullName(), user.getCpf(), user.getEmail(),
                user.getPhone(), user.getRole(), hash, user.getBirthDate(), null, true);
        userDAO.insert(comHash);
    }

    public User findById(UUID id) {
        if (id == null){
            throw new RequiredFieldException("id");
        }
        return userDAO.findById(id);
    }

    public List<User> findAll()  {
        return userDAO.findAll();
    }

    public void update(User user) {
        validarUsuario(user, false);
        if (user.getIdUser() == null) {
            throw new RequiredFieldException("id");
        }
        userDAO.update(user);
    }

    public void delete(UUID id) {
        if (id == null){
            throw new RequiredFieldException("id");
        }
        userDAO.delete(id);
    }

    public User authenticate(String email, String password) {
        if (email == null || email.isBlank()){
            throw new RequiredFieldException("email");
        }
        if (password == null  || password.isBlank()){
            throw new RequiredFieldException("password");
        }

        User user = userDAO.findByEmail(email);

        if (user == null || !user.isActive() || !BCrypt.checkpw(password, user.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        return user;
    }

    private void validarUsuario(User user, boolean newUser) {

        if (user == null){
            throw new ValidationException("Fill in the required fields.");
        }

        if (newUser && (user.getPassword() == null || user.getPassword().isBlank())) {
            throw new RequiredFieldException("password");
        }
        if (newUser && (!Validador.senhaValida(user.getPassword()))) {
            throw new ValidationException("Password must be 8 to 64 characters");
        }

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RequiredFieldException("full_name");
        }
        if (user.getFullName().length() > 120){
            throw new ValidationException("The name cannot exceed 120 characters");
        }

        if (user.getRole().length() > 50){
            throw new ValidationException("The role cannot exceed 50 characters");
        }

        if (user.getCpf() == null || !Validador.cpfValido(user.getCpf())) {
            throw new ValidationException("Invalid CPF");
        }
        if (user.getEmail() == null || !Validador.emailValido(user.getEmail())) {
            throw new ValidationException("Invalid email");
        }

        if (user.getEmail().length() > 120){
            throw new ValidationException("The email cannot  exceed 120 characters");
        }

        if (user.getPhone() != null && !user.getPhone().isBlank() && !Validador.telefoneValido(user.getPhone())) {
            throw new ValidationException("Invalid phone number");
        }

        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date cannot be in the future");
        }
    }
}
