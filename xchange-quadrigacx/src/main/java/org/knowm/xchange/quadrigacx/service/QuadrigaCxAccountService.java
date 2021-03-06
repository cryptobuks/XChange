package org.knowm.xchange.quadrigacx.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.quadrigacx.QuadrigaCxAdapters;
import org.knowm.xchange.quadrigacx.dto.account.QuadrigaCxBalance;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class QuadrigaCxAccountService extends QuadrigaCxAccountServiceRaw implements AccountService {

  public QuadrigaCxAccountService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {

    QuadrigaCxBalance quadrigaCxBalance = getQuadrigaCxBalance();
    return new AccountInfo(exchange.getExchangeSpecification().getUserName(), quadrigaCxBalance.getFee(),
        QuadrigaCxAdapters.adaptWallet(quadrigaCxBalance));
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address) throws IOException {

    if (currency.equals(Currency.BTC))
      return withdrawBitcoin(amount, address);
    else if (currency.equals(Currency.ETH))
      return withdrawEther(amount, address);
    else
      throw new IllegalStateException("unsupported ccy " + currency);
  }

  /**
   * This returns the currently set deposit address. It will not generate a new address (ie. repeated calls will return the same address).
   */
  @Override
  public String requestDepositAddress(Currency currency, String... arguments) throws IOException {
    if (currency.equals(Currency.BTC))
      return getQuadrigaCxBitcoinDepositAddress().getDepositAddress();
    else if (currency.equals(Currency.ETH))
      return getQuadrigaCxEtherDepositAddress().getDepositAddress();
    else
      throw new IllegalStateException("unsupported ccy " + currency);
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public List<FundingRecord> getFundingHistory(
      TradeHistoryParams params) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    throw new NotYetImplementedForExchangeException();
  }
}
