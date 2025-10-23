variable "project_id" {
  type = string
}

variable "region" {
  type = string
}

variable "artifact_repo" {
  type = string
}

variable "image_name" {
  type = string
}

variable "source_bucket" {
  type = string
}

variable "source_object" {
  type = string
  default = "source.tar.gz"
}

resource "null_resource" "build_image" {
  provisioner "local-exec" {
    interpreter = ["PowerShell", "-Command"]
    command = "gcloud builds submit ../demo --project ${var.project_id} --tag ${var.image_name}"
  }

  triggers = {
    project_id = var.project_id
    image_name = var.image_name
    source_bucket = var.source_bucket
    source_object = var.source_object
  }
}

output "build_id" {
  value = "build-triggered-${var.image_name}"
}
