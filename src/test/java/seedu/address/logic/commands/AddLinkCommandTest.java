package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_ALIAS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_ALIAS_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_ALIAS_3;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CS2106_MODULE_CODE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CS9999_MODULE_CODE_NOT_IN_TYPICAL_ADDRESS_BOOK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GE3238_MODULE_CODE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MA2001_MODULE_CODE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_URL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_URL_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_URL_3;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_LINK_URL_NO_HTTPS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.module.link.Link;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.ModuleTitle;
import seedu.address.model.module.task.Task;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddLinkCommand.
 */
public class AddLinkCommandTest {
    private static final int MODULE_INDEX_NONEXISTENT_LARGE = 999999;
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private static final String MODULE_CODE_WITH_LINK = VALID_GE3238_MODULE_CODE;

    @Test
    public void execute_addLinkCommandFilteredList_success() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Set<Link> linksToAdd = new HashSet<Link>(Arrays.asList(
                new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL)));

        Module moduleToEdit = expectedModel.getModuleUsingModuleCode(new ModuleCode(VALID_CS2106_MODULE_CODE), true);
        ModuleCode moduleCode = moduleToEdit.getModuleCode();
        ModuleTitle moduleTitle = moduleToEdit.getModuleTitle();
        List<Task> moduleTasks = moduleToEdit.getTasks();
        Set<Link> moduleLinks = moduleToEdit.copyLinks();
        moduleLinks.addAll(linksToAdd);
        Module moduleToAddLink = new Module(moduleCode, moduleTitle, moduleTasks, moduleLinks);

        AddLinkCommand addLinkCommand = new AddLinkCommand(new ModuleCode(VALID_CS2106_MODULE_CODE), linksToAdd);
        expectedModel.setModule(moduleToEdit, moduleToAddLink);
        String expectedMessage = String.format(AddLinkCommand.MESSAGE_ADD_LINK_SUCCESS, moduleToAddLink);

        assertCommandSuccess(addLinkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateModuleLinkAliasAndUrlFilteredList_failure() {
        Module module = model.getModuleUsingModuleCode(new ModuleCode(MODULE_CODE_WITH_LINK), true);
        Set<Link> links = module.copyLinks();
        AddLinkCommand addLinkCommand = new AddLinkCommand(new ModuleCode(MODULE_CODE_WITH_LINK), links);

        assertCommandFailure(addLinkCommand, model,
                String.format(AddLinkCommand.MESSAGE_DUPLICATE_LINK
                        + new ModuleCode(MODULE_CODE_WITH_LINK).getModuleCodeAsUpperCaseString() + "]",
                        links.iterator().next()));
    }

    @Test
    public void execute_duplicateModuleLinkAliasFilteredList_failure() {
        Set<Link> links = new HashSet<Link>(Arrays.asList(
                new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL_3)));
        AddLinkCommand addLinkCommand = new AddLinkCommand(new ModuleCode(MODULE_CODE_WITH_LINK), links);

        assertCommandFailure(addLinkCommand, model,
                String.format(AddLinkCommand.MESSAGE_DUPLICATE_LINK
                        + new ModuleCode(MODULE_CODE_WITH_LINK).getModuleCodeAsUpperCaseString() + "]",
                        new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL_3)));
    }

    @Test
    public void execute_duplicateModuleLinkUrlFilteredList_failure() {
        Set<Link> links = new HashSet<Link>(Arrays.asList(
                new Link(VALID_MODULE_LINK_ALIAS_3, VALID_MODULE_LINK_URL)));
        AddLinkCommand addLinkCommand = new AddLinkCommand(new ModuleCode(MODULE_CODE_WITH_LINK), links);

        assertCommandFailure(addLinkCommand, model,
                String.format(AddLinkCommand.MESSAGE_DUPLICATE_LINK
                        + new ModuleCode(MODULE_CODE_WITH_LINK).getModuleCodeAsUpperCaseString() + "]",
                        new Link(VALID_MODULE_LINK_ALIAS_3, VALID_MODULE_LINK_URL)));
    }

    @Test
    public void execute_duplicateModuleLinkUrlIgnoreHTTPSFilteredList_failure() {
        Set<Link> links = new HashSet<Link>(Arrays.asList(
                new Link(VALID_MODULE_LINK_ALIAS_3, VALID_MODULE_LINK_URL_NO_HTTPS)));
        AddLinkCommand addLinkCommand = new AddLinkCommand(new ModuleCode(MODULE_CODE_WITH_LINK), links);

        assertCommandFailure(addLinkCommand, model,
                String.format(AddLinkCommand.MESSAGE_DUPLICATE_LINK
                                + new ModuleCode(MODULE_CODE_WITH_LINK).getModuleCodeAsUpperCaseString() + "]",
                        new Link(VALID_MODULE_LINK_ALIAS_3, VALID_MODULE_LINK_URL)));
    }

    @Test
    public void execute_nonExistentModuleCodeFilteredList_failure() {
        Set<Link> links = new HashSet<Link>(Arrays.asList(
                new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL)));
        AddLinkCommand addLinkCommand = new AddLinkCommand(
                new ModuleCode(VALID_CS9999_MODULE_CODE_NOT_IN_TYPICAL_ADDRESS_BOOK), links);

        assertCommandFailure(addLinkCommand, model,
                String.format(Messages.MESSAGE_NO_MODULE_IN_FILTERED_LIST,
                        VALID_CS9999_MODULE_CODE_NOT_IN_TYPICAL_ADDRESS_BOOK));
    }

    @Test
    public void equals() {
        final AddLinkCommand standardCommand = new AddLinkCommand(new ModuleCode(VALID_CS2106_MODULE_CODE),
                new HashSet<Link>(Arrays.asList(new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL))));

        // same values -> returns true
        Set<Link> copyLinks = new HashSet<Link>(
                Arrays.asList(new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL)));
        AddLinkCommand commandWithSameValues = new AddLinkCommand(
                new ModuleCode(VALID_CS2106_MODULE_CODE), copyLinks);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different module code -> returns false
        assertFalse(standardCommand.equals(new AddLinkCommand(
                new ModuleCode(VALID_MA2001_MODULE_CODE), copyLinks)));

        // different link url -> returns false
        assertFalse(standardCommand.equals(new AddLinkCommand(
                new ModuleCode(VALID_CS2106_MODULE_CODE),
                new HashSet<Link>(Arrays.asList(new Link(VALID_MODULE_LINK_ALIAS, VALID_MODULE_LINK_URL_2))))));

        // different link alias -> returns false
        assertFalse(standardCommand.equals(new AddLinkCommand(
                new ModuleCode(VALID_CS2106_MODULE_CODE),
                new HashSet<Link>(Arrays.asList(new Link(VALID_MODULE_LINK_ALIAS_2, VALID_MODULE_LINK_URL))))));
    }
}
