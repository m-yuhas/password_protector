"""Build packages for Mac OSX."""


import logging
from os import path, makedirs
from subprocess import call
from sys import platform, argv
from typing import List

import cv2
import numpy
from PIL import Image, ImageDraw


PLIST = """
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
"""


LAUNCHER = """
#!/bin/bash
java -jar PasswordProtector.jar
"""

def parse_args() -> List[str]:
    recipes = ['app', 'dmg', 'cask', 'brew']
    if len(argv) == 1:
        return recipes
    build_list = []
    for arg in argv:
        if arg.lower() in recipes:
            build_list.append(arg.lower())
    if len(build_list) == 0:
        print("Usage")
    return build_list

def generate_icon() -> None:
    icon = Image.new('RGB', (1024, 1024), (0, 0, 0))
    draw = ImageDraw.Draw(icon)
    draw.pieslice([(0, 0), (1024, 1024)], start=0, end=360, fill=(0, 0, 128))
    draw.pieslice([(50, 50), (974, 974)], start=0, end=360, fill=(0, 128, 128))
    draw.pieslice([(325, 600), (699, 974)], start=0, end=360, fill=(255, 255, 0))
    draw.polygon([(487, 620), (487, 100), (537, 100), (537, 200), (587, 200), (587, 100), (687, 100), (687, 150), (637, 150), (637, 300), (687, 300), (687, 350), (587, 350), (587, 250), (537, 250), (537, 620)], fill=(255, 255, 0))
    draw.pieslice([(375, 650), (649, 924)], start=0, end=360, fill=(0, 128, 128))
    sizes = {
        'icon_512x512@2x.png': (1024, 1024),
        'icon_512x512.png': (512, 512),
        'icon_256x256@2x.png': (512, 512),
        'icon_256x256.png': (256, 256),
        'icon_128x128@2x.png': (256, 256),
        'icon_128x128.png': (128, 128),
        'icon_32x32@2x.png': (64, 64),
        'icon_32x32.png': (32, 32),
        'icon_16x16@2x.png': (32, 32),
        'icon_16x16.png': (16, 16)}
    for size in sizes:
        current_icon = icon.resize(sizes[size])
        current_icon.save(size, transparency=(0, 0, 0))
    # call(['iconutil', '-c', 'icns' 'blah.iconset'])

def check_os_version() -> None:
    if platform != 'darwin':
        raise Exception(f'Invalid Operating System: {platform}')


def build_app_bundle() -> None:
    LOGGER.info('Creating App Bundle Directory Tree')
    BUNDLE_DIR = path.join(DIST_DIR, 'PasswordProtector.app', 'Contents')
    try:
        makedirs(path.join(BUNDLE_DIR, 'MacOS'))
        makedirs(path.join(BUNDLE_DIR, 'Resources'))
    except FileExistsError:
        LOGGER.info('Directories already exist')
    with open(path.join(BUNDLE_DIR, 'Info.plist'), 'w') as plist_file:
        plist_file.write(PLIST)
    with open(path.join(BUNDLE_DIR, 'MacOS', 'launch.sh'), 'w') as launch_file:
        launch_file.write(LAUNCHER)
    LOGGER.info('Creating Icon Set...')
    generate_icon()


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
DIST_DIR = path.join(path.dirname(__file__), '..', 'dist')
if __name__ == '__main__':
    main()
