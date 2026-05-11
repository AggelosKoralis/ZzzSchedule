FROM debian:bookworm-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl git unzip xz-utils zip libglu1-mesa openjdk-17-jdk-headless wget \
    && rm -rf /var/lib/apt/lists/*

# Set up environment variables
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV FLUTTER_HOME=/opt/flutter
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools:$FLUTTER_HOME/bin"

# Install Android SDK Command-line Tools
RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/tools.zip && \
    unzip /tmp/tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest && \
    rm /tmp/tools.zip

# Accept Android Licenses
RUN yes | sdkmanager --licenses

# Install Platform Tools (ADB)
RUN sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Use an Argument that defaults to 1000 if not provided
ARG UID=1000
ARG GID=1000

# Install Flutter
RUN git clone https://github.com/flutter/flutter.git -b stable $FLUTTER_HOME

# Use the variables instead of hardcoded numbers
RUN chown -R ${UID}:${GID} $FLUTTER_HOME
RUN git config --system --add safe.directory $FLUTTER_HOME

# Install Flutter
RUN flutter doctor

WORKDIR /app
