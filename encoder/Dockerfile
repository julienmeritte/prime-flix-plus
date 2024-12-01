#### Stage 1: Build the application
FROM amazoncorretto:11 as build

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY ./pom.xml ../pom.xml
COPY ./encoder/pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY ./encoder/src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM amazoncorretto:11.0.14-al2

ARG DEPENDENCY=/app/target/dependency

RUN mkdir -p /app/static/videos/
RUN yum install -y wget
RUN yum install -y tar
RUN yum install -y xz
RUN mkdir -p /usr/local/bin/ffmpeg
COPY ./encoder/ffmpeg-release-amd64-static.tar.xz /usr/local/bin/ffmpeg/ffmpeg-release-amd64-static.tar.xz
RUN tar -xf /usr/local/bin/ffmpeg/ffmpeg-release-amd64-static.tar.xz -C /usr/local/bin/ffmpeg/
RUN mv /usr/local/bin/ffmpeg/ffmpeg-5.0.1-amd64-static/ /usr/local/bin/ffmpeg/ffmpeg
RUN ln -s /usr/local/bin/ffmpeg/ffmpeg/ffmpeg /bin/ffmpeg
RUN ln -s /usr/local/bin/ffmpeg/ffmpeg/ffprobe /bin/ffprobe



# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.etna.primeencoderplus.PrimeEncoderPlusApplication"]
