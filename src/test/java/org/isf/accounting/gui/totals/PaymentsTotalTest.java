/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.accounting.gui.totals;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;

import org.isf.accounting.TestBill;
import org.isf.accounting.TestPayment;
import org.junit.jupiter.api.Test;

public class PaymentsTotalTest {

	@Test
	public void shouldCalculateFromPayments() {
		// given:
		PaymentsTotal paymentsTotal = new PaymentsTotal(
				Arrays.asList(1, 2),
				Arrays.asList(
						TestPayment.withAmountAndBill(10, TestBill.notDeletedBillWithBalance(1, 1)),
						TestPayment.withAmountAndBill(15, TestBill.notDeletedBillWithBalance(2, 1))
				)
		);

		// when:
		BigDecimal result = paymentsTotal.getValue();

		// then:
		assertThat(result.longValue()).isEqualTo(25);
	}

	@Test
	public void shouldSkipPaymentsForDeletedBills() {
		// given:
		PaymentsTotal paymentsTotal = new PaymentsTotal(
				Arrays.asList(1),
				Arrays.asList(
						TestPayment.withAmountAndBill(10, TestBill.notDeletedBillWithBalance(1, 1)),
						TestPayment.withAmountAndBill(15, TestBill.deletedBillWithBalance(2, 1))
				)
		);

		// when:
		BigDecimal result = paymentsTotal.getValue();

		// then:
		assertThat(result.longValue()).isEqualTo(10);
	}
}