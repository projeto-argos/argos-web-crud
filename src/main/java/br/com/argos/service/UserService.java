package br.com.argos.service;

import br.com.argos.dao.UserDAO;
import br.com.argos.model.User;
import br.com.argos.util.Validador;

import java.sql.SQLException;
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
        this.userDAO = Objects.requireNonNull(userDAO, "userDAO não pode ser nulo");
    }

    public void create(User user) throws SQLException {
        validarUsuario(user, true);
        userDAO.insert(user);
    }

    public User findById(UUID id) throws SQLException {
        Objects.requireNonNull(id, "O identificador do usuário é obrigatório");
        return userDAO.findById(id);
    }

    public List<User> findAll() throws SQLException {
        return userDAO.findAll();
    }

    public void update(User user) throws SQLException {
        validarUsuario(user, false);
        if (user.getIdUser() == null) {
            throw new IllegalArgumentException("O identificador do usuário é obrigatório para atualização");
        }
        userDAO.update(user);
    }

    public void delete(UUID id) throws SQLException {
        Objects.requireNonNull(id, "O identificador do usuário é obrigatório");
        userDAO.delete(id);
    }

    private void validarUsuario(User user, boolean novoUsuario) {
        Objects.requireNonNull(user, "O usuário é obrigatório");

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new IllegalArgumentException("O nome completo é obrigatório");
        }
        if (user.getCpf() == null || !Validador.cpfValido(user.getCpf())) {
            throw new IllegalArgumentException("O CPF informado é inválido");
        }
        if (user.getEmail() == null || !Validador.emailValido(user.getEmail())) {
            throw new IllegalArgumentException("O e-mail informado é inválido");
        }
        if (user.getPhone() != null && !user.getPhone().isBlank()
                && !Validador.telefoneValido(user.getPhone())) {
            throw new IllegalArgumentException("O telefone informado é inválido");
        }
        if (novoUsuario && (user.getPassword() == null || user.getPassword().isBlank())) {
            throw new IllegalArgumentException("A senha é obrigatória para cadastrar um usuário");
        }
        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode estar no futuro");
        }
    }
}
