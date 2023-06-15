package infrastructure.factory.configurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InjectByTypeAnnotationObjectConfiguratorTest {

    @InjectMocks
    InjectByTypeAnnotationObjectConfigurator configurator;

    @Mock
    ApplicationContext context;

    @Test
    public void configureAllCorrect() {
        class FakeClass {
            @InjectByType
            TestClass object;

            public TestClass getObject() {
                return object;
            }
        }
        when(context.getObject(any())).thenReturn(new TestClass());
        FakeClass fakeClass = new FakeClass();

        configurator.configure(fakeClass, FakeClass.class, context);

        verify(context, times(1)).getObject(any());
        assertNotNull(fakeClass.getObject());
    }

    @Test
    public void configureAllCorrectNoAnnotatedFeels() {
        class FakeClass {
            TestClass object;

            public TestClass getObject() {
                return object;
            }
        }
        FakeClass fakeClass = new FakeClass();

        configurator.configure(fakeClass, FakeClass.class, context);

        verify(context, times(0)).getObject(any());
        assertNull(fakeClass.getObject());
    }

    private class TestClass {
    }

}
