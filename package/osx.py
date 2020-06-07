"""Build packages for Mac OSX."""


import logging
from sys import platform


def check_os_version() -> None:
    if platform != 'darwin':
        raise Exception(f'Invalid Operating System: {platform}')


def main() -> None:
    logger.info("Building Packages for Mac OSX.")
    try:
        logger.info("Checking OS Version...")
    except Exception as error:
        logger.info(f'{error}')


logging.basicConfig()
logger = logging.getLogger("Password Protector Packaging")
logger.setLevel(logging.INFO)
if __name__ == '__main__':
    main()
