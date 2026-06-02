# Registro de Defectos - Practica1-Testing

## Defecto 01
- **Caso**: Edad -1
- **Esperado**: INVALID_AGE
- **Obtenido**: VALID (antes de implementar validaciones)
- **Causa probable**: Falta de validación en límites de edad
- **Estado**: Resuelto (implementado en Registry.java)
- **Fecha de resolución**: 2026-06-02

## Defecto 02
- **Caso**: Persona con edad > 120
- **Esperado**: INVALID_AGE
- **Obtenido**: VALID (antes de implementar validaciones)
- **Causa probable**: Falta de validación en límite superior de edad
- **Estado**: Resuelto (implementado en Registry.java)
- **Fecha de resolución**: 2026-06-02

## Defecto 03
- **Caso**: Persona con ID <= 0
- **Esperado**: INVALID_ID
- **Obtenido**: VALID (antes de implementar validaciones)
- **Causa probable**: Falta de validación de ID positivo
- **Estado**: Resuelto (implementado en Registry.java)
- **Fecha de resolución**: 2026-06-02

## Defecto 04
- **Caso**: Persona con edad < 18
- **Esperado**: UNDERAGE
- **Obtenido**: VALID (antes de implementar validaciones)
- **Causa probable**: Falta de validación de mayoría de edad
- **Estado**: Resuelto (implementado en Registry.java)
- **Fecha de resolución**: 2026-06-02

## Defecto 05
- **Caso**: Persona duplicada (mismo ID)
- **Esperado**: DUPLICATED
- **Obtenido**: VALID (antes de implementar almacenamiento)
- **Causa probable**: Falta de registro de IDs y validación de duplicados
- **Estado**: Resuelto (implementado en Registry.java con HashSet)
- **Fecha de resolución**: 2026-06-02
