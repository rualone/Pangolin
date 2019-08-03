# Pangoline for Android

_Browse like no one’s watching. The new browser Pangoline automatically blocks a wide range of online trackers — from the moment you launch it to the second you leave it. Easily erase your history, passwords and cookies, so you won’t get followed by things like unwanted ads._ 

Pangoline provides automatic ad blocking and tracking protection on an easy-to-use private browser.

## Build Instructions


1. Clone or Download the repository.

2. Import the project into Android Studio **or** build on the command line:

  ```shell
  ./gradlew clean app:assembleFocusArmDebug
  ```

3. Make sure to select the correct build variant in Android Studio:
- **pangolineWebviewArmDebug** for ARM
- **pangolineWebviewX86Debug** for X86
- **pangolineWebviewAarch64Debug** for ARM64

## License


    This Source Code Form is subject to the terms of the Mozilla Public
    License, v. 2.0. If a copy of the MPL was not distributed with this
    file, You can obtain one at http://mozilla.org/MPL/2.0/
