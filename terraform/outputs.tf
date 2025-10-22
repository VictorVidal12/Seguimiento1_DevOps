output "artifact_repository" {
  value = module.artifact_repository.repo_name
}

output "cloud_run_url" {
  value = module.cloud_run.service_url
}

output "sql_instance_connection_name" {
  value = module.cloud_sql.instance_connection_name
}

output "logs_bucket" {
  value = module.cloud_storage.logs_bucket_name
}
