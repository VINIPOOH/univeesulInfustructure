package infrastructure.config;

import infrastructure.сonfig.JavaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JavaConfigTest {

    @InjectMocks
    JavaConfig javaConfig;

    @Mock
    Reflections scanner;
    @Mock
    Set<Class> classes = Mockito.mock(Set.class);

    @Test(expected = RuntimeException.class)
    public void getImplClassWhenImplClassesMoreThenOne() {
        when(classes.size()).thenReturn(2);
        when(scanner.getSubTypesOf(any(Class.class))).thenReturn(classes);

        javaConfig.getImplClass(Object.class);

        fail();
    }

    @Test(expected = RuntimeException.class)
    public void getImplClassWhenImplClassesLessThenOne() {
        when(classes.size()).thenReturn(0);
        when(scanner.getSubTypesOf(any(Class.class))).thenReturn(classes);

        javaConfig.getImplClass(Object.class);

        fail();
    }

    @Test(expected = RuntimeException.class)
    public void getImplClassWhenImplClassesOne() {
        Iterator<Class> iterator = Mockito.mock(Iterator.class);
        Class expected = Object.class;
        when(iterator.next()).thenReturn(expected);
        when(classes.iterator()).thenReturn(iterator);
        when(classes.size()).thenReturn(1);
        when(scanner.getSubTypesOf(any(Class.class))).thenReturn(classes);

        Class result = javaConfig.getImplClass(Object.class);

        verify(scanner, times(1)).getSubTypesOf(any(Class.class));
        verify(iterator, times(1)).next();
        verify(classes, times(1)).iterator();
        verify(classes, times(1)).size();
        assertEquals(expected, result);
    }
}