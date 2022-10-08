import argparse
import json
import logging
import os
import random
import requests
import subprocess
import time
from threading import Thread

import settings

logging.basicConfig(filename=os.path.join(settings.LOG_PATH, "block-notify.log"),
                    filemode='a',
                    format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    level=logging.DEBUG)

logger = logging.getLogger("odradekPaySocketServer")

parser = argparse.ArgumentParser(description='Block notify script')

parser.add_argument("blockhash", metavar='N', type=str, nargs='+',
                    help='new block hash')

args = parser.parse_args()
block_hashes = args.blockhash
block_height = None
block_hash = None

if block_hashes:
    block_hash = block_hashes[0]

script_id = random.randint(1000000, 1000000000)
logger.info(f"<======================== STARTING EXECUTING SCRIPT <{script_id}> =============================>\n\n\n\n")

if block_hash:
    block_data = subprocess.Popen(
        [f"{settings.COIN_NAME} -conf={settings.CONF_PATH} getblock {block_hash} 1"],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    block_json, err = block_data.communicate(b"")
    block_json = json.loads(block_json)
    block_height = block_json.get("height")
    logger.info(block_json)
    logger.info(f"BLOCK HEIGHT: {block_height}")

data_to_send = {"blockNumber": block_height, "coin": settings.COIN_TICKER}


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


for url in settings.NOTIFY_URLS:
    Thread(target=post_notification, args=(url,)).start()

logger.info(f"\n\n\n<======================== END EXECUTING SCRIPT <{script_id}>=============================>\n\n\n\n")
