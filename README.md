# MunicipioSeguro - Aplicación de Seguridad Comunal y Reporte Ciudadano

**MunicipioSeguro** es una aplicación móvil nativa para Android desarrollada con **Kotlin** y **Jetpack Compose**, diseñada para fortalecer la seguridad y la convivencia comunal en la **Municipalidad de San Joaquín**.

La plataforma permite a los vecinos y residentes reportar situaciones no urgentes en su barrio (luminarias apagadas, veredas dañadas, ruidos molestos), hacer seguimiento a sus casos en tiempo real e informarse sobre avisos municipales. Además, cuenta con un panel exclusivo de gestión para operadores, supervisores y administradores municipales.

---

## 📋 Cumplimiento de Rúbrica del Proyecto

Este proyecto cumple y supera los requerimientos solicitados en la evaluación académica:

| Requisito de Rúbrica | Estado | Detalle en el Proyecto |
| :--- | :---: | :--- |
| **Consumo de APIs Externas (Min. 7 endpoints)** | ✅ | **7 endpoints** implementados en Xano.com (GET, POST, DELETE). |
| **Pruebas Unitarias (Al menos 2 pruebas)** | ✅ | 3 pruebas automatizadas pasando exitosamente (`MockWebServer` y ViewModel). |
| **Generación de APK Firmado (Release)** | ✅ | Documentado y soportado mediante Gradle / Android Studio. |
| **Repositorio GIT Actualizado** | ✅ | Versionado y sincronizado con ramas y merges en GitHub. |
| **Tablero Trello Actualizado** | ✅ | Gestión de tareas y sprints en tablero Trello del equipo. |
| **Documentación Técnica (README)** | ✅ | Especificación completa de arquitectura, endpoints y uso. |
| **APK con Ícono Personalizado** | ✅ | Configurado mediante iconos adaptativos (`mipmap`). |
| **2 Recursos Nativos** | ✅ | **Cámara** (`TakePicturePreview`) y **GPS / Geocodificación** (`Geocoder` + `GoogleMap`). |

---

## 🌐 Consumo de APIs Externas (Backend en Xano.com)

La aplicación utiliza un backend en la nube alojado en **Xano.com** mediante **Retrofit 2** y **Gson**, integrando un modelo híbrido (*offline-first*) que coexiste con **Room Database** como caché local.

### Listado de los 7 Endpoints Consumidos:
1. **`GET /caso`**: Obtiene la lista completa de todos los reportes de seguridad comunal.
2. **`POST /caso`**: Registra un nuevo reporte ciudadano en el servidor.
3. **`POST /caso/{id}`**: Actualiza el estado o datos de un caso existente.
4. **`GET /caso/{id}`**: Consulta el detalle específico de un reporte por su ID/Folio.
5. **`DELETE /caso/{id}`**: Elimina un registro de caso en el servidor (soporte de borrado).
6. **`GET /usuarios`**: Obtiene la lista de usuarios registrados en el sistema.
7. **`POST /usuarios`**: Registra un nuevo usuario (vecino o funcionario municipal) con encriptación de credenciales.

---

## ⚙️ 2 Recursos Nativos del Dispositivo

1. **Cámara del Dispositivo**:
   * Implementada mediante *Activity Result Contracts* (`ActivityResultContracts.TakePicturePreview()`) en `CrearReporteScreen` y `PerfilUsuarioScreen`. Permite a los usuarios tomar fotografías en tiempo real para adjuntarlas como evidencia en los reportes o actualizar su foto de perfil.
2. **GPS y Geocodificación (`Geocoder` + `GoogleMap`)**:
   * Utiliza la API de geocodificación de Android junto con **Google Maps Compose** (`maps-compose`) para convertir ubicaciones descritas por el usuario en coordenadas geográficas precisas (`LatLng`), visualizando la posición exacta del incidente en un mapa interactivo.

---

## 📦 Generación de APK Firmado (Modo Release)

Para generar el APK de producción firmado:
1. En Android Studio, ve al menú superior: **Build > Build Bundle(s) / APK(s) > Build APK(s)** (o usa **Generate Signed Bundle / APK** para firmarlo con tu keystore de producción).
2. Selecciona la variante **release**.
3. El APK compilado se generará en la ruta:
   `app/build/outputs/apk/release/app-release.apk`

---

## 🎨 Ícono Personalizado de la Aplicación

El proyecto cuenta con un ícono personalizado configurado mediante **Adaptive Icons** en el directorio `app/src/main/res/mipmap`, asegurando compatibilidad con las formas adaptativas requeridas por las versiones modernas de Android (círculos, squircles y cuadrados).

---

## 📊 Tablero Trello y Gestión de Proyecto

El desarrollo del proyecto se ha organizado y gestionado utilizando un **Tablero Trello**, estructurado en columnas de progreso:
* *Backlog / Por hacer*
* *En desarrollo (In Progress)*
* *Pruebas y Verificación (Testing)*
* *Terminado (Done)*

---

## Características Principales

### Para los Residentes / Vecinos
* **Autenticación con Base de Datos Local y Nube**: Registro e inicio de sesión sincronizado entre Room y Xano.
* **Persistencia de Sesión**: Mantenimiento automático de sesión mediante `SharedPreferences`.
* **Creación de Reportes**: Formulario para incidentes (*Alumbrado público, Espacio público, Convivencia vecinal, Otro*).
* **Captura de Evidencia Fotográfica y GPS**.
* **Mis Reportes y Comunidad**: Pestañas de seguimiento de incidentes.
* **Avisos Comunales y Perfil de Usuario**.

### Para Operadores, Supervisores y Administradores
* **Panel de Administración Municipal**.
* **Actualización de Estado en Tiempo Real** (*Registrado*, *En revisión*, *Derivado*, *Resuelto*).
* **Historial de Observaciones** cronológicas.

---

## Arquitectura del Proyecto (MVVM + Clean Architecture)

El proyecto sigue estrictamente el patrón **Model-View-ViewModel (MVVM)**:
```text
com.duoc.municipioseguro
├── data/local/                 <-- Persistencia SQLite con Room Database
├── Model/                      <-- Modelos de Dominio e Interfaces Selladas
├── repository/                 <-- Capa de Repositorios (Room + Xano API)
├── remote/                     <-- Configuración de Retrofit y ApiService
├── viewModel/                  <-- Lógica de Presentación (StateFlows)
├── ui/                         <-- Capa de Interfaz de Usuario (Jetpack Compose)
└── MainActivity.kt             <-- Punto de entrada y orquestador
```

---

## Pruebas Unitarias e Integración (`app:testDebugUnitTest`)

El proyecto incluye pruebas automatizadas con **`kotlinx-coroutines-test`** y **`MockWebServer`**:
* **`CasoSeguridadRepositoryTest`**: Valida las respuestas HTTP y el consumo de la API mediante un servidor simulado.
* **`CasoSeguridadViewModelTest`**: Valida el comportamiento del ViewModel, el *dispatching* de corrutinas y la carga de datos iniciales.

Para correr las pruebas desde la terminal:
```bash
./gradlew :app:testDebugUnitTest
```

---

## Credenciales de Prueba

| Rol | Correo Electrónico | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `admin@sanjoaquin.cl` | `admin123` |
| **Operador** | `operador@sanjoaquin.cl` | `operador123` |
| **Residente (Vecino)** | `vecino@sanjoaquin.cl` | `vecino123` |

---

## Licencia y Créditos

Proyecto desarrollado para la **Municipalidad de San Joaquín** y evaluación académica en **DUOC UC**.
