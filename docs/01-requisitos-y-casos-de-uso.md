# ESPECIFICACIÓN DE REQUISITOS Y CASOS DE USO

## 1. REGLAS GENERALES Y ARQUITECTURA
- **Patrón de Capas:** Controller -> Service -> Repository -> Entity/DTO.
- **Inmutabilidad y DTOs:** No se exponen Entities directamente en la API.
- **Rendimiento:** Carga perezosa (LAZY) en todas las relaciones @ManyToOne de Movimientos.
- **Consultas Paginadas:** Uso de JOIN FETCH en las consultas de históricos de movimientos para evitar el problema N+1.
- **Auditoría:** La tabla Movimientos mantiene un snapshot del conductor (nombre y DNI) para conservar el histórico aunque cambie el chófer asignado al vehículo.

---

## 2. DOMINIO DE MOVIMIENTOS
### CU-01: Registrar Movimiento (Entrada / Salida)
- **Actor:** Trabajador.
- **Entradas:** ID Silo, Matrícula Transportista (búsqueda/alta), ID Material (autocompletado por Silo o manual), Peso Entrada, Peso Salida, Tipo Movimiento (ENTRADA / SALIDA), Fecha/Hora Entrada, Fecha/Hora Salida, Humedad (opcional), Proteína (opcional).
- **Validaciones Backend:**
  - `FECHA_HORA_SALIDA` debe ser >= `FECHA_HORA_ENTRADA`.
  - Si el Silo está `HABILITADO = false`, se RECHAZA cualquier movimiento de tipo 'ENTRADA' (HTTP 409 Conflict).
- **Efectos Secundarios:**
  - Calcula la cantidad neta y actualiza `CANTIDAD_ACTUAL` en la tabla `SILOS`.
  - Conecta de forma asíncrona a la API Meteorológica externa para capturar humedad ambiental y precipitación.
  - Si el transportista no existe, se crea automáticamente en el mismo flujo transaccional.

---

## 3. DOMINIO DE SILOS
### CU-02: Ciclo de Vida y Asignación de Silos
- **Actor:** Admin.
- **Estado Inicial:** `HABILITADO = false`, `ID_EMPRESA = null`, `CANTIDAD_ACTUAL = 0`, `CAPACIDAD_MAX = 500000` (configurable).
- **Asignación de Empresa:** Al vincular una `ID_EMPRESA`, pasa automáticamente a `HABILITADO = true`.
- **Semáforo Visual (UI):**
  - Verde: Cantidad < 80%.
  - Naranja (Revisar): Cantidad >= 80%.
  - Rojo (Alerta): Cantidad >= 95% (permite exceder el 100% por tolerancia física de volumen/peso).
- **Inhabilitación Manual:** El admin o el operario pueden cambiar el switch a `HABILITADO = false` por verificación física.
- **Reasignación / Vaciado de Silo:**
  - Al cambiar de empresa (o pasar a `null`), el sistema desencadena un cálculo de merma.
  - Genera automáticamente un registro en `MOVIMIENTOS` con el `ID_TRANSPORTISTA` de sistema (ID 0 / MERMA) ajustando la `CANTIDAD_ACTUAL` a 0.

---

## 4. DOMINIO DE TRANSPORTISTAS
### CU-03: Búsqueda y Gestión de Transportistas "Al Vuelo"
- **Actor:** Trabajador (durante el pesaje).
- **Identificador Único:** `MATRICULA_CAMION` (VARCHAR UNIQUE, indexado).
- **Flujo:**
  - Búsqueda por matrícula: si existe, devuelve los datos para autocompletar.
  - Si no existe: la UI habilita la creación manual.
  - Si los datos cambian (nuevo chófer): se actualiza la ficha del transportista y se guarda un snapshot de auditoría en el registro del movimiento.

---

## 5. DOMINIO DE MATERIALES
### CU-04: Catálogo y Fijación por Silo
- **Estructura:** Tipo General + Tipo Específico + Nombre Descriptivo.
- **Gestión:** CRUD exclusivo para **Admin**.
- **Comportamiento en Silo:** Se permite asignar un `ID_MATERIAL` por defecto a un Silo para autocompletar el formulario del trabajador.

---

## 6. DOMINIO DE EMPRESAS Y SEGURIDAD
### CU-05: Gestión de Propietarios e Identidad Visual
- **Gestión:** CRUD exclusivo para **Admin**.
- **Campos:** CIF, Nombre, Color Hexadecimal (ej. `#FF5733`).
- **Aislamiento de Datos:** El usuario con `ROL_EMPRESA` solo puede consultar datos de los silos vinculados a su `ID_EMPRESA`.