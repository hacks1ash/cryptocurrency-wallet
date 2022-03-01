import os

COIN_NAME = "/usr/local/bin/bitcoin-cli"
COIN_TICKER = "lbtc"
CONF_PATH = "/home/ubuntu/.bitcoin/bitcoin.conf"

NOTIFY_URLS = [
    "https://webhook.site/4372d9ef-843c-4127-8ee9-970e7e72b63c"
]

WEBHOOK_HANDLER_URLS = [
   "https://webhook.site/4372d9ef-843c-4127-8ee9-970e7e72b63c"
]

LOG_PATH = os.path.dirname(os.path.realpath(__file__))
