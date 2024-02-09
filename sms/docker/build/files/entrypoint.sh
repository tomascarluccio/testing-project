#!/bin/bash -e


if ! [ -f /etc/ssl/certs/cert.crt ]; then
	openssl req -newkey rsa:2048 -new -x509 -sha256 -days 3652 -nodes -out /etc/ssl/certs/cert.crt -keyout /etc/ssl/private/cert.key -subj "/C=CH/ST=vaud/L=lausanne/O=altipeak/CN=$(hostname)"
	chown www-data:www-data /etc/ssl/certs/cert.crt
	chmod 600 /etc/ssl/certs/cert.crt
	chown www-data:www-data /etc/ssl/private/cert.key
	chmod 600 /etc/ssl/private/cert.key
fi

cd /home/sms
source venv/bin/activate
gunicorn app:app \
	--certfile /etc/ssl/certs/cert.crt \
	--keyfile /etc/ssl/private/cert.key \
	--bind 0.0.0.0:9999 \
	--worker-connections 50 \
	--workers 3 \
	--timeout 600 \
	--name sms_server \
	--log-level info \
	--log-file /dev/stdout \
    --access-logfile /dev/stderr