package com.hacks1ash.crypto.wallet.blockchain.model;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class UTXORawTxBuilder {

  private final UTXORPCClient rpcClient;
  private final String walletId;


  public UTXORawTxBuilder(UTXORPCClient rpcClient, String walletId) {
    this.rpcClient = rpcClient;
    this.walletId = walletId;
  }

  public Set<TxInput> inputs = new LinkedHashSet<>();
  public List<TxOutput> outputs = new ArrayList<>();

  public UTXORawTxBuilder out(String address, BigDecimal amount) {
    return out(address, amount, null);
  }

  public UTXORawTxBuilder out(String address, BigDecimal amount, byte[] data) {
    outputs.add(new BasicTxOutput(address, amount, data));
    return this;
  }

  public String create(UTXOProvider utxoProvider, NetworkParams networkParams) throws GenericRpcException {
    return rpcClient.createRawTransaction(utxoProvider, walletId, new ArrayList<>(inputs), outputs, networkParams);
  }

  private static class Input extends BasicTxInput {

    public Input(String txid, Integer vout) {
      super(txid, vout);
    }

    public Input(TxInput copy) {
      this(copy.txid(), copy.vout());
    }

    @Override
    public int hashCode() {
      return txid.hashCode() + vout;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (!(obj instanceof TxInput))
        return false;
      TxInput other = (TxInput) obj;
      return Objects.equals(vout, other.vout()) && txid.equals(other.txid());
    }

  }

  public interface TxInput extends Serializable {

    String txid();

    Integer vout();

    String scriptPubKey();

    BigDecimal amount();
  }

  public interface TxOutput extends Serializable {

    public String address();

    /**
     * @return The label associated with {@link #address()}
     */
    public String label();

    public BigDecimal amount();

    /**
     * @return Whether we have the private keys to spend this output
     */
    public Boolean spendable();

    /**
     * @return Whether we know how to spend this output, ignoring the lack of keys
     */
    public Boolean solvable();

    /**
     * @return (only when solvable) A descriptor for spending this output
     */
    public String desc();

    /**
     * @return Whether this output is considered safe to spend. Unconfirmed
     *         transactions from outside keys and unconfirmed replacement
     *         transactions are considered unsafe and are not eligible for spending
     *         by fundrawtransaction and sendtoaddress.
     */
    public Boolean safe();

    public byte[] data();
  }

  public static class BasicTxInput implements TxInput {

    public String txid;
    public Integer vout;
    public String scriptPubKey;
    public BigDecimal amount;

    public BasicTxInput(String txid, Integer vout) {
      this.txid = txid;
      this.vout = vout;
    }

    public BasicTxInput(String txid, Integer vout, String scriptPubKey) {
      this(txid, vout);
      this.scriptPubKey = scriptPubKey;
    }

    public BasicTxInput(String txid, Integer vout, String scriptPubKey, BigDecimal amount) {
      this(txid, vout, scriptPubKey);
      this.amount = amount;
    }

    @Override
    public String txid() {
      return txid;
    }

    @Override
    public Integer vout() {
      return vout;
    }

    @Override
    public String scriptPubKey() {
      return scriptPubKey;
    }

    @Override
    public BigDecimal amount() {
      return amount;
    }
  }

  public static class BasicTxOutput implements TxOutput {

    private static final long serialVersionUID = 4906609252978270536L;

    public final String address;
    public final String label;
    public final BigDecimal amount;
    public final Boolean spendable;
    public final Boolean solvable;
    public final String desc;
    public final Boolean safe;
    public final byte[] data;

    public BasicTxOutput(String address, BigDecimal amount) {
      this(address, null, amount, null, null, null, null, null);
    }

    public BasicTxOutput(String address, BigDecimal amount, byte[] data) {
      this(address, null, amount, null, null, null, null, data);
    }

    public BasicTxOutput(String address, BigDecimal amount, Boolean spendable, byte[] data) {
      this(address, null, amount, spendable, null, null, null, data);
    }

    public BasicTxOutput(String address, String label, BigDecimal amount, Boolean spendable, Boolean solvable, String desc, Boolean safe, byte[] data) {
      this.address = address;
      this.label = label;
      this.amount = amount;
      this.spendable = spendable;
      this.solvable = solvable;
      this.desc = desc;
      this.safe = safe;
      this.data = data;
    }

    @Override
    public String address() {
      return address;
    }

    @Override
    public BigDecimal amount() {
      return amount;
    }

    @Override
    public byte[] data() {
      return data;
    }

    @Override
    public String label()
    {
      return label;
    }

    @Override
    public Boolean spendable()
    {
      return spendable;
    }

    @Override
    public Boolean solvable()
    {
      return solvable;
    }

    @Override
    public String desc()
    {
      return desc;
    }

    @Override
    public Boolean safe()
    {
      return safe;
    }
  }


}