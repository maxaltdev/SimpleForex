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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrencyPairTests {

    @Nested
    class InvolvesCurrency {

        @ParameterizedTest
        @ValueSource(strings = {"USD", "EUR"})
        void involvesReturnsTrueOnInvolvedCurrency(Currency currency) {
            var pair = CurrencyPair.fromIsoCodes("USD", "EUR");

            assertTrue(pair.involves(currency));
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"JPY", "KRW", "RUB", "PLN", "BYN", "CAD", "GBP"})
        void involvesReturnsFalseOnUninvolvedCurrency(Currency currency) {
            var pair = CurrencyPair.fromIsoCodes("USD", "EUR");

            assertFalse(pair.involves(currency));
        }
    }

    @Nested
    class PositionOf {

        @ParameterizedTest
        @CsvSource({"XTS,0", "GBP,1"})
        void returnsExpectedIntOnInvolvedCurrency(Currency currency, int expected) {
            var pair = CurrencyPair.fromIsoCodes("XTS", "GBP");

            assertThat(pair.positionOf(currency)).hasValue(expected);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"USD", "ILS", "RUB", "EUR", "XAU"})
        void returnsEmptyOnUninvolvedCurrency(Currency currency) {
            var pair = CurrencyPair.fromIsoCodes("XTS", "GBP");

            assertThat(pair.positionOf(currency)).isEmpty();
        }
    }

    @Nested
    class StringConversions {

        @ParameterizedTest
        @CsvSource({
                "EUR,USD,EURUSD",
                "USD,JPY,USDJPY",
                "USD,RUB,USDRUB",
                "CAD,AUD,CADAUD",
                "USD,CNY,USDCNY"
        })
        void toStringReturnsExpectedFormat(Currency base, Currency quote, String expectedString) {
            var pair = new CurrencyPair(base, quote);

            assertEquals(expectedString, pair.toString());
        }

        // TODO: unit tests for parse(String) static factory method (after it's added)
    }

    @Test
    void equalsAndHashCodeContract() {
        // If we let EqualsVerifier use random currencies, it'll generate something like USD/USD, which violates the invariant of base != quote.
        // We should provide prefab values where possible options for "base" do not intersect with possible options for "quote".

        var euro = Currency.getInstance("EUR");
        var swissFranc = Currency.getInstance("CHF");
        var britishPound = Currency.getInstance("GBP");
        var canadianDollar = Currency.getInstance("CAD");

        EqualsVerifier.forClass(CurrencyPair.class)
                .withPrefabValuesForField("base", euro, swissFranc)
                .withPrefabValuesForField("quote", britishPound, canadianDollar)
                .verify();
    }
}
