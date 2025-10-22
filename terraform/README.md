# Devops entrega 4  (con GCP)

La carpeta `terraform/` crea los siguientes servicios (de forma modular):
- Artifact Registry (un contenedor)
- Cloud Run (para desplegar las API's)
- Cloud SQL (para la instancia de PostgreSQL 17 y la database `Casa_empennio`)
- Cloud Storage (un bucket para los logs)
- Cloud Logging (un servicio para generar loggins y posteriormente exportarlos al bucket)
- Cloud Build (para generar la imagen docker del proyecto y poder utilizarla en Artifact Registry)


## Paso a paso para el despliegue
1. Instalar y configurar el CLI de GCP:
   ```bash
   gcloud auth login
   gcloud config set project YOUR_PROJECT_ID
   gcloud auth application-default login
   ```
2. Una vez configurado GCP y habiendo instalado previamente terraform, realizamos lo siguiente:
   ```bash
   cd terraform
   terraform init
   terraform apply
   ```
3. Si queremos borrar completamente la infraestructura creada por terraform, podemos realizar el siguiente comando.
   ```bash
   terraform destroy
   ```

## Dockerfile
Tener en cuenta que para la correcta implementación del proyecto en terraform, se debe tener un Dockerfile (para subirlo a Artifact Registry)


**Notas**
- Como tal, no me dejó eliminar completamente la base de datos debido a que siempre había alguien "Conectado". No supe solucionar éste problema.
- El servicio extra llamado "Cloud Build" fue necesario para poder hacer que todo funcionara automáticamente por Terraform, sin éste, me hubiese tocado crear la imagen y montarla manualmente.

