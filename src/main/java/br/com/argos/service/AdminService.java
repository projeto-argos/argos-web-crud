package br.com.argos.service;

import br.com.argos.dao.AdminDAO;
import br.com.argos.model.Admin;
import br.com.argos.util.Validador;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.exceptions.RequiredFieldException;

public class AdminService {

    private final AdminDAO adminDAO = new AdminDAO();


    public void cadastrar(Admin admin) {
        validarAdmin(admin, true);
        adminDAO.insert(admin);
    }

    public void atualizar(Admin admin) {
        if (admin.getIdAdmin() == null) {
            throw new RequiredFieldException("O id do Admin é obrigatório para atualização.");
        }
        validarAdmin(admin, false);
        adminDAO.update(admin);
    }

    private void validarAdmin(Admin admin, boolean novoAdmin) {

        if (admin.getFullName() != null || admin.getFullName().isBlank()){
            throw new RequiredFieldException("O nome completo é obrigatório.");
        }
        if (admin.getFullName().length() > 120){
            throw new IllegalArgumentException("O nome não pode passar de 120 caracteres.");
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
            throw new RequiredFieldException("O senha é obrigatória.");
        }
    }

}
