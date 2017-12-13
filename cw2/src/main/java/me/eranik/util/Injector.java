package me.eranik.util;

import me.eranik.util.exceptions.AmbiguousImplementationException;
import me.eranik.util.exceptions.ImplementationNotFoundException;
import me.eranik.util.exceptions.InjectionCycleException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * Creates instance of a specified class resolving all dependencies.
 */
public class Injector {

    private static int implNumber;
    private static int type[];
    private static HashMap<Class<?>, Integer> idByClass;
    private static Object[] constructed;

    /**
     * Creates instance of the class with specified name.
     * Uses implementations of classes from the specified list to resolve all dependencies.
     * @param rootClassName name of the root class
     * @param implementations available implementations
     * @return instance of the class with specified name
     * @throws AmbiguousImplementationException if found multiple implementations of one class
     * @throws ImplementationNotFoundException if found no implementation of some used class
     * @throws InjectionCycleException if found cycle in dependencies
     * @throws NoSuchMethodException if some used method was not found
     * @throws InstantiationException if could not create an instance of some implementation
     * @throws IllegalAccessException if there is no access to some method
     */
    public static Object initialize(String rootClassName, Class<?>[] implementations)
            throws AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object result;

        try {
            implNumber = implementations.length;

            type = new int[implNumber];
            constructed = new Object[implNumber];
            idByClass = new HashMap<>();
            for (int i = 0; i < implNumber; i++) {
                Class<?> implementation = implementations[i];
                idByClass.put(implementation, i);
            }


            Class<?> rootClass = getRootClass(rootClassName, implementations);
            resolveDependencies(rootClass, implementations);
            result = constructed[idByClass.get(rootClass)];
        }
        finally {
            clear();
        }

        return result;
    }

    private static void resolveDependencies(@NotNull Class<?> o, @NotNull Class<?>[] allImplementations)
            throws AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        type[idByClass.get(o)] = 1;

        Constructor constructor = o.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterImplementation = getImplementation(parameter.getType(), allImplementations);

            int parameterId = idByClass.get(parameterImplementation);
            if (type[parameterId] == 0) {
                resolveDependencies(parameterImplementation, allImplementations);
            }
            if (type[parameterId] == 1) {
                throw new InjectionCycleException();
            }

            dependencies[i] = constructed[idByClass.get(parameterImplementation)];
        }

        constructed[idByClass.get(o)] = constructor.newInstance(dependencies);
        type[idByClass.get(o)] = 2;
    }

    private static Class<?> getImplementation(@NotNull Class<?> o, @NotNull Class<?>[] allImplementations)
            throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> result = null;
        for (Class<?> implementation : allImplementations) {
            int implMod = implementation.getModifiers();
            if (Modifier.isInterface(implMod) || Modifier.isAbstract(implMod)) {
                continue;
            }
            if (o.isAssignableFrom(implementation)) {
                if (result == null) {
                    result = implementation;
                } else {
                    throw new AmbiguousImplementationException();
                }
            }
        }
        if (result == null) {
            throw new ImplementationNotFoundException();
        }

        return result;
    }

    private static Class<?> getRootClass(@NotNull String rootClassName, @NotNull Class<?>[] implementations)
            throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> rootClass = null;
        for (int i = 0; i < implNumber; i++) {
            Class<?> implementation = implementations[i];
            if (implementation.getName().equals(rootClassName)) {
                if (rootClass == null) {
                    rootClass = implementation;
                } else {
                    throw new AmbiguousImplementationException();
                }
            }
        }
        if (rootClass == null) {
            throw new ImplementationNotFoundException();
        }
        return rootClass;
    }

    private static void clear() {
        type = null;
        idByClass = null;
        constructed = null;
    }
}
