package com.hacks1ash.crypto.wallet.blockchain.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportMultiRequest {

  private String desc;

  private Object scriptPubKey;

  private String timestamp = "now";

  private String redeemScript;

  private String witnessScript;

  private List<String> pubKeys;

  private List<String> privKeys;

  private List<Integer> range;

  private boolean internal = false;

  private boolean watchOnly = false;

  private String label = "";

  private boolean keypool = false;

  public ImportMultiRequest(Object scriptPubKey, String redeemScript, String witnessScript, List<String> privKeys, boolean internal, String label) {
    this.scriptPubKey = scriptPubKey;
    this.redeemScript = redeemScript;
    this.witnessScript = witnessScript;
    this.privKeys = privKeys;
    this.internal = internal;
    this.label = label;
  }

  public LinkedHashMap<String, Object> toJson() {
    return new LinkedHashMap<>() {
      {
        if (getDesc() != null) put("desc", getDesc());
        if (getScriptPubKey() != null) put("scriptPubKey", getScriptPubKey());
        if (getTimestamp() != null) put("timestamp", getTimestamp());
        if (getRedeemScript() != null) put("redeemscript", getRedeemScript());
        if (getWitnessScript() != null) put("witnessscript", getWitnessScript());
        if (getPubKeys() != null) put("pubkeys", getPubKeys());
        if (getPrivKeys() != null) put("keys", getPrivKeys());
        if (getRange() != null) put("range", getRange());
        put("internal", isInternal());
        put("watchonly", isWatchOnly());
        if (getLabel() != null) put("label", getLabel());
        put("keypool", isKeypool());
      }
    };
  }
}
