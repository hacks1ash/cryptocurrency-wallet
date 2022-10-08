import os

COIN_NAME = "/root/bitcoin/bin/bitcoin-cli"
COIN_TICKER = "lbtc"
CONF_PATH = "/root/.bitcoin/bitcoin.conf"

NOTIFY_URLS = [
    "https://webhook.site/3765ebf1-e3ee-40ff-a960-0b624515b526"
]

WEBHOOK_HANDLER_URLS = [
   "https://webhook.site/3765ebf1-e3ee-40ff-a960-0b624515b526"
]

LOG_PATH = os.path.dirname(os.path.realpath(__file__))
