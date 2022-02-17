package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.bitcoin.config.UTXOConfigProperties;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.BatchParam;
import com.hacks1ash.crypto.wallet.blockchain.model.RPCError;
import com.hacks1ash.crypto.wallet.blockchain.model.RPCException;
import com.hacks1ash.crypto.wallet.blockchain.utils.Base64Coder;
import com.hacks1ash.crypto.wallet.blockchain.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public abstract class RPCClientImpl implements RPCClient {

  private static final Charset QUERY_CHARSET = Charset.forName("ISO8859-1");
  private static final int CONNECT_TIMEOUT = (int) TimeUnit.MINUTES.toMillis(1);
  private static final int READ_TIMEOUT = (int) TimeUnit.MINUTES.toMillis(5);

  private HostnameVerifier hostnameVerifier;
  private SSLSocketFactory sslSocketFactory;

  private final UTXOConfigProperties utxoConfigProperties;

  public RPCClientImpl(UTXOConfigProperties utxoConfigProperties) {
    this.utxoConfigProperties = utxoConfigProperties;
  }

  public HostnameVerifier getHostnameVerifier() {
    return hostnameVerifier;
  }

  public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
    this.hostnameVerifier = hostnameVerifier;
  }

  public SSLSocketFactory getSslSocketFactory() {
    return sslSocketFactory;
  }

  public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
    this.sslSocketFactory = sslSocketFactory;
  }

  protected byte[] prepareRequest(final String method, final Object... params) {
    return JSON.stringify(new LinkedHashMap<String, Object>() {
      {
        put("method", method);
        put("params", params);
        put("id", "1");
      }
    }).getBytes(QUERY_CHARSET);
  }

  protected byte[] prepareBatchRequest(final String method, final List<BatchParam> paramsList) {
    return JSON.stringify(paramsList.stream().map(batchParam -> new LinkedHashMap<String, Object>() {
      {
        put("method", method);
        put("params", batchParam.params);
        put("id", batchParam.id);
      }
    }).collect(Collectors.toList())).getBytes(QUERY_CHARSET);
  }

  private static byte[] loadStream(InputStream in) throws IOException {
    ByteArrayOutputStream o = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    for (; ; ) {
      int nr = in.read(buffer);

      if (nr == -1)
        break;
      if (nr == 0)
        throw new IOException("Read timed out");

      o.write(buffer, 0, nr);
    }
    return o.toByteArray();
  }

  @SuppressWarnings("rawtypes")
  private Object loadResponse(InputStream in) throws IOException, GenericRpcException {
    try (in) {
      String r = new String(loadStream(in), QUERY_CHARSET);
      log.info(String.format("Bitcoin JSON-RPC response:\n%s", r));
      try {
        Map response = (Map) JSON.parse(r);
        return getResponseObject("1", response);
      } catch (ClassCastException ex) {
        throw new RPCException("Invalid server response format (data: \"" + r + "\")");
      }
    }
  }

  @SuppressWarnings({"rawtypes","unchecked", "unsafe"})
  private Object loadBatchResponse(InputStream in, List<BatchParam> batchParams) throws IOException, GenericRpcException {
    try (in) {
      String r = new String(loadStream(in), QUERY_CHARSET);
      log.info(String.format("Bitcoin JSON-RPC response:\n%s", r));
      try {
        List<Map> response = (List<Map>) JSON.parse(r);

        return response.stream().map(item -> {
          try {
            Object expectedId = batchParams.stream()
              .filter(batchParam -> batchParam.id.equals(item.get("id")))
              .findFirst().orElseGet(() -> new BatchParam(null, null)).id;
            return getResponseObject(expectedId, item);
          } catch (RPCException e) {
            return e;
          }
        }).collect(Collectors.toList());
      } catch (ClassCastException ex) {
        throw new RPCException("Invalid server response format (data: \"" + r + "\")");
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private Object getResponseObject(Object expectedID, Map response) {
    if (!expectedID.equals(response.get("id")))
      throw new RPCException("Wrong response ID (expected: " + expectedID + ", response: " + response.get("id") + ")");

    if (response.get("error") != null)
      throw new RPCException(new RPCError(response));

    Object result = response.get("result");
    if (result != null) {
      if (result instanceof List) {
        List<Object> resultList = (List<Object>) result;
        for (Object obj : resultList) {
          LinkedHashMap map = (LinkedHashMap) obj;
          boolean success = (boolean) map.get("success");
          if (!success) {
            throw new RPCException(new RPCError(response), (String) ((LinkedHashMap) map.get("error")).get("message"));
          }
        }
      }
    }

    return result;
  }

  /**
   * Set an authenticated connection with Bitcoin server
   */
  private HttpURLConnection setConnection(UTXOProvider utxoProvider, String walletId) {
    HttpURLConnection conn;



    try {
      Pair<URL, String> urlFromConfig = getURLFromConfig(utxoProvider);
      URL noAuthURL = urlFromConfig.getFirst();
      String authStr = urlFromConfig.getSecond();
      URL connectionUrl = noAuthURL;

      if (walletId != null) {
        connectionUrl = new URI(noAuthURL.getProtocol(), null, noAuthURL.getHost(), noAuthURL.getPort(), "/wallet/" + walletId, noAuthURL.getQuery(), noAuthURL.getQuery()).toURL();
      }

      conn = (HttpURLConnection) connectionUrl.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);

      conn.setConnectTimeout(CONNECT_TIMEOUT);
      conn.setReadTimeout(READ_TIMEOUT);

      if (conn instanceof HttpsURLConnection) {
        if (hostnameVerifier != null)
          ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
        if (sslSocketFactory != null)
          ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
      }
      conn.setRequestProperty("Authorization", "Basic " + authStr);
      return conn;
    } catch (IOException | URISyntaxException ex) {
      throw new RPCException("Fail to set authenticated connection with server");
    }
  }

  @Override
  public Object query(UTXOProvider utxoProvider, String walletId, String method, Object... o) throws GenericRpcException {
    try {
      return loadResponse(queryForStream(utxoProvider, walletId, method, o));
    } catch (IOException ex) {
      throw new RPCException(method, Arrays.deepToString(o), ex);
    }
  }

  @Override
  public InputStream queryForStream(UTXOProvider utxoProvider, String walletId, String method, Object... o) throws GenericRpcException {
    HttpURLConnection conn = setConnection(utxoProvider, walletId);
    try {
      byte[] r = prepareRequest(method, o);
      log.info(String.format("Bitcoin JSON-RPC request:\n%s", new String(r, QUERY_CHARSET)));
      conn.getOutputStream().write(r);
      conn.getOutputStream().close();
      int responseCode = conn.getResponseCode();
      if (responseCode != 200) {
        InputStream errorStream = conn.getErrorStream();
        throw new RPCException(method,
          Arrays.deepToString(o),
          responseCode,
          conn.getResponseMessage(),
          errorStream == null ? null : new String(loadStream(errorStream)));
      }
      return conn.getInputStream();
    } catch (IOException ex) {
      throw new RPCException(method, Arrays.deepToString(o), ex);
    }
  }

  @Override
  public Object batchQuery(UTXOProvider utxoProvider, String walletId, String method, List<BatchParam> batchParams) throws GenericRpcException {
    HttpURLConnection conn = setConnection(utxoProvider, walletId);
    try {
      byte[] r = prepareBatchRequest(method, batchParams);
      log.info(String.format("Bitcoin JSON-RPC request:\n%s", new String(r, QUERY_CHARSET)));
      conn.getOutputStream().write(r);
      conn.getOutputStream().close();
      int responseCode = conn.getResponseCode();
      if (responseCode != 200) {
        InputStream errorStream = conn.getErrorStream();
        throw new RPCException(method,
          batchParams.stream().map(param -> Arrays.deepToString(param.params)).collect(Collectors.joining()),
          responseCode,
          conn.getResponseMessage(),
          errorStream == null ? null : new String(loadStream(errorStream)));
      }
      return loadBatchResponse((conn.getInputStream()), batchParams);
    } catch (IOException ex) {
      throw new RPCException(method, batchParams.stream()
        .map(param -> Arrays.deepToString(param.params)).collect(Collectors.joining()), ex);
    }
  }

  private Pair<URL, String> getURLFromConfig(UTXOProvider provider) throws MalformedURLException {
    URL noAuthURL;
    String authStr;
    UTXOConfigProperties.NodeConfig nodeConfig = utxoConfigProperties.getNodes().get(provider);
    URL rpc = new URL(nodeConfig.getScheme() + "://" + nodeConfig.getUsername() + ":" + nodeConfig.getPassword() + "@" + nodeConfig.getHost() + ":" + nodeConfig.getPort());
    try {
      noAuthURL = new URI(rpc.getProtocol(), null, rpc.getHost(), rpc.getPort(), rpc.getPath(), rpc.getQuery(), null).toURL();
    } catch (MalformedURLException | URISyntaxException ex) {
      throw new IllegalArgumentException(rpc.toString(), ex);
    }
    authStr = rpc.getUserInfo() == null ? null : String.valueOf(Base64Coder.encode(rpc.getUserInfo().getBytes(Charset.forName("ISO8859-1"))));

    return  Pair.of(noAuthURL, authStr);
  }
}