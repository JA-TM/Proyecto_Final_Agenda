# G_Agenda — Generador de agendas semanales

Proyecto final del **Grupo ROSIJO**.  
Aplicación en Java que genera calendarios semanales en HTML a partir de un fichero de peticiones (cierres y reservas de salas).

---

## ¿Qué hace?

1. Lee la configuración del mes/año e idioma (`config.txt`).
2. Lee las peticiones de uso de salas (`peticions.txt`).
3. Valida, ordena (prioriza `Cerrado`) y asigna franjas horarias.
4. Genera una página HTML por sala (`Sala1.html`, `Sala2.html`).
5. Escribe los conflictos de ocupación en `incidencias.log`.

No hace falta modificar el código Java para cambiar mes, idioma o reservas: se controla con los ficheros de datos.

---

## Requisitos

- JDK 17 o superior (probado con Temurin 25).
- Editor opcional: VS Code / Cursor (extensión Java) o terminal.

---

## Estructura del proyecto

```
Proyecto_Final_Agenda-main/
├── src/                      # Código fuente Java
│   ├── Main.java             # Punto de entrada
│   ├── Configuracion.java
│   ├── GestionDatosTiempo.java
│   ├── ProcesadorAgenda.java
│   ├── Peticion.java
│   └── GeneradorHTML.java
├── bin/                      # Clases compiladas (.class)
├── config.txt                # Mes/año e idiomas
├── peticions.txt             # Peticiones (cierres y actividades)
├── internacional.ESP         # Diccionario español
├── internacional.ENG         # Diccionario inglés
├── Sala1.html / Sala2.html   # Salida (se regeneran al ejecutar)
├── incidencias.log           # Conflictos de ocupación
├── Manual.txt                # Manual de usuario
└── Informes/                 # Memoria y documentación del grupo
```

---

## Configuración (`config.txt`)

Dos líneas:

```text
2025 11
esp ENG
```

| Línea | Formato | Ejemplo | Significado |
|-------|---------|---------|-------------|
| 1 | `AÑO MES` | `2025 11` | Agenda de noviembre de 2025 |
| 2 | `ENTRADA SALIDA` | `esp ENG` | Idioma de lectura / idioma de la web |

### Idiomas de salida

| Código | Resultado |
|--------|-----------|
| `ESP` | Agenda en español (`internacional.ESP`) |
| `ENG` | Agenda en inglés (`internacional.ENG`) |

> El catalán (`CAT`) no se utiliza en este proyecto.

---

## Peticiones (`peticions.txt`)

Cada línea es una petición:

```text
Actividad  Sala  FechaInicio  FechaFin  Dias  Horas
```

Ejemplo:

```text
Cerrado Sala1 01/01/2025 31/12/2025 LMXJVSD 00-07_21-24
ReunioJava Sala1 15/10/2025 20/12/2025 LMXJV 12-21
```

| Campo | Descripción |
|-------|-------------|
| Actividad | Nombre (`Cerrado`, `ReunioPerl`, `ReunioJava`, …) |
| Sala | Espacio (`Sala1`, `Sala2`, …) |
| Fechas | `dd/mm/aaaa` (inicio y fin) |
| Días | Máscara semanal, p. ej. `LMXJVSD` (Lun…Dom) |
| Horas | Rangos `inicio-fin`; varios unidos con `_` (p. ej. `12-13_17-18`) |

Reglas importantes:

- Si la fecha de fin es anterior a la de inicio, la petición se descarta.
- Los `Cerrado` se procesan primero.
- Si dos actividades piden la misma franja, gana la primera; la otra se anota en `incidencias.log`.

---

## Cómo ejecutar

### Opción A — VS Code / Cursor

1. Ajusta `config.txt` y `peticions.txt` si lo necesitas.
2. Abre `src/Main.java`.
3. Pulsa **Run** (▶).
4. Abre en el navegador `Sala1.html` y `Sala2.html`.

### Opción B — Terminal

```bash
cd ruta/al/proyecto
javac -d bin src/*.java
java -cp bin Main
open Sala1.html Sala2.html   # macOS
```

En macOS, si `java` no está en el PATH:

```bash
export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
```

---

## Salidas

| Archivo | Contenido |
|---------|-----------|
| `Sala1.html`, `Sala2.html` | Calendario semanal del mes configurado |
| `incidencias.log` | Franjas que no se pudieron asignar por estar ocupadas |

En el HTML:

- **Gris** → cerrado
- **Azul** → actividad / reunión

---

## Módulos (grupo)

| Parte | Responsabilidad aproximada |
|-------|----------------------------|
| Datos y tiempo | `config.txt`, diccionarios, fechas |
| Procesador de agenda | Lectura, validación, matriz, incidencias |
| Generador HTML | Páginas de cada sala |

---

## Documentación adicional

- `Manual.txt` — manual de usuario breve
- `README_DatosYTiempo.md` — detalle del módulo de datos y tiempo
- Carpeta `Informes/` — memoria y presentaciones

---

## Uso académico

Proyecto académico — Grupo ROSIJO.
