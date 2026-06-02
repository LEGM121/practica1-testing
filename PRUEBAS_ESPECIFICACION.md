# Especificación de Pruebas - Matriz de Clases de Equivalencia y Valores Límite

## Análisis de Clases de Equivalencia para registerVoter(Person)

### 1. ATRIBUTO: EDAD

| Clase de Equivalencia | Rango | Representante | Límites | Resultado Esperado |
|---|---|---|---|---|
| Edad inválida (negativa) | edad < 0 | -1 | -1, -5 | INVALID_AGE |
| Menor de edad | 0 ≤ edad < 18 | 17 | 0, 17 | UNDERAGE |
| Mayor de edad válido | 18 ≤ edad ≤ 120 | 30, 25 | 18, 120 | VALID (si otras reglas pasan) |
| Edad inválida (mayor a máximo) | edad > 120 | 121 | 121, 150 | INVALID_AGE |

### 2. ATRIBUTO: ESTADO DE VIDA (alive)

| Clase de Equivalencia | Valor | Descripción | Resultado Esperado |
|---|---|---|---|
| Persona viva | true | Continúa con otras validaciones | Depende de otras reglas |
| Persona muerta | false | No puede registrarse | DEAD |

### 3. ATRIBUTO: IDENTIFICADOR (ID)

| Clase de Equivalencia | Rango | Representante | Límites | Resultado Esperado |
|---|---|---|---|---|
| ID inválido | id ≤ 0 | 0, -5 | 0, -1 | INVALID_ID |
| ID duplicado | ID ya registrado | 777 (ya registrado) | N/A | DUPLICATED |
| ID único válido | id > 0 y no registrado | 1, 2, 100 | N/A | Continúa validación |

### 4. ATRIBUTO: NULIDAD

| Clase de Equivalencia | Valor | Resultado Esperado |
|---|---|---|
| Persona nula | null | INVALID |

---

## Matriz de Pruebas Unitarias con Trazabilidad BDD

| # | Nombre del Test (JUnit) | Escenario BDD (Given–When–Then) | Clase de Equivalencia | Valores Límite | Resultado Esperado |
|---|---|---|---|---|---|
| 1 | shouldRegisterValidPerson | Given persona viva, edad 30, id 1; When intento registrarla; Then VALID | Edad válida, viva, id único | N/A | VALID |
| 2 | shouldAcceptAdultAt18 | Given persona viva de 18 años, id único; When intento registrarla; Then VALID | Límite inferior edad válida | 18 | VALID |
| 3 | shouldAcceptMaxAge120 | Given persona viva de 120 años, id único; When intento registrarla; Then VALID | Límite superior edad válida | 120 | VALID |
| 4 | shouldReturnInvalidWhenPersonIsNull | Given persona es null; When intento registrarla; Then INVALID | Nulidad | null | INVALID |
| 5 | shouldRejectDeadPerson | Given persona muerta; When intento registrarla; Then DEAD | Persona muerta | alive=false | DEAD |
| 6 | shouldRejectWhenIdIsZero | Given id = 0, edad 25, viva; When intento registrarla; Then INVALID_ID | ID inválido | 0 | INVALID_ID |
| 7 | shouldRejectWhenIdIsNegative | Given id = -5, edad 25, viva; When intento registrarla; Then INVALID_ID | ID inválido | -5 | INVALID_ID |
| 8 | shouldRejectUnderageAt17 | Given persona de 17 años, viva, id válido; When intento registrarla; Then UNDERAGE | Menor de edad (límite inferior) | 17 | UNDERAGE |
| 9 | shouldRejectNegativeAge | Given persona con edad -1, viva, id válido; When intento registrarla; Then INVALID_AGE | Edad inválida (negativa) | -1 | INVALID_AGE |
| 10 | shouldRejectInvalidAgeOver120 | Given persona de 121 años, viva, id válido; When intento registrarla; Then INVALID_AGE | Edad inválida (mayor a máximo) | 121 | INVALID_AGE |
| 11 | shouldRejectAgeWayOver120 | Given persona de 150 años, viva, id válido; When intento registrarla; Then INVALID_AGE | Edad inválida (extremo) | 150 | INVALID_AGE |
| 12 | shouldRejectDuplicateId | Given dos personas con mismo id (777); When intento registrar ambas; Then 1° VALID, 2° DUPLICATED | ID duplicado | 777 (duplicado) | DUPLICATED |
| 13 | shouldRejectDeadUnderageWithInvalidId | Given persona muerta, menor, id inválido; When intento registrarla; Then DEAD | Múltiples condiciones | N/A | DEAD (por prioridad) |
| 14 | shouldRejectUnderageBeforeDuplicateCheck | Given persona menor que intenta usar id duplicado; When intento registrarla; Then UNDERAGE | Validación de prioridad | 16, 999 (dup) | UNDERAGE |

---

## Prioridad de Validaciones en Registry.registerVoter()

La implementación sigue este orden de validación (de mayor a menor prioridad):

1. **null** → INVALID
2. **!alive** → DEAD
3. **id <= 0** → INVALID_ID
4. **age < 0 || age > 120** → INVALID_AGE
5. **age < 18** → UNDERAGE
6. **id duplicado** → DUPLICATED
7. **Todas las validaciones pasan** → VALID (registrar)

Esta prioridad asegura que las validaciones defensivas se ejecuten primero, seguidas de las validaciones de dominio en orden de importancia funcional.

---

## Cobertura de Código Esperada

Con las pruebas implementadas, se espera lograr:

- **Cobertura de líneas**: > 95%
- **Cobertura de ramas**: > 90%
- **Cobertura de métodos**: 100%

Ejecutar para medir:
```bash
mvn clean test
mvn jacoco:report
# Revisar: target/site/jacoco/index.html
```
