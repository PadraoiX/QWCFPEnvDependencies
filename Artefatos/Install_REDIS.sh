adduser redis
yum groupinstall "Development Tools"
yum install clang
yum install tcl
wget -c http://download.redis.io/redis-stable.tar.gz
tar -xvzf redis-stable.tar.gz
cd redis-stable
export CC=clang
make clean
make
make test
make install
mkdir -p /etc/redis
mkdir -p /var/lib/redis
mkdir -p /dados/redis/
mkdir -p /dados/log/redis/
mkdir -p /var/log/redis

cat <<EOF
Atencao para estes parametros alterados no /etc/redis/redis.conf
=================================================================
port           6379                        #default port is already 6379.
daemonize      yes                         #run as a daemon
supervised     systemd                     #signal systemd
pidfile        /var/run/redis.pid          #specify pid file
loglevel       notice                      #server verbosity level
logfile        /dados/log/redis/redis.log  #log file name
dir            /dados/redis                #redis directory
bind           192.168.0.0                 #bind for subnetwork da PIX
protected-mode no                          #Esta opcao deve NO se difere de 127.0.0.1
EOF
	
read -p "Pressione [ENTER] para continuar" X

cp redis.conf /etc/redis/
cp redis.service /etc/systemd/system/redis.service

chown -R redis:redis /etc/redis /var/lib/redis /var/log/redis /dados/redis /dados/log/redis/
chmod -R 770 /var/lib/redis /var/lib/redis /dados/redis /dados/log/redis/ /var/log/redis

# WARNING: The TCP backlog setting of 511 cannot be enforced 
#          because /proc/sys/net/core/somaxconn is set to the 
#          lower value of 128.

# WARNING overcommit_memory is set to 0! Background save may 
#         fail under low memory condition. To fix this issue 
#         add 'vm.overcommit_memory = 1' to /etc/sysctl.conf 
#         and then reboot or run the command 
#         'sysctl vm.overcommit_memory=1' for this to take effect.

# WARNING you have Transparent Huge Pages (THP) support enabled 
#         in your kernel. This will create latency and memory 
#         usage issues with Redis. To fix this issue the command  
#         echo never > /sys/kernel/mm/transparent_hugepage/enabled
#         as root, and add it to your /etc/rc.local in order to 
#         retain the setting after a reboot. Redis must be 
#         restarted after THP is disabled.

cat <<EOF
Incluir estas linhas no /etc/rc.local para os próximos Boots
=============================================================
sysctl fs.file-max=100000
sysctl net.core.somaxconn=65535
sysctl vm.overcommit_memory=1
echo never > /sys/kernel/mm/transparent_hugepage/enabled
EOF
	
read -p "Pressione [ENTER] para continuar" X

sysctl fs.file-max=100000
sysctl net.core.somaxconn=65535
sysctl vm.overcommit_memory=1
echo never > /sys/kernel/mm/transparent_hugepage/enabled

systemctl enable redis
systemctl start redis
systemctl status redis 
