package com.hacks1ash.crypto.wallet.blockchain.factory;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AddressQualifier {

  UTXOProvider[] providers();

}
