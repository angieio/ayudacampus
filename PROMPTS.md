# Registro de Prompts - Fase 2 (AyudaCampus)

Este documento registra los prompts utilizados con Gemini (o herramientas de IA) durante el desarrollo de las mejoras de la Fase 2, especificando qué se propuso y cómo se aplicó o corrigió respetando la arquitectura MVVM + Repository.

---

## Mejora 1: Tiempo promedio de resolución al panel
- **Prompt usado:** 
  > *"Propón una consulta SQL en Room para calcular el promedio de tiempo de resolución (diferencia entre fechaCierre y fecha) para los tickets cerrados, y expónlo en el PanelViewModel como StateFlow."*
- **Propuesta de la IA:** 
  La IA propuso una consulta `SELECT AVG(fechaCierre - fecha) FROM tickets WHERE estado = 'CERRADO'` y mapearlo en el repositorio.
- **Aplicación y Corrección:** 
  Se ajustó la consulta para asegurar que `fechaCierre IS NOT NULL`, y en el ViewModel se mapeó el resultado en millis a un formato legible de horas y minutos, asegurando que la Vista solo consuma el `StateFlow` sin acceder a la base de datos directamente.

---

## Mejora 2: Contador de SLA por prioridad
- **Prompt usado:**
  > *"Diseña una consulta SQL en Room que cuente los tickets activos (no cerrados ni resueltos) que hayan superado su SLA según su prioridad (Alta: 4h, Media: 24h, Baja: 72h)."*
- **Propuesta de la IA:**
  Se propuso usar un `CASE WHEN` en SQLite comparando la diferencia de tiempo actual con los umbrales en milisegundos.
- **Aplicación y Corrección:**
  Se integró la consulta en `TicketDao`, se añadió el método en la interfaz `AyudaRepository` y su implementación en `AyudaRepositoryRoom`, exponiéndolo en `PanelViewModel` y mostrándolo en `PanelScreen`.

---

## Mejora 3: Búsqueda avanzada en la bandeja
- **Prompt usado:**
  > *"¿Cómo puedo implementar un filtrado simultáneo en BandejaViewModel que combine texto de búsqueda, prioridad y categoría sobre el Flow de tickets?"*
- **Propuesta de la IA:**
  Utilizar `combine` de Kotlin Coroutines combinando el flujo de tickets con `MutableStateFlow` para texto, prioridad y categoría, filtrando la lista en memoria.
- **Aplicación y Corrección:**
  Se aplicó exactamente este enfoque combinando los flujos reactivos en `BandejaViewModel`, y se añadieron `OutlinedTextField` y `LazyRow` con chips de filtrado en `BandejaScreen`.
