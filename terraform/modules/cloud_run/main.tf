variable "project_id" { type = string }
variable "region"     { type = string }
variable "service_name" { type = string }
variable "image" { type = string }
variable "db_user" { type = string }
variable "db_password" { type = string }
variable "allow_unauth" {
  type = bool
  default = false
}


resource "google_service_account" "run_sa" {
  account_id   = "${var.service_name}-sa"
  project      = var.project_id
  display_name = "Service account for Cloud Run ${var.service_name}"
}

resource "google_project_iam_member" "run_sa_cloudsql" {
  project = var.project_id
  role    = "roles/cloudsql.client"
  member  = "serviceAccount:${google_service_account.run_sa.email}"
}


resource "google_cloud_run_service" "default" {
  name     = var.service_name
  project  = var.project_id
  location = var.region

  template {
    metadata {
      annotations = {
        "run.googleapis.com/cloudsql-instances" = "my-project-terraform-474601:us-central1:my-project-terraform-474601-sql"
      }
    }

    spec {
      service_account_name = google_service_account.run_sa.email

      containers {
        image = var.image

        env {
          name  = "DB_NAME"
          value = "Casa_empennio"
        }
        env {
          name  = "DB_USER"
          value = var.db_user
        }
        env {
          name  = "DB_PASSWORD"
          value = var.db_password
        }
        env {
          name  = "SPRING_DATASOURCE_URL"
          value = "jdbc:postgresql:///Casa_empennio?cloudSqlInstance=my-project-terraform-474601:us-central1:my-project-terraform-474601-sql&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
        }
        env {
          name  = "SPRING_DATASOURCE_USERNAME"
          value = "postgres"
        }
        env {
          name  = "SPRING_DATASOURCE_PASSWORD"
          value = var.db_password
        }

        env {
          name  = "SPRING_JPA_HIBERNATE_DDL_AUTO"
          value = "update"
        }

      }
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
}

resource "google_cloud_run_service_iam_member" "invoker" {
  count    = var.allow_unauth ? 1 : 0
  project  = var.project_id
  location = var.region
  service  = google_cloud_run_service.default.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}

output "service_url" {
  value       = try(google_cloud_run_service.default.status[0].url, null)
  description = "URL del servicio Cloud Run"
}
