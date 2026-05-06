
```markdown
# Proyecto de Desarrollo - Grupo de Estudio



---

## 📌 1. Flujo de Trabajo (Git Flow Simplificado)

Nadie tiene permitido subir código directamente a la rama `main` (o `master`). Todo cambio debe seguir estrictamente este pipeline:

```
[Rama main] ──► Crear rama de tarea ──► Commits locales ──► Subir a GitHub ──► Abrir PR ──► Revisión y Aprobación ──► Merge a main
```

### Paso a paso:

1. **Sincroniza tu repositorio local:**
   Antes de empezar cualquier tarea, asegúrate de tener la versión más reciente del proyecto:
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Crea una rama para tu tarea:**
   Usa un nombre descriptivo para tu rama según el tipo de tarea:
   * Para nuevas funcionalidades: `feature/nombre-de-la-tarea`
   * Para corrección de errores: `bugfix/nombre-del-error`
   * Para documentación: `docs/nombre-de-la-guia`
   
   *Ejemplo:*
   ```bash
   git checkout -b feature/pantalla-login
   ```

3. **Trabaja en tu código y haz commits:**
   Realiza commits limpios y con mensajes descriptivos.
   ```bash
   git add .
   git commit -m "feat: agrega interfaz del formulario de login y validaciones"
   ```

4. **Sube tu rama a GitHub:**
   ```bash
   git push origin feature/pantalla-login
   ```

5. **Abre un Pull Request (PR):**
   * Ve a la pestaña **Pull Requests** en GitHub y haz clic en **New pull request**.
   * Selecciona tu rama para fusionarla hacia `main`.
   * Completa la plantilla de descripción del PR explicándole al equipo qué cambios realizaste y cómo probarlos.
   * Asigna al menos a **2 compañeros** como revisores (*Reviewers*).

6. **Revisión y Fusión (Merge):**
   * Tus compañeros revisarán tu código, dejarán comentarios o sugerencias si es necesario y finalmente marcarán tu PR como **Approved** (Aprobado).
   * Una vez aprobado, se realizará el *Merge* para integrar tus cambios a la rama principal. ¡Felicidades! 🎉

---

## 🤝 2. Reglas de Oro de Convivencia en Git

* 🚫 **PROHIBIDO hacer `git push` directo a `main`:** Todo cambio, por pequeño que sea (incluso una línea de texto o un comentario), debe pasar obligatoriamente por un Pull Request.
* 🔄 **Mantente actualizado:** Antes de abrir un PR o si tu tarea toma varios días, integra los cambios recientes de `main` en tu rama para resolver conflictos localmente:
  ```bash
  git checkout feature/tu-rama
  git fetch origin
  git merge origin/main
  # Resuelve los conflictos en tu editor si los hay, haz commit y sube los cambios.
  ```
* 💬 **Revisiones constructivas:** Al revisar el código de tus compañeros, sé respetuoso y claro en tus comentarios. El objetivo del *Code Review* es aprender en grupo y asegurar que el código funcione correctamente.
* 🧪 **Prueba antes de aprobar:** No apruebes un Pull Request "a ciegas". Descarga la rama de tu compañero de forma local si es necesario, pruébala en tu máquina y verifica que compile/corra sin errores.

---

## 🛠️ 3. Estándar de Mensajes de Commit (Conventional Commits)

Para mantener el historial del proyecto legible y profesional, utilizaremos el estándar de *Conventional Commits*. Tus mensajes deben tener la siguiente estructura:

`tipo: descripción breve en minúsculas`

### Tipos permitidos:
* **`feat:`** Una nueva funcionalidad para el usuario (ej: `feat: agrega botón de recuperar contraseña`).
* **`fix:`** Corrección de un error o bug (ej: `fix: corrige desbordamiento en el contenedor de perfil`).
* **`docs:`** Cambios únicamente en la documentación (ej: `docs: actualiza instrucciones de instalación`).
* **`style:`** Cambios que no afectan el comportamiento del código (espacios en blanco, formateo, punto y coma faltantes, etc.).
* **`refactor:`** Un cambio en el código que no corrige un bug ni añade una función, sino que mejora su estructura o rendimiento.

---

## 👥 4. Organización del Equipo

Dado que somos un equipo grande de 30 personas, nos organizamos de la siguiente manera para agilizar el desarrollo:

* **Coordinadores/Líderes de Proyecto:** Encargados de la arquitectura general, resolución de conflictos complejos y aprobación final de entregables clave.
* **Células de Trabajo:** Nos dividiremos en subgrupos para atacar distintos módulos del proyecto de manera paralela. ¡Comunícate constantemente con tu célula para no duplicar esfuerzos!

---

## 💻 5. Requisitos e Instalación Local

*(Completa este espacio con las instrucciones específicas para correr tu proyecto localmente)*

1. Clonar el proyecto:
   ```bash
   git clone <URL_DE_TU_REPOSITORIO>
   ```
2. Instalar dependencias:
   ```bash
   # Ejemplo: npm install, flutter pub get, composer install, etc.
   ```
3. Configurar variables de entorno (crear archivo `.env` si aplica).
4. Ejecutar el proyecto en modo desarrollo:
   ```bash
   # Ejemplo: npm run dev, flutter run, etc.
   ```

---

*¡Hagamos de este un gran proyecto! Si tienes dudas con el flujo de Git, no dudes en preguntar en el canal de comunicación del grupo.*
```
