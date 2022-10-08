import argparse
import logging
import os
import random
import requests
import time
from threading import Thread

import settings

logging.basicConfig(filename=os.path.join(settings.LOG_PATH, "wallet-notify.log"),
                    filemode='a',
                    format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    level=logging.DEBUG)

logger = logging.getLogger("odradekPaySocketServer")

parser = argparse.ArgumentParser(description='Wallet notify script')

parser.add_argument("txid", metavar='N', type=str, nargs='+', help='txid which changed')
parser.add_argument("wallet_name", metavar='N', type=str, nargs='+', help='wallet name')
parser.add_argument("block_height", metavar='N', type=str, nargs='+',
                    help="tranasction blockheigh -1 if transaction is not confirmed")

args = parser.parse_args()
txids = args.txid
wallet_names = args.wallet_name
block_heights = args.block_height
txid = None
wallet_name = None
block_height = None

if txids:
    txid = txids[0]
if wallet_names:
    wallet_name = wallet_names[0]
if block_heights:
    block_height = block_heights[0]

script_id = random.randint(1000000, 1000000000)
logger.info(f"<======================== STARTING EXECUTING SCRIPT <{script_id}> =============================>\n\n\n\n")

data_to_send = {"blockHeight": block_height, "walletName": wallet_name, "txId": txid, "coin": settings.COIN_TICKER}

def post_notification(notify_url):
    sleep_time = 30
    tries = 0
    while tries < 5:
        tries += 1
        request_url = notify_url
        try:
            response = requests.post(request_url, json=data_to_send)
        except Exception as _:
            logger.info(f"Occured exception while sending request : {request_url}")
            logger.info("Next request after 30 seconds")
            time.sleep(sleep_time)
            sleep_time += 30
        else:
            if int(response.status_code) == 200:
                logger.info(f"Sent webhook data to {request_url}")
                break
            else:
                logger.info(f"Handler didn't returned status code 200, reason : {response.content}")
                time.sleep(sleep_time)
                sleep_time += 30


for url in settings.WEBHOOK_HANDLER_URLS:
    Thread(target=post_notification, args=(url,)).start()

logger.info(f"\n\n\n<======================== END EXECUTING SCRIPT <{script_id}>=============================>\n\n\n\n")
