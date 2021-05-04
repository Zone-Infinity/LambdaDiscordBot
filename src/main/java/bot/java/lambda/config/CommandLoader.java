package bot.java.lambda.config;

import bot.java.lambda.CommandManager;
import bot.java.lambda.command.type.CommandHandler;
import bot.java.lambda.command.type.ICommand;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public record CommandLoader(String commandsDirectoryPrefix) {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLoader.class);
    private static List<Class<? extends ICommand>> classes;

    public void loadCommands() {
        Reflections reflections = new Reflections(commandsDirectoryPrefix);

        final Set<Class<? extends ICommand>> iCommands = reflections.getSubTypesOf(ICommand.class);
        final Set<Class<?>> commandHandlers = reflections.getTypesAnnotatedWith(CommandHandler.class);
        iCommands.retainAll(commandHandlers);

        System.out.println(iCommands.size());

        classes = new ArrayList<>(iCommands);
        classes.sort(Comparator.comparing(Class::getName));

    }

    public boolean addAllCommands(CommandManager manager, Object... arguments) {
        for (Class<? extends ICommand> cmd : classes) {
            final List<Class<?>> types = getConstructorTypes(cmd);
            List<Object> objects = new ArrayList<>(List.of(manager));
            objects.addAll(Arrays.asList(arguments));

            try {
                List<Object> args = new LinkedList<>();
                ICommand instance;
                if (types.size() > 0) {
                    types.forEach(
                            type -> {
                                final List<Object> object = objects.stream().filter(type::isInstance).collect(Collectors.toList());
                                if (object.size() > 0)
                                    args.add(object.get(0));
                            });

                }

                instance = createInstance(cmd, args.toArray());
                manager.addCommand(instance);
            } catch (Exception e) {
                LOGGER.error("Failed to create instance of {}", cmd.getName(), e);
                return false;
            }
        }
        return true;
    }

    private ICommand createInstance(Class<? extends ICommand> iCommand, Object... arguments) throws
            InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<?> declaredConstructor = iCommand.getConstructors()[0];

        if (arguments.length > 0)
            return (ICommand) declaredConstructor.newInstance(arguments);
        else
            return (ICommand) declaredConstructor.newInstance();

    }

    private List<Class<?>> getConstructorTypes(Class<? extends ICommand> iCommand) {
        final Class<?>[] types = iCommand.getDeclaredConstructors()[0].getParameterTypes();
        return Arrays.asList(types);
    }
}
