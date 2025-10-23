module "project_services" {
  source = "./modules/project_services"
  project_id = var.project_id
  region = var.region
}

module "artifact_repository" {
  source = "./modules/artifact_registry"
  project_id = var.project_id
  location = var.artifact_repo_location
  repo_name = var.artifact_repo_name
}

module "cloud_storage" {
  source = "./modules/cloud_storage"
  project_id = var.project_id
  region = var.region
  bucket_name = "${var.project_id}-logs-bucket"
}

module "cloud_logging" {
  source = "./modules/cloud_logging"
  project_id = var.project_id
  bucket_name = module.cloud_storage.logs_bucket_name
}

module "cloud_sql" {
  source = "./modules/cloud_sql"
  project_id = var.project_id
  region = var.region
  root_password = var.sql_root_password
  database_name = var.db_name
  app_user_password = var.app_user_password
}

module "cloud_run" {
  source = "./modules/cloud_run"
  project_id = var.project_id
  region = var.region
  service_name = var.service_name
  image = module.cloud_build.image_name
  allow_unauth = true
  db_user = var.db_user
  db_password  = var.db_password
  depends_on = [
    module.artifact_repository,
    module.cloud_storage
  ]
  db_name = var.db_name
  instance_cloud_sql = var.instance_cloud_sql
  spring_jpa_hibernate = var.spring_jpa_hibernate
  spring_url_socket = var.spring_url_socket
  spring_user_name = var.spring_user_name
}


module "cloud_build" {
  source = "./modules/cloud_build"
  project_id = var.project_id
  region = var.region
  artifact_repo = var.artifact_repo_name
  image_name = var.image
  source_bucket = var.bucket_name
  source_object = "source.tar.gz"
}

resource "null_resource" "create_and_upload_source" {
  triggers = {
    project_id = var.project_id
    bucket = module.cloud_storage.logs_bucket_name
    object = "source.tar.gz"
  }

  provisioner "local-exec" {
    command = <<EOT
      Write-Host "Packing source directory into $env:TEMP\source.zip..."
      Compress-Archive -Path ../* -DestinationPath "$env:TEMP\source.zip" -Force

      Write-Host "Uploading $env:TEMP\source.zip to gs://${module.cloud_storage.logs_bucket_name}/source.zip..."
      gsutil cp "$env:TEMP\source.zip" "gs://${module.cloud_storage.logs_bucket_name}/source.zip"

      if ($LASTEXITCODE -ne 0) {
        Write-Host "Error uploading source.zip to Cloud Storage" -ForegroundColor Red
        exit 1
      }
      EOT

    interpreter = ["PowerShell", "-Command"]
  }

  depends_on = [module.cloud_storage, module.artifact_repository]

}

resource "null_resource" "trigger_cloud_build_after_upload" {
  triggers = {
    upload_ts = timestamp()
  }

  depends_on = [
    null_resource.create_and_upload_source,
    module.cloud_build
  ]
}

resource "google_project_service" "run" {
  project = var.project_id
  service = "run.googleapis.com"
}