package seedu.address.logic.commands.deliverycommands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.delivery.Delivery;

/**
 * Toggles which deliveries are selected in the filtered list (same behaviour as the checkboxes).
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles selection for deliveries at the given indices in the displayed list (like checkboxes).\n"
            + "Parameters: INDEX [INDEX]... | none\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD + " 1 3\n"
            + "Example: " + COMMAND_WORD + " none (clear all selection)";

    public static final String MESSAGE_CLEAR_SUCCESS = "Cleared delivery selection.";

    public static final String MESSAGE_TOGGLE_SUCCESS = "Toggled selection for %1$d index(es). "
            + "Currently %2$d delivery(s) selected.";

    private final boolean clearAll;
    private final List<Index> indicesToToggle;

    /**
     * @param clearAll if true, {@code indicesToToggle} is ignored and selection is cleared
     */
    public SelectCommand(boolean clearAll, List<Index> indicesToToggle) {
        this.clearAll = clearAll;
        this.indicesToToggle = indicesToToggle == null ? List.of() : List.copyOf(indicesToToggle);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (clearAll) {
            model.clearDeliverySelection();
            return new CommandResult(MESSAGE_CLEAR_SUCCESS);
        }

        List<Delivery> shown = model.getFilteredDeliveryList();
        for (Index index : indicesToToggle) {
            if (index.getZeroBased() >= shown.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_DELIVERY_DISPLAYED_INDEX);
            }
        }
        for (Index index : indicesToToggle) {
            model.toggleDeliverySelection(index);
        }

        int count = model.getSelectedDeliveriesInDisplayOrder().size();
        return new CommandResult(String.format(MESSAGE_TOGGLE_SUCCESS, indicesToToggle.size(), count));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SelectCommand)) {
            return false;
        }
        SelectCommand o = (SelectCommand) other;
        return clearAll == o.clearAll && indicesToToggle.equals(o.indicesToToggle);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(clearAll, indicesToToggle);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clearAll", clearAll)
                .add("indicesToToggle", indicesToToggle)
                .toString();
    }
}
