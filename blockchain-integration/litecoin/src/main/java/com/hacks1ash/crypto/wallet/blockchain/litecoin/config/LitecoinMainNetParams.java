package com.hacks1ash.crypto.wallet.blockchain.litecoin.config;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.AbstractBitcoinNetParams;


/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class LitecoinMainNetParams extends AbstractBitcoinNetParams {
  public static final int MAINNET_MAJORITY_WINDOW = 1000;
  public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
  public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;
  private static final Sha256Hash GENESIS_HASH = Sha256Hash.wrap("12a765e31ffd4059bada1e25190f6e98c99d9714d334efa41a195a7e7e04bfe2");
  private static LitecoinMainNetParams instance;

  public LitecoinMainNetParams() {
    super();
    targetTimespan = TARGET_TIMESPAN;
    maxTarget = Utils.decodeCompactBits(0x1e0fffffL);
    addressHeader = 48;
    dumpedPrivateKeyHeader = 128 + addressHeader;
    p2shHeader = 5;
    segwitAddressHrp = "ltc";
    port = 9333;
    packetMagic = 0xfbc0b6dbL;
    bip32HeaderP2PKHpub = 0x019da462; // The 4 byte header that serializes in base58 to "Ltub".
    bip32HeaderP2PKHpriv = 0x019d9cfe; // The 4 byte header that serializes in base58 to "Ltpv"
    bip32HeaderP2WPKHpub = 0x04b24746; // The 4 byte header that serializes in base58 to "zpub".
    bip32HeaderP2WPKHpriv = 0x04b2430c; // The 4 byte header that serializes in base58 to "zprv"

    majorityEnforceBlockUpgrade = MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
    majorityRejectBlockOutdated = MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
    majorityWindow = MAINNET_MAJORITY_WINDOW;

    id = ID_MAINNET;
    subsidyDecreaseBlockCount = 840000;
    spendableCoinbaseDepth = 100;
    // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
    // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
    // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
    // Having these here simplifies block connection logic considerably.
    checkpoints.put(1500, Sha256Hash.wrap("841a2965955dd288cfa707a755d05a54e45f8bd476835ec9af4402a2b59a2967"));
    checkpoints.put(4032, Sha256Hash.wrap("9ce90e427198fc0ef05e5905ce3503725b80e26afd35a987965fd7e3d9cf0846"));
    checkpoints.put(8064, Sha256Hash.wrap("eb984353fc5190f210651f150c40b8a4bab9eeeff0b729fcb3987da694430d70"));
    checkpoints.put(16128, Sha256Hash.wrap("602edf1859b7f9a6af809f1d9b0e6cb66fdc1d4d9dcd7a4bec03e12a1ccd153d"));
    checkpoints.put(23420, Sha256Hash.wrap("d80fdf9ca81afd0bd2b2a90ac3a9fe547da58f2530ec874e978fce0b5101b507"));
    checkpoints.put(50000, Sha256Hash.wrap("69dc37eb029b68f075a5012dcc0419c127672adb4f3a32882b2b3e71d07a20a6"));
    checkpoints.put(80000, Sha256Hash.wrap("4fcb7c02f676a300503f49c764a89955a8f920b46a8cbecb4867182ecdb2e90a"));
    checkpoints.put(120000, Sha256Hash.wrap("bd9d26924f05f6daa7f0155f32828ec89e8e29cee9e7121b026a7a3552ac6131"));
    checkpoints.put(161500, Sha256Hash.wrap("dbe89880474f4bb4f75c227c77ba1cdc024991123b28b8418dbbf7798471ff43"));
    checkpoints.put(179620, Sha256Hash.wrap("2ad9c65c990ac00426d18e446e0fd7be2ffa69e9a7dcb28358a50b2b78b9f709"));
    checkpoints.put(240000, Sha256Hash.wrap("7140d1c4b4c2157ca217ee7636f24c9c73db39c4590c4e6eab2e3ea1555088aa"));
    checkpoints.put(383640, Sha256Hash.wrap("2b6809f094a9215bafc65eb3f110a35127a34be94b7d0590a096c3f126c6f364"));
    checkpoints.put(409004, Sha256Hash.wrap("487518d663d9f1fa08611d9395ad74d982b667fbdc0e77e9cf39b4f1355908a3"));
    checkpoints.put(456000, Sha256Hash.wrap("bf34f71cc6366cd487930d06be22f897e34ca6a40501ac7d401be32456372004"));
    checkpoints.put(541794, Sha256Hash.wrap("1cbccbe6920e7c258bbce1f26211084efb19764aa3224bec3f4320d77d6a2fd2"));
    checkpoints.put(585010, Sha256Hash.wrap("ea9ea06840de20a18a66acb07c9102ee6374ad2cbafc71794e576354fea5df2d"));
    checkpoints.put(638902, Sha256Hash.wrap("15238656e8ec63d28de29a8c75fcf3a5819afc953dcd9cc45cecc53baec74f38"));

    dnsSeeds = new String[]{
      "seed-a.litecoin.loshan.co.uk",
      "dnsseed.thrasher.io",
      "dnsseed.litecointools.com",
      "dnsseed.litecoinpool.org"
    };
    httpSeeds = null;
    addrSeeds = null;
  }

  public static synchronized LitecoinMainNetParams get() {
    if (instance == null) {
      instance = new LitecoinMainNetParams();
    }
    return instance;
  }

  @Override
  public String getPaymentProtocolId() {
    return PAYMENT_PROTOCOL_ID_MAINNET;
  }

}