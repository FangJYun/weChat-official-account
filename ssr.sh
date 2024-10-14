#!/bin/bash
#使用root帐号
cd /home/ec2-user/
mkdir ssr
sudo yum groupinstall -y "Development Tools"
sudo yum install -y zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gdbm-devel libpcap-devel xz-devel libffi-devel
wget https://www.python.org/ftp/python/3.5.2/Python-3.5.2.tgz
tar xvf Python-3.5.2.tgz
cd Python-3.5.2
./configure --enable-optimizations
make altinstall
python3.5 --version
whereis python3.5
ln -sf /usr/local/bin/python3.5 /usr/bin/python3
cd /usr/local/bin
ln -sf ./python3.5 python
#ssr脚本的存放文件夹，自己定义
cd /home/ec2-user/ssr
wget -N –no-check-certificate https://raw.githubusercontent.com/ToyoDAdoubiBackup/doubi/master/ssr.sh && chmod +x ssr.sh
bash ssr.sh
