variable "project_id" {
  description = "GCP project id"
  type        = string
}

variable "region" {
  description = "GCP region"
  type        = string
}

variable "allow_unauth" {
  description = "Unauth"
  type        = bool
}

variable "db_password" {
  description = "Db password for administrator"
  type        = string
}

variable "db_user" {
  description = "Db user for administrator"
  type        = string
}

variable "image" {
  description = "Path of image Artifact Registry"
  type        = string
}

variable "zone" {
  description = "GCP zone"
  type        = string
}

variable "sql_root_password" {
  description = "Root password for Cloud SQL."
  type        = string
  sensitive   = true
}

variable "service_name" {
  description = "Name for Cloud Run service"
  type        = string
}

variable "artifact_repo_location" {
  description = "Location for Artifact Registry"
  type        = string
}

variable "artifact_repo_name" {
  description = "Artifact Registry repo name"
  type        = string
}

variable "image_name" {
  description = "Container image path (eg: LOCATION-docker.pkg.dev/PROJECT/REPO/IMAGE:TAG)"
  type        = string
}

variable "app_user_password" {
  description = "Password for User Empennio"
  type        = string
}

variable "bucket_name" {
  description = "Name of Bucket in Cloud Bucket"
  type        = string
}