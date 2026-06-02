package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegistryTest {

    private Registry registry;

    @Before
    public void setUp() {
        registry = new Registry();
    }

    // ====== CAMINO FELIZ (HAPPY PATH) ======

    @Test
    public void shouldRegisterValidPerson() {
        // Arrange: preparar los datos y el objeto a probar
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act: ejecutar la acción que queremos probar
        RegisterResult result = registry.registerVoter(person);

        // Assert: verificar el resultado esperado
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    public void shouldAcceptAdultAt18() {
        // Arrange
        Person person = new Person("Juan", 2, 18, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    public void shouldAcceptMaxAge120() {
        // Arrange
        Person person = new Person("Anciano", 3, 120, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    // ====== VALIDACIÓN NULA ======

    @Test
    public void shouldReturnInvalidWhenPersonIsNull() {
        // Arrange
        Person person = null;

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

    // ====== VALIDACIÓN DE VIDA ======

    @Test
    public void shouldRejectDeadPerson() {
        // Arrange: preparar los datos y el objeto a probar
        Person dead = new Person("Carlos", 4, 40, Gender.MALE, false);

        // Act: ejecutar la acción que queremos probar
        RegisterResult result = registry.registerVoter(dead);

        // Assert: verificar el resultado esperado
        assertEquals(RegisterResult.DEAD, result);
    }

    // ====== VALIDACIÓN DE ID ======

    @Test
    public void shouldRejectWhenIdIsZero() {
        // Arrange
        Person person = new Person("Pedro", 0, 25, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_ID, result);
    }

    @Test
    public void shouldRejectWhenIdIsNegative() {
        // Arrange
        Person person = new Person("Maria", -5, 25, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_ID, result);
    }

    // ====== VALIDACIÓN DE EDAD (LÍMITES) ======

    @Test
    public void shouldRejectUnderageAt17() {
        // Arrange
        Person person = new Person("Menor", 5, 17, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.UNDERAGE, result);
    }

    @Test
    public void shouldRejectNegativeAge() {
        // Arrange
        Person person = new Person("Invalido", 6, -1, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    public void shouldRejectInvalidAgeOver120() {
        // Arrange
        Person person = new Person("Muy_Viejo", 7, 121, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    public void shouldRejectAgeWayOver120() {
        // Arrange
        Person person = new Person("Imposible", 8, 150, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    // ====== VALIDACIÓN DE DUPLICADOS ======

    @Test
    public void shouldRejectDuplicateId() {
        // Arrange
        Person person1 = new Person("Ana", 777, 25, Gender.FEMALE, true);
        Person person2 = new Person("Carlos", 777, 30, Gender.MALE, true);

        // Act
        RegisterResult result1 = registry.registerVoter(person1);
        RegisterResult result2 = registry.registerVoter(person2);

        // Assert
        assertEquals(RegisterResult.VALID, result1);
        assertEquals(RegisterResult.DUPLICATED, result2);
    }

    // ====== CASOS COMBINADOS (MÚLTIPLES CONDICIONES) ======

    @Test
    public void shouldRejectDeadUnderageWithInvalidId() {
        // Prioridad: null -> dead -> invalid_id -> invalid_age -> underage -> duplicated -> valid
        // En este caso, dead tiene prioridad sobre underage
        Person person = new Person("Joven_Muerto", -1, 15, Gender.MALE, false);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.DEAD, result);
    }

    @Test
    public void shouldRejectUnderageBeforeDuplicateCheck() {
        // Arrange
        Person validAdult = new Person("Valido", 999, 25, Gender.MALE, true);
        Person underage = new Person("Menor", 999, 16, Gender.FEMALE, true);

        // Act
        RegisterResult result1 = registry.registerVoter(validAdult);
        RegisterResult result2 = registry.registerVoter(underage);

        // Assert
        assertEquals(RegisterResult.VALID, result1);
        // underage (edad < 18) se valida antes de duplicates
        assertEquals(RegisterResult.UNDERAGE, result2);
    }
}
