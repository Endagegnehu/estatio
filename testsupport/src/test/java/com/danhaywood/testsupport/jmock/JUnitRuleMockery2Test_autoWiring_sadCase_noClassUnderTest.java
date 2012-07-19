package com.danhaywood.testsupport.jmock;

import org.jmock.auto.Mock;
import org.junit.Rule;
import org.junit.Test;

import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.Mode;

public class JUnitRuleMockery2Test_autoWiring_sadCase_noClassUnderTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private Collaborator collaborator;

    // @ClassUnderTest
	@SuppressWarnings("unused")
	private Collaborating collaborating;

    @Test(expected=IllegalStateException.class)
    public void cannotFindClassUnderTest() {
    	context.getClassUnderTest();
    }

}
