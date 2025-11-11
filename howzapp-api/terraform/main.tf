terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "6.38.0"
    }
  }
}

provider "google" {
  project = var.project
  region = var.region
  credentials = sensitive(file("credentials.json"))
}

variable project {
  type = string
}

variable region {
  type = string
}

variable zone {
  type = string
}

variable machine_type {
  type = string
  default = "e2-micro"
}

variable machine_image {
  type = string
  default = "projects/debian-cloud/global/images/debian-12-bookworm-arm64-v20251014"
}

variable "disk_type" {
  type = string
  default = "pd-standard"
}

variable "disk_size" {
  type = number
  default = 10
}

resource "google_compute_firewall" "allow_app_ports" {
  name    = "allow-app-ports"
  description = "Listening Ports for the application"

  network = "default"

  allow {
    protocol = "tcp"
    ports = ["80", "15672"]
  }
  source_ranges = ["0.0.0.0/0"]
  target_tags = ["app-ports"]
}

resource "google_compute_instance" "app_server" {
  name = "app-server"
  machine_type = var.machine_type

  zone = var.zone

  metadata = {
    # ssh-keys = sensitive("${var.ssh_user}:${file("~/.ssh/id_rsa.pub")}")

    startup-script = file("${path.root}/scripts/main.sh")
  }

  boot_disk {
    device_name = "app-server"

    initialize_params {
      image = var.machine_image
      size = var.disk_size
      type = var.disk_type
    }
  }

  network_interface {
    access_config {
      network_tier = "STANDARD"
    }
    queue_count = 0
    stack_type  = "IPV4_ONLY"
    subnetwork  = "projects/howzapp-techullurgy-full/regions/us-central1/subnetworks/default"
  }

  tags = ["app-ports"]

  scheduling {
    automatic_restart   = false
    on_host_maintenance = "TERMINATE"
    preemptible         = true
    provisioning_model  = "SPOT"
  }

  service_account {
    email  = "620351186909-compute@developer.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/devstorage.read_only", "https://www.googleapis.com/auth/logging.write", "https://www.googleapis.com/auth/monitoring.write", "https://www.googleapis.com/auth/service.management.readonly", "https://www.googleapis.com/auth/servicecontrol", "https://www.googleapis.com/auth/trace.append"]
  }
}

resource "google_compute_instance" "instance-20251111-004649" {
  boot_disk {
    auto_delete = true
    device_name = "instance-20251111-004649"

    initialize_params {
      image = "projects/debian-cloud/global/images/debian-12-bookworm-v20251014"
      size  = 10
      type  = "pd-standard"
    }

    mode = "READ_WRITE"
  }

  can_ip_forward      = false
  deletion_protection = false
  enable_display      = false

  labels = {
    goog-ec-src = "vm_add-tf"
  }

  machine_type = "e2-micro"

  metadata = {
    startup-script = "kajsdh\nakjsdhakjdh\naslkdjalksd\naskldjalksdhl"
  }

  name = "instance-20251111-004649"

  network_interface {
    access_config {
      network_tier = "STANDARD"
    }

    queue_count = 0
    stack_type  = "IPV4_ONLY"
    subnetwork  = "projects/howzapp-techullurgy-full/regions/us-central1/subnetworks/default"
  }

  scheduling {
    automatic_restart   = false
    on_host_maintenance = "TERMINATE"
    preemptible         = true
    provisioning_model  = "SPOT"
  }

  service_account {
    email  = "620351186909-compute@developer.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/devstorage.read_only", "https://www.googleapis.com/auth/logging.write", "https://www.googleapis.com/auth/monitoring.write", "https://www.googleapis.com/auth/service.management.readonly", "https://www.googleapis.com/auth/servicecontrol", "https://www.googleapis.com/auth/trace.append"]
  }

  shielded_instance_config {
    enable_integrity_monitoring = true
    enable_secure_boot          = false
    enable_vtpm                 = true
  }

  zone = "us-central1-f"
}