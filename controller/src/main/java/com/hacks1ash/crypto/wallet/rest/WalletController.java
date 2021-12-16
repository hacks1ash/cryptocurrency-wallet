package com.hacks1ash.crypto.wallet.rest;

import co.elastic.apm.api.CaptureTransaction;
import com.hacks1ash.crypto.wallet.core.WalletManager;
import com.hacks1ash.crypto.wallet.core.model.request.AddressCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WalletCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "wallet")
public class WalletController {

  private WalletManager walletManager;

  @Operation(
    method = "createWallet",
    summary = "Create Wallet",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Name is alias name for wallet. \n Currency is coin name -> either btc, tbtc. \n hdSeed is optional param specify 12 words seperated by space to recover wallet form these words.",
      required = true,
      content = @Content(
        schema = @Schema(
          implementation = WalletCreationRequest.class
        )
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Wallet", content = @Content(schema = @Schema(implementation = WalletResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @PostMapping
  @CaptureTransaction
  public WalletResponse createWallet(@Valid @RequestBody WalletCreationRequest request) {
    return walletManager.createWallet(request.validate());
  }

  @Operation(
    method = "listWallets",
    summary = "Get wallets ",
    responses = {
      @ApiResponse(responseCode = "200", description = "Wallets", content = @Content(array = @ArraySchema(schema = @Schema(implementation = WalletResponse.class)))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @GetMapping
  @CaptureTransaction
  public List<WalletResponse> listWallets() {
    return walletManager.listWallets();
  }

  @Operation(
    method = "getBalance",
    summary = "Get balance for specified wallet",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    responses = {
      @ApiResponse(responseCode = "200", description = "Balance", content = @Content(schema = @Schema(implementation = BigInteger.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/balance", method = RequestMethod.GET)
  @CaptureTransaction
  public BigInteger getBalance(@PathVariable String walletId) {
    return walletManager.getBalance(walletId);
  }

  @Operation(
    method = "createAddress",
    summary = "Create address for specified wallet",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Name is address label. AddressType can have following values: P2SH, P2PKH, BECH_32, DEFAULT - same as P2SH",
      required = true,
      content = @Content(
        schema = @Schema(
          implementation = AddressCreationRequest.class
        )
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Address", content = @Content(schema = @Schema(implementation = RequestMapping.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/address", method = RequestMethod.POST)
  @CaptureTransaction
  public AddressResponse createAddress(@PathVariable String walletId, @RequestBody AddressCreationRequest request) {
    return walletManager.createAddress(walletId, request.validate());
  }

  @Operation(
    method = "getAddresses",
    summary = "Get wallet addresses ",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    responses = {
      @ApiResponse(responseCode = "200", description = "Addresses", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressResponse.class)))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/address", method = RequestMethod.GET)
  @CaptureTransaction
  public List<AddressResponse> getAddresses(@PathVariable String walletId) {
    return walletManager.getAddresses(walletId);
  }

  @Operation(
    method = "estimateFee",
    summary = "Estimate Transaction Fee",
    description = "Get exact amount of fee required to execute transaction",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "'recipients' is required at least 1. All amount should be specified in minor units for all the currencies. For fee it should be specified either speed or feePerByte, both cannot be chosen or non of them",
      required = true,
      content = @Content(
        schema = @Schema(
          implementation = TransactionRequest.class
        )
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Address", content = @Content(schema = @Schema(implementation = EstimateFeeResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/estimateFee", method = RequestMethod.PUT)
  @CaptureTransaction
  public EstimateFeeResponse estimateFee(@PathVariable String walletId, @RequestBody TransactionRequest request) {
    return walletManager.estimateFee(walletId, request.validate());
  }

  @Operation(
    method = "sendTransaction",
    summary = "Send wallet transaction",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "'recipients' is required at least 1. All amount should be specified in minor units for all the currencies. For fee it should be specified either speed or feePerByte, both cannot be chosen or non of them",
      required = true,
      content = @Content(
        schema = @Schema(
          implementation = TransactionRequest.class
        )
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Address", content = @Content(schema = @Schema(implementation = SendTransactionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/transaction", method = RequestMethod.POST)
  @CaptureTransaction
  public SendTransactionResponse sendTransaction(@PathVariable String walletId, @RequestBody TransactionRequest request) {
    return walletManager.sendTransaction(walletId, request.validate());
  }

  @Operation(
    method = "getTransaction",
    summary = "Get wallet transaction",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request"),
      @Parameter(name = "txId", required = true, description = "Transaction id for wallet transaction send/receive")
    },
    responses = {
      @ApiResponse(responseCode = "200", description = "Transaction", content = @Content(schema = @Schema(implementation = GetTransactionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet/transaction", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/transaction/{txId}", method = RequestMethod.GET)
  @CaptureTransaction
  public GetTransactionResponse getTransaction(@PathVariable String walletId, @PathVariable String txId) {
    return walletManager.getTransaction(walletId, txId);
  }


  @Operation(
    method = "getTransactions",
    summary = "Get wallet transaction list",
    parameters = {
      @Parameter(name = "walletId", required = true, description = "Wallet ID returned in wallet creation request")
    },
    responses = {
      @ApiResponse(responseCode = "200", description = "Transactions", content = @Content(array = @ArraySchema(schema =  @Schema(implementation = GetTransactionResponse.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Unable to find wallet/transaction", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Something unexpected happened", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
  )
  @RequestMapping(value = "/{walletId}/transaction", method = RequestMethod.GET)
  @CaptureTransaction
  public List<GetTransactionResponse> getTransactions(@PathVariable String walletId) {
    return walletManager.getTransactions(walletId);
  }

  @Autowired
  public void setWalletManager(WalletManager walletManager) {
    this.walletManager = walletManager;
  }
}
