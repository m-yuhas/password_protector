"""Build packages for Mac OSX."""


import logging
from os import path, makedirs
from subprocess import call
from sys import platform, argv, exit
from typing import List

import cv2
import numpy
from PIL import Image, ImageDraw


PROJECT_ROOT = path.join(path.dirname(__file__), '..')


APP_BUNDLE_DIR = path.join(PROJECT_ROOT, 'dist', 'PasswordProtector.app')


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
    """
    Parse the command line arguments and return a list of the packages to build.
    If no arguments are provided return a list of all the build options.  If an
    invalid argument is provided, an exception should be raised, and the help
    menu printed.

    Returns:
        A list of strings corresponding to the packages to build.  Currently
        app, dmg, cask, and brew are supported.

    Raises:
        ValueError if the user enters an argument that is not on the list of
        supoprted command line arguments.
    """
    recipes = ['app', 'dmg', 'cask', 'brew']
    if len(argv) == 1:
        return recipes
    build_list = []
    for arg in argv[1:]:
        build_list.append(arg.lower())
        if arg.lower() not in recipes:
            print(
                "Build packages for Mac OSX\n"
                "Usage: python osx.py <app> <dmg> <cask> <brew>\n"
                "If none of the optional arguments are added, all package \n"
                "types will be built\n"
                "app - build a Mac OSX app bundle.\n"
                "dmg - build a dmg installer for Mac OSX\n"
                "cask - build a homebrew cask\n"
                "brew - build a homebrew brew")
            raise ValueError("Invalid command line argument")
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


if __name__ == '__main__':
    packages = []
    try:
        packages = parse_args()
    except ValueError as error:
        exit(1)
    logging.basicConfig()
    logger = logging.getLogger('Password Protector Packaging')
    logger.setLevel(logging.INFO)
    logger.info(f'Checking current platform...')
    if platform != 'darwin':
        logger.critical(f'Platform "{platform}" not supported!')
        exit(1)
    logger.info(f'Platform is {platform}.')
    if 'app' or 'dmg' in packages:
        build_app_bundle()
    
