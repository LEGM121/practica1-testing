package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import java.util.HashSet;
import java.util.Set;

public class Registry {
    private final Set<Integer> registeredIds = new HashSet<>();

    public RegisterResult registerVoter(Person p) {
        // Validación defensiva: persona nula
        if (p == null) {
            return RegisterResult.INVALID;
        }

        // Validación: persona muerta
        if (!p.isAlive()) {
            return RegisterResult.DEAD;
        }

        // Validación: ID inválido (debe ser positivo)
        if (p.getId() <= 0) {
            return RegisterResult.INVALID_ID;
        }

        // Validación: edad inválida (menores a 0 o mayores a 120)
        if (p.getAge() < 0 || p.getAge() > 120) {
            return RegisterResult.INVALID_AGE;
        }

        // Validación: menor de edad
        if (p.getAge() < 18) {
            return RegisterResult.UNDERAGE;
        }

        // Validación: ID duplicado
        if (registeredIds.contains(p.getId())) {
            return RegisterResult.DUPLICATED;
        }

        // Si todas las validaciones pasan, registrar la persona
        registeredIds.add(p.getId());
        return RegisterResult.VALID;
    }
}
