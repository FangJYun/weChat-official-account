#!/bin/bash
#使用root帐号:passwd root && sudo su root
cd /usr/bin
ln -sf python3 python
ssr_path="/home/ec2-user/ssr"
cd /home/ec2-user/
mkdir ssr
cd ${ssr_path}
wget -N –no-check-certificate https://raw.githubusercontent.com/ToyoDAdoubiBackup/doubi/master/ssr.sh && chmod +x ssr.sh
bash ssr.sh

#选择加密none，协议origin，混淆tls1.2_ticket
