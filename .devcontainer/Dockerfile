FROM debian:bookworm-slim

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    git \
    unzip \
    xz-utils \
    zip \
    libglu1-mesa \
    openjdk-17-jdk-headless \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Create a non-root user for development
ARG USERNAME=dev
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -s /bin/bash

USER $USERNAME
WORKDIR /home/$USERNAME

# Setup Flutter and Android SDK paths
ENV SDK_ROOT=/home/$USERNAME/android-sdk
ENV FLUTTER_HOME=/home/$USERNAME/flutter
ENV PATH=$PATH:$FLUTTER_HOME/bin:$SDK_ROOT/cmdline-tools/latest/bin:$SDK_ROOT/platform-tools
ENV PUB_CACHE=/home/$USERNAME/.pub-cache
ENV PATH=$PATH:$PUB_CACHE/bin

# Install Flutter
RUN git clone https://github.com/flutter/flutter.git -b stable $FLUTTER_HOME

# Install Android Command Line Tools
RUN mkdir -p $SDK_ROOT/cmdline-tools \
    && wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O cmdline-tools.zip \
    && unzip cmdline-tools.zip -d $SDK_ROOT/cmdline-tools \
    && mv $SDK_ROOT/cmdline-tools/cmdline-tools $SDK_ROOT/cmdline-tools/latest \
    && rm cmdline-tools.zip

# Accept Android Licenses (The "magic" step)
RUN yes | sdkmanager --licenses \
    && sdkmanager "platform-tools" \
                  "platforms;android-34" \
                  "platforms;android-36" \
                  "build-tools;34.0.0" \
                  "build-tools;36.0.0" \
                  "build-tools;28.0.3"

RUN flutter doctor
