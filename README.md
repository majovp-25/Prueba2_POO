SISTEMA DE GESTION DE LICENCIAS DE CONDUCIR - ECUADOR
Este proyecto consiste en una aplicacion de escritorio desarrollada en Java (Swing) para la simulacion y gestion de los procesos de la Agencia Nacional de Transito (ANT). El sistema permite administrar usuarios, conductores, licencias y pruebas psicometricas de manera integral.

CARACTERISTICAS PRINCIPALES
SEGURIDAD Y CONTROL DE ACCESO

Autenticacion de usuarios contra base de datos PostgreSQL.

Politicas de contraseñas robustas: Validacion estricta que exige al menos una letra mayuscula, un numero y un caracter especial.

Protocolo de primer ingreso: Sistema de cambio obligatorio de contraseña si se detecta el uso de credenciales por defecto.

Roles definidos: Administrador (gestion total) y Analista (operativo).

AUDITORIA Y TRAZABILIDAD

Registro de autoria: El sistema almacena automaticamente la identidad del usuario responsable de la creacion de nuevos registros (columna "creado_por").

Control de sesiones activas en la interfaz principal.

REPORTES Y DOCUMENTACION

Generacion de reportes oficiales en formato PDF utilizando la libreria iText.

Inclusion dinamica de logotipos institucionales y marcas de tiempo.

Listados de usuarios con informacion de auditoria.

ARQUITECTURA Y TECNOLOGIAS
Lenguaje: Java (JDK 17 o superior).

Interfaz Grafica: Java Swing (Diseño responsivo y componentes personalizados).

Base de Datos: PostgreSQL (Despliegue en Railway).

Persistencia: JDBC.

NOTA: La capa de conexion a la base de datos y la configuracion del driver JDBC fueron implementadas y optimizadas personalmente por el autor para asegurar la integridad y estabilidad de las transacciones en la nube.

INSTRUCCIONES DE INSTALACION Y USO
CONFIGURACION DE BASE DE DATOS

Ejecutar el script SQL proporcionado (script.sql) en su gestor de base de datos.

Verificar que la tabla "usuarios" contenga la columna de auditoria "creado_por".

REQUISITOS DEL ENTORNO

Asegurese de tener el archivo de imagen (logo) en la ruta configurada para la generacion de reportes (Por defecto: C:\Users\User\Pictures\ANT\ImagenANT.jpg).

CREDENCIALES DE PRUEBA Para el primer acceso al sistema, utilice las siguientes credenciales por defecto. El sistema solicitara una actualizacion de seguridad inmediata.

Rol: Administrador Usuario: admin Contrasena: 1234

Rol: Analista Usuario: analista1 Contrasena: 1234

La nueva contraseña debe cumplir con los requisitos de complejidad (Mayuscula, Numero, Simbolo).

AUTOR
Proyecto desarrollado para la asignatura de Programacion Orientada a Objetos. Implementacion de logica, interfaz grafica y arquitectura de conexion a datos realizada por el/los autor/es.
