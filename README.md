# 🌾 silOS — Sistema de Operaciones y Stock para Cereal

<p align="center">
  <img src="docs/logo.svg" alt="silOS Logo" width="520">
</p>

Plataforma web profesional concebida para la gestión integral de **Operaciones y Stock de Silos en plantas logísticas de cereal**. **silOS** centraliza la recepción, control de capacidad, análisis de calidad (humedad y proteína), trazabilidad de transportistas y auditoría meteorológica de cada movimiento en tiempo real.

Proyecto de desarrollo para **2º DAW**.

## 💡 Origen y Motivación del Proyecto

Este proyecto nace directamente de mi experiencia personal trabajando como **operario de gestión de silos**. Durante mi desempeño diario en planta, identifiqué ineficiencias críticas que afectaban tanto a la rentabilidad del almacén como a la relación con los clientes:

* **Incoherencias por el uso de hojas de cálculo (Excel):** Múltiples ficheros desactualizados circulando entre turnos, alto riesgo de sobrescritura accidental y desajustes frecuentes en el stock real.
* **Duplicidad de tareas:** El almacén registraba los pesajes manualmente y las empresas clientes debían llevar su propio control independiente, generando discusiones constantes durante las liquidaciones de campaña.
* **Falta de transparencia en tiempo real:** Las empresas no disponían de un canal directo para consultar el nivel de sus silos o el histórico de sus descargas, obligándolas a realizar peticiones continuas por teléfono o correo electrónico.

**silOS** (*Sistema de Operaciones y Stock*) se diseñó para erradicar estos problemas actuando como una **Única Fuente de la Verdad** (*Single Source of Truth*), conectando en una sola plataforma las operaciones físicas de báscula con la consulta remota de los clientes.

## 📌 Descripción del Problema

En un almacén de grano conviven camiones de distintas empresas (cooperativas, agricultores) para cargar o descargar mercancía. **silOS** resuelve las siguientes necesidades operativas reales:

* **Identificación rápida:** Búsqueda por matrícula e ingreso "al vuelo" de transportistas.
* **Gestión de pesos y tara:** Registro manual de pesaje de entrada y salida con cálculo automático de cantidad neta.
* **Control flexible de capacidad y semáforos:** Alertas visuales al 80% (Naranja) y 95% (Rojo), permitiendo sobrepasar el 100% nominal por variaciones de densidad del cereal hasta el bloqueo manual por verificación física del operario.
* **Ajuste automático de mermas:** Cuadre automático de residuos (mermas positivas o negativas) mediante movimientos de sistema (Transportista ID 0) al liberar o cambiar de propietario un silo.
* **Auditoría e Inmutabilidad:** Trazabilidad de quién conducía el vehículo en la fecha exacta del pesaje, aunque el vehículo cambie de chófer en el futuro.
* **Condiciones climáticas:** Integración con API meteorológica externa para registrar precipitación y humedad ambiental en la fecha y hora exacta del movimiento.
* **Privacidad Multi-empresa:** Aislamiento de datos para que cada cliente consulte exclusivamente el histórico de sus silos.

## 🛠️ Tecnologías

| Capa | Tecnología |
| ----- | ----- |
| **Backend** | Java 17+ / **Spring Boot 3** (Spring Web, Spring Data JPA, Spring Security) |
| **Documentación API** | **OpenAPI / Swagger UI** (Contrato interactivo para el Front-end) |
| **Frontend (Fase 1)** | **Thymeleaf**, integrado en el backend para prototipado rápido |
| **Frontend (Objetivo)** | SPA independiente en **React** / JS consumiendo la API REST |
| **Base de datos** | **PostgreSQL** (Dockerizado) en `snake_case` nativo |
| **Datos meteorológicos** | API REST externa de histórico meteorológico (Open-Meteo / OpenWeather) |

## 🗂️ Modelo de Datos y Entidades

### Resumen de Entidades Principales

| Entidad | Descripción |
| ----- | ----- |
| `usuarios` | Cuentas de acceso con roles: Administrador (`AD`), Trabajador (`TR`) y Empresa (`EM`). |
| `empresas` | Clientes propietarios de la mercancía. Incluye código `color_hex` para la interfaz. |
| `silos` | Silos con `capacidad_max`, `cantidad_actual`, switch `habilitado` y `id_material` por defecto. |
| `materiales` | Catálogo de granos organizado por `tipo_general` y `tipo_especifico`. |
| `transportistas` | Registro de camiones con `matricula_camion` (Única e indexada) y datos del chófer habitual. |
| `movimientos` | Registro inmutable de pesajes con snapshots de auditoría (`conductor_nombre_snapshot`, `conductor_dni_snapshot`) y datos de calidad/clima. |

## 🔒 Restricciones de Integridad y Reglas de Negocio

1. **Gestión de Capacidad y Estado del Silo:**
   * Un silo deshabilitado (`habilitado = false`) **bloquea cualquier movimiento de Entrada ('E')**, pero permite movimientos de Salida ('S').
   * La inhabilitación es controlada manualmente por el operario tras inspección física.

2. **Cuadre de Mermas al Cambiar de Empresa:**
   * Al reasignar o vaciar la empresa de un silo, si `cantidad_actual != 0`, el backend ejecuta una transacción que inserta un movimiento de ajuste de merma asociándolo al transportista reservado (`id_transportista = 0`), dejando la `cantidad_actual` a 0.

3. **Transportistas "Al Vuelo":**
   * Búsqueda optimizada por `matricula_camion`. Si no existe, permite el alta instantánea. Si los datos del chófer han cambiado, actualiza la ficha actual y guarda una "foto fija" (snapshot) en la tabla `movimientos`.

4. **Coherencia Temporal:**
   * `fecha_hora_salida` debe ser strictly mayor o igual a `fecha_hora_entrada`.

5. **Aislamiento Multi-tenant:**
   * El usuario con rol `EM` solo puede consultar datos e históricos de silos asociados a su `id_empresa`.

## 📁 Estructura de Documentación ("Docs as Code")

```
.
├── README.md
├── docs/
│   ├── 01-requisitos-y-casos-de-uso.md  # Especificación completa de negocio y casos de uso
│   ├── 02-modelo-datos.md               # Documentación detallada de entidades y relaciones
│   ├── 03-script-postgresql.sql         # DDL de creación de tablas, índices e inserciones iniciales
│   └── modelo-er.png                    # Diagrama entidad-relación (Lucidchart)
└── backend/                             # Proyecto Spring Boot
```

## ⚙️ Configuración Inicial

### Base de Datos (Docker)

```bash
# Iniciar contenedor de PostgreSQL
docker run --name postgres-silos \
  -e POSTGRES_DB=silos_db \
  -e POSTGRES_USER=silos_user \
  -e POSTGRES_PASSWORD=silos_pass \
  -p 5432:5432 -d postgres:16
```

Ejecutar el script DDL ubicado en `docs/03-script-postgresql.sql` para crear la estructura de tablas, restricciones de integridad e índices estratégicos.

## 👤 Autor

**José Pedro González Hens**

Proyecto Final de Grado Superior — 2º DAW