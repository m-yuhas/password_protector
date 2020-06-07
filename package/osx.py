"""Build packages for Mac OSX."""


import logging
from os import path, makedirs
from sys import platform


def check_os_version() -> None:
    if platform != 'darwin':
        raise Exception(f'Invalid Operating System: {platform}')


def build_app_bundle() -> None:
    LOGGER.info('Creating App Bundle Directory Tree')
    BUNDLE_DIR = path.join(DIST_DIR, 'PasswordProtector.app', 'Contents')
    makedirs(path.join(BUNDLE_DIR, 'MacOS'))
    makedirs(path.join(BUNDLE_DIR, 'Resources'))
    with open(path.join(BUNDLE_DIR, 'Info.plist'), 'w') as plist_file:
        plist_file.write("""
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>CFBundleGetInfoString</key>
  <string>Password Protector</string>
  <key>CFBundleExecutable</key>
  <string>launch.sh</string>
  <key>CFBundleIdentifier</key>
  <string></string>
  <key>CFBundleName</key>
  <string>Password Protector</string>
  <key>CFBundleIconFile</key>
  <string>PasswordProtector.icns</string>
  <key>CFBundleShortVersionString</key>
  <string>0.1</string>
  <key>CFBundleInfoDictionaryVersion</key>
  <string>6.0</string>
  <key>CFBundlePackageType</key>
  <string>APPL</string>
  <key>IFMajorVersion</key>
  <integer>0</integer>
  <key>IFMinorVersion</key>
  <integer>1</integer>
</dict>
</plist>
        """)
    with open(path.join(BUNDLE_DIR, 'MacOS', 'launch.sh'), 'w') as launch_file:
        launch_file.write("""
#!/bin/bash
java -jar PasswordProtector.jar
        """)


def main() -> None:
    LOGGER.info('Building Packages for Mac OSX.')
    try:
        LOGGER.info('Checking OS Version...')
        check_os_version()
    except Exception as error:
        LOGGER.critical(f'{error}')
        return
    build_app_bundle()


logging.basicConfig()
LOGGER = logging.getLogger('Password Protector Packaging')
LOGGER.setLevel(logging.INFO)
DIST_DIR = '../dist'
if __name__ == '__main__':
    main()
