# Plan de Implementación: Refactorización Estricta MVVM (Limpieza de Lógica en UI)

## Descripción del Objetivo
Refactorizar las pantallas de la interfaz de usuario (UI / Composables) para separar la lógica de negocio, validaciones complejas y filtrados de datos, trasladándolas al `CasoSeguridadViewModel` y a los repositorios, dejando la UI exclusivamente como una capa visual limpia y reactiva (*Frontend puro*).

---

## Cambios Propuestos por Componente

### 1. ViewModels (`viewModel/CasoSeguridadViewModel.kt`)
- Centralizar las validaciones de formularios:
  - Validación de Login (`validarLogin(correo, contrasena)`).
  - Validación de Registro de Usuario (`validarRegistro(...)`).
  - Validación de Creación de Reportes (`validarNuevoReporte(...)`).
- Exponer estados limpios para los filtros en pantallas como `AdministracionScreen` y `HomeScreen` si es necesario, o mantener las funciones de filtrado desacopladas.

### 2. Pantallas / UI (`ui/screens/`)
- **`LoginScreen.kt`**: Remover la validación directa de `Patterns.EMAIL_ADDRESS` y campos vacíos del Composable; delegar la ejecución al ViewModel y recibir el resultado de error o éxito.
- **`CrearCuenta.kt`**: Remover las comprobaciones de campos vacíos y delegarlas al ViewModel.
- **`CrearReporteScreen.kt`**: Remover la generación aleatoria de folios (`"SJ-2026-..."`) y validaciones directas de campos vacíos, delegando la creación al ViewModel.
- **`AdministracionScreen.kt` y `HomeScreen.kt`**: Mantener la UI enfocada en renderizar estados, recibiendo listas ya procesadas o delegando filtros al ViewModel.

---

## Plan de Verificación

### Pruebas Automatizadas
- Ejecutar pruebas unitarias existentes (`app:testDebugUnitTest`).
- Compilar la aplicación con Gradle (`app:assembleDebug`).

### Verificación Manual
- Probar flujos de login, creación de cuentas y registro de reportes para asegurar que las validaciones y la persistencia siguen funcionando correctamente a través del ViewModel.
