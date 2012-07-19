package com.danhaywood.testsupport.jmock;

import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.ClassUnderTest;
import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.Mode;
import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.One;

public class JUnitRuleMockery2Test_mockAnnotatedWithOne_sadCase {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @One
    @Mock
    private Collaborator collaborator;

    @ClassUnderTest
	private Collaborating collaborating;

    @Before
	public void setUp() throws Exception {
    	collaborating = (Collaborating) context.getClassUnderTest();
	}
    
    @Ignore("This isn't actually possible to test, because the test is actually thrown by the rule, which is further up the callstack than the test method")    @Test(expected=AssertionError.class)
    public void invocationOnCollaboratorIsIgnored() {
    	collaborating.dontCollaborateWithCollaborator();
    }


}
