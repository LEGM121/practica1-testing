# Practica1-Testing: Guía Completa de Testing TDD

## 📚 Descripción del Proyecto

Este proyecto implementa un sistema de **Registro de Votantes** siguiendo la metodología **TDD (Test-Driven Development)** con patrones **AAA (Arrange-Act-Assert)** y **BDD (Behavior-Driven Development)**.

El sistema valida personas para registro de votación según reglas de negocio específicas: validación de edad, estado de vida, identificador único y otros criterios.

---

## 📁 Estructura del Proyecto

```
practica1-testing/
├── src/
│   ├── main/java/edu/unisabana/tyvs/domain/
│   │   ├── model/
│   │   │   ├── RegisterResult.java    # Enum de resultados
│   │   │   ├── Gender.java            # Enum de géneros
│   │   │   └── Person.java            # Modelo de Persona
│   │   └── service/
│   │       └── Registry.java          # Servicio de registro
│   └── test/java/edu/unisabana/tyvs/domain/service/
│       └── RegistryTest.java          # Pruebas unitarias (14 tests)
├── pom.xml                             # Configuración Maven
├── defectos.md                         # Registro de defectos
├── PRUEBAS_ESPECIFICACION.md          # Especificación de pruebas
└── README.md                           # Este archivo
```

---

## 🔧 Requisitos Previos

- **Java 8+** instalado
- **Maven 3.6+** instalado
- Git (opcional)

### Verificar instalación:
```bash
java -version
mvn -version
```

---

## 🚀 Instalación y Ejecución

### 1. Clonar o descargar el proyecto

```bash
git clone https://github.com/LEGM121/practica1-testing.git
cd practica1-testing
```

### 2. Compilar el proyecto

```bash
mvn clean compile
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXXs
```

---

## 🧪 Ejecutar Pruebas

### Opción 1: Ejecutar todas las pruebas

```bash
mvn clean test
```

**Resultado esperado:**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running edu.unisabana.tyvs.domain.service.RegistryTest
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: X.XXXs
[INFO] -------------------------------------------------------
[INFO] BUILD SUCCESS
```

### Opción 2: Ejecutar prueba específica

```bash
# Ejecutar solo la prueba de camino feliz
mvn test -Dtest=RegistryTest#shouldRegisterValidPerson

# Ejecutar solo las pruebas de edad
mvn test -Dtest=RegistryTest#shouldRejectUnderageAt17
mvn test -Dtest=RegistryTest#shouldRejectInvalidAgeOver120

# Ejecutar solo pruebas de duplicados
mvn test -Dtest=RegistryTest#shouldRejectDuplicateId
```

### Opción 3: Ejecutar con verbosidad

```bash
mvn test -X
```

---

## 📊 Medición de Cobertura de Código (JaCoCo)

### Generar reporte de cobertura

```bash
# Paso 1: Compilar y ejecutar pruebas con JaCoCo
mvn clean test

# Paso 2: Generar reporte
mvn jacoco:report
```

**Resultado esperado:**
```
[INFO] Generating JaCoCo report at target/site/jacoco/index.html
[INFO] BUILD SUCCESS
```

### Revisar el reporte

- **Navegador**: Abrir `target/site/jacoco/index.html` en navegador
- **Línea de comandos**: 
  ```bash
  # En Linux/Mac
  open target/site/jacoco/index.html
  
  # En Windows
  start target/site/jacoco/index.html
  ```

**Métricas esperadas:**
- Cobertura de líneas: **> 95%**
- Cobertura de ramas: **> 90%**
- Cobertura de métodos: **100%**

---

## 📋 Resumen de Pruebas Unitarias

El proyecto contiene **14 casos de prueba** organizados por tipo:

### ✅ Camino Feliz (Happy Path) - 3 pruebas

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRegisterValidPerson` | Ana, id=1, edad=30, viva | **VALID** ✓ |
| `shouldAcceptAdultAt18` | Juan, id=2, edad=18, vivo | **VALID** ✓ |
| `shouldAcceptMaxAge120` | Anciano, id=3, edad=120, vivo | **VALID** ✓ |

### ❌ Validación Nula - 1 prueba

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldReturnInvalidWhenPersonIsNull` | null | **INVALID** ✗ |

### 💀 Validación de Vida - 1 prueba

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRejectDeadPerson` | Carlos, id=4, edad=40, muerto | **DEAD** ✗ |

### 🆔 Validación de ID - 2 pruebas

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRejectWhenIdIsZero` | Pedro, id=0, edad=25, vivo | **INVALID_ID** ✗ |
| `shouldRejectWhenIdIsNegative` | Maria, id=-5, edad=25, viva | **INVALID_ID** ✗ |

### 🎂 Validación de Edad - 4 pruebas

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRejectUnderageAt17` | Menor, id=5, edad=17, viva | **UNDERAGE** ✗ |
| `shouldRejectNegativeAge` | Invalido, id=6, edad=-1, vivo | **INVALID_AGE** ✗ |
| `shouldRejectInvalidAgeOver120` | Muy_Viejo, id=7, edad=121, vivo | **INVALID_AGE** ✗ |
| `shouldRejectAgeWayOver120` | Imposible, id=8, edad=150, viva | **INVALID_AGE** ✗ |

### 📌 Validación de Duplicados - 1 prueba

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRejectDuplicateId` | Dos personas con id=777 | 1°: **VALID** ✓, 2°: **DUPLICATED** ✗ |

### 🔗 Casos Combinados - 2 pruebas

| Test | Entrada | Resultado Esperado |
|---|---|---|
| `shouldRejectDeadUnderageWithInvalidId` | Muerto, menor, id=-1 | **DEAD** ✗ (por prioridad) |
| `shouldRejectUnderageBeforeDuplicateCheck` | Menor con id duplicado | **UNDERAGE** ✗ (validación por orden) |

---

## 🔍 Orden de Prioridad de Validaciones

La clase `Registry.registerVoter()` aplica validaciones en este orden:

```java
1. null                        → INVALID
2. !alive (muerto)            → DEAD
3. id ≤ 0 (inválido)          → INVALID_ID
4. age < 0 || age > 120       → INVALID_AGE
5. age < 18 (menor)           → UNDERAGE
6. id ya registrado           → DUPLICATED
7. Todas las validaciones OK  → VALID (registra la persona)
```

**Ejemplo de prioridades en acción:**

```java
Person dead_underage = new Person("Joven_Muerto", -1, 15, Gender.MALE, false);
registry.registerVoter(dead_underage);
// Resultado: DEAD (porque se valida antes que UNDERAGE)
```

---

## 📝 Patrón AAA (Arrange-Act-Assert)

Cada prueba sigue esta estructura:

```java
@Test
public void shouldRejectUnderageAt17() {
    // ARRANGE: Preparar datos y objetos
    Person person = new Person("Menor", 5, 17, Gender.FEMALE, true);
    
    // ACT: Ejecutar la acción
    RegisterResult result = registry.registerVoter(person);
    
    // ASSERT: Verificar resultado
    assertEquals(RegisterResult.UNDERAGE, result);
}
```

---

## 🎭 Trazabilidad BDD (Given-When-Then)

Ejemplo de mapeo entre test y requerimiento:

```
Escenario: Rechazar persona menor de edad
  Dado (Given)    que existe una persona viva de 17 años
  Cuando (When)   intento registrarla como votante
  Entonces (Then) el resultado debe ser UNDERAGE

Implementación (JUnit):
  @Test
  public void shouldRejectUnderageAt17() { ... }
```

Todos los 14 tests están documentados en `PRUEBAS_ESPECIFICACION.md` con esta trazabilidad.

---

## 📊 Resultado Esperado Completo

Al ejecutar `mvn clean test`, debe obtener:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running edu.unisabana.tyvs.domain.service.RegistryTest

[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0

[INFO] BUILD SUCCESS
```

**Cobertura de código esperada:**
```
Line Coverage:    ████████████████████ 95%+
Branch Coverage:  ██████████████████   90%+
Method Coverage:  ████████████████████ 100%
```

---

## 🐛 Defectos Documentados

Se han identificado y resuelto 5 defectos principales:

| Defecto | Caso | Esperado | Estado |
|---------|------|----------|--------|
| Defecto 01 | Edad -1 | INVALID_AGE | ✅ Resuelto |
| Defecto 02 | Edad > 120 | INVALID_AGE | ✅ Resuelto |
| Defecto 03 | ID ≤ 0 | INVALID_ID | ✅ Resuelto |
| Defecto 04 | Edad < 18 | UNDERAGE | ✅ Resuelto |
| Defecto 05 | ID duplicado | DUPLICATED | ✅ Resuelto |

Ver `defectos.md` para detalles completos.

---

## 📚 Clases de Equivalencia

El análisis incluye clases de equivalencia para cada atributo:

### Edad
- **Inválida (negativa)**: edad < 0 → INVALID_AGE
- **Menor**: 0 ≤ edad < 18 → UNDERAGE
- **Válida**: 18 ≤ edad ≤ 120 → VALID (si pasan otras validaciones)
- **Inválida (máxima)**: edad > 120 → INVALID_AGE

### Estado de Vida
- **Viva**: true → continúa validaciones
- **Muerta**: false → DEAD

### Identificador
- **Inválido**: id ≤ 0 → INVALID_ID
- **Duplicado**: ya registrado → DUPLICATED
- **Único válido**: id > 0 y no registrado → continúa

### Nulidad
- **Nula**: null → INVALID

---

## 🛠️ Comandos Útiles Maven

```bash
# Compilar sin ejecutar tests
mvn clean compile

# Ejecutar solo tests (omitiendo compilación si está actualizada)
mvn test

# Ejecutar test específico
mvn test -Dtest=RegistryTest#shouldRejectDeadPerson

# Ejecutar con salida de error detallada
mvn test -X

# Limpiar generados y volver a compilar
mvn clean compile test

# Generar reporte de cobertura
mvn clean test jacoco:report

# Ejecutar y empaquetar
mvn clean package

# Ver dependencias
mvn dependency:tree
```

---

## 📖 Archivos de Referencia

- **`PRUEBAS_ESPECIFICACION.md`** - Matriz completa de clases de equivalencia, valores límite y trazabilidad BDD
- **`defectos.md`** - Registro detallado de defectos encontrados y resueltos
- **`src/test/java/.../RegistryTest.java`** - Código fuente de todas las pruebas

---

## ✅ Ciclo TDD: Red → Green → Refactor

Este proyecto implementa el ciclo completo:

### 🔴 RED
Escribir prueba que falla (e.g., `shouldRejectUnderageAt17`)

### 🟢 GREEN
Implementar validación mínima en `Registry.registerVoter()`
```java
if (p.getAge() < 18) {
    return RegisterResult.UNDERAGE;
}
```

### 🔵 REFACTOR
Mejorar código manteniendo todas las pruebas en verde:
```java
// Se refactoriza el orden de validaciones, se agrega HashSet, etc.
private final Set<Integer> registeredIds = new HashSet<>();
```

---

## 📞 Soporte y Referencia

Para más información sobre:
- **TDD**: Ver `PRUEBAS_ESPECIFICACION.md`
- **AAA Pattern**: Ver ejemplos en `RegistryTest.java`
- **BDD Scenarios**: Ver tabla en `PRUEBAS_ESPECIFICACION.md`
- **Defectos**: Ver `defectos.md`

---

## 📄 Licencia

Proyecto educativo - Universidad de Sabana

---

## 👨‍💻 Autor

**LEGM121** - Práctica 1 Testing

---

**Última actualización**: 2026-06-02
**Estado del Proyecto**: ✅ COMPLETO Y FUNCIONAL
