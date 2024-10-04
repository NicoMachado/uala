# Uala Challenge - Android Compose Application

## Descripción

Este proyecto es una aplicación Android desarrollada en **Jetpack Compose** que permite a los usuarios buscar ciudades, filtrar favoritos, y navegar a una pantalla de detalles donde se muestra un mapa con las coordenadas de la ciudad seleccionada.

### Funcionalidades Principales
- **Búsqueda de Ciudades**: Permite buscar ciudades por nombre usando un `TextField`. La búsqueda es reactiva y filtra la lista de ciudades mientras el usuario escribe.
- **Filtro de Favoritos**: Un interruptor (`Switch`) permite alternar entre mostrar todas las ciudades o solo las ciudades favoritas.
- **Navegación a la Pantalla de Mapa**: Al hacer clic en una ciudad, se navega a una pantalla de detalles que muestra un mapa centrado en las coordenadas de la ciudad seleccionada.
- **Mapa con Marcador**: La pantalla de detalles incluye un mapa de Google Maps que muestra un marcador en la ubicación de la ciudad (latitud y longitud).

## Estructura del Proyecto

### `MainViewModel`
El `MainViewModel` se encarga de manejar la lógica de la pantalla principal, incluyendo:

- **Búsqueda**: El `ViewModel` contiene un `StateFlow` llamado `searchQuery` que es observado por la UI para filtrar las ciudades según el texto ingresado.
- **Filtro de Favoritos**: Utiliza otro `StateFlow` llamado `isFavouritesOnly` para alternar entre mostrar todas las ciudades o solo las favoritas.
- **Navegación basada en eventos**: El `ViewModel` emite eventos de navegación mediante un `Channel` que es consumido por la UI. Esto permite que el `ViewModel` maneje la lógica de navegación sin depender directamente de `NavController`.

#### Principales métodos del `ViewModel`:

- `onSearchQueryChanged(query: String)`: Filtra las ciudades según el texto de búsqueda.
- `onToggleFavourites(onlyFavourites: Boolean)`: Alterna entre mostrar todas las ciudades o solo las favoritas.
- `navigateToMap(lat: Double, lon: Double)`: Emite un evento de navegación con las coordenadas de la ciudad seleccionada.

### `MainScreen`
Esta pantalla muestra una lista de ciudades, permitiendo al usuario buscar y filtrar favoritos. La `MainScreen` observa los cambios en el `ViewModel` y actualiza la UI automáticamente. Además, reacciona a los eventos emitidos por el `ViewModel` para navegar a la pantalla del mapa cuando el usuario selecciona una ciudad.

#### Componentes Clave:

- **TextField de Búsqueda**: Utiliza un `TextField` con un `leadingIcon` para la búsqueda y un `trailingIcon` para borrar el texto.
- **Switch de Favoritos**: Un interruptor para mostrar solo las ciudades favoritas.
- **CityCard**: Cada ciudad es representada en una `Card` que permite alternar su estado de favorito y navegar a la pantalla del mapa.

### `CityMapScreen`
Esta pantalla muestra un mapa de Google centrado en las coordenadas de la ciudad seleccionada. Utiliza la API de **Google Maps Compose** para integrar el mapa en la aplicación.

#### Detalles Técnicos:
- **GoogleMap**: Muestra el mapa y un marcador en la posición indicada por la latitud y longitud.
- **Scaffold**: La pantalla está contenida en un `Scaffold` que incluye una `TopAppBar` para la navegación.

## Decisiones de Implementación

1. **Navegación basada en eventos**: Opté por utilizar un patrón de eventos en el `ViewModel`, utilizando `Channel` y `StateFlow` para manejar la navegación. Esto permite una mejor separación de responsabilidades entre la lógica de la aplicación y la UI.

2. **Google Maps en Compose**: La integración de Google Maps se realizó utilizando la librería oficial `maps-compose`, lo que facilita el uso de mapas dentro de composables. La decisión de usar esta librería se tomó para aprovechar las capacidades de Compose y mantener el código más conciso.

3. **Filtrado de Ciudades**: La lógica de filtrado en el `ViewModel` asegura que se pueden combinar filtros de búsqueda y favoritos sin problemas de rendimiento, manteniendo el `StateFlow` sincronizado con la UI en todo momento.

## Suposiciones Realizadas
- Se asume que todas las ciudades tienen coordenadas válidas (latitud y longitud), por lo que no se ha implementado manejo de errores para coordenadas inválidas.
- La lista de ciudades y el estado de favoritos son manejados en memoria para simplificar la lógica de la aplicación. En una implementación real, estos datos podrían provenir de una base de datos local o una API externa.
- El mapa siempre está centrado en la ciudad seleccionada con un nivel de zoom predeterminado (10f).

