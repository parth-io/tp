package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalModules.CS2103T;
import static seedu.address.testutil.TypicalModules.CS2103TWithTaskA;
import static seedu.address.testutil.TypicalModules.getTypicalAddressBook;
import static seedu.address.testutil.TypicalModules.getTypicalModules;
import static seedu.address.testutil.TypicalTasks.getTypicalTasks;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTaskCommand.AddTaskToModuleDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.module.Module;
import seedu.address.testutil.AddTaskToModuleDescriptorBuilder;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.ModuleBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddTaskCommand}.
 */
public class AddTaskCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    @Test
    public void constructor_nullModule_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTaskCommand(null));
    }
    @Test
    public void execute_taskAcceptedByModule_success() {
        AddTaskToModuleDescriptor descriptor = new AddTaskToModuleDescriptorBuilder(CS2103T).build();
        AddTaskCommand addTaskCommand = new AddTaskCommand(descriptor);

        String expectedMessage = String.format(AddTaskCommand.MESSAGE_ADD_TASK_SUCCESS,
                CS2103TWithTaskA);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setModule(CS2103T, CS2103TWithTaskA);

        assertCommandSuccess(addTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() {
        Module moduleToAddTaskTo =
                new ModuleBuilder().withTasks(getTypicalTasks()).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setModule(model.getFilteredModuleList().get(0), moduleToAddTaskTo);

        String duplicateTaskDescription =
                getTypicalTasks().get(0).getTaskDescription();
        AddTaskToModuleDescriptor descriptor =
                new AddTaskToModuleDescriptorBuilder(moduleToAddTaskTo,
                        duplicateTaskDescription).build();
        AddTaskCommand addTaskCommand = new AddTaskCommand(descriptor);



        assertThrows(CommandException.class, AddTaskCommand.MESSAGE_DUPLICATE_TASK, () ->
                addTaskCommand.execute(expectedModel));
    }

    @Test
    public void execute_nonExistentModuleCode_throwsCommandException() {
        Module nonExistentModule =
                new ModuleBuilder().withModuleCode("CS1234").build();
        AddTaskToModuleDescriptor descriptor =
                new AddTaskToModuleDescriptorBuilder(nonExistentModule).build();
        AddTaskCommand addTaskCommand = new AddTaskCommand(descriptor);

        assertThrows(CommandException.class,
                AddTaskCommand.MESSAGE_MODULE_CODE_DOES_NOT_EXIST, () ->
                addTaskCommand.execute(model));
    }

    @Test
    public void equals() {
        final List<Module> modules = getTypicalModules();
        final AddTaskToModuleDescriptor standardDescriptor =
                new AddTaskToModuleDescriptorBuilder(modules.get(0)).build();
        final AddTaskCommand standardCommand = new AddTaskCommand(standardDescriptor);

        // same values -> returns true
        AddTaskToModuleDescriptor copyDescriptor = new AddTaskToModuleDescriptorBuilder(modules.get(0)).build();
        AddTaskCommand commandWithSameValues = new AddTaskCommand(copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new AddModuleCommand(modules.get(0))));

        // different module code -> returns false
        AddTaskToModuleDescriptor differentModuleDescriptor =
                new AddTaskToModuleDescriptorBuilder(modules.get(1)).build();
        assertFalse(standardCommand.equals(new AddTaskCommand(differentModuleDescriptor)));

        // different task description -> returns false
        final AddTaskToModuleDescriptor differentTaskDescriptionDescriptor =
                new AddTaskToModuleDescriptorBuilder(modules.get(0),
                        "different description").build();
        assertFalse(standardCommand.equals(new AddTaskCommand(differentTaskDescriptionDescriptor)));
    }

    /**
     * A Model stub that contains a single module.
     */
    private class ModelStubWithModule extends ModelStub {
        private final Module module;

        ModelStubWithModule(Module module) {
            requireNonNull(module);
            this.module = module;
        }

        @Override
        public boolean hasModule(Module module) {
            requireNonNull(module);
            return this.module.isSameModule(module);
        }
    }
}
