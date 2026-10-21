# AyudaCampus

Aplicación Android para reportar y gestionar incidencias de soporte TI en un campus. El estudiante puede crear y consultar tickets; el perfil de Soporte TI permite atenderlos, responder y administrar categorías.

### Nombres y Apellidos: Angieluz Vasquez Macalupu

## Funcionalidades

- Inicio de sesión como Estudiante o Soporte TI.
- Creación y seguimiento de tickets con prioridad, categoría y ubicación.
- Búsqueda y filtros por estado.
- Conversación entre estudiante y Soporte TI.
- Resolución, calificación, cierre y reapertura de tickets.
- Panel de soporte con contadores y gráficos.
- Preferencias de modo oscuro, notificaciones y recepción de tickets.
- Persistencia local con Room y DataStore.

## Tecnologías

- Kotlin
- Jetpack Compose y Material 3
- MVVM + Repository
- Room y KSP
- DataStore Preferences
- Coroutines y Flow

## Estructura de paquetes

```text
com.vasquez.ayudacampus
├── AyudaApp.kt
├── MainActivity.kt
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   ├── relation
│   │   ├── AyudaDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── prefs
│   │   └── PreferenciasStore.kt
│   └── repository
│       ├── AyudaRepository.kt
│       └── AyudaRepositoryRoom.kt
├── model
│   ├── ConteoCategoria.kt
│   ├── DatoGrafico.kt
│   ├── EstadoTicket.kt
│   ├── Prioridad.kt
│   └── Rol.kt
├── ui
│   ├── cliente
│   ├── components
│   ├── navigation
│   ├── soporte
│   └── theme
└── viewmodel
```
## Pruebas 

| Prueba | Resultado esperado | OK |
|---|---|---|
| Login con clave incorrecta | Muestra error y no inicia sesión | ✅ |
| Estudiante: crear ticket vacío | Muestra errores junto a los campos y no crea el ticket | ✅ |
| Estudiante: crear ticket válido | Aparece en Mis tickets como ABIERTO y con la prioridad elegida | ✅ |
| Soporte: bandeja | Aparece el ticket; prioridad Alta se ordena primero | ✅ |
| Soporte: cambiar a En proceso y responder | El estudiante ve el estado y la respuesta sin reiniciar | ✅ |
| Soporte: intentar cerrar un ticket | La opción Cerrado está deshabilitada | ✅ |
| Soporte: marcar Resuelto | El estudiante ve el aviso, califica y cierra | ✅ |
| Estudiante: reabrir ticket resuelto | Vuelve a ABIERTO y se borra la calificación | ✅ |
| Comentar en ticket cerrado | El campo no aparece o el comentario no se guarda | ✅ |
| Panel | Contadores, gráficos y alerta coinciden con los tickets | ✅ |
| Desactivar categoría | Ya no aparece al crear un ticket | ✅ |
| Cerrar y abrir la aplicación | Los tickets siguen ahí y perfil y modo oscuro se conservan | ✅ |
