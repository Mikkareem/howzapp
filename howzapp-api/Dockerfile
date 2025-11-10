FROM eclipse-temurin:21 AS jre-build
COPY src /app/src
COPY gradle /app/gradle
COPY build.gradle.kts /app/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts
COPY gradlew /app/gradlew

RUN sudo chmod +x /app/gradlew \
    && /app/gradlew build -x test \
    && sudo mv /app/build/libs/howzapp-1.0.jar /app/howzapp.jar \
    && sudo apt update \
    && sudo apt install unzip -y \
    && unzip /app/howzapp.jar -d temp \
    && $JAVA_HOME/bin/jdeps \
      --print-module-deps \
      --ignore-missing-deps \
      --recursive \
      --multi-release 17 \
      --class-path="./temp/BOOT-INF/lib/*" \
      --module-path="./temp/BOOT-INF/lib/*" \
      build/libs/howzapp-0.0.1-SNAPSHOT.jar > ./jre-modules.txt \
    && $JAVA_HOME/bin/jlink \
      --verbose \
      --add-modules "$(cat ./jre-modules.txt)" \
      --strip-debug \
      --no-man-pages \
      --no-header-files \
      --compress=2 \
      --output /tmp/jre

FROM debian:buster-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /tmp/jre $JAVA_HOME
COPY --from=jre-build /app/howzapp.jar /app/howzapp.jar

CMD ["java", "-jar", "/app/howzapp.jar"]