## Instrucciones para instalar y ejecutar el proyecto desde GitHub

### Requisitos previos
Asegúrate de tener instalados los siguientes requisitos previos:

- **Java**: Debes tener Java 8 o una versión superior instalada en tu sistema.

- **Git**: Debes tener Git instalado en tu sistema.

### Pasos para instalar y ejecutar el proyecto

1. **Clonar el repositorio**:
   - Clona el repositorio desde GitHub con el siguiente comando:

     ```bash
     git clone https://github.com/DavidVal6/LAB-04-AREP.git
     ```

2. **Navegar al directorio del proyecto**:
   - Ingresa al directorio del proyecto:

     ```bash
     cd LAB-04-AREP
     ```

3. **Compilar el proyecto**:
   - Utiliza Maven para compilar el proyecto:

     ```bash
     mvn clean install
     ```

4. **Ejecutar la aplicación**:
   - Inicia la aplicación con el siguiente comando:

     ```bash
     java -cp target/classes edu.eci.arep.ServerExecuter
     ```

5. **Realizar pruebas**:
   - Ejecuta las pruebas unitarias en el paquete `edu.eci.arep`:

     ```bash
     mvn test
     ```

6. **Acceder a la aplicación**:
   - Accede a la aplicación a través de tu navegador web o una herramienta de cliente HTTP, como [cURL](https://curl.se/). La aplicación se ejecuta en `http://localhost:35000`.

     - Para obtener un saludo, visita `http://localhost:35000/hello`.
     - Para ver una imagen codificada en base64, visita `http://localhost:35000/image`.
     - Para cargar una página HTML, visita `http://localhost:35000/host`

¡Listo! Ahora deberías poder instalar, compilar y ejecutar el proyecto desde GitHub, así como realizar pruebas en él.

### Pruebas de ejecucion:
