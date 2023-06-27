FROM gradle:jdk19-jammy
ENV PROJECT_ROOT=/home/gradle/password_protector

# Copy in project files
WORKDIR ${PROJECT_ROOT}/
COPY resources ${PROJECT_ROOT}/resources
COPY src ${PROJECT_ROOT}/src
COPY build.gradle ${PROJECT_ROOT}/build.gradle

# Output build artifacts to a volume
VOLUME ${PROJECT_ROOT}/dist

# Build the application
RUN gradle clean && gradle jarCli && gradle jarGui

# By default launch a shell in the build artifact directory
WORKDIR ${PROJECT_ROOT}/dist
ENTRYPOINT ["/bin/sh"]