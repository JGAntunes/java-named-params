FROM alpine

WORKDIR /code

ENV APK_PACKAGES bash openjdk8 apache-ant
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV CLASSPATH="./*:/code/lib/*"

RUN apk --update add ${APK_PACKAGES}

COPY . .

RUN ant

CMD ["/bin/bash", "java ist.meic.pa.KeyConstructors Widget]
