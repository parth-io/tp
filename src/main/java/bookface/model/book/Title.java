package bookface.model.book;

import static java.util.Objects.requireNonNull;

import bookface.commons.util.AppUtil;

/**
 * Represents a title of the Book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTitle(String)}
 */
public class Title {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters, punctuations and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[\\p{Alnum}\\p{Punct}][\\p{Alnum}\\p{Punct} ]*$";

    public final String bookTitle;

    /**
     * Constructs a {@code Title}.
     *
     * @param title A valid title.
     */
    public Title(String title) {
        requireNonNull(title);
        String trimmedTitle = title.trim();
        AppUtil.checkArgument(isValidTitle(trimmedTitle), MESSAGE_CONSTRAINTS);
        bookTitle = trimmedTitle;
    }

    /**
     * Returns true if a given string is a valid title.
     */
    public static boolean isValidTitle(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return bookTitle;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && bookTitle.equals(((Title) other).bookTitle)); // state check
    }

    @Override
    public int hashCode() {
        return bookTitle.hashCode();
    }
}
