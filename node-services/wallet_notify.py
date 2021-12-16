import argparse
import random
import time

import requests
import json
import subprocess
import logging
import settings
from threading import Thread
import os

logging.basicConfig(filename=os.path.join(settings.LOG_PATH, "wallet-notify.log"),
                    filemode='a',
                    format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    level=logging.DEBUG)

logger = logging.getLogger("cwsSocketServer")

parser = argparse.ArgumentParser(description='Wallet notify script')

parser.add_argument("txid", metavar='N', type=str, nargs='+',
                    help='txid which changed')

args = parser.parse_args()
txids = args.txid
txid = None

if txids:
    txid = txids[0]

script_id = random.randint(1000000, 1000000000)
logger.info(f"<======================== STARTING EXECUTING SCRIPT <{script_id}> =============================>\n\n\n\n")

tx_hex = subprocess.Popen(
    [f"{settings.COIN_NAME} -conf={settings.CONF_PATH} getrawtransaction {txid} true"],
    stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
raw_transaction, err = tx_hex.communicate(b"")

json_data = json.loads(raw_transaction)
block_hash = json_data.get("blockhash")
block_height = -1
addresses = list()

if block_hash:
    block_data = subprocess.Popen(
        [f"{settings.COIN_NAME} -conf={settings.CONF_PATH} getblock {block_hash} 1"],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    block_json, err = block_data.communicate(b"")
    block_json = json.loads(block_json)
    block_height = block_json.get("height")
    logger.info(f"BLOCK HEIGHT: {block_height}")

try:
    logger.info(json_data)
    vins = json_data.get("vin")
    vouts = json_data.get("vout")
    for vin in vins:
        v_txid = vin.get("txid")
        vout_index = vin.get("vout")
        tx_hex = subprocess.Popen(
            [f"{settings.COIN_NAME} -conf={settings.CONF_PATH} getrawtransaction {v_txid} true"],
            stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        raw_transaction, err = tx_hex.communicate(b"")
        json_data_v = json.loads(raw_transaction)
        vouts_v = json_data_v.get("vout")
        output = vouts_v[int(vout_index)]
        addresses.append(output.get("scriptPubKey").get("addresses")[0])

    for vout in vouts:
        scr = vout.get("scriptPubKey")
        address = scr.get("addresses")[0]
        addresses.append(address)
except Exception as e:
    logger.info(e)

data_to_send = dict()
data_to_send['txid'] = txid
data_to_send['addresses'] = list(set(addresses))
data_to_send['blockHeight'] = block_height
data_to_send['coin'] = settings.COIN_TICKER


def post_notification(url_param):
    sleep_time = 30
    tries = 0
    while tries < 5:
        tries += 1
        try:
            logger.info(f"SENDING REQUEST TO : {url_param} \t DATA: {data_to_send}")
            response = requests.post(url_param, json=data_to_send)
        except Exception as ex:
            logger.info(
                f"Occured exception while sending request to url {url_param}, data: {data_to_send}, reason {ex}")
            logger.info("Next request after 30 seconds")
            time.sleep(sleep_time)
            sleep_time += 30
        else:
            if int(response.status_code) == 200:
                logger.info(f"Webhook successfully send to {url_param} for txid {data_to_send.get('txid')}")
                break
            else:
                logger.info(
                    f"Webhook handler {url_param} didn't returned status code 200 for txid {txid},"
                    f" reason : {response.content}"
                )
                time.sleep(sleep_time)
                sleep_time += 30


for url in settings.WEBHOOK_HANDLER_URLS:
    Thread(target=post_notification, args=(url,)).start()

logger.info(f"\n\n\n<======================== END EXECUTING SCRIPT <{script_id}>=============================>\n\n\n\n")
