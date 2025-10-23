variable "project_id" { type = string }
variable "bucket_name" { type = string }

data "google_storage_bucket" "logs_bucket" {
  name = var.bucket_name
  project = var.project_id
}

resource "google_logging_project_sink" "to_bucket" {
  name = "export-logs-to-bucket"
  project  = var.project_id
  destination = "storage.googleapis.com/${data.google_storage_bucket.logs_bucket.name}"
  filter = ""
  unique_writer_identity = true
}

resource "google_storage_bucket_iam_member" "sink_writer" {
  depends_on = [google_logging_project_sink.to_bucket]
  bucket = data.google_storage_bucket.logs_bucket.name
  role = "roles/storage.objectCreator"
  member = google_logging_project_sink.to_bucket.writer_identity
}
