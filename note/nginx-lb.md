## nginx로 로드밸런싱

1. vm 3대 설치하고 nginx 설치

   ``` bash
   $ su
   $ apt-get update
   $ apt-get upgrade
   $ apt-get nginx
   ```

2. vm2,3[was] / nginx.conf

   ```bash
   # nginx.conf
   worker_processes 1; // VM을 1코어로 잡아 1로 설정
   
   events {
   	worker_connections 1024;
   }
   
   http {
   	server {
   		listen <port>;
   		server_name <server_name>;
   		
   		location / {
   			root html;
   			index index.html;
   		}
   	}
   }
   ```

3. vm1[lb]/ nginx.conf

   ```bash
   # nginx.conf
   
   worker_processes 1;
   
   events {
   	worker_connections 1024;
   }
   
   http{
   	
   	upstream <serverSetName> {		# lb에 붙일 was 들
   		server <address>;
   		server <address>;
   		server <address>;
   	}
   	
   	server {
   		listen <port>;
   		server_name <server_name>;
   		
   		location / {
   			proxy_pass http://<serverSetName>; # '/' url로 들어오는 요청을 was로 넘겨줌
   		}
   	}
   }
   
   ```
