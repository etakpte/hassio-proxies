# Run nginx in foreground.
daemon off;

# This is run inside Docker.
user nginx;

# Pid storage location.
pid /var/run/nginx.pid;

# Set number of worker processes.
worker_processes 1;

# Enables the use of JIT for regular expressions to speed-up their processing.
pcre_jit on;

# Write error log to the add-on log.
error_log /proc/1/fd/1 error;

# Max num of simultaneous connections by a worker process.
events {
    worker_connections 512;
}

http {
    access_log              off;
    client_max_body_size    4G;
    default_type            application/octet-stream;
    keepalive_timeout       65;
    sendfile                off;
    server_tokens           off;
    tcp_nodelay             on;
    tcp_nopush              on;

    map $http_upgrade $connection_upgrade {
        default upgrade;
        ''      close;
    }
    resolver 127.0.0.11 ipv6=off;

    server {
        listen 8099 default_server;

        root /dev/null;
        server_name _;

        location / {
            allow   172.30.32.2;
            deny    all;

            # Must be host:port ONLY, no path, no trailing slash
            # e.g. http://192.168.31.27:9092
            set     $target "{{ .server }}";

            proxy_pass                  $target;
            proxy_http_version          1.1;
            proxy_ignore_client_abort   off;
            proxy_read_timeout          86400s;
            proxy_send_timeout          86400s;
            proxy_max_temp_file_size    0;

            # Rewrite redirects from FlexGet so they stay inside the Ingress path
            proxy_redirect ~^(?:https?://[^/]+)?/(.*)$ $http_x_ingress_path/$1;

            proxy_no_cache     1;
            proxy_cache_bypass 1;

            add_header Cache-Control "no-store, no-cache, must-revalidate, proxy-revalidate, max-age=0";
            add_header Pragma "no-cache";
            add_header Expires 0;

            # Needed so sub_filter can read the response (no gzip)
            proxy_set_header Accept-Encoding "";
            proxy_set_header Connection $connection_upgrade;
            proxy_set_header Host $http_host;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-NginX-Proxy true;
            proxy_set_header X-Real-IP $remote_addr;

            # Rewrite absolute /flexget/ paths in HTML/JS/CSS to include the Ingress prefix
            sub_filter_once  on;
            sub_filter_types text/html;
            sub_filter '<base href="/flexget/">' '<base href="$http_x_ingress_path/flexget/">';
        }
    }
}