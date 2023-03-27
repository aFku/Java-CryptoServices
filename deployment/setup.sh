#!/bin/bash

# Install python 3
yum install gcc openssl-devel bzip2-devel libffi-devel -y
curl https://www.python.org/ftp/python/3.8.1/Python-3.8.1.tgz -o /tmp/python381.tgz
cd /tmp
tar -xzf python381.tgz
cd Python-3.8.1/
./configure --enable-optimizations
make altinstall

# Install Rust
#curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs -o /tmp/sh.rustup.rs
#sh ./sh.rustup.rs
#rustup update
#su - user -c "cd ~"

# Install ansible
su - user -c "python3.8 -m pip install --user --upgrade pip"
su - user -c "python3.8 -m pip install --user ansible"

# Install ansible module
su - user -c "ansible-galaxy collection install kubernetes.core"

