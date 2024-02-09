import os
import logging


BASE_DIR = os.path.abspath(os.path.dirname(__file__))
DEBUG = True
BASE_PATH = os.path.join(BASE_DIR, "debug") if DEBUG else "/home/altipeak/config"
LOGGING_LEVEL = logging.DEBUG if DEBUG else logging.WARNING
LOGGING_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"

API_TOKEN = os.environ.get("API_TOKEN", "Zo3hPVJQEdXSdw2mumL6fX2W6TI2XXOs")

REDIS_HOST = os.environ.get("REDIS_HOST", "127.0.0.1")
REDIS_PORT = int(os.environ.get("REDIS_PORT", "6379"))
REDIS_PASSWORD = os.environ.get("REDIS_PASSWORD")
REDIS_DATABASE = int(os.environ.get("REDIS_DATABASE", "0"))
REDIS_PREFIX = os.environ.get("REDIS_PREFIX", ":default")

if REDIS_PASSWORD:
    REDIS_LOCATION = "redis://:%s@%s:%s/%s" % (REDIS_PASSWORD, REDIS_HOST, REDIS_PORT, REDIS_DATABASE)
else:
    REDIS_LOCATION = "redis://@%s:%s/%s" % (REDIS_HOST, REDIS_PORT, REDIS_DATABASE)
