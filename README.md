# FulboMatch

Aplicación móvil para conectar jugadores y organizadores de fútbol amateur.

FulboMatch busca centralizar la organización de partidos que normalmente se coordinan por WhatsApp o redes sociales, facilitando la creación de partidos, la búsqueda de encuentros disponibles y la participación de jugadores según ubicación, nivel y disponibilidad.

## Entrega H1 - Pantallas mockeadas

Esta versión corresponde a la primera entrega obligatoria del Trabajo Práctico Integrador de Desarrollo de Aplicaciones I.

La aplicación presenta una demo mockeada de los principales flujos de navegación, sin integración real con backend. La integración con Firebase, persistencia local y lógica real queda prevista para la entrega H2.

## Funcionalidades implementadas

* Onboarding inicial.
* Login, registro y recuperación de contraseña.
* Home con buscador y filtros.
* Listado de partidos próximos y finalizados.
* Detalle de partido.
* Sumarse automáticamente a un partido con cupo disponible.
* Bajarse de un partido y liberar el cupo.
* Crear partido.
* Editar o cancelar partido propio.
* Perfil de usuario.
* Editar perfil.
* Panel de notificaciones.
* Detalle de partidos finalizados.
* Estadísticas de partidos finalizados.
* Modo claro y modo oscuro en las pantallas principales.

## Flujos principales

* Autenticación: onboarding, login, registro y recuperación de contraseña.
* Explorar partidos: visualización de partidos mediante cards.
* Sumarse a partido: inscripción automática si hay cupo disponible.
* Bajarse de partido: el usuario puede abandonar un partido en el que está anotado.
* Crear partido: formulario para cargar título, fecha, hora, ubicación, precio, cupo, nivel y descripción.
* Editar o cancelar partido: disponible para el usuario creador del partido.
* Perfil: visualización y edición de datos del usuario.
* Partidos pasados: consulta de resultados y estadísticas.
* Notificaciones: avisos por cambios, altas, bajas y cancelaciones.

## Tecnologías utilizadas

* Kotlin
* Jetpack Compose
* Material Design 3
* Navigation Compose
* Git / GitHub

## Arquitectura propuesta

El proyecto está orientado a una arquitectura MVVM + Repository.

Para H1, los datos se encuentran mockeados para representar los flujos principales de la aplicación. Para H2, se prevé incorporar:

* Firebase Authentication para gestión de usuarios.
* Firestore como base de datos remota.
* Firebase Storage para imágenes.
* Room Database para persistencia local y soporte offline.
* Repository para decidir entre fuente remota o local.

## Próximos pasos para H2

* Integración con Firebase Auth, Firestore y Storage.
* Persistencia local con Room.
* Modo offline y sincronización de datos.
* Implementación completa de lógica de negocio.
* Validaciones reales de formularios.
* Uso de sensores o dispositivo de captura según requisito de la consigna.
* Pruebas, métricas y documentación final.

## Recursos

* Tablero de seguimiento: https://trello.com/invite/b/6a10cb1c015e998ce5b5a8a0/ATTI89f853c190eff9925318b204f511508483CC90E0/fulbomatch-tp-aplicaciones-mobile
* Prototipo: https://www.figma.com/design/jrV89ulsyAm47VuzjaMWJH/DesaApp1?node-id=94-2&t=z933aQEHbXTbKCdd-1

