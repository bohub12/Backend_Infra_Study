## Haproxy + Keepalived [VRRP]

1. vm 설치

   ```bash
   $ sudo -i
   >> enter your user password :
   $ passwd <user name>
   >> enter <user name> password you want to change
   $ passwd root
   >> enter root password you want to change
   ```

2. openssh-server로 내 터미널에서 접속 가능하게 변경(안해도 상관 x)

   ```bash
   $ su
   >> root 패스워드 입력
   # root로 접속
   $ apt-get update
   $ apt-get upgrade
   $ apt-get install openssh-server
   $ vim /etc/ssh/sshd_config
   $ # PermitRootLogin 항목 yes로 수정
   $ ufw allow 22
   $ ufw reload
   ```

3. 내 터미널에서 ssh로 vm 접속(안해도 상관 x)

   ```bash
   [ubuntu]$ ip -bri a
   # vm 인터페이스 ip 확인
   [terminal]$ vim /etc/hosts
   # hosts에 vm ip 추가 
   # ex) 127.0.0.1 myVM1
   [terminal]$ ssh <vm username>@myVM1 -p 22
   # ubuntu 접속 완료
   [vm1]$ sudo vim /etc/hosts
   >> hosts에 자신의 ip 등록
   # ex) 192.168.10.5 vm1
   ```

4. nginx 설치

   ```bash
   $ apt-get install nginx -y
   ```

5. nginx configuration 파일 설정

   ```bash
   $ nginx -V
   # arguments 들중 --conf-path 항목이 있는데 이 디렉토리에 nginx.conf에 들어간다
   $ vim /etc/nginx/nginx.conf
   
   ############################
   # nginx.conf
   user nginx nginx # 워크프로세스를 실행할 유저 설정
   worker_processes  1; # 생성할 워크프로세스 수
   
   pid  logs/nginx.pid; # nginx pid를 저장할 디렉토리
   
   events {
       worker_connections  1024; # 한 워크프로세스에 연결 가능한 커넥션(클라이언트)수
   }
   
   
   http {
       include       mime.types; # 다른 설정파일 불러오기
       default_type  application/octet-stream; # defualt response type
       
       server { # server 생성
   				listen    8081; # 포트 설정
   				server_name    was2; # server naming
   
   				location / {
   		    root html; # root directory 설정
   	    	index was2.html; # html 파일 설정
   				}
       }
   
       server {
           listen       8080;
           server_name  was1;
   
           location / {
               root   html;
               index  was1.html;
           }
       }
   }
   
   # nginx root direcoty 확인방법
   $ nginx -V
   # --prefix에 해당하는 경로가 root directory
   
   ```

5. nginx 실행 테스트

   ```bash
   $ service nginx start
   $ service nginx status
   $ netstat -anv | grep LISTEN
   # :8080, :8081 포트가 열려있는지 확인
   $ curl localhost:<your server port>
   # html 파일이 제대로 반환되는지 확인
   ```

6. Haproxy 설치

   ```bash
   $ apt-get install haproxy
   $ systemctl status haproxy #haproxy 설치 확인
   ```

7. haproxy.cfg

   ```bash
   $ vim /etc/haproxy/haproxy.cfg
   
   ##############################
   # haproxy.cfg
   global
       log 127.0.0.1   local0
       log 127.0.0.1   local1 debug
       maxconn 4096
   
     defaults
       log     global
       mode    http
       option  httplog
       option  dontlognull
       retries 3
       option redispatch
       maxconn 2000
       timeout connect      5000
       timeout client      50000
       timeout server      50000
   
     frontend localnodes
       bind *:8000 # 8000번 포트로 접속하면 haproxy가 server들로 lb
       mode http
       default_backend nodes
   
     backend nodes
       mode http
       balance roundrobin # lb 방식
       server server1 vm1:8080 check # 서버 세팅
       server server2 vm1:8081 check
       
   ```

8. haproxy 실행 확인

   ```bash
   $ systemctl stop haproxy
   $ systemctl start haproxy
   $ systemctl status haproxy # haproxy status 확인
   $ netstat -anv | grep LISTEN # 8000 포트 확인
   $ sudo ufw allow 8000 # 8000 포트 방화벽 허용
   $ sudo ufw reload # 방화벽 리로드
   $ curl myVM1:8000
   # res : was 1.html 
   $ curl myVM1:8000
   # res : was 2.html 
   $ curl myVM1:8000
   # res : was 3.html 
   $ curl myVM1:8000
   # res : was 4.html
   
   # 포트를 허용하여서 내 터미널에서도 접속이 가능함
   [terminal]$ curl myVM1:8000
   
   # 403 forbiden : 루트 경로에 html파일이 존재하는지 확인
   ```

9. vm2 생성 및 세팅

   ```bash
   # 1~8번 동일하게 수행
   ##################
   # vm1
   # 8000번 포트는 위에서 이미 허용
   [vm1]$ ufw allow 8080
   [vm1]$ ufw allow 8081
   [vm1]$ ufw reload
   [vm1]$ sudo vim /etc/hosts
   >> vm2 ip hosts에 등록
   # ex) 192.168.10.5 vm2
   ##################
   # vm2
   [vm2]$ ufw allow 8080
   [vm2]$ ufw allow 8081
   [vm2]$ ufw reload
   [vm2]$ sudo vim /etc/hosts
   >> vm1 ip hosts에 등록
   ##################
   # vm1 -> vm2 , vm2 -> vm1 해당 포트로 잘 들어가지는지 확인
   [vm1]$ curl vm2@vm2 -p 8080
   [vm1]$ curl vm2@vm2 -p 8081
   [vm2]$ curl vm1@vm1 -p 8080
   [vm2]$ curl vm1@vm1 -p 8081
   
   ```

10. vm1,vm2 keepalived 설치 및 세팅

    ```bash
    $ su 
    $ apt-get install keepalived
    $ vim /etc/keepalived/keepalived.conf
    #####################
    # keepalived.conf
    vrrp_instance VI_1 { # instance 이름은 vm1,vm2 
    	state MASTER # master lb : vm1 
    # state BACKUP # backup lb : vm2
    	interface enp0s8 # vm의 인터페이스 이름 등록 -> 이 인터페이스에 vip 부여됨
    	virtual_router_id 51 # vm1,vm2 동일
    	priority 200 # master lb가 더 높은 값을 갖도록 설정 ex) vm2 : priority 100
    	advert_int 1 # vm1, vm2 동일
    
    	vrrp_unicast_vind <vm1 ip>
    	vrrp_unicast_peer <vm2 ip>
    	unicast_src_ip <vm1 ip>
    	unicast_peer {
    		<vm2 ip>
    	}
    
    	authentication { # 인증부분도 vm1,vm2 동일
    		auth_type PASS 
    		auth_pass 12345
    	}
    
    	virtual_ipaddress { # vip 설정 vm1,vm2 동일
    		192.168.64.200/24
    	}
    }
    ```

11. keepalived 동작 확인

    ```bash
    ##################################
    # vm1,vm2에서 동시 수행
    $ su
    $ service nginx stop
    $ systemctl stop haproxy
    $ systemctl stop keepalived
    $ netstat -anv | grep LISTEN
    >> 포트들 모두 닫혀있는지 확인
    
    $ ip -bri a 
    >> vm 인터페이스에 달린 ip 확인 (vip 없어야 정상)
    
    $ service nginx start
    $ systemctl start haproxy
    $ systemctl start keepalived
    $ netstat -anv | grep LISTEN
    >> 8000, 8080, 8081 포트 열려있는지 확인
    
    $ ip -bri a
    >> vm1 인터페이스에 vip가 붙어있는지 확인
    
    ##################################
    # vm1, vm2 구분
    [vm1]$ systemctl stop haproxy
    [vm1]$ ip -bri a # vm1 vip 빠진거 확인
    [vm2]$ ip -bri a # vm2 vip 할당됬는지 확인
    
    [vm1]$ systemctl start haproxy
    [vm1]$ ip -bri a # vm1 vip 할당됬는지 확인
    [vm2]$ ip -bri a # vm2 vip 빠진거 확인
    
    ##################################
    # [vm1],[vm2]가 안붙어있다면
    # terminal, vm1, vm2 아무데서나 상관x
    $ curl vip -p 8000 # LB 잘 동작하는지 확인
    $ curl vip -p 8000 # LB 잘 동작하는지 확인
    $ curl vip -p 8000 # LB 잘 동작하는지 확인
    $ curl vip -p 8000 # LB 잘 동작하는지 확인
    
    
    [vm1]$ systemctl stop keepalived
    $ curl vip -p 8000 # LB 잘 동작하는지 확인
    ```

    

