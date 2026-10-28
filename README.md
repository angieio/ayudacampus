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


## Capturas:

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/81d7e71d-037d-4113-8148-7ca690314a46" width="180"></td>
    <td><img src="https://github.com/user-attachments/assets/929e07e4-208c-49aa-89a6-b1ddcac6f309" width="180"></td>
    <td><img src="https://github.com/user-attachments/assets/afae8188-06f2-481a-b33d-2ac6ae745c64" width="180"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d16d8410-9d75-4279-bab2-0a0ca636bc6c" width="180"></td>
    <td><img src="https://github.com/user-attachments/assets/005c2252-fd18-4fb0-8243-cb1d1bbacfe1" width="180"></td>
    <td><img src="https://github.com/user-attachments/assets/e90e0947-8ba6-4e31-8d5c-ee6933789e9a" width="180"></td>
  </tr>
</table>


## Preguntas:

### ¿Por qué las reglas de estado están en el ViewModel y no en el DAO ni en el Composable?
El ViewModel aplica las reglas de negocio antes de pedirle al repositorio que guarde los cambios. Así, el Composable se encarga de mostrar la interfaz y el DAO de acceder a Room.
### ¿Por qué crearTicket es suspend y observarTicket es Flow?
crearTicket realiza una operación de escritura que puede tardar, por eso es suspend y no bloquea la interfaz. observarTicket devuelve un Flow porque los datos pueden cambiar y la pantalla debe recibir las actualizaciones.
### ¿Qué pasaría si un ticket pudiera cerrarse sin calificación?
Se podrían cerrar tickets sin registrar la opinión del estudiante. Eso dejaría incompleta la evaluación de la atención y no cumpliría la regla de cierre del proyecto.
### ¿Qué debería cambiar si mañana los tickets viven en un servidor?
Se reemplazaría la implementación local del repositorio por otra que use el servidor. La interfaz seguiría llamando al ViewModel, y este seguiría usando la interfaz AyudaRepository; así, las pantallas no tendrían que conocer de dónde vienen los datos.
