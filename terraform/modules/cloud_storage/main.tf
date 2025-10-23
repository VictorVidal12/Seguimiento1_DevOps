variable "project_id" { type = string }
variable "region" { type = string }
variable "bucket_name" { type = string }

resource "google_storage_bucket" "logs" {
  name = var.bucket_name
  project = var.project_id
  location = var.region
  force_destroy = true
  uniform_bucket_level_access = true
}

output "logs_bucket_name" {
  value = google_storage_bucket.logs.name
}
