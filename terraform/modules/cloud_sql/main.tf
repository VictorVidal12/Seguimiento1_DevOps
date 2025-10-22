variable "project_id" { type = string }
variable "region" { type = string }
variable "root_password" { type = string }
variable "database_name" {
  type = string
  default = "Casa_empennio"
}
variable "app_user_password" {type = string }

resource "google_sql_database_instance" "default" {
  project = var.project_id
  name    = "${var.project_id}-sql"
  region  = var.region

  database_version = "POSTGRES_17"

  settings {
    tier = "db-perf-optimized-N-2"
    ip_configuration {
      ipv4_enabled = true
    }
    backup_configuration {
      enabled = true
    }
  }
  root_password = var.root_password
}

resource "google_sql_database" "app_db" {
  name     = var.database_name
  project  = var.project_id
  instance = google_sql_database_instance.default.name
}

resource "google_sql_user" "app_user" {
  name     = "empennio_user"
  instance = google_sql_database_instance.default.name
  project  = var.project_id
  password = var.app_user_password
}

output "instance_connection_name" {
  value = google_sql_database_instance.default.connection_name
}
