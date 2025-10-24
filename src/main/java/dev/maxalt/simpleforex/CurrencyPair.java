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
import org.jspecify.annotations.Nullable;

import java.util.Currency;
import java.util.Objects;

/// A [pair of currencies](https://en.wikipedia.org/wiki/Currency_pair) in an exchange.
///
/// Simply put, the *base* currency is the one we're selling, while the *quote* currency is the one we're buying.
/// An instance of this class will never have identical base and quote currencies.
///
/// This is an immutable, value-based class. Use it as you would use [java.time.LocalDate] or [java.util.Optional].
///
/// @see Currency
@NullMarked
public record CurrencyPair(Currency base, Currency quote) {

    /// Main constructor.
    ///
    /// @param base  the pair's base currency, not `null`
    /// @param quote the pair's quote currency, not `null`
    /// @throws NullPointerException     if either currency is `null`
    /// @throws IllegalArgumentException if `base` is equal to `quote`
    public CurrencyPair {
        Objects.requireNonNull(base, "currency pair cannot contain a null base currency");
        Objects.requireNonNull(quote, "currency pair cannot contain a null quote currency");

        if (base.equals(quote)) {
            throw new IllegalArgumentException("currency pair base and quote cannot be identical");
        }
    }

    /// A shortcut to create a [CurrencyPair] from [ISO 4217](https://www.iso.org/iso-4217-currency-codes.html) currency codes directly.
    ///
    /// @param baseCode  the base currency code, not `null`
    /// @param quoteCode the quote currency code, not `null`
    /// @return a new instance
    /// @throws NullPointerException     if either currency code is `null`
    /// @throws IllegalArgumentException if either currency code is unsupported by Java's [Currency] class or the base and quote are equal
    public static CurrencyPair fromIsoCodes(String baseCode, String quoteCode) {
        var base = Currency.getInstance(baseCode);
        var quote = Currency.getInstance(quoteCode);
        return new CurrencyPair(base, quote);
    }

    // TODO: parse() static factory method for values created by toString() (for symmetry)

    /// Creates a new pair with the base and quote swapped.
    ///
    /// @return a new instance
    public CurrencyPair swapped() {
        return new CurrencyPair(quote, base);
    }

    /// Checks if the given currency is involved in this pair.
    ///
    /// @param currency an arbitrary currency, `null` is allowed
    /// @return `true` if the given currency is either the base or the quote, `false` if not or it is `null`
    public boolean involves(@Nullable Currency currency) {
        return base.equals(currency) || quote.equals(currency);
    }

    /// Formats this pair to a concatenation of its currency codes, such as `EURUSD` or `USDJPY`.
    ///
    /// This is the forex industry convention for web APIs,
    /// [Bloomberg terminals](https://www.bloomberg.com/professional/products/bloomberg-terminal/), and other computerized financial systems.
    ///
    /// @return a concatenation of two ISO 4217 currency codes (the resulting string is always 6 uppercase latin letters)
    @Override
    public String toString() {
        return base.getCurrencyCode() + quote.getCurrencyCode();
    }
}
