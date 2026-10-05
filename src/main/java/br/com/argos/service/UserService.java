package br.com.argos.service;

import br.com.argos.dao.UserDAO;
import br.com.argos.model.User;
import br.com.argos.util.Validador;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;


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
        userDAO.insert(user);
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
            throw new RequiredFieldException("O identificador do usuário é obrigatório para atualização");
        }
        userDAO.update(user);
    }

    public void delete(UUID id) {
        if (id == null){
            throw new RequiredFieldException("id");
        }
        userDAO.delete(id);
    }

    public void deactivate(UUID id){

    }

    private void validarUsuario(User user, boolean novoUsuario) {
        Objects.requireNonNull(user, "O usuário é obrigatório");

        if (user == null){
            throw new ValidationException("Fill in the required fields.");
        }

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RequiredFieldException("O nome completo é obrigatório");
        }
        if (user.getCpf() == null || !Validador.cpfValido(user.getCpf())) {
            throw new ValidationException("O CPF informado é inválido");
        }
        if (user.getEmail() == null || !Validador.emailValido(user.getEmail())) {
            throw new ValidationException("O e-mail informado é inválido");
        }
        if (user.getPhone() != null && !user.getPhone().isBlank()
                && !Validador.telefoneValido(user.getPhone())) {
            throw new ValidationException("O telefone informado é inválido");
        }
        if (novoUsuario && (user.getPassword() == null || user.getPassword().isBlank())) {
            throw new RequiredFieldException("A senha é obrigatória para cadastrar um usuário");
        }
        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("A data de nascimento não pode estar no futuro");
        }
    }
}
