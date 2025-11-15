// BSD 3-Clause License
//
// Copyright (c) 2025, Maxim Altoukhov
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// 3. Neither the name of the copyright holder nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package dev.maxalt.simpleforex;

import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/// An abstract provider of currency and exchange rate data.
///
/// Note: [Stream]-returning methods follow the JDK's "plural noun" [de-facto naming convention](https://stackoverflow.com/a/28805669).
@NullMarked
public interface ForexDataProvider {

    /// Fetches exchange rates of specific currency pairs on a specific date (if available)
    ///
    /// The returned dataset might not fully match the given criteria.
    /// For instance, if you request `USDCAD` and `EURCHF` on December 31st 2025,
    /// you might only get an exchange rate for `USDCAD` if `EURCHF` is not available on this date.
    /// Exchange rates may or may not be missing depending on the pair(s) or the date.
    ///
    /// Basically, just think of this method as an SQL `SELECT` query - it gives you what it finds.
    ///
    /// @param pairs the currency pairs you want exchange rates for *(this set is defensively copied)*
    /// @param date  the date for which you want exchange rates
    /// @return a stream of exchange rates that match the criteria, or an empty stream if none do
    /// @throws NullPointerException if an argument is `null` or contains `null`
    Stream<ExchangeRate> exchangeRates(Set<CurrencyPair> pairs, LocalDate date);

    /// Fetches exchange rates of specific currency pairs within a specific date range (if available)
    ///
    /// The returned dataset might not fully match the given criteria.
    /// See the [single-date `exchangeRates()` method][#exchangeRates(Set, LocalDate)] Javadoc for more info.
    ///
    /// The order of the dates does not matter. Given dates `x` and `y`, specifying the range as `x,y` or `y,x` is treated identically.
    /// If the dates are equal, calling this is equivalent to calling the [single-date `exchangeRates()` method][#exchangeRates(Set, LocalDate)].
    ///
    /// @param pairs the currency pairs you want exchange rates for *(this set is defensively copied)*
    /// @param start range start date, inclusive
    /// @param end   range end date, inclusive
    /// @return a stream of exchange rates that match the criteria, or an empty stream if none do
    /// @throws NullPointerException if an argument is `null` or contains `null`
    Stream<ExchangeRate> exchangeRates(Set<CurrencyPair> pairs, LocalDate start, LocalDate end);

    /// Finds the exchange rate of the given currency pair on the given date (if available).
    ///
    /// @param pair the currency pair you want the exchange rate of
    /// @param date the date of the currency exchange
    /// @return an `Optional` with an exchange rate if one was found, an empty `Optional` otherwise
    /// @throws NullPointerException if either argument is `null`
    Optional<ExchangeRate> findExchangeRate(CurrencyPair pair, LocalDate date);

    /// Returns all currencies supported by this provider.
    ///
    /// @return a stream of all supported currencies (neither the stream nor its contents are `null`)
    default Stream<Currency> supportedCurrencies() {
        return Currency.availableCurrencies();
    }

    /// Returns the earliest date this provider has exchange rate data for.
    ///
    /// If the implementation doesn't have a specific earliest date, it should return [LocalDate#MIN] to indicate there's no lower boundary.
    ///
    /// The purpose of this method is to potentially help developers narrow down the range of valid dates for
    /// [methods that fetch exchange rates][#exchangeRates(Set, LocalDate)].
    ///
    /// @return the earliest supported date (never `null`), or [LocalDate#MIN] if there's no limit
    default LocalDate getEarliestSupportedDate() {
        return LocalDate.MIN;
    }
}
