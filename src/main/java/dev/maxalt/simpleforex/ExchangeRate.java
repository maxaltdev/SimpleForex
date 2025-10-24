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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/// An exchange rate between two currencies at a specific moment in time.
///
/// This is an immutable, value-based class. Use it as you would use [java.time.LocalDate] or [java.util.Optional].
@NullMarked
public record ExchangeRate(CurrencyPair currencyPair, BigDecimal value, Instant timestamp) {

    /// Main constructor.
    ///
    /// @param currencyPair a pair of exchanged currencies
    /// @param value        the numeric value of this exchange rate (must be positive)
    /// @param timestamp    the timestamp of when this exchange rate was recorded
    /// @throws NullPointerException     if any argument is `null`
    /// @throws IllegalArgumentException if `value` is zero or negative
    public ExchangeRate {
        Objects.requireNonNull(currencyPair, "an exchange rate cannot be of a null currency pair");
        Objects.requireNonNull(value, "an exchange rate cannot have a null numeric value");
        Objects.requireNonNull(timestamp, "an exchange rate cannot have a null timestamp");

        if (value.signum() <= 0) {
            throw new IllegalArgumentException("the value of an exchange rate must be positive");
        }
    }

    // TODO: Override Javadoc for equals and hashCode, documenting how they ignore scale

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        return obj instanceof ExchangeRate(var otherPair, var otherVal, var otherTimestamp)
                && currencyPair.equals(otherPair)
                && value.compareTo(otherVal) == 0
                && timestamp.equals(otherTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyPair, value.stripTrailingZeros(), timestamp);
    }

    /// Formats this exchange rate to a concise string representation.
    ///
    /// Unlike [CurrencyPair], this method does not guarantee a specific format.
    ///
    /// @return a string representation of this exchange rate
    @Override
    public String toString() {
        return currencyPair + " " + value.toPlainString() + " " + timestamp;
    }
}
