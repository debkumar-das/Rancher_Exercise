provider "aws" {
  region = "us-east-1"  # Updated to a region that matches your availability zone
}

resource "aws_vpc" "deb_vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "DebVPC"
  }
}

resource "aws_subnet" "deb_subnet" {
  vpc_id            = aws_vpc.deb_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"  # Use a valid availability zone for your region
}

resource "aws_security_group" "debec2_sg" {
  vpc_id      = aws_vpc.deb_vpc.id
  name        = "debec2-sg"
  description = "Allow SSH and HTTP"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allow SSH from anywhere (update with your IP for better security)
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allow HTTP from anywhere
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "myec2" {
  ami           = "ami-00c39f71452c08778"
  instance_type = "t2.micro"
  key_name      = "deb-key-pair"  # Replace with your key pair name

  vpc_security_group_ids = [aws_security_group.debec2_sg.id]
  subnet_id              = aws_subnet.deb_subnet.id

  tags = {
    Name = "MyEC2Instance"
  }
}


