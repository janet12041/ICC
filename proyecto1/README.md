Introducción a Ciencias de la Computación
=========================================

Proyecto 1
-------------------------------------------------------

### Fecha de entrega: martes 4 de diciembre, 2020

Aplicación de una base de datos que almacena registros de tipo Libro, los cuales
cuentan con 7 campos: titulo, autor, editorial, año de publicación, número de
edición, número de páginas y precio.

La aplicación funciona pidiendo varios registros campo por campo y guardando la
base de datos en un archivo especificado por la línea de comandos; o bien
cargando la base de datos de un archivo especificado por la línea de comandos.
En ambos casos el programa muestra los registros en la base de datos y hace dos
consultas sobre campos diferentes de los registros.

El proyecto debe compilar al hacer:

```
$ mvn compile
```

También debe pasar todas sus pruebas unitarias al hacer:

```
$ mvn test
```

Por último, se debe ejecutar correctamente el programa escrito en la clase
[Proyecto1] al hacer:

```
$ mvn install
...
$ java -jar target/proyecto1.jar -g archivo.db # guarda la base de datos
...
$ java -jar target/proyecto1.jar -c archivo.db # carga la base de datos
```

