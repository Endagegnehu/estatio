package org.estatio.integtests.budget;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.dom.asset.Property;
import org.estatio.dom.asset.PropertyRepository;
import org.estatio.dom.budgeting.budget.Budget;
import org.estatio.dom.budgeting.budget.BudgetRepository;
import org.estatio.dom.budgeting.keytable.FoundationValueType;
import org.estatio.dom.budgeting.keytable.KeyTable;
import org.estatio.dom.budgeting.keytable.KeyTableRepository;
import org.estatio.dom.budgeting.keytable.KeyValueMethod;
import org.estatio.fixture.asset.PropertyForOxfGb;
import org.estatio.fixture.budget.BudgetBaseLineFixture;
import org.estatio.fixture.budget.BudgetTeardownFixture;
import org.estatio.fixture.budget.BudgetsForOxf;
import org.estatio.integtests.EstatioIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyTableRepository_IntegTest extends EstatioIntegrationTest {

    @Inject
    KeyTableRepository keyTableRepository;

    @Inject
    PropertyRepository propertyRepository;

    @Inject
    BudgetRepository budgetRepository;

    final static String TABLE_NAME_1 = "Table 1";
    final static String TABLE_NAME_2 = "Table 2";

    @Before
    public void setupData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(final ExecutionContext executionContext) {
                executionContext.executeChild(this, new BudgetBaseLineFixture());
                executionContext.executeChild(this, new BudgetsForOxf());
            }
        });
    }

    @After
    public void teardownData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(final ExecutionContext executionContext) {
                executionContext.executeChild(this, new BudgetTeardownFixture());
            }
        });
    }

    public static class FindByBudgetAndName extends KeyTableRepository_IntegTest {


        @Test
        public void happyCase() throws Exception {
            // given
            Property property = propertyRepository.findPropertyByReference(PropertyForOxfGb.REF);
            Budget budget = budgetRepository.findByPropertyAndStartDate(property, BudgetsForOxf.BUDGET_2015_START_DATE);
            budget.createKeyTable(TABLE_NAME_1, FoundationValueType.AREA, KeyValueMethod.PROMILLE);

            // when
            final KeyTable keyTable = keyTableRepository.findByBudgetAndName(budget, TABLE_NAME_1);
            // then
            assertThat(keyTable.getName()).isEqualTo(TABLE_NAME_1);
            assertThat(keyTable.getBudget().getProperty()).isEqualTo(property);

        }

    }

    public static class FindByBudget extends KeyTableRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {
            // given
            Property property = propertyRepository.findPropertyByReference(PropertyForOxfGb.REF);
            Budget budget = budgetRepository.findByPropertyAndStartDate(property, BudgetsForOxf.BUDGET_2015_START_DATE);
            budget.createKeyTable(TABLE_NAME_1, FoundationValueType.AREA, KeyValueMethod.PROMILLE);
            budget.createKeyTable(TABLE_NAME_2, FoundationValueType.AREA, KeyValueMethod.PROMILLE);

            // when
            final List<KeyTable> keyTables = keyTableRepository.findByBudget(budget);
            // then
            assertThat(keyTables.size()).isEqualTo(2);
            assertThat(keyTables.get(0).getBudget()).isEqualTo(budget);
        }

    }

}
