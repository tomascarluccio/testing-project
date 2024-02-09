FROM ubuntu:22.04


ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV MAVEN_VERSION 3.9.4
ENV MAVEN_HOME /opt/maven
ENV CHROME_VERSION 114.0.5735.90
ENV CHROME_DRIVER_VERSION 114.0.5735.90
ENV CHROME_DRIVER /usr/bin/chromedriver
ENV NODE_VERSION 20
ENV PATH $MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH


# Prerrequisites
RUN DEBIAN_FRONTEND=noninteractive
RUN apt-get update -y
RUN apt-get install -y curl git tar unzip gnupg2 swaks nano vim ca-certificates tree

# Install OpenJDK 8
RUN apt-get install -y openjdk-8-jdk


# Install Maven
RUN curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN tar -xf /tmp/apache-maven.tar.gz -C /opt/
RUN ln -s /opt/apache-maven-$MAVEN_VERSION $MAVEN_HOME
RUN ln -s $MAVEN_HOME/bin/mvn /usr/local/bin/mvn
RUN rm -f /tmp/apache-maven.tar.gz


# Install Chrome
RUN curl -sS -o /tmp/google-chrome-stable.deb https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_${CHROME_VERSION}-1_amd64.deb
RUN apt-get install -y /tmp/google-chrome-stable.deb
RUN rm /tmp/google-chrome-stable.deb


# Install ChromeDriver
RUN curl -sS -o /tmp/chromedriver_linux64.zip https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip 
RUN unzip /tmp/chromedriver_linux64.zip -d /usr/bin 
RUN rm /tmp/chromedriver_linux64.zip 
RUN chmod +x /usr/bin/chromedriver


# Install NodeJS
RUN curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg
RUN echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_VERSION.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list
RUN apt-get update -y
RUN apt-get install -y nodejs


# Install Newman
RUN npm install -g newman
RUN npm install -g newman-reporter-htmlextra


# Set working directory
RUN mkdir -p /app/
RUN mkdir -p /app/download
RUN mkdir -p /app/assets/certs


# Cleanup
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*


WORKDIR /app/

ADD files/app/utilities /app/utilities
ADD files/app/assets/ /app/assets/
ADD files/app/licenses /app/licenses
ADD files/entrypoint.sh .

CMD ["./entrypoint.sh"]
#CMD ["/bin/bash"]
