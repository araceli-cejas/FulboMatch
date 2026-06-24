# FulboMatch

Aplicación móvil para conectar jugadores y organizadores de fútbol amateur, diseñada para optimizar la organización de partidos y la gestión de comunidades deportivas.

## Descripción del Proyecto
FulboMatch centraliza la organización de partidos, eliminando la fricción de los grupos de WhatsApp y redes sociales. La plataforma facilita la creación de encuentros, la búsqueda según disponibilidad, ubicación y nivel, y gestiona la participación de jugadores mediante un sistema reactivo en tiempo real.

## Características Técnicas Destacadas

* **Arquitectura:** Implementación de **MVVM (Model-View-ViewModel)** con patrón **Repository** para una clara separación de responsabilidades.
* **Persistencia Híbrida:** Estrategia **Offline-First** utilizando **Room Database** (local) y **Firebase Firestore** (remota) para garantizar disponibilidad constante.
* **Tiempo Real:** Notificaciones automáticas ante inscripciones o bajas mediante listeners de Firestore.
* **UI/UX:** Interfaz moderna desarrollada con **Jetpack Compose** y **Material Design 3**, con soporte completo para modo claro/oscuro.
* **Calidad:** Suite de pruebas automatizadas (**UI Tests**) con `Compose Test Rule` para validar los casos de uso críticos.

## Funcionalidades Implementadas

* **Autenticación:** Sistema de Login/Registro seguro vía Firebase Auth.
* **Gestión de Partidos:** Creación, edición, cancelación y visualización de partidos próximos/finalizados.
* **Reactividad:** Sistema de notificaciones en tiempo real para organizadores.
* **Sincronización:** Soporte offline con visualización de estado de conexión.
* **Perfil:** Gestión de datos personales y estadísticas de juego.

## Tecnologías y Herramientas

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose
* **Arquitectura:** MVVM + Clean Architecture (Repository Pattern)
* **Persistencia:** Room (Local), Firebase Firestore (Remoto)
* **Autenticación:** Firebase Authentication
* **Testing:** JUnit, Espresso / Compose UI Test
* **Versionado:** Git / GitHub

## Estructura del Repositorio

* `/ui`: Capa de presentación (Screens, ViewModels, Components).
* `/data`: Repositorios y Modelos.
* `/data/local`: Entidades y DAOs de Room.
* `/androidTest`: Pruebas de integración y UI.

## Cómo ejecutar el proyecto

1. Clonar el repositorio: `git clone [URL_DEL_REPO]`
2. Abrir en Android Studio (versión estable recomendada).
3. Configurar el archivo `google-services.json` de Firebase en el módulo `:app`.
4. Ejecutar tests: Clic derecho en `androidTest` > **Run 'All Tests'**.
5. Ejecutar aplicación: Seleccionar emulador y presionar **Run**.

## Próximos pasos (Roadmap)

* [ ] Optimización de carga de imágenes en perfil.
* [ ] Implementación de geolocalización avanzada para búsqueda por radio.
* [ ] Generación de reportes de estadísticas mediante Firebase Cloud Functions.

## Recursos

* **Tablero de seguimiento:** [Trello](https://trello.com/invite/b/6a10cb1c015e998ce5b5a8a0/ATTI89f853c190eff9925318b204f511508483CC90E0/fulbomatch-tp-aplicaciones-mobile)
* **Prototipo:** [Figma](https://www.figma.com/design/jrV89ulsyAm47VuzjaMWJH/DesaApp1?node-id=94-2&t=z933aQEHbXTbKCdd-1)
