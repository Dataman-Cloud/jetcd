package com.coreos.jetcd;

import com.coreos.jetcd.kv.TxnResponse;
import com.coreos.jetcd.op.Cmp;
import com.coreos.jetcd.op.Op;
import java.util.concurrent.CompletableFuture;

/**
 * Txn is the interface that wraps mini-transactions.
 *
 * <h3>Usage examples</h3>
 *
 * <pre>{@code
 * txn.If(
 *    new Cmp(KEY, Cmp.Op.GREATER, CmpTarget.value(VALUE)),
 *    new Cmp(KEY, cmp.Op.EQUAL, CmpTarget.version(2))
 * ).Then(
 *    Op.put(KEY2, VALUE2, PutOption.DEFAULT),
 *    Op.put(KEY3, VALUE3, PutOption.DEFAULT)
 * ).Else(
 *    Op.put(KEY4, VALUE4, PutOption.DEFAULT),
 *    Op.put(KEY4, VALUE4, PutOption.DEFAULT)
 * ).commit();
 * }</pre>
 *
 * <p>Txn also supports If, Then, and Else chaining. e.g.

 * <pre>{@code
 * txn.If(
 *    new Cmp(KEY, Cmp.Op.GREATER, CmpTarget.value(VALUE))
 * ).If(
 *    new Cmp(KEY, cmp.Op.EQUAL, CmpTarget.version(VERSION))
 * ).Then(
 *    Op.put(KEY2, VALUE2, PutOption.DEFAULT)
 * ).Then(
 *    Op.put(KEY3, VALUE3, PutOption.DEFAULT)
 * ).Else(
 *    Op.put(KEY4, VALUE4, PutOption.DEFAULT)
 * ).Else(
 *    Op.put(KEY4, VALUE4, PutOption.DEFAULT)
 * ).commit();
 * }</pre>
 */
public interface Txn {

  /**
   * takes a list of comparison. If all comparisons passed in succeed,
   * the operations passed into Then() will be executed. Or the operations
   * passed into Else() will be executed.
   */
  //CHECKSTYLE:OFF
  Txn If(Cmp... cmps);
  //CHECKSTYLE:ON

  /**
   * takes a list of operations. The Ops list will be executed, if the
   * comparisons passed in If() succeed.
   */
  //CHECKSTYLE:OFF
  Txn Then(Op... ops);
  //CHECKSTYLE:ON

  /**
   * takes a list of operations. The Ops list will be executed, if the
   * comparisons passed in If() fail.
   */
  //CHECKSTYLE:OFF
  Txn Else(Op... ops);
  //CHECKSTYLE:OFF

  /**
   * tries to commit the transaction.
   *
   * @return a TxnResponse wrapped in CompletableFuture
   */
  CompletableFuture<TxnResponse> commit();
}
