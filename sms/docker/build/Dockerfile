FROM debian:bullseye-slim

#
# Packages
#
RUN apt-get clean && apt-get update
RUN DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y git build-essential ca-certificates libssl-dev curl systemd openssl
RUN apt-get install -y python3 python3-virtualenv python3-dev python3-setuptools python3-pip cython3 python3-psycopg2 python3-venv


#
# SMS server
#
COPY files/sms /home/sms
RUN python3 -m venv /home/sms/venv
RUN /home/sms/venv/bin/pip install -e /home/sms
RUN sed -i "s|DEBUG.*=.*|DEBUG = False|" /home/sms/settings.py


#####################


EXPOSE 9999
ADD files/entrypoint.sh /entrypoint.sh
RUN chmod +x /*.sh
WORKDIR /
CMD ["/entrypoint.sh"]
