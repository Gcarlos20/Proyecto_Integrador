# Sistema de Inventario PI_Inventario

## Descripción

Este proyecto es un sistema de gestión de inventario desarrollado en Java utilizando el patrón de diseño MVC (Modelo-Vista-Controlador). Permite administrar productos, proveedores, usuarios, compras, ventas y movimientos de inventario de manera eficiente.

## Características

- **Gestión de Productos**: Crear, leer, actualizar y eliminar productos con información detallada (código, nombre, categoría, descripción, precio, stock, etc.).
- **Gestión de Proveedores**: Administrar proveedores con datos de contacto.
- **Gestión de Usuarios**: Sistema de autenticación con roles (admin, usuario, consultor).
- **Compras y Ventas**: Registro de transacciones de compra y venta con detalles.
- **Movimientos de Inventario**: Historial completo de entradas, salidas y ajustes de stock.
- **Reportes**: Generación de reportes sobre inventario, ventas y movimientos.
- **Interfaz Gráfica**: Interfaz de usuario intuitiva para todas las operaciones.

## Requisitos del Sistema

- **Java**: JDK 11 o superior
- **Base de Datos**: MySQL 5.7 o superior
- **Dependencias**: JDBC para conexión a MySQL

## Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu-usuario/PI_Inventario.git
   cd PI_Inventario
   ```

2. **Configurar la base de datos**:
   - Instalar MySQL
   - Crear la base de datos ejecutando los scripts en `database/`:
     ```bash
     mysql -u root -p < database/schema.sql
     mysql -u root -p < database/migracion_compras_ventas.sql
     mysql -u root -p < database/migracion_productos_campos_extra.sql
     mysql -u root -p < database/migracion_productos_codigo.sql
     ```

3. **Configurar la conexión a la base de datos**:
   - Editar `src/conexion/conexionBD.java` con tus credenciales de MySQL.

4. **Compilar el proyecto**:
   ```bash
   javac -cp ".:lib/*" src/**/*.java
   ```

5. **Ejecutar la aplicación**:
   ```bash
   java -cp ".:lib/*" main.main
   ```

## Configuración

### Base de Datos

Los archivos de configuración de la base de datos están en la carpeta `database/`:

- `schema.sql`: Esquema inicial de la base de datos
- `migracion_compras_ventas.sql`: Migración para tablas de compras y ventas
- `migracion_productos_campos_extra.sql`: Campos adicionales para productos
- `migracion_productos_codigo.sql`: Campo de código para productos

### Conexión a BD

Editar `src/conexion/conexionBD.java` para configurar:
- URL de la base de datos
- Usuario y contraseña
- Nombre de la base de datos

## Uso

### Inicio de Sesión

Al ejecutar la aplicación, aparecerá la pantalla de login. Usar credenciales válidas según los roles definidos.

### Roles de Usuario

- **Admin**: Acceso completo a todas las funciones
- **Usuario**: Acceso limitado a operaciones básicas
- **Consultor**: Solo lectura de información

### Operaciones Principales

1. **Productos**: Gestionar catálogo de productos
2. **Proveedores**: Administrar proveedores
3. **Compras**: Registrar compras y actualizar stock
4. **Ventas**: Registrar ventas y reducir stock
5. **Movimientos**: Ver historial de movimientos
6. **Reportes**: Generar reportes del sistema

## Estructura del Proyecto

```
PI_Inventario/
├── database/                    # Scripts SQL para base de datos
│   ├── schema.sql
│   ├── migracion_compras_ventas.sql
│   ├── migracion_productos_campos_extra.sql
│   └── migracion_productos_codigo.sql
├── lib/                         # Librerías externas
├── src/                         # Código fuente
│   ├── controller/              # Controladores (lógica de negocio)
│   │   ├── CompraController.java
│   │   ├── InventarioController.java
│   │   ├── LoginController.java
│   │   ├── MovimientoController.java
│   │   ├── productoController.java
│   │   ├── ProveedorController.java
│   │   └── UsuarioController.java (si existe)
│   ├── Dao/                     # Data Access Objects (operaciones CRUD)
│   │   ├── CompraDao.java       # CRUD para compras
│   │   ├── MovimientoDao.java   # CRUD para movimientos
│   │   ├── ProductoDao.java     # CRUD para productos
│   │   ├── ProveedorDao.java    # CRUD para proveedores
│   │   └── UsuarioDao.java      # CRUD para usuarios
│   ├── main/                    # Punto de entrada
│   │   └── main.java
│   ├── model/                   # Modelos de datos
│   │   ├── movimiento.java
│   │   ├── producto.java
│   │   ├── proveedor.java
│   │   └── Usuario.java
│   ├── util/                    # Utilidades
│   │   ├── Permisos.java
│   │   └── session.java
│   ├── vista/                   # Vistas (interfaz gráfica)
│   │   ├── AyudaVista.java
│   │   ├── CompraVista.java
│   │   ├── ConfiguracionVista.java
│   │   ├── DeshboardVista.java
│   │   ├── InventarioVista.java
│   │   ├── LoginVista.java
│   │   ├── productoVista.java
│   │   ├── ProveedorVista.java
│   │   ├── ReporteVista.java
│   │   └── UltimosMovimientosVista.java
│   └── conexion/                # Conexión a base de datos
│       └── conexionBD.java
└── README.md                    # Este archivo
```

## Operaciones CRUD

Las operaciones CRUD (Create, Read, Update, Delete) están implementadas en las clases DAO:

### ProductoDao
- **Create**: `agregar()` - Insertar nuevo producto
- **Read**: `listarTodos()`, `buscarPorId()`, `buscarPorCodigo()` - Consultar productos
- **Update**: `actualizar()` - Modificar producto existente
- **Delete**: `eliminar()` - Eliminar producto

### ProveedorDao
- **Create**: `agregar()` - Insertar nuevo proveedor
- **Read**: `listarTodos()`, `buscarPorId()` - Consultar proveedores
- **Update**: `actualizar()` - Modificar proveedor
- **Delete**: `eliminar()` - Eliminar proveedor

### UsuarioDao
- **Create**: `agregar()` - Insertar nuevo usuario
- **Read**: `login()` - Validar credenciales
- **Update**: `actualizar()` - Modificar usuario
- **Delete**: `eliminar()` - Eliminar usuario

### MovimientoDao
- **Create**: `registrar()` - Insertar nuevo movimiento
- **Read**: `obtenerTodos()`, `obtenerRecientes()`, `obtenerPorProducto()`, `obtenerPorTipo()`, `calcularSaldo()` - Consultar movimientos

### CompraDao
- **Create**: `registrarCompra()` - Insertar nueva compra
- **Read**: `listarDetalleCompra()`, `listarVentas()`, `listarDetalleVenta()` - Consultar compras y ventas

## Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Soporte

Para soporte técnico o preguntas, por favor contactar al equipo de desarrollo.

## Versiones

- **v1.0.0**: Versión inicial con funcionalidades básicas de inventario