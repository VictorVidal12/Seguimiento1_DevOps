variable "project_id" { type = string }
variable "location" { type = string }
variable "repo_name" { type = string }

resource "google_artifact_registry_repository" "repo" {
  provider = google
  project = var.project_id
  location = var.location
  repository_id = var.repo_name
  description = "Artifact Registry for container images"
  format = "DOCKER"
}

output "repo_name" {
  value = google_artifact_registry_repository.repo.repository_id
}
