FROM alpine:latest AS loader

ARG OVERARCH_VERSION

RUN apk add --no-cache \
  wget \
  ca-certificates

RUN wget \
  "https://github.com/soulspace-org/overarch/releases/download/${OVERARCH_VERSION}/overarch.jar" \
  -O /opt/overarch.jar

FROM eclipse-temurin:17-jre-jammy

ENV LANG en_US.UTF-8

COPY --from=loader /opt/overarch.jar /opt/overarch.jar

WORKDIR /data

ENTRYPOINT ["java", "-jar", "/opt/overarch.jar"]
CMD ["-h"]
