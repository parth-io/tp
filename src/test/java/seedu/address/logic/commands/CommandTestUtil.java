package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_NUMBER;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalModules.CS2103T;
import static seedu.address.testutil.TypicalModules.CS2106;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddTaskToModuleDescriptorBuilder;
import seedu.address.testutil.DeleteTaskFromModuleDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_CS_MODULE_CODE = "CS2106";
    public static final String VALID_CS_MODULE_TITLE = "Introduction to Operating Systems";
    public static final String VALID_MA_MODULE_CODE = "MA2001";
    public static final String VALID_MA_MODULE_TITLE = "Linear Algebra I";
    public static final String VALID_MODULE_CODE = CS2103T.getModuleCode().toString();
    public static final String VALID_MODULE_TITLE = CS2103T.getModuleTitle().toString();
    public static final String VALID_TASK_A = "Complete assignment for week 8";
    public static final String VALID_TASK_B = "Submit the quiz@LumiNUS";
    public static final String VALID_TASK_C = "Pay $5 to Ann for project costs";

    // Module code cannot have brackets
    public static final String INVALID_MODULE_CODE = "(CS2103T)";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String MODULE_CODE_DESC_CS2106 = " " + PREFIX_MODULE_CODE + VALID_CS_MODULE_CODE;
    public static final String MODULE_CODE_DESC_MA2001 = " " + PREFIX_MODULE_CODE + VALID_MA_MODULE_CODE;
    public static final String MODULE_TITLE_DESC_CS2106 = " " + PREFIX_MODULE_TITLE + VALID_CS_MODULE_TITLE;
    public static final String MODULE_TITLE_DESC_MA2001 = " " + PREFIX_MODULE_TITLE + VALID_MA_MODULE_TITLE;
    public static final String MODULE_TASK_DESC_A = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_A;
    public static final String MODULE_TASK_DESC_B = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_B;
    public static final String MODULE_TASK_DESC_C = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_C;
    public static final String MODULE_TASKLIST_DESC_NUMBER_ONE =
            " " + PREFIX_TASK_NUMBER + "1";
    public static final String MODULE_TASKLIST_DESC_NUMBER_TWO =
            " " + PREFIX_TASK_NUMBER + "2";
    public static final String MODULE_TASKLIST_DESC_NUMBER_THREE =
            " " + PREFIX_TASK_NUMBER + "3";


    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    // '$' not allowed in module code
    public static final String INVALID_MODULE_CODE_DESC = " " + PREFIX_MODULE_CODE + "C$2500";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";
    public static final String INVALID_TASK_DESC = " " + PREFIX_TASK_DESCRIPTION; // empty string
    public static final String INVALID_TASK_NUMBER_DESC_NON_NUMERIC =
            " " + PREFIX_TASK_NUMBER + "-99$9";
    public static final String INVALID_TASK_NUMBER_DESC_NEGATIVE =
            " " + PREFIX_TASK_NUMBER + "-999";
    public static final String INVALID_TASK_NUMBER_DESC_ZERO =
            " " + PREFIX_TASK_NUMBER + "0";

    public static final EditContactCommand.EditPersonDescriptor DESC_AMY;
    public static final EditContactCommand.EditPersonDescriptor DESC_BOB;
    public static final AddTaskCommand.AddTaskToModuleDescriptor DESC_CS2106_ADD_TASK_A;
    public static final AddTaskCommand.AddTaskToModuleDescriptor DESC_CS2106_ADD_TASK_B;
    public static final DeleteTaskCommand.DeleteTaskFromModuleDescriptor DESC_CS2106_DELETE_TASK_ONE;
    public static final DeleteTaskCommand.DeleteTaskFromModuleDescriptor DESC_CS2106_DELETE_TASK_TWO;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withEmail(VALID_EMAIL_AMY)
                .withPhone(VALID_PHONE_AMY).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withPhone(VALID_PHONE_BOB).build();

        DESC_CS2106_ADD_TASK_A =
                new AddTaskToModuleDescriptorBuilder(CS2106, VALID_TASK_A).build();
        DESC_CS2106_ADD_TASK_B =
                new AddTaskToModuleDescriptorBuilder(CS2106, VALID_TASK_B).build();
        DESC_CS2106_DELETE_TASK_ONE =
                new DeleteTaskFromModuleDescriptorBuilder(CS2106,
                        Index.fromOneBased(1)).build();
        DESC_CS2106_DELETE_TASK_TWO =
                new DeleteTaskFromModuleDescriptorBuilder(CS2106,
                        Index.fromOneBased(2)).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
