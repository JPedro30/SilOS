# ESPECIFICACIÓN DEL MODELO DE DATOS (ER)

Este documento detalla la estructura del modelo relacional, las tablas, sus campos, claves primarias, claves foráneas y los principios de auditoría e integridad aplicados a la base de datos de gestión de silos y logística.

## 1. DIAGRAMA ER (RELACIONAL)

```
  +------------------+         +----------------------+         +--------------------+
  |    EMPRESAS      |         |        SILOS         |         |     MATERIALES     |
  +------------------+         +----------------------+         +--------------------+
  | PK ID_EMPRESA    |1       *| PK ID_SILO           |*       1| PK ID_MATERIAL     |
  |    CIF           |--------<| FK ID_EMPRESA (NULL) |>--------|    TIPO_GENERAL    |
  |    NOMBRE        |         | FK ID_MATERIAL(NULL) |         |    TIPO_ESPECIFICO |
  |    COLOR_HEX     |         |    NOMBRE            |         |    NOMBRE          |
  +------------------+         |    CAPACIDAD_MAX     |         +--------------------+
           |                   |    CANTIDAD_ACTUAL   |
           |                   |    HABILITADO        |
           |                   +----------------------+
           |                              | 1
           |                              |
           |                              | *
           |                   +------------------------------+
           |                   |         MOVIMIENTOS          |
           |                   +------------------------------+
           |                   | PK ID_MOVIMIENTO             |
           +------------------<| FK ID_EMPRESA                |
                               | FK ID_SILO                   |
                               | FK ID_MATERIAL               |
                               | FK ID_TRANSPORTISTA          |
                               | FK ID_USUARIO                |
                               |    TIPO_MOVIMIENTO           |
                               |    PESO_ENTRADA              |
                               |    PESO_SALIDA               |
                               |    CANTIDAD_NETA             |
                               |    FECHA_HORA_ENTRADA        |
                               |    FECHA_HORA_SALIDA         |
                               |    HUMEDAD_MATERIAL          |
                               |    PROTEINA                  |
                               |    HUMEDAD_AMBIENTAL         |
                               |    PRECIPITACION             |
                               |    CONDUCTOR_NOMBRE_SNAPSHOT |
                               |    CONDUCTOR_DNI_SNAPSHOT    |
                               +------------------------------+
                                          | *
                                          |
                                          | 1
                               +------------------------+
                               |     TRANSPORTISTAS     |
                               +------------------------+
                               | PK ID_TRANSPORTISTA    |
                               |    MATRICULA_CAMION    | (UNIQUE)
                               |    MATRICULA_REMOLQUE  |
                               |    NOMBRE_CONDUCTOR    |
                               |    DNI_CONDUCTOR       |
                               +------------------------+

  +------------------+         +----------------------+
  |     USUARIOS     |         |        ROLES         |
  +------------------+         +----------------------+
  | PK ID_USUARIO    |*       1| PK ID_ROL            |
  | FK ID_EMPRESA(N) |>--------|    NOMBRE_ROL        |
  | FK ID_ROL        |         +----------------------+
  |    USERNAME      |
  |    PASSWORD      |
  +------------------+

```

## 2. DICCIONARIO DE TABLAS Y CAMPOS

### 2.1 Tabla: `EMPRESAS`

Representa a los clientes propietarios del material almacenado en los silos.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_EMPRESA` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `CIF` | `VARCHAR(20)` | NO | Código de identificación fiscal (Único). | 
| `NOMBRE` | `VARCHAR(100)` | NO | Razón social o nombre comercial de la empresa. | 
| `COLOR_HEX` | `VARCHAR(7)` | NO | Código de color para UI (ej. `#FF5733`). Usado para representación visual en el mapa de silos. | 

### 2.2 Tabla: `SILOS`

Almacena la información de infraestructura de la planta de almacenamiento.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_SILO` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `ID_EMPRESA` | `BIGINT` (FK) | SÍ | Referencia a `EMPRESAS.ID_EMPRESA`. `NULL` si el silo está libre. | 
| `ID_MATERIAL` | `BIGINT` (FK) | SÍ | Referencia a `MATERIALES.ID_MATERIAL`. Material asignado por defecto para autocompletar operaciones. | 
| `NOMBRE` | `VARCHAR(50)` | NO | Identificador visual o número del silo (ej. "Silo A-01"). | 
| `CAPACIDAD_MAX` | `NUMERIC(10,2)` | NO | Capacidad estimada en kg (configurable, por defecto 500,000.00 kg). | 
| `CANTIDAD_ACTUAL` | `NUMERIC(10,2)` | NO | Stock en kg guardado actualmente en el silo. Se actualiza tras cada movimiento. | 
| `HABILITADO` | `BOOLEAN` | NO | Switch operativo. `true` = permite entradas y salidas; `false` = bloquea nuevos movimientos de entrada. | 

### 2.3 Tabla: `TRANSPORTISTAS`

Guarda el catálogo actualizado de vehículos y chóferes que acceden a la planta.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_TRANSPORTISTA` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `MATRICULA_CAMION` | `VARCHAR(20)` | NO | Matrícula del vehículo principal. Campo **ÚNICO** e indexado para búsquedas rápidas. | 
| `MATRICULA_REMOLQUE` | `VARCHAR(20)` | SÍ | Matrícula del remolque o acoplado si aplica. | 
| `NOMBRE_CONDUCTOR` | `VARCHAR(150)` | NO | Nombre y apellidos del chófer asignado actualmente a este vehículo. | 
| `DNI_CONDUCTOR` | `VARCHAR(12)` | NO | Documento de identidad del chófer actual. | 

### 2.4 Tabla: `MATERIALES`

Catálogo maestro de granos y productos almacenables.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_MATERIAL` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `TIPO_GENERAL` | `VARCHAR(50)` | NO | Clasificación broad (ej. "Trigo Duro", "Trigo Blando", "Triticale"). | 
| `TIPO_ESPECIFICO` | `VARCHAR(50)` | SÍ | Variedad específica (ej. "Panigale", "Kiko Nick"). | 
| `NOMBRE` | `VARCHAR(100)` | NO | Nombre descriptivo completo para exposición en selección (`TIPO_GENERAL + TIPO_ESPECIFICO`). | 

### 2.5 Tabla: `MOVIMIENTOS` (Núcleo Transaccional)

Registro inmutable de todas las entradas, salidas y ajustes de stock en la planta.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_MOVIMIENTO` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `ID_EMPRESA` | `BIGINT` (FK) | NO | Empresa propietaria de la carga en el momento del pesaje. | 
| `ID_SILO` | `BIGINT` (FK) | NO | Silo origen/destino del material. | 
| `ID_MATERIAL` | `BIGINT` (FK) | NO | Material específico transportado. | 
| `ID_TRANSPORTISTA` | `BIGINT` (FK) | NO | Referencia al camión utilizado. | 
| `ID_USUARIO` | `BIGINT` (FK) | NO | Operario con rol Trabajador que registró el pesaje. | 
| `TIPO_MOVIMIENTO` | `CHAR(1)` | NO | 'E' (Entrada) o 'S' (Salida). | 
| `PESO_ENTRADA` | `NUMERIC(10,2)` | NO | Peso bruto en báscula al entrar (kg). | 
| `PESO_SALIDA` | `NUMERIC(10,2)` | NO | Peso en báscula al salir (kg). | 
| `CANTIDAD_NETA` | `NUMERIC(10,2)` | NO | Valor absoluto: `abs(PESO_ENTRADA - PESO_SALIDA)`. | 
| `FECHA_HORA_ENTRADA` | `TIMESTAMP` | NO | Fecha y hora de entrada a la báscula. | 
| `FECHA_HORA_SALIDA` | `TIMESTAMP` | NO | Fecha y hora de pesaje de salida. Debe ser >= `FECHA_HORA_ENTRADA`. | 
| `HUMEDAD_MATERIAL` | `NUMERIC(4,2)` | SÍ | Porcentaje de humedad medida al grano. | 
| `PROTEINA` | `NUMERIC(4,2)` | SÍ | Porcentaje de proteína del grano. | 
| `HUMEDAD_AMBIENTAL` | `NUMERIC(4,2)` | SÍ | Capturado de la API Meteorológica al momento de guardar. | 
| `PRECIPITACION` | `NUMERIC(5,2)` | SÍ | Capturado de la API Meteorológica al momento de guardar (mm). | 
| `CONDUCTOR_NOMBRE_SNAPSHOT` | `VARCHAR(150)` | SÍ | **Foto fija / Auditoría:** Nombre del chófer en la fecha de la operación. | 
| `CONDUCTOR_DNI_SNAPSHOT` | `VARCHAR(12)` | SÍ | **Foto fija / Auditoría:** DNI del chófer en la fecha de la operación. | 

### 2.6 Tabla: `USUARIOS`

Entidad para autenticación, autorización y control de acceso.

| Campo | Tipo de Dato | Nulo | Descripción / Regla | 
| ----- | ----- | ----- | ----- | 
| `ID_USUARIO` | `BIGINT` (PK) | NO | Identificador único autoincremental. | 
| `ID_ROL` | `BIGINT` (FK) | NO | Rol del sistema (ADMIN, TRABAJADOR, EMPRESA). | 
| `ID_EMPRESA` | `BIGINT` (FK) | SÍ | Solo obligatoria si `ID_ROL` = EMPRESA. Limita el scope de consulta. | 
| `USERNAME` | `VARCHAR(50)` | NO | Credencial de acceso (Único). | 
| `PASSWORD` | `VARCHAR(255)` | NO | Hash BCrypt de la contraseña del usuario. | 

## 3. DECISIONES DE DISEÑO Y PATRONES APLICADOS

### 3.1 Captura de Foto Fija (Snapshot Pattern)

En la tabla `MOVIMIENTOS` se duplican intencionadamente los campos de nombre y DNI del conductor (`CONDUCTOR_NOMBRE_SNAPSHOT` y `CONDUCTOR_DNI_SNAPSHOT`).

* **Justificación:** Si un vehículo cambia de chófer en la tabla `TRANSPORTISTAS`, la actualización no modificará las firmas históricas de los pesajes antiguos. Los históricos mantendrán la integridad legal de la foto del momento.

### 3.2 Manejo de Transportista Ficticio para Mermas

Para soportar el reajuste automático a cero al vaciar o cambiar de propietario un silo, se define en la base de datos un registro reservado de transportista:

* `ID_TRANSPORTISTA`: `0` (o reservado mediante script Seed)

* `MATRICULA_CAMION`: `SISTEMA-MERMA`

* `NOMBRE_CONDUCTOR`: `AJUSTE AUTOMATICO DE STOCK`

### 3.3 Relaciones JPA y Rendimiento

* Todas las relaciones `@ManyToOne` en la entidad `MovimientoEntity` serán mapeadas con estrategia `FetchType.LAZY` para evitar lecturas ansiosas pesadas e innecesarias en la base de datos.

* Para el listado del histórico de movimientos se utilizarán consultas explicitas con `JOIN FETCH` para traer las entidades relacionadas (`Silo`, `Material`, `Transportista`, `Empresa`) en una única consulta SQL optimizada.

```
eof

He generado el archivo `docs/02-modelo-datos.md` con todo el detalle de la arquitectura de la base de datos, relaciones, diccionario de campos y explicaciones de diseño.

Quedo a tu disposición si deseas ajustar algo de la documentación o si estás listo para iniciar la configuración del proyecto Spring Boot.

```