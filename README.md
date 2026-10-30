# 🛡️ MunicipioSeguro - Aplicación de Seguridad Comunal y Reporte Ciudadano

**MunicipioSeguro** es una aplicación móvil nativa para Android desarrollada con **Kotlin** y **Jetpack Compose**, diseñada para fortalecer la seguridad y la convivencia comunal en la **Municipalidad de San Joaquín**.

La plataforma permite a los vecinos y residentes reportar situaciones no urgentes en su barrio (luminarias apagadas, veredas dañadas, ruidos molestos), hacer seguimiento a sus casos en tiempo real e informarse sobre avisos municipales. Además, cuenta con un panel exclusivo de gestión para operadores, supervisores y administradores municipales.

---

## ✨ Características Principales

### 👥 Para los Residentes / Vecinos
* 🔑 **Autenticación con Base de Datos Local**: Registro e inicio de sesión seguro almacenado en una base de datos SQLite con **Room Database**.
* 🔐 **Persistencia de Sesión**: La sesión del usuario se mantiene activa automáticamente entre reinicios de la aplicación mediante `SharedPreferences`.
* 📝 **Creación de Reportes**: Formulario intuitivo para ingresar incidentes seleccionando categoría (*Alumbrado público, Espacio público, Convivencia vecinal, Otro*), ubicación, nivel de urgencia/prioridad (*Baja, Media, Alta*) y descripción.
* 📸 **Captura de Evidencia Fotográfica**: Integración nativa con la cámara del teléfono (`TakePicturePreview`) para adjuntar fotografías a los reportes.
* 📍 **Geocodificación GPS Automática**: Convierte las direcciones de texto escritas por el usuario en coordenadas GPS reales (latitud y longitud) en la comuna de San Joaquín.
* 📋 **Mis Reportes y Comunidad**: Pestañas para consultar tanto los reportes personales como la lista pública de incidentes reportados en la comuna.
* 🔍 **Detalle y Seguimiento del Caso**: Ficha completa del incidente con mapa, estado actual y una línea de tiempo cronológica con los avances u observaciones del municipio.
* 📢 **Avisos Comunales**: Sección con comunicados oficiales de la municipalidad sobre cortes programados de servicios, operativos veterinarios y talleres vecinales.
* 👤 **Perfil de Usuario**: Vista de cuenta personalizable donde se puede cambiar la foto de perfil utilizando la cámara del dispositivo.

---

### 💼 Para Operadores, Supervisores y Administradores
* 🛠️ **Panel de Administración**: Vista de control que permite a los funcionarios municipales revisar todos los incidentes recibidos.
* 🔄 **Actualización de Estado en Tiempo Real**: Capacidad de cambiar el estado de un caso entre *Registrado*, *En revisión*, *Derivado a cuadrilla* y *En seguimiento / Resuelto*.
* 💬 **Historial de Observaciones**: Posibilidad de añadir notas técnicas y comentarios oficiales a la línea de tiempo del reporte para informar al vecino.

---

## 🏛️ Arquitectura del Proyecto (MVVM + Clean Architecture)

El proyecto está construido siguiendo las mejores prácticas de desarrollo Android recomendadas por Google, utilizando el patrón **Model-View-ViewModel (MVVM)** y **Kotlin Coroutines**:

```text
com.duoc.municipioseguro
├── data/local/                 <-- Persistencia SQLite con Room Database
│   ├── MunicipioDatabase.kt    <-- Clase base de datos Room y prepoblación inicial
│   ├── UsuarioDao.kt           <-- Consultas SQL para login y registro
│   └── UsuarioEntity.kt        <-- Entidad/Tabla de usuarios en SQLite
├── Model/                      <-- Modelos de Dominio e Interfaces Selladas
│   ├── CasoSeguridad.kt
│   ├── CategoriaCaso.kt
│   ├── Estado.kt
│   ├── Observacion.kt
│   ├── Prioridad.kt
│   ├── SesionUsuario.kt
│   └── UsuarioRol.kt
├── repository/                 <-- Capa de Repositorios (Lógica de Datos)
│   ├── AuthRepository.kt       <-- Manejo de Room + SharedPreferences
│   └── CasoSeguridadRepository.kt <-- Gestión de casos en memoria / base local
├── viewModel/                  <-- Lógica de Presentación
│   └── CasoSeguridadViewModel.kt <-- Manejo de StateFlows y Corrutinas
├── ui/                         <-- Capa de Interfaz de Usuario (Jetpack Compose)
│   ├── screens/                <-- Pantallas funcionales (Login, Home, Reportes, etc.)
│   └── theme/                  <-- Estilos, paleta de colores y tipografía Material 3
└── MainActivity.kt             <-- Punto de entrada y orquestador de navegación
```

---

## 🛠️ Tecnologías y Librerías Utilizadas

* **Lenguaje**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) + **Material Design 3**
* **Base de Datos Local**: [Room Database](https://developer.android.com/training/data-storage/room) (SQLite) + **KSP**
* **Manejo de Estado**: `ViewModel`, `StateFlow` y `Kotlin Coroutines`
* **Geocodificación**: Android `Geocoder API`
* **Cámara**: Activity Result Contracts (`TakePicturePreview`)
* **Manejo de Preferencias**: Android `SharedPreferences`

---

## 🔑 Credenciales de Prueba por Defecto

Al abrir la aplicación por primera vez, la base de datos local **Room** se prepuebla automáticamente con las siguientes cuentas de prueba:

| Rol | Correo Electrónico | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `admin@sanjoaquin.cl` | `admin123` |
| **Operador** | `operador@sanjoaquin.cl` | `operador123` |
| **Residente (Vecino)** | `vecino@sanjoaquin.cl` | `vecino123` |

*También se puede registrar un nuevo usuario desde la opción **"Crear cuenta"** o usar el selector de roles simulado en la pantalla de inicio de sesión.*

---

## 🚀 Cómo Ejecutar el Proyecto

1. **Requisitos Previos**:
   * Android Studio Ladybug (o superior).
   * JDK 17 o superior.
   * Dispositivo físico o Emulador Android con versión **Android 7.0 (API 24)** o superior.

2. **Pasos para Iniciar**:
   * Clona este repositorio o descarga los archivos fuente.
   * Abre la carpeta del proyecto en **Android Studio**.
   * Espera a que **Gradle Sync** descargue las dependencias.
   * Haz clic en **Run 'app'** (`Shift + F10`) para compilar y ejecutar en tu emulador o celular.

---

## 📝 Licencia y Créditos

Proyecto desarrollado como prototipo funcional para la **Municipalidad de San Joaquín** y la evaluación académica en **DUOC UC**. Todos los datos e incidentes cargados son de carácter ficticio para demostración de software.
