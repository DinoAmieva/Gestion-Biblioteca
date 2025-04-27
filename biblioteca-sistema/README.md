# Sistema de Gestión de Biblioteca

Este es un sistema de gestión de biblioteca desarrollado en Java que permite administrar libros, préstamos y generar reportes.

## Características

- Gestión completa de libros (agregar, eliminar, buscar)
- Sistema de préstamos y devoluciones
- Generación de reportes
- Interfaz gráfica intuitiva
- Búsqueda por título, autor o ISBN
- Almacenamiento de historial de préstamos

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior

## Instalación

1. Clonar el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
```

2. Navegar al directorio del proyecto:
```bash
cd biblioteca-sistema
```

3. Compilar el proyecto con Maven:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
java -jar target/biblioteca-sistema-1.0-SNAPSHOT.jar
```

## Uso

### Gestión de Libros

1. En la pestaña "Gestión de Libros":
   - Ingrese los datos del libro (título, autor, ISBN, año)
   - Haga clic en "Agregar Libro"
   - Use el panel de búsqueda para encontrar libros específicos

### Préstamos y Devoluciones

1. En la pestaña "Préstamos y Devoluciones":
   - Para prestar un libro: ingrese el ISBN y el nombre del usuario
   - Para devolver un libro: ingrese el ISBN del libro prestado

### Reportes

1. En la pestaña "Reportes":
   - Genere reportes de libros disponibles
   - Genere reportes de libros prestados
   - Los reportes se guardan en archivos de texto en el directorio de trabajo

## Estructura de Datos

El sistema utiliza las siguientes estructuras de datos:

- **Lista Enlazada**: Para almacenar y gestionar los libros
- **Cola**: Para manejar los préstamos activos
- **HashMap**: Para búsquedas rápidas por ISBN
- **ArrayList**: Para generar reportes y mostrar resultados

## Contribuir

Si desea contribuir al proyecto:

1. Haga un fork del repositorio
2. Cree una rama para su función (`git checkout -b feature/nueva-funcion`)
3. Haga commit de sus cambios (`git commit -am 'Agrega nueva función'`)
4. Haga push a la rama (`git push origin feature/nueva-funcion`)
5. Cree un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles. 