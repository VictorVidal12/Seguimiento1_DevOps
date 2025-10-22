variable "local_source_path" {
  type = string
  default = "../.."
}

resource "null_resource" "package_and_upload" {
  triggers = {
    source_path = var.local_source_path
    object_name = var.source_object
  }

  provisioner "local-exec" {
    interpreter = ["PowerShell", "-Command"]
    command = <<EOT
      $timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
      $zipPath = "$env:TEMP\\source-$timestamp.zip"

      Write-Host "Empaquetando código desde ../demo en $zipPath ..."
      Compress-Archive -Path ../demo/* -DestinationPath $zipPath -Force

      if (Test-Path $zipPath) {
        Write-Host "Archivo creado correctamente en $zipPath"
      } else {
        Write-Host "No se creó el archivo ZIP, deteniendo ejecución."
        exit 1
      }

      Write-Host "Subiendo $zipPath a gs://my-project-terraform-474601-logs-bucket/source.zip ..."
      gsutil cp $zipPath gs://my-project-terraform-474601-logs-bucket/source.zip
      EOT
  }

}
